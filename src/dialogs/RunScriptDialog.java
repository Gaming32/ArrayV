package dialogs;

final public class RunScriptDialog extends FileDialog {
    public RunScriptDialog() {
        super();

        fileDialog.setDialogTitle("Choose a script file...");

        fileDialog.showDialog(null, "Select");
        this.file = fileDialog.getSelectedFile();
    }
}