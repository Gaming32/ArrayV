package dialogs;

final public class LoadCustomDistributionDialog extends FileDialog {
    public LoadCustomDistributionDialog() {
        super();

        fileDialog.setDialogTitle("Choose a distribution file...");

        fileDialog.showDialog(null, "Select");
        this.file = fileDialog.getSelectedFile();
    }
}