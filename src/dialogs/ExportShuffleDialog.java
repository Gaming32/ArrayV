package dialogs;

final public class ExportShuffleDialog extends FileDialog {
    public ExportShuffleDialog() {
        super();

        fileDialog.setDialogTitle("Choose where to export the current shuffle graph...");

        fileDialog.showSaveDialog(null);
        this.file = fileDialog.getSelectedFile();
    }
}