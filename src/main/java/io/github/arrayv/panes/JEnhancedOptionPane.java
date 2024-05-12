package io.github.arrayv.panes;

import javax.swing.*;
import java.awt.*;

// Many thanks to Freek de Bruijn on StackOverflow for providing a custom JOptionPane.
// https://stackoverflow.com/questions/14407804/how-to-change-the-default-text-of-buttons-in-joptionpane-showinputdialog?noredirect=1&lq=1
public final class JEnhancedOptionPane extends JOptionPane {
    private static final long serialVersionUID = 1L;

    /**
     * Prompts the user with a textbox and returns their answer, or null if they didn't confirm.
     * The buttons are customizable, but option 0 is always defined as the Confirm action.
     *
     * @param title Title bar
     * @param message Prompt message
     * @param options Buttons to be clicked. You usually just want to pass a String[] of labels here, e.g. ["OK", "Cancel"].
     *
     * @return The user input, or null if they closed out or picked a secondary option.
     */
    public static String showInputDialog(final String title, final Object message, final Object[] options)
            throws HeadlessException {
        final JOptionPane pane = new JOptionPane(message, QUESTION_MESSAGE,
                OK_CANCEL_OPTION, null,
                options, null);
        pane.setWantsInput(true);
        pane.setComponentOrientation((getRootFrame()).getComponentOrientation());
        pane.setMessageType(QUESTION_MESSAGE);
        pane.selectInitialValue();

        final JDialog dialog = pane.createDialog(null, title);
        dialog.setVisible(true);
        dialog.dispose();

        final Object input = pane.getInputValue();
        final Object button = pane.getValue();

        return button == options[0] ? (String) input : null;
    }
}
