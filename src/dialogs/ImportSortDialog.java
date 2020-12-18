package dialogs;

import javax.swing.filechooser.FileNameExtensionFilter;

final public class ImportSortDialog extends FileDialog {
    public ImportSortDialog() {
        super();

        FileNameExtensionFilter javaFiles = new FileNameExtensionFilter("Java Source Files (.java)", "java");
        this.removeAllFilesOption();
        this.fileDialog.addChoosableFileFilter(javaFiles);

        this.fileDialog.setDialogTitle("Choose a sort file to import...");

        this.fileDialog.showDialog(null, "Select");
        this.file = this.fileDialog.getSelectedFile();
    }
}