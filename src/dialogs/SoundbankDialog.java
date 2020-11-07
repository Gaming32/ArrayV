package dialogs;

import javax.swing.filechooser.FileNameExtensionFilter;

final public class SoundbankDialog extends FileDialog {
    public SoundbankDialog() {
        super();
        
        FileNameExtensionFilter allSoundbanks = new FileNameExtensionFilter("Any MIDI Soundbank (.sf2, .dls, .gm)", "sf2", "dls", "gm");
        FileNameExtensionFilter soundfonts = new FileNameExtensionFilter("Soundfonts (.sf2)", "sf2");
        FileNameExtensionFilter downloadableSounds = new FileNameExtensionFilter("Downloadable Sounds (.dls)", "dls");
        FileNameExtensionFilter generalMIDI = new FileNameExtensionFilter("General MIDI (.gm)", "gm");
        
        this.removeAllFilesOption();
        this.fileDialog.addChoosableFileFilter(allSoundbanks);
        this.fileDialog.addChoosableFileFilter(soundfonts);
        this.fileDialog.addChoosableFileFilter(downloadableSounds);
        this.fileDialog.addChoosableFileFilter(generalMIDI);
        
        this.fileDialog.setDialogTitle("Choose a MIDI soundbank...");

        this.fileDialog.showDialog(null, "Select");
        this.file = this.fileDialog.getSelectedFile();
    }
}