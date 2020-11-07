package utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JOptionPane;

import dialogs.LoadingDialog;
import dialogs.SoundbankDialog;
import frames.SoundFrame;
import main.ArrayVisualizer;
import panes.JErrorPane;
import resources.soundfont.SFXFetcher;

/*
 * 
MIT License

Copyright (c) 2019 w0rthy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

final public class Sounds {
    private int[] array;
    
    private ArrayVisualizer ArrayVisualizer;
    
    private Thread AudioThread;
    
    private Highlights Highlights;
    
    private volatile Synthesizer synth;
    private volatile MidiChannel[] channels;
    
    private volatile int noteDelay;
    
    private boolean soundEnabled;
    
    private volatile boolean SOUND;
    private volatile boolean MIDI;
    private int NUMCHANNELS; //Number of Audio Channels
    private double PITCHMIN; //Minimum Pitch
    private double PITCHMAX; //Maximum Pitch
    private double SOUNDMUL;
    private boolean SOFTERSOUNDS;
    
    final private int SUSTAIN_PEDAL = 64;
    final private int REVERB = 91;
    
    private String defaultSoundbank = "Default (Roland SoundCanvas Sound Set)";
    private String selectedSoundbank;
    
    private int sineWaveIndex;
    final private int DEFAULT_SINE_WAVE_INDEX = 193;
    final private int DEFAULT_ROCK_ORGAN_INDEX = 16;
    
    private volatile LoadingDialog infoMsg;
    
    private volatile int instrumentChoice;
    private volatile int testInstrumentChoice;
    
    @SuppressWarnings("unused")
    public Sounds(int[] array, ArrayVisualizer arrayVisualizer) {
        this.array = array;
        this.ArrayVisualizer = arrayVisualizer;
        this.Highlights = ArrayVisualizer.getHighlights();
        
        this.SOUND = true;
        this.MIDI = true;
        this.NUMCHANNELS = 16;
        this.PITCHMIN = 25d;
        this.PITCHMAX = 105d;
        this.SOUNDMUL = 1d;
        
        this.noteDelay = 1;
        
        this.soundEnabled = true;
        
        try {
            MidiSystem.getSequencer(false);
            this.synth = MidiSystem.getSynthesizer();
            this.synth.open();
        }
        catch (MidiUnavailableException e) {
            JErrorPane.invokeCustomErrorMessage("The default MIDI device is unavailable, possibly because it is already being used by another application.");
            this.soundEnabled = false;
        }
        catch (SecurityException e) {
            JErrorPane.invokeErrorMessage(e);
            this.soundEnabled = false;
        }
        
        this.instrumentChoice = 0;
        this.prepareDefaultSoundbank();
        
        this.AudioThread = new Thread() {
            @Override
            public void run() {
                while(Sounds.this.soundEnabled) {
                    for(MidiChannel channel : channels) {
                        channel.allNotesOff();
                    }
                    if(SOUND == false || MIDI == false || JErrorPane.errorMessageActive) {
                        continue;
                    }

                    int noteCount = Math.min(Highlights.getMarkCount(), NUMCHANNELS);
                    int voice = 0;

                    for(int i : Highlights.highlightList()) {
                        try {
                            if(i != -1) {
                                int currentLen = ArrayVisualizer.getCurrentLength();

                                //PITCH
                                double pitch = Sounds.this.array[Math.min(Math.max(i, 0), currentLen - 1)] / (double) currentLen * (PITCHMAX - PITCHMIN) + PITCHMIN;
                                int pitchmajor = (int) pitch;
                                int pitchminor = (int)((pitch-((int)pitch))*8192d)+8192;

                                int vel = (int) (Math.pow(PITCHMAX - pitchmajor, 2d) * (Math.pow(noteCount, -0.25)) * 64d * SOUNDMUL) / 2; //I'VE SOLVED IT!!

                                if(SOUNDMUL >= 1 && vel < 256) {
                                    vel *= vel;
                                }

                                channels[voice].noteOn(pitchmajor, vel);
                                channels[voice].setPitchBend(pitchminor);
                                channels[voice].controlChange(REVERB, 10);
                                
                                if((++voice % Math.max(noteCount, 1)) == 0)
                                    break;
                            }
                        }
                        catch (Exception e) {
                            JErrorPane.invokeErrorMessage(e);
                        }
                    }
                    try {
                        for(int i = 0; i < Sounds.this.noteDelay; i++) {
                            sleep(1);
                        }
                    } catch(Exception e) {
                        JErrorPane.invokeErrorMessage(e);
                    }
                }
            }
        };
    }
    
    public int getInstrumentChoice() {
        return this.instrumentChoice;
    }
    public void setInstrumentChoice(int choice) {
        this.instrumentChoice = choice;
        this.assignInstruments();
    }
    
    public String getSelectedSoundbank() {
        return this.selectedSoundbank;
    }
    
    //TODO: Make infoMsg into reusable class (including Custom Image)
    public void selectCustomSoundbank(SoundFrame menu) {
        SoundbankDialog dialog = new SoundbankDialog();
        File soundbank = dialog.getFile();

        if(soundbank != null) {
            this.infoMsg = new LoadingDialog(soundbank.getName(), menu);

            this.prepareCustomSoundbank(soundbank);
            if(this.soundEnabled) {
                this.selectedSoundbank = soundbank.getName();
            }
            
            this.infoMsg.closeDialog();
        }
        
        menu.dispose();
        SoundFrame soundMenu = new SoundFrame(this);
        soundMenu.setVisible(true);
    }
    
    public void selectDefaultSoundbank(SoundFrame menu) {
        this.infoMsg = new LoadingDialog("resources/soundfont/sfx.sf2", menu);
        
        this.prepareDefaultSoundbank();
        
        this.infoMsg.closeDialog();
        menu.dispose();
        SoundFrame soundMenu = new SoundFrame(this);
        soundMenu.setVisible(true);
    }
    
    private void prepareDefaultSoundbank() {
        this.sineWaveIndex = this.DEFAULT_SINE_WAVE_INDEX;
        SFXFetcher sfxFetcher = new SFXFetcher();
        this.loadInstruments(sfxFetcher);
        this.selectedSoundbank = this.defaultSoundbank;
    }
    
    private void prepareCustomSoundbank(File file) {
        SFXFetcher sfxFetcher = new SFXFetcher(file);
        this.loadInstruments(sfxFetcher);
    }
    
    private String formatInstrumentName(String rawName) {
        int length = rawName.length();
        while(rawName.charAt(length - 1) == ' ') {
            length--;
        }
        StringBuilder formatter = new StringBuilder(rawName);
        String formattedName = formatter.substring(0, length);
        
        if(formattedName.length() > 3) {
            if(formattedName.subSequence(0, 4).equals("Type")) {
                return "Unnamed Sample";
            }
        }
        return formattedName;
    }
    
    public String[] getInstrumentList() {
        ArrayList<String> instrumentNames = new ArrayList<String>();
        Instrument[] instruments = this.synth.getLoadedInstruments();
        
        String rockOrgan = instruments[this.DEFAULT_ROCK_ORGAN_INDEX].getName();
        instrumentNames.add("a. Default Sound Effect (" + this.formatInstrumentName(rockOrgan) + ")");
        
        this.sineWaveIndex = 0;
        while(this.sineWaveIndex < instruments.length && !instruments[this.sineWaveIndex].getName().toLowerCase().trim().contains("sine")) {
            this.sineWaveIndex++;
        }
        if(this.sineWaveIndex >= instruments.length && this.DEFAULT_SINE_WAVE_INDEX < instruments.length) {
            this.sineWaveIndex = this.DEFAULT_SINE_WAVE_INDEX;
        }
        else if(this.DEFAULT_SINE_WAVE_INDEX >= instruments.length) {
            this.sineWaveIndex = 0;
        }
        
        String sineWave = instruments[this.sineWaveIndex].getName();
        instrumentNames.add("b. w0rthy's Original Sound Effect (" + this.formatInstrumentName(sineWave) + ")");
        
        for(int i = 0; i < instruments.length; i++) {
            String nextInstrument = instruments[i].getName();
            instrumentNames.add((i + 1) + ". " + this.formatInstrumentName(nextInstrument));
        }
        
        String[] instrumentArray = new String[instruments.length];
        for(int i = 0; i < instruments.length; i++) {
            instrumentArray[i] = instrumentNames.get(i);
        }
        
        return instrumentArray;
    }
    
    private void loadInstruments(SFXFetcher sfxFetcher) {
        BufferedInputStream stream = sfxFetcher.getStream();
        
        try {
            this.synth.loadAllInstruments(MidiSystem.getSoundbank(stream));
        }
        catch (NullPointerException e) {
            JErrorPane.invokeCustomErrorMessage("soundfont/sfx.sf2 missing: Couldn't find the default soundbank for the program's sound effects! The OS default will be used instead.");
        }
        catch (InvalidMidiDataException e) {
            JErrorPane.invokeCustomErrorMessage("soundfont/sfx.sf2 invalid or corrupt: The file for the program's default soundbank was not recognized as a proper MIDI soundfont! The OS default will be used instead.");
        }
        catch (IOException e) {
            JErrorPane.invokeErrorMessage(e);
        }
        finally {
            try {
                stream.close();
            }
            catch (Exception e) {
                JErrorPane.invokeErrorMessage(e);
            }
        }
        
        if(this.channels == null) {
            this.channels = new MidiChannel[this.NUMCHANNELS];
        }
        this.assignInstruments();
    }
    
    private void assignInstruments() {
        try {
            int programIndex;
            
            //TODO: Consider making into a function  
            switch(this.instrumentChoice) {
            case 0:  programIndex = this.DEFAULT_ROCK_ORGAN_INDEX; break;
            case 1:  programIndex = this.sineWaveIndex;            break;
            default: programIndex = this.instrumentChoice - 2;     break;
            }
            
            for(int i = 0; i < this.NUMCHANNELS; i++) {
                this.channels[i] = this.synth.getChannels()[i];
                this.channels[i].programChange(this.synth.getLoadedInstruments()[programIndex].getPatch().getProgram());
                this.channels[i].setChannelPressure(1);
            }
            if(this.channels[0].getProgram() == -1) {
                JErrorPane.invokeCustomErrorMessage("Could not find a valid MIDI instrument.");
                this.soundEnabled = false;
            }
        }
        catch(Exception e) {
            JErrorPane.invokeErrorMessage(e);
            this.soundEnabled = false;
        }
    }
    
    public void testInstrument(int programIndex) {
        this.testInstrumentChoice = programIndex;
        
        if(this.ArrayVisualizer.getSortingThread() != null && this.ArrayVisualizer.getSortingThread().isAlive()) {
            new Thread() {
                @Override
                public void run() {                
                    switch(Sounds.this.testInstrumentChoice) {
                    case 0:  Sounds.this.testInstrumentChoice  = Sounds.this.DEFAULT_ROCK_ORGAN_INDEX; break;
                    case 1:  Sounds.this.testInstrumentChoice  = Sounds.this.sineWaveIndex;            break;
                    default: Sounds.this.testInstrumentChoice -= 2;                                    break;
                    }

                    int savedInstrument;
                    //TODO: Consider making into a function  
                    switch(Sounds.this.instrumentChoice) {
                    case 0:  savedInstrument = Sounds.this.DEFAULT_ROCK_ORGAN_INDEX; break;
                    case 1:  savedInstrument = Sounds.this.sineWaveIndex;            break;
                    default: savedInstrument = Sounds.this.instrumentChoice - 2;     break;
                    }

                    try {
                        for(int i = 0; i < Sounds.this.NUMCHANNELS; i++) {
                            Sounds.this.channels[i].programChange(Sounds.this.synth.getLoadedInstruments()[Sounds.this.testInstrumentChoice].getPatch().getProgram());    
                        }
                        sleep(2000);
                        for(int i = 0; i < Sounds.this.NUMCHANNELS; i++) {
                            Sounds.this.channels[i].programChange(Sounds.this.synth.getLoadedInstruments()[savedInstrument].getPatch().getProgram());
                        }
                    } catch (InterruptedException e) {
                        JErrorPane.invokeErrorMessage(e);
                    }
                }
            }.start();
        }
        else {
            new Thread() {
                @Override
                public void run() {                
                    switch(Sounds.this.testInstrumentChoice) {
                    case 0:  Sounds.this.testInstrumentChoice  = Sounds.this.DEFAULT_ROCK_ORGAN_INDEX; break;
                    case 1:  Sounds.this.testInstrumentChoice  = Sounds.this.sineWaveIndex;            break;
                    default: Sounds.this.testInstrumentChoice -= 2;                                    break;
                    }

                    int savedInstrument;
                    //TODO: Consider making into a function  
                    switch(Sounds.this.instrumentChoice) {
                    case 0:  savedInstrument = Sounds.this.DEFAULT_ROCK_ORGAN_INDEX; break;
                    case 1:  savedInstrument = Sounds.this.sineWaveIndex;            break;
                    default: savedInstrument = Sounds.this.instrumentChoice - 2;     break;
                    }

                    try {
                        Sounds.this.channels[0].programChange(Sounds.this.synth.getLoadedInstruments()[Sounds.this.testInstrumentChoice].getPatch().getProgram());

                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 64);

                        Sounds.this.channels[0].noteOn(60, 100);
                        Sounds.this.channels[0].noteOn(64, 100);
                        Sounds.this.channels[0].noteOn(67, 100);
                        Sounds.this.channels[0].noteOn(72, 100);

                        sleep(500);

                        Sounds.this.channels[0].noteOff(60);
                        Sounds.this.channels[0].noteOff(64);
                        Sounds.this.channels[0].noteOff(67);
                        Sounds.this.channels[0].noteOff(72);
                        
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 0);
                        
                        /*
                        int eighth = 200;
                        int note;
                        
                        note = 62;
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 64);
                        Sounds.this.channels[0].noteOn(note, 100);
                        sleep(eighth - 10);
                        Sounds.this.channels[0].noteOff(note);
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 0);
                        sleep(10);
                        
                        note = 67;
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 64);
                        Sounds.this.channels[0].noteOn(note, 100);
                        sleep(eighth - 10);
                        Sounds.this.channels[0].noteOff(note);
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 0);
                        sleep(10);
                        
                        note = 69;
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 64);
                        Sounds.this.channels[0].noteOn(note, 100);
                        sleep(eighth - 10);
                        Sounds.this.channels[0].noteOff(note);
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 0);
                        sleep(10);
                        
                        note = 71;
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 64);
                        Sounds.this.channels[0].noteOn(note, 100);
                        sleep((eighth * 2) - 10);
                        Sounds.this.channels[0].noteOff(note);
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 0);
                        sleep(10);
                        
                        note = 71;
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 64);
                        Sounds.this.channels[0].noteOn(note, 100);
                        sleep(eighth - 10);
                        Sounds.this.channels[0].noteOff(note);
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 0);
                        sleep(10);
                        
                        note = 74;
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 64);
                        Sounds.this.channels[0].noteOn(note, 100);
                        sleep(eighth - 10);
                        Sounds.this.channels[0].noteOff(note);
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 0);
                        sleep(10);
                        
                        note = 69;
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 64);
                        Sounds.this.channels[0].noteOn(note, 100);
                        sleep((eighth * 2) - 10);
                        Sounds.this.channels[0].noteOff(note);
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 0);
                        sleep(10);
                        
                        note = 69;
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 64);
                        Sounds.this.channels[0].noteOn(note, 100);
                        sleep(eighth - 10);
                        Sounds.this.channels[0].noteOff(note);
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 0);
                        sleep(10);
                        
                        note = 71;
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 64);
                        Sounds.this.channels[0].noteOn(note, 100);
                        sleep(eighth - 10);
                        Sounds.this.channels[0].noteOff(note);
                        Sounds.this.channels[0].controlChange(Sounds.this.SUSTAIN_PEDAL, 0);
                        sleep(10);
                        */
                        
                        Sounds.this.channels[0].programChange(Sounds.this.synth.getLoadedInstruments()[savedInstrument].getPatch().getProgram());
                    } catch (InterruptedException e) {
                        JErrorPane.invokeErrorMessage(e);
                    }
                }
            }.start();
        }
    }
    
    public synchronized void toggleSounds(boolean val) {
        this.SOUND = val;
    }
    
    public synchronized void toggleSound(boolean val) {
        this.MIDI = val;
    }
    
    //Double check logic
    public void toggleSofterSounds(boolean val) {
        this.SOFTERSOUNDS = val;
        
        if(this.SOFTERSOUNDS) this.SOUNDMUL = 0.01;
        else                  this.SOUNDMUL = 1;
    }
    
    public double getVolume() {
        return this.SOUNDMUL;
    }
    public void changeVolume(double val) {
        this.SOUNDMUL = val;
    }
    
    public void changeNoteDelayAndFilter(int noteFactor) {
        if(noteFactor != this.noteDelay) {
            if(noteFactor > 1) {
                this.noteDelay = noteFactor;
                this.SOUNDMUL = 1d / noteFactor;
            }
            //Double check logic
            else {
                this.noteDelay = 1;
                
                if(this.SOFTERSOUNDS) this.SOUNDMUL = 0.01;
                else                  this.SOUNDMUL = 1;
            }
        }
    }
    
    public void startAudioThread() {
        if(!this.soundEnabled) {
            JOptionPane.showMessageDialog(null, "Sound is disabled.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        AudioThread.start();
    }
    
    public void closeSynth() {
        if(this.soundEnabled) {
            this.soundEnabled = false;
            this.synth.close();
        }
    }
}