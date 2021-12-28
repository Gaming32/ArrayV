package dialogs;

import javax.swing.filechooser.FileNameExtensionFilter;

final public class ImportSortDialog extends FileDialog {
    public ImportSortDialog() {
        super();

        FileNameExtensionFilter javaFiles = new FileNameExtensionFilter("Java Source Files (.java)", "java");
        this.removeAllFilesOption();
        fileDialog.addChoosableFileFilter(javaFiles);

        fileDialog.setDialogTitle("Choose a sort file to import...");

        fileDialog.showOpenDialog(null);
        this.file = fileDialog.getSelectedFile();
    }
}