package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JOptionPane;

import dialogs.LoadingDialog;
import dialogs.SoundbankDialog;
import frames.SoundFrame;
import main.ArrayVisualizer;
import panes.JErrorPane;

/*
 *
MIT License

Copyright (c) 2019 w0rthy
Copyright (c) 2021 ArrayV Team

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

public final class Sounds {
    private static final boolean ALLOW_PERCUSSION_SOUNDS = Boolean.getBoolean("arrayv.allowPercussion");
    private static final boolean DISABLE_JITTER_CORRECTION = Boolean.getBoolean("arrayv.disableJitterCorrection");

    private static final Class<?> SOFT_SYNTHESIZER_CLASS;

    static {
        if (DISABLE_JITTER_CORRECTION) {
            Class<?> synthesizerClass;
            try {
                synthesizerClass = Class.forName("com.sun.media.sound.SoftSynthesizer");
            } catch (Exception e) {
                synthesizerClass = null;
            }
            SOFT_SYNTHESIZER_CLASS = synthesizerClass;
        } else {
            SOFT_SYNTHESIZER_CLASS = null;
        }
    }

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

    private String defaultSoundbank = "Default (Yamaha XG Sound Set)";
    private String selectedSoundbank;

    private int sineWaveIndex;
    final private int DEFAULT_SINE_WAVE_INDEX = 193;
    final private int DEFAULT_ROCK_ORGAN_INDEX = 16;

    private volatile LoadingDialog infoMsg;

    private volatile int instrumentChoice;
    private volatile int testInstrumentChoice;

    public Sounds(int[] array, ArrayVisualizer arrayVisualizer) {
        this.array = array;
        this.ArrayVisualizer = arrayVisualizer;
        this.Highlights = ArrayVisualizer.getHighlights();

        this.SOUND = true;
        this.MIDI = true;
        this.NUMCHANNELS = ALLOW_PERCUSSION_SOUNDS ? 16 : 15;
        this.PITCHMIN = 25d;
        this.PITCHMAX = 105d;
        this.SOUNDMUL = 1d;

        this.noteDelay = 1;

        this.soundEnabled = true;

        try {
            MidiSystem.getSequencer(false);
            synth = MidiSystem.getSynthesizer();
            if (SOFT_SYNTHESIZER_CLASS != null && SOFT_SYNTHESIZER_CLASS.isInstance(synth)) {
                Map<String, Object> params = Collections.singletonMap("jitter correction", Boolean.FALSE);
                try {
                    Method openMethod = SOFT_SYNTHESIZER_CLASS.getDeclaredMethod("open", SourceDataLine.class, Map.class);
                    openMethod.invoke(synth, null, params);
                } catch (IllegalAccessException e) {
                    synth.open(); // Can't do special opening here. Java 9's module system stops us.
                } catch (Exception e) {
                    System.err.println("Failed to open SoftSynthesizer specially, opening normally");
                    e.printStackTrace();
                    synth.open();
                }
            } else {
                synth.open();
            }
        } catch (MidiUnavailableException e) {
            JErrorPane.invokeCustomErrorMessage("The default MIDI device is unavailable, possibly because it is already being used by another application.");
            this.soundEnabled = false;
        } catch (SecurityException e) {
            JErrorPane.invokeErrorMessage(e);
            this.soundEnabled = false;
        }

        this.instrumentChoice = 0;
        this.prepareDefaultSoundbank();

        this.AudioThread = new Thread("AudioThread") {
            @Override
            public void run() {
                while (Sounds.this.soundEnabled) {
                    for (MidiChannel channel : channels) {
                        channel.allNotesOff();
                    }
                    if (SOUND == false || MIDI == false || JErrorPane.errorMessageActive) {
                        synchronized (Sounds.this) {
                            try {
                                Sounds.this.wait();
                            } catch (InterruptedException e) {
                                break;
                            }
                        }
                        continue;
                    }

                    int noteCount = Math.min(Highlights.getMarkCount(), NUMCHANNELS);
                    noteCount = noteCount < 0 ? NUMCHANNELS : noteCount;
                    int channel = 0;

                    int playNoteCount = Math.max(noteCount, 1);
                    int currentLen = ArrayVisualizer.getCurrentLength();

                    for (int i : Highlights.highlightList()) {
                        try {
                            if (i != -1) {
                                if (!ALLOW_PERCUSSION_SOUNDS && channel == 9) {
                                    channel++;
                                    playNoteCount++;
                                }

                                //PITCH
                                double pitch = Sounds.this.array[Math.min(Math.max(i, 0), currentLen - 1)] / (double) currentLen * (PITCHMAX - PITCHMIN) + PITCHMIN;
                                int pitchmajor = (int) pitch;
                                int pitchminor = (int)((pitch-((int)pitch))*8192d)+8192;

                                int vel = (int) (Math.pow(PITCHMAX - pitchmajor, 2d) * (Math.pow(noteCount, -0.25)) * 64d * SOUNDMUL) / 2; //I'VE SOLVED IT!!

                                if (SOUNDMUL >= 1 && vel < 256) {
                                    vel *= vel;
                                }

                                channels[channel].noteOn(pitchmajor, vel);
                                channels[channel].setPitchBend(pitchminor);
                                channels[channel].controlChange(REVERB, 10);

                                if (++channel == playNoteCount)
                                    break;
                            }
                        } catch (Exception e) {
                            JErrorPane.invokeErrorMessage(e);
                        }
                    }
                    try {
                        for (int i = 0; i < Sounds.this.noteDelay; i++) {
                            sleep(1);
                        }
                    } catch (Exception e) {
                        JErrorPane.invokeErrorMessage(e);
                    }
                }
            }
        };
    }

    public boolean isEnabled() {
        return this.soundEnabled;
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

        if (soundbank != null) {
            this.infoMsg = new LoadingDialog(soundbank.getName(), menu);

            this.prepareCustomSoundbank(soundbank);
            if (this.soundEnabled) {
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
        InputStream is = getClass().getResourceAsStream("/sfx.sf2");
        this.loadInstruments(is);
        this.selectedSoundbank = this.defaultSoundbank;
    }

    private void prepareCustomSoundbank(File file) {
        InputStream is;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            JErrorPane.invokeErrorMessage(e);
            return;
        }
        this.loadInstruments(is);
    }

    private String formatInstrumentName(String rawName) {
        int length = rawName.length();
        while (rawName.charAt(length - 1) == ' ') {
            length--;
        }
        StringBuilder formatter = new StringBuilder(rawName);
        String formattedName = formatter.substring(0, length);

        if (formattedName.length() > 3) {
            if (formattedName.subSequence(0, 4).equals("Type")) {
                return "Unnamed Sample";
            }
        }
        return formattedName;
    }

    public String[] getInstrumentList() {
        ArrayList<String> instrumentNames = new ArrayList<String>();
        Instrument[] instruments = this.synth.getLoadedInstruments();

        if (instruments.length == 0) {
            return new String[0];
        }

        String rockOrgan = instruments[this.DEFAULT_ROCK_ORGAN_INDEX].getName();
        instrumentNames.add("a. Default Sound Effect (" + this.formatInstrumentName(rockOrgan) + ")");

        this.sineWaveIndex = 0;
        while (this.sineWaveIndex < instruments.length && !instruments[this.sineWaveIndex].getName().toLowerCase().trim().contains("sine")) {
            this.sineWaveIndex++;
        }
        if (this.sineWaveIndex >= instruments.length && this.DEFAULT_SINE_WAVE_INDEX < instruments.length) {
            this.sineWaveIndex = this.DEFAULT_SINE_WAVE_INDEX;
        } else if (this.DEFAULT_SINE_WAVE_INDEX >= instruments.length) {
            this.sineWaveIndex = 0;
        }

        String sineWave = instruments[this.sineWaveIndex].getName();
        instrumentNames.add("b. w0rthy's Original Sound Effect (" + this.formatInstrumentName(sineWave) + ")");

        for (int i = 0; i < instruments.length; i++) {
            String nextInstrument = instruments[i].getName();
            instrumentNames.add((i + 1) + ". " + this.formatInstrumentName(nextInstrument));
        }

        String[] instrumentArray = new String[instruments.length];
        for (int i = 0; i < instruments.length; i++) {
            instrumentArray[i] = instrumentNames.get(i);
        }

        return instrumentArray;
    }

    private void loadInstruments(InputStream stream) {
        try {
            this.synth.loadAllInstruments(MidiSystem.getSoundbank(stream));
        } catch (NullPointerException e) {
            JErrorPane.invokeCustomErrorMessage("soundfont/sfx.sf2 missing: Couldn't find the default soundbank for the program's sound effects! The OS default will be used instead.");
        } catch (InvalidMidiDataException e) {
            JErrorPane.invokeCustomErrorMessage("soundfont/sfx.sf2 invalid or corrupt: The file for the program's default soundbank was not recognized as a proper MIDI soundfont! The OS default will be used instead.");
        } catch (IOException e) {
            JErrorPane.invokeErrorMessage(e);
        } finally {
            try {
                stream.close();
            } catch (Exception e) {
                JErrorPane.invokeErrorMessage(e);
            }
        }

        if (this.channels == null) {
            this.channels = new MidiChannel[this.NUMCHANNELS];
        }
        this.assignInstruments();
    }

    private void assignInstruments() {
        try {
            int programIndex;

            //TODO: Consider making into a method
            switch(this.instrumentChoice) {
            case 0:  programIndex = this.DEFAULT_ROCK_ORGAN_INDEX; break;
            case 1:  programIndex = this.sineWaveIndex;            break;
            default: programIndex = this.instrumentChoice - 2;     break;
            }

            for (int i = 0; i < this.NUMCHANNELS; i++) {
                this.channels[i] = this.synth.getChannels()[i];
                this.channels[i].programChange(this.synth.getLoadedInstruments()[programIndex].getPatch().getProgram());
                this.channels[i].setChannelPressure(1);
            }
            if (this.channels[0].getProgram() == -1) {
                JErrorPane.invokeCustomErrorMessage("Could not find a valid MIDI instrument.");
                this.soundEnabled = false;
            }
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
            this.soundEnabled = false;
        }
    }

    public void testInstrument(int programIndex) {
        this.testInstrumentChoice = programIndex;

        if (this.ArrayVisualizer.isActive()) {
            new Thread("TestInstrumentThread") {
                @Override
                public void run() {
                    switch(Sounds.this.testInstrumentChoice) {
                    case 0:  Sounds.this.testInstrumentChoice  = Sounds.this.DEFAULT_ROCK_ORGAN_INDEX; break;
                    case 1:  Sounds.this.testInstrumentChoice  = Sounds.this.sineWaveIndex;            break;
                    default: Sounds.this.testInstrumentChoice -= 2;                                    break;
                    }

                    int savedInstrument;
                    //TODO: Consider making into a method
                    switch(Sounds.this.instrumentChoice) {
                    case 0:  savedInstrument = Sounds.this.DEFAULT_ROCK_ORGAN_INDEX; break;
                    case 1:  savedInstrument = Sounds.this.sineWaveIndex;            break;
                    default: savedInstrument = Sounds.this.instrumentChoice - 2;     break;
                    }

                    try {
                        for (int i = 0; i < Sounds.this.NUMCHANNELS; i++) {
                            Sounds.this.channels[i].programChange(Sounds.this.synth.getLoadedInstruments()[Sounds.this.testInstrumentChoice].getPatch().getProgram());
                        }
                        sleep(2000);
                        for (int i = 0; i < Sounds.this.NUMCHANNELS; i++) {
                            Sounds.this.channels[i].programChange(Sounds.this.synth.getLoadedInstruments()[savedInstrument].getPatch().getProgram());
                        }
                    } catch (InterruptedException e) {
                        JErrorPane.invokeErrorMessage(e);
                    }
                }
            }.start();
        } else {
            new Thread("TestInstrumentThread") {
                @Override
                public void run() {
                    switch(Sounds.this.testInstrumentChoice) {
                    case 0:  Sounds.this.testInstrumentChoice  = Sounds.this.DEFAULT_ROCK_ORGAN_INDEX; break;
                    case 1:  Sounds.this.testInstrumentChoice  = Sounds.this.sineWaveIndex;            break;
                    default: Sounds.this.testInstrumentChoice -= 2;                                    break;
                    }

                    int savedInstrument;
                    //TODO: Consider making into a method
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
        this.notifyAll();
    }

    public synchronized void toggleSound(boolean val) {
        this.MIDI = val;
        this.notifyAll();
    }

    //Double check logic
    public void toggleSofterSounds(boolean val) {
        this.SOFTERSOUNDS = val;

        if (this.SOFTERSOUNDS) this.SOUNDMUL = 0.01;
        else                   this.SOUNDMUL = 1;
    }

    public double getVolume() {
        return this.SOUNDMUL;
    }
    public void changeVolume(double val) {
        this.SOUNDMUL = val;
    }

    public void changeNoteDelayAndFilter(int noteFactor) {
        if (noteFactor != this.noteDelay) {
            if (noteFactor > 1) {
                this.noteDelay = noteFactor;
                this.SOUNDMUL = 1d / noteFactor;
            } else {
                //Double check logic
                this.noteDelay = 1;

                if (this.SOFTERSOUNDS) this.SOUNDMUL = 0.01;
                else                   this.SOUNDMUL = 1;
            }
        }
    }

    public void startAudioThread() {
        if (!this.soundEnabled) {
            JOptionPane.showMessageDialog(null, "Sound is disabled.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        AudioThread.start();
    }

    public void closeSynth() {
        if (this.soundEnabled) {
            this.soundEnabled = false;
            this.synth.close();
        }
    }
}