package io.github.arrayv.dialogs;

final public class ImportShuffleDialog extends FileDialog {
    public ImportShuffleDialog() {
        super();

        fileDialog.setDialogTitle("Choose where to import the current shuffle graph from...");

        fileDialog.showOpenDialog(null);
        this.file = fileDialog.getSelectedFile();
    }
}