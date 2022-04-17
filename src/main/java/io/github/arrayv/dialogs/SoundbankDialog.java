package io.github.arrayv.dialogs;

import javax.swing.JFileChooser;

/*
MIT License

Copyright (c) 2020 Musicombo
Copyright (c) 2021-2022 ArrayV Team

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
*/

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
