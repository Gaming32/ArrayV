package io.github.arrayv.dialogs;

/*
MIT License

Copyright (c) 2020 Musicombo
Copyright (c) 2022 ArrayV Team

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

import java.awt.Dialog.ModalityType;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class LoadingDialog {
    private JOptionPane pane;
    private JDialog dialog;

    public LoadingDialog(String resource, JFrame parent) {
        this.pane = new JOptionPane("Loading " + resource + "...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[] { }, null);
        this.dialog = this.pane.createDialog(parent, "Info");
        this.dialog.setModalityType(ModalityType.MODELESS);
        this.dialog.setAlwaysOnTop(this.dialog.isAlwaysOnTopSupported());
        this.dialog.pack();
        this.dialog.setVisible(true);
    }

    public void closeDialog() {
        this.dialog.setVisible(false);
        this.dialog.dispose();
    }
}
