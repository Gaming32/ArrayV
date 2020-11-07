package dialogs;

import java.awt.Dialog.ModalityType;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class LoadingDialog {
    /**
     * 
     */
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;
    private JOptionPane pane;
    private JDialog dialog;
    
    public LoadingDialog(String resource, JFrame parent) {
        this.pane = new JOptionPane("Loading " + resource + "...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[] {}, null);
        this.dialog = this.pane.createDialog(parent, "Info");
        this.dialog.setModalityType(ModalityType.MODELESS);
        this.dialog.setAlwaysOnTop(this.dialog.isAlwaysOnTopSupported());
        this.dialog.pack();
        this.dialog.setVisible(true);
    }

    public void closeDialog() {
        this.dialog.setVisible(false);
        this.dialog.dispose();
    }
}