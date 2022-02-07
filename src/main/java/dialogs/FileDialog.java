package dialogs;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public abstract class FileDialog {
    protected static JFileChooser fileDialog;
    protected File file;
    protected static volatile boolean initialized;

    public FileDialog() {
        while (!FileDialog.initialized) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        FileDialog.fileDialog.resetChoosableFileFilters();
    }

    public static void initialize() {
        FileDialog.fileDialog = new JFileChooser();
        FileDialog.fileDialog.setCurrentDirectory(new File(System.getProperty("user.dir")));
        FileDialog.fileDialog.setMultiSelectionEnabled(false);
        FileDialog.initialized = true;
    }

    protected void removeAllFilesOption() {
        FileFilter allFiles = FileDialog.fileDialog.getChoosableFileFilters()[0];
        FileDialog.fileDialog.removeChoosableFileFilter(allFiles);
    }

    public File getFile() {
        return this.file;
    }
}