package dialogs;

import javax.swing.filechooser.FileNameExtensionFilter;

final public class SaveArrayDialog extends FileDialog {
    public SaveArrayDialog() {
        super();

        this.fileDialog.setDialogTitle("Choose where to save the contents of the main array...");

        this.fileDialog.showSaveDialog(null);
        this.file = this.fileDialog.getSelectedFile();
    }
}