package dialogs;

import javax.swing.filechooser.FileNameExtensionFilter;

final public class CustomImageDialog extends FileDialog {
    public CustomImageDialog() {
        super();
        
        FileNameExtensionFilter allImages = new FileNameExtensionFilter("Java-compatible Image Files (.jpeg, .jpg, .png, .gif, .bmp, .wbmp)", "jpeg", "jpg", "png", "gif", "bmp", "wbmp");
        FileNameExtensionFilter jpegImages = new FileNameExtensionFilter("JPEG Images", "jpeg", "jpg");
        FileNameExtensionFilter pngImages = new FileNameExtensionFilter("PNG Images", "png");
        FileNameExtensionFilter gifImages = new FileNameExtensionFilter("GIF Images", "gif");
        FileNameExtensionFilter bmpImages = new FileNameExtensionFilter("BMP Images", "bmp");
        FileNameExtensionFilter webmpImages = new FileNameExtensionFilter("WEBMP Images", "wbmp");

        this.removeAllFilesOption();
        this.fileDialog.addChoosableFileFilter(allImages);
        this.fileDialog.addChoosableFileFilter(jpegImages);
        this.fileDialog.addChoosableFileFilter(pngImages);
        this.fileDialog.addChoosableFileFilter(gifImages);
        this.fileDialog.addChoosableFileFilter(bmpImages);
        this.fileDialog.addChoosableFileFilter(webmpImages);
        
        this.fileDialog.setDialogTitle("Choose an image...");
        
        this.fileDialog.showDialog(null, "Select");
        this.file = this.fileDialog.getSelectedFile();
    }
}