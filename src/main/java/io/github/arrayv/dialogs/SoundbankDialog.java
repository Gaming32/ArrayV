package io.github.arrayv.dialogs;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class SoundbankDialog extends FileDialog {
    public SoundbankDialog() {
        super();

        FileNameExtensionFilter allSoundbanks = new FileNameExtensionFilter("Any MIDI Soundbank (.sf2, .dls, .gm)", "sf2", "dls", "gm");
        FileNameExtensionFilter soundfonts = new FileNameExtensionFilter("Soundfonts (.sf2)", "sf2");
        FileNameExtensionFilter downloadableSounds = new FileNameExtensionFilter("Downloadable Sounds (.dls)", "dls");
        FileNameExtensionFilter generalMIDI = new FileNameExtensionFilter("General MIDI (.gm)", "gm");

        this.removeAllFilesOption();
        fileDialog.addChoosableFileFilter(allSoundbanks);
        fileDialog.addChoosableFileFilter(soundfonts);
        fileDialog.addChoosableFileFilter(downloadableSounds);
        fileDialog.addChoosableFileFilter(generalMIDI);

        fileDialog.setDialogTitle("Choose a MIDI soundbank...");

        this.file = fileDialog.showDialog(null, "Select") == JFileChooser.APPROVE_OPTION
            ? fileDialog.getSelectedFile()
            : null;
    }
}
