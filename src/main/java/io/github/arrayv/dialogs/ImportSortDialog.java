package io.github.arrayv.dialogs;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class ImportSortDialog extends FileDialog {
    public ImportSortDialog() {
        super();

        FileNameExtensionFilter javaFiles = new FileNameExtensionFilter("Java Source Files (.java)", "java");
        this.removeAllFilesOption();
        fileDialog.addChoosableFileFilter(javaFiles);

        fileDialog.setDialogTitle("Choose a sort file to import...");

        this.file = fileDialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION
            ? fileDialog.getSelectedFile()
            : null;
    }
}
