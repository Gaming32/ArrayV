package io.github.arrayv.dialogs;

/*
MIT License

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

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public abstract class FileDialog {
    protected static JFileChooser fileDialog;
    protected File file;
    protected static volatile boolean initialized;

    public FileDialog() {
        while (!FileDialog.initialized) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        FileDialog.fileDialog.resetChoosableFileFilters();
    }

    public static void initialize() {
        FileDialog.fileDialog = new JFileChooser();
        FileDialog.fileDialog.setCurrentDirectory(new File(System.getProperty("user.dir")));
        FileDialog.fileDialog.setMultiSelectionEnabled(false);
        FileDialog.initialized = true;
    }

    protected void removeAllFilesOption() {
        FileFilter allFiles = FileDialog.fileDialog.getChoosableFileFilters()[0];
        FileDialog.fileDialog.removeChoosableFileFilter(allFiles);
    }

    public File getFile() {
        return this.file;
    }
}
