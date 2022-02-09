package dialogs;

final public class SaveArrayDialog extends FileDialog {
    public SaveArrayDialog() {
        super();

        fileDialog.setDialogTitle("Choose where to save the contents of the main array...");

        fileDialog.showSaveDialog(null);
        this.file = fileDialog.getSelectedFile();
    }
}