package dialogs;

final public class LoadCustomDistributionDialog extends FileDialog {
    public LoadCustomDistributionDialog() {
        super();
        
        this.fileDialog.setDialogTitle("Choose a distribution file...");
        
        this.fileDialog.showDialog(null, "Select");
        this.file = this.fileDialog.getSelectedFile();
    }
}