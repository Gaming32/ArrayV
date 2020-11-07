package dialogs;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public abstract class FileDialog {
    protected JFileChooser fileDialog;
    protected File file;
    
    public FileDialog() {
        this.fileDialog = new JFileChooser();
        this.fileDialog.setMultiSelectionEnabled(false);
    }
    
    protected void removeAllFilesOption() {
        FileFilter allFiles = this.fileDialog.getChoosableFileFilters()[0];
        this.fileDialog.removeChoosableFileFilter(allFiles);
    }
    
    public File getFile() {
        return this.file;
    }
}