package dialogs;

final public class RunScriptDialog extends FileDialog {
    public RunScriptDialog() {
        super();

        this.fileDialog.setDialogTitle("Choose a script file...");

        this.fileDialog.showDialog(null, "Select");
        this.file = this.fileDialog.getSelectedFile();
    }
}