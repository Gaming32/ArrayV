package io.github.arrayv.frames;

import io.github.arrayv.dialogs.RunScriptDialog;
import io.github.arrayv.main.ArrayManager;
import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.panes.JErrorPane;
import io.github.arrayv.prompts.ShufflePrompt;
import io.github.arrayv.prompts.SortPrompt;
import io.github.arrayv.prompts.ViewPrompt;
import io.github.arrayv.utils.Delays;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Sounds;
import io.github.arrayv.utils.Timer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/*
 *
MIT License

Copyright (c) 2019 w0rthy
Copyright (c) 2021-2022 ArrayV Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

public final class UtilFrame extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;

    private boolean jCheckBox9WarningShown = true; //set to false to enable warning

    private final int[] array;

    private final ArrayManager arrayManager;
    private final ArrayVisualizer arrayVisualizer;
    private final Delays Delays;
    private final Highlights Highlights;
    private final JFrame frame;
    private final Timer Timer;
    private final Sounds Sounds;

    private AppFrame abstractFrame;

    public UtilFrame(int[] array, ArrayVisualizer arrayVisualizer) {
        this.array = array;

        this.arrayVisualizer = arrayVisualizer;
        this.arrayManager = arrayVisualizer.getArrayManager();

        this.Delays = arrayVisualizer.getDelays();
        this.frame = arrayVisualizer.getMainWindow();
        this.Highlights = arrayVisualizer.getHighlights();
        this.Timer = arrayVisualizer.getTimer();
        this.Sounds = arrayVisualizer.getSounds();

        setUndecorated(true);
        initComponents();
        setLocation(Math.min((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth(), frame.getX() + frame.getWidth()), frame.getY() + 29);
        setAlwaysOnTop(false);
        setVisible(true);
    }

    public void reposition(ArrayFrame af){
        toFront();
        setLocation(Math.min((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth(), frame.getX() + frame.getWidth() + af.getWidth()), frame.getY() + 29);
        if (this.abstractFrame != null && abstractFrame.isVisible())
            abstractFrame.reposition();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        // Variables declaration - do not modify//GEN-BEGIN:variables
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        this.jButton1 = new javax.swing.JButton();
        this.jButton2 = new javax.swing.JButton();
        JButton jButton3 = new JButton();
        this.jCheckBox1 = new javax.swing.JCheckBox();
        this.jCheckBox2 = new javax.swing.JCheckBox();
        JButton jButton4 = new JButton();
        JButton jButton7 = new JButton();
        this.jCheckBox3 = new javax.swing.JCheckBox();
        this.jCheckBox4 = new javax.swing.JCheckBox();
        JButton jButton5 = new JButton();
        this.jCheckBox5 = new javax.swing.JCheckBox();
        this.jButton6 = new javax.swing.JButton();
        this.jCheckBox6 = new javax.swing.JCheckBox();
        this.jCheckBox7 = new javax.swing.JCheckBox();
        this.jCheckBox8 = new javax.swing.JCheckBox();
        this.jCheckBox9 = new javax.swing.JCheckBox();
        this.jComboBox1 = new javax.swing.JComboBox();

        jLabel1.setText("Settings");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jButton1ResetText();
        jButton1.addActionListener(evt -> jButton1ActionPerformed());

        jButton2ResetText();
        jButton2.addActionListener(evt -> jButton2ActionPerformed());

        jButton3.setText("Change Speed");
        jButton3.addActionListener(evt -> jButton3ActionPerformed());

        jButton4.setText("Cancel Delays");
        jButton4.addActionListener(evt -> jButton4ActionPerformed());

        jButton7.setText("Cancel Sort");
        jButton7.addActionListener(evt -> jButton7ActionPerformed());

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Show Shuffle");
        jCheckBox1.addActionListener(evt -> jCheckBox1ActionPerformed());

        jCheckBox2.setSelected(false);
        jCheckBox2.setText("Linked Dots");
        jCheckBox2.addActionListener(evt -> jCheckBox2ActionPerformed());

        jCheckBox3.setSelected(true);
        jCheckBox3.setText("End Sweep Anim");
        jCheckBox3.addActionListener(evt -> jCheckBox3ActionPerformed());

        jCheckBox4.setSelected(true);
        jCheckBox4.setText("Calc Real Time");
        jCheckBox4.addActionListener(evt -> jCheckBox4ActionPerformed());

        jButton5.setText("Clear Stats");
        jButton5.addActionListener(evt -> jButton5ActionPerformed());

        jCheckBox5.setSelected(false);
        jCheckBox5.setText("Softer Sounds");
        jCheckBox5.addActionListener(evt -> jCheckBox5ActionPerformed());

        jButton6ResetText();
        jButton6.addActionListener(evt -> jButton6ActionPerformed());

        jCheckBox6.setSelected(true);
        jCheckBox6.setText("Display Stats");
        jCheckBox6.addActionListener(evt -> jCheckBox6ActionPerformed());

        jCheckBox7.setSelected(true);
        jCheckBox7.setText("Enable Sounds");
        jCheckBox7.addActionListener(evt -> jCheckBox7ActionPerformed());

        jCheckBox8.setSelected(false);
        jCheckBox8.setText("Enable Color");
        jCheckBox8.addActionListener(evt -> jCheckBox8ActionPerformed());

        jCheckBox9.setSelected(false);
        jCheckBox9.setText("Show Aux Arrays");
        jCheckBox9.addActionListener(evt -> jCheckBox9ActionPerformed());

        jComboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
            "Sorting",
            "AntiQSort",
            "Stability Check",
            "Sorting Networks",
            "Reversed Sorting"
            // "*Simple* Benchmarking"
        }));
        jComboBox1.addActionListener(evt -> jComboBox1ActionPerformed());
        if (arrayVisualizer.isDisabledStabilityCheck()) {
            jComboBox1.removeItem("Stability Check");
        }

        JButton runScriptButton = new JButton("Run Script");
        runScriptButton.addActionListener(e -> {
            File scriptFile = new RunScriptDialog().getFile();
            if (scriptFile == null) return;
            try {
                arrayVisualizer.getScriptManager().runInThread(scriptFile);
            } catch (IOException e1) {
                JErrorPane.invokeErrorMessage(e1, "Run Script");
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER, true)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER, true)
                        .addComponent(jLabel1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, true)
                            .addComponent(this.jCheckBox1)
                            .addComponent(this.jCheckBox2)
                            .addComponent(this.jCheckBox3)
                            .addComponent(this.jCheckBox4)
                            .addComponent(this.jCheckBox6)
                            .addComponent(this.jCheckBox7)
                            .addComponent(this.jCheckBox8)
                            .addComponent(this.jCheckBox9)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, true)
                                .addComponent(this.jCheckBox5)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(this.jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(this.jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(this.jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(runScriptButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(this.jComboBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(0, 10, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, true)
                .addGroup(layout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addComponent(jLabel1)
                    .addGap(7, 7, 7)
                    .addComponent(this.jComboBox1)
                    .addGap(10, 10, 10)
                    .addComponent(this.jButton2)
                    .addGap(5, 5, 5)
                    .addComponent(this.jCheckBox2)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(this.jCheckBox8)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(this.jCheckBox9)
                    .addGap(7, 7, 7)
                    .addComponent(jButton3)
                    .addGap(12, 12, 12)
                    .addComponent(this.jButton1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton7)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton4)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(this.jButton6)
                    .addGap(7, 7, 7)
                    .addComponent(this.jCheckBox1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(this.jCheckBox7)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(this.jCheckBox5)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(this.jCheckBox3)
                    .addGap(8, 8, 8)
                    .addComponent(jButton5)
                    .addGap(5, 5, 5)
                    .addComponent(this.jCheckBox6)
                    .addComponent(this.jCheckBox4)
                    .addGap(5, 5, 5)
                    .addComponent(runScriptButton)
                    .addGap(8, 8, 8))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void setMode(String mode) {
        this.jComboBox1.setSelectedItem(mode);
    }

    private void jButton1ActionPerformed() {//GEN-FIRST:event_jButton1ActionPerformed
        //CHANGE SORT
        if (this.abstractFrame != null && abstractFrame.isVisible()){
            boolean tmp = this.abstractFrame instanceof SortPrompt;
            abstractFrame.dispose();
            jButton1ResetText();
            if (tmp)
                return;
        }
        this.abstractFrame = new SortPrompt(this.array, this.arrayVisualizer, this.frame, this);
        jButton1.setText("Close");
        jButton2ResetText();
        jButton6ResetText();
    }//GEN-LAST:event_jButton1ActionPerformed

    public void jButton1ResetText() {
        jButton1.setText("Choose Sort");
    }

    public void jButton1Enable() {
        jButton1.setEnabled(true);
    }

    public void jButton1Disable() {
        jButton1.setEnabled(false);
    }

    private void jButton2ActionPerformed() {//GEN-FIRST:event_jButton2ActionPerformed
        //CHANGE VIEW
        if (this.abstractFrame != null && abstractFrame.isVisible()){
            boolean tmp = this.abstractFrame instanceof ViewPrompt;
            jButton2ResetText();
            abstractFrame.dispose();
            if (tmp)
                return;
        }
        this.abstractFrame = new ViewPrompt(this.arrayVisualizer, this.frame, this);
        jButton2.setText("Close");
        jButton1ResetText();
        jButton6ResetText();
    }//GEN-LAST:event_jButton2ActionPerformed

    public void jButton2ResetText() {
        jButton2.setText("Visual Style");
    }

    private void jButton3ActionPerformed() {//GEN-FIRST:event_jButton3ActionPerformed
        boolean speedPromptAllowed;

        if (this.abstractFrame == null) {
            speedPromptAllowed = true;
        } else {
            speedPromptAllowed = !this.abstractFrame.isVisible();
        }

        if (speedPromptAllowed) {
            boolean showPrompt = true;
            while (showPrompt) {
                try {
                    double oldRatio = Delays.getSleepRatio();
                    String userInput = JOptionPane.showInputDialog(null, "Modify the visual's speed below (Ex. 10 = Ten times faster)", oldRatio);
                    if (userInput != null) {
                        double newRatio = Double.parseDouble(userInput);
                        if (newRatio == 0) throw new Exception("Divide by zero");
                        Delays.setSleepRatio(newRatio);
                        Delays.updateCurrentDelay(oldRatio, Delays.getSleepRatio());
                    }
                    showPrompt = false;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Not a number! (" + e.getMessage() + ")", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jCheckBox1ActionPerformed() {//GEN-FIRST:event_jCheckBox2ActionPerformed
        arrayVisualizer.toggleShuffleAnimation(jCheckBox1.isSelected());
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed() {//GEN-FIRST:event_jCheckBox3ActionPerformed
        arrayVisualizer.toggleLinkedDots(jCheckBox2.isSelected());
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox3ActionPerformed() {//GEN-FIRST:event_jCheckBox3ActionPerformed
        Highlights.toggleFancyFinishes(jCheckBox3.isSelected());
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jButton4ActionPerformed() {//GEN-FIRST:event_jButton4ActionPerformed
        Delays.changeSkipped(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton7ActionPerformed() {//GEN-FIRST:event_jButton4ActionPerformed
        arrayVisualizer.setCanceled(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jCheckBox4ActionPerformed() {//GEN-FIRST:event_jButton4ActionPerformed
        Timer.toggleRealTimer(jCheckBox4.isSelected());
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jButton5ActionPerformed() {//GEN-FIRST:event_jButton4ActionPerformed
        arrayVisualizer.resetAllStatistics();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jCheckBox5ActionPerformed() {//GEN-FIRST:event_jButton4ActionPerformed
        Sounds.setSofterSounds(jCheckBox5.isSelected());
    }//GEN-LAST:event_jCheckBox5ActionPerformed

    private void jButton6ActionPerformed() {//GEN-FIRST:event_jButton2ActionPerformed
        //CHANGE SIZE
        if (this.abstractFrame != null && abstractFrame.isVisible()){
            boolean tmp = this.abstractFrame instanceof ShufflePrompt;
            abstractFrame.dispose();
            jButton6ResetText();
            if (tmp)
                return;
        }
        this.abstractFrame = new ShufflePrompt(this.arrayManager, this.frame, this);
        jButton6.setText("Close");
        jButton1ResetText();
        jButton2ResetText();
    }//GEN-LAST:event_jButton7ActionPerformed

    public void jButton6ResetText() {
        jButton6.setText("Choose Shuffle");
    }

    private void jCheckBox6ActionPerformed() {//GEN-FIRST:event_jButton4ActionPerformed
        arrayVisualizer.toggleStatistics(jCheckBox6.isSelected());
    }//GEN-LAST:event_jCheckBox6ActionPerformed

    private void jCheckBox7ActionPerformed() {//GEN-FIRST:event_jButton4ActionPerformed
        Sounds.toggleSounds(jCheckBox7.isSelected());
    }//GEN-LAST:event_jCheckBox7ActionPerformed

    private void jCheckBox8ActionPerformed() {//GEN-FIRST:event_jButton4ActionPerformed
        arrayVisualizer.toggleColor(jCheckBox8.isSelected());
    }//GEN-LAST:event_jCheckBox8ActionPerformed

    private void jCheckBox9ActionPerformed() {//GEN-FIRST:event_jButton4ActionPerformed
        if (!jCheckBox9WarningShown && jCheckBox9.isSelected()) {
            if (JOptionPane.showConfirmDialog(
                null,
                "<html>This will cause some sorts have extreme strobing/flashing."
                    + "<br><strong>It is highly recommended to NOT enable the \"" + jCheckBox9.getText() + "\" option if you may be at risk of seizures.</strong>"
                    + "<br>Are you sure you wish to enable this option?</html>",
                "Seizure Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            ) == JOptionPane.NO_OPTION) {
                jCheckBox9.setSelected(false);
                return;
            }
            jCheckBox9WarningShown = true;
        }
        arrayVisualizer.toggleExternalArrays(jCheckBox9.isSelected());
    }//GEN-LAST:event_jCheckBox8ActionPerformed

    private void jComboBox1ActionPerformed() {//GEN-FIRST:event_jButton4ActionPerformed
        //noinspection DataFlowIssue
        switch ((String)jComboBox1.getSelectedItem()) {
            case "Sorting":
                if (arrayVisualizer.enableBenchmarking(false))
                    break;
                jButton6.setEnabled(true);
                arrayVisualizer.setComparator(0);
                break;

            case "AntiQSort":
                if (arrayVisualizer.enableBenchmarking(false))
                    break;
                if (this.abstractFrame != null && abstractFrame.isVisible()){
                    abstractFrame.dispose();
                    jButton6ResetText();
                }
                jButton6.setEnabled(false);
                arrayVisualizer.setComparator(1);
                break;

            case "Stability Check":
                if (arrayVisualizer.enableBenchmarking(false))
                    break;
                jButton6.setEnabled(true);
                arrayVisualizer.setComparator(2);
                break;

            case "Sorting Networks":
                if (arrayVisualizer.enableBenchmarking(false))
                    break;
                jButton6.setEnabled(true);
                arrayVisualizer.setComparator(4);
                if (arrayVisualizer.getCurrentLength() > 1024) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Large sorting networks can take a long time (and high RAM usage) to visualize.\n" +
                            "A length of 1024 or less is recommended.",
                        "Sorting Network Visualizer", JOptionPane.WARNING_MESSAGE
                    );
                }
                break;

            case "Reversed Sorting":
                if (arrayVisualizer.enableBenchmarking(false))
                    break;
                jButton6.setEnabled(true);
                arrayVisualizer.setComparator(3);
                break;

            case "*Simple* Benchmarking":
                jButton6.setEnabled(true);
                arrayVisualizer.setComparator(0);
                arrayVisualizer.enableBenchmarking(true);
                break;
        }
    }//GEN-LAST:event_jCheckBox8ActionPerformed

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton6;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jComboBox1;
}
