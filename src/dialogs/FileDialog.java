package dialogs;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public abstract class FileDialog {
    protected static JFileChooser fileDialog;
    protected File file;
    
    public FileDialog() {
        if (FileDialog.fileDialog == null) {
            FileDialog.fileDialog = new JFileChooser();
            FileDialog.fileDialog.setMultiSelectionEnabled(false);
        }
        FileDialog.fileDialog.resetChoosableFileFilters();
    }
    
    protected void removeAllFilesOption() {
        FileFilter allFiles = FileDialog.fileDialog.getChoosableFileFilters()[0];
        FileDialog.fileDialog.removeChoosableFileFilter(allFiles);
    }
    
    public File getFile() {
        return this.file;
    }
}