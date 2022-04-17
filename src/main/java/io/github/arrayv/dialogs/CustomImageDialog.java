package io.github.arrayv.dialogs;

import javax.swing.JFileChooser;

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

import javax.swing.filechooser.FileNameExtensionFilter;

public final class CustomImageDialog extends FileDialog {
    public CustomImageDialog() {
        super();

        FileNameExtensionFilter allImages = new FileNameExtensionFilter("Java-compatible Image Files (.jpeg, .jpg, .png, .gif, .bmp, .wbmp)", "jpeg", "jpg", "png", "gif", "bmp", "wbmp");
        FileNameExtensionFilter jpegImages = new FileNameExtensionFilter("JPEG Images", "jpeg", "jpg");
        FileNameExtensionFilter pngImages = new FileNameExtensionFilter("PNG Images", "png");
        FileNameExtensionFilter gifImages = new FileNameExtensionFilter("GIF Images", "gif");
        FileNameExtensionFilter bmpImages = new FileNameExtensionFilter("BMP Images", "bmp");
        FileNameExtensionFilter webmpImages = new FileNameExtensionFilter("WEBMP Images", "wbmp");

        this.removeAllFilesOption();
        fileDialog.addChoosableFileFilter(allImages);
        fileDialog.addChoosableFileFilter(jpegImages);
        fileDialog.addChoosableFileFilter(pngImages);
        fileDialog.addChoosableFileFilter(gifImages);
        fileDialog.addChoosableFileFilter(bmpImages);
        fileDialog.addChoosableFileFilter(webmpImages);

        fileDialog.setDialogTitle("Choose an image...");

        this.file = fileDialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION
            ? fileDialog.getSelectedFile()
            : null;
    }
}
