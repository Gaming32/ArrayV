package io.github.arrayv.panes;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public final class JErrorPane extends JOptionPane {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static volatile boolean errorMessageActive = false;

    private static JTextArea createTextArea(String errorMsg) {
        JTextArea error = new JTextArea();
        error.setText(errorMsg);
        error.setCaretPosition(0);
        error.setEditable(false);
        return error;
    }

    public static void invokeErrorMessage(Throwable e, String title) {
        errorMessageActive = true;

        StringWriter exceptionString = new StringWriter();
        e.printStackTrace(new PrintWriter(exceptionString));
        String printException = exceptionString.toString();

        JTextArea error = createTextArea(printException);

        JOptionPane.showMessageDialog(null, error, title, JOptionPane.ERROR_MESSAGE);
        errorMessageActive = false;
    }

    public static void invokeErrorMessage(Throwable e) {
        JErrorPane.invokeErrorMessage(e, "Error");
    }

    public static void invokeCustomErrorMessage(String errorMsg) {
        errorMessageActive = true;

        JOptionPane.showMessageDialog(null, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
        errorMessageActive = false;
    }

    public static boolean isErrorMessageActive() {
        return errorMessageActive;
    }
}
