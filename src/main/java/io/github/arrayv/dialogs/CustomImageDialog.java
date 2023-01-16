package io.github.arrayv.dialogs;

import javax.swing.*;
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
