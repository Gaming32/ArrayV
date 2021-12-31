/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.MouseEvent;

import main.ArrayManager;
import main.ArrayVisualizer;
import panes.JEnhancedOptionPane;
import panes.JErrorPane;
import utils.Highlights;

/*
 *
MIT License

Copyright (c) 2019 w0rthy

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

/**
 *
 * @author S630690
 */

final public class ArrayFrame extends javax.swing.JFrame {
    final private static long serialVersionUID = 1L;

    private int[] array;

    private ArrayManager ArrayManager;
    private ArrayVisualizer ArrayVisualizer;
    private AppFrame abstractFrame;
    private Highlights Highlights;
    private JFrame Frame;
    private UtilFrame UtilFrame;

    private boolean lockToPow2;

    public ArrayFrame(int[] array, ArrayVisualizer arrayVisualizer) {
        this.array = array;

        this.ArrayVisualizer = arrayVisualizer;
        this.ArrayManager = ArrayVisualizer.getArrayManager();

        this.Highlights = ArrayVisualizer.getHighlights();
        this.Frame = ArrayVisualizer.getMainWindow();
        this.UtilFrame = ArrayVisualizer.getUtilFrame();

        setUndecorated(true);
        initComponents();
        setLocation(Math.min((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth(), Frame.getX() + Frame.getWidth()), Frame.getY() + 29);
        setAlwaysOnTop(false);
        setVisible(true);
    }

    public void reposition(){
        toFront();
        setLocation(Math.min((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth() - UtilFrame.getWidth(), Frame.getX() + Frame.getWidth()), Frame.getY() + 29);
        if (this.abstractFrame != null && abstractFrame.isVisible())
            abstractFrame.reposition();
    }

    public void setLengthSlider(int length) {
        boolean mutable = ArrayManager.isLengthMutable();
        ArrayManager.toggleMutableLength(true);
        jSlider1.setValue(calculateSliderValue(length));
        ArrayManager.toggleMutableLength(mutable);
    }

    public void setUniqueSlider(int length) {
        boolean mutable = ArrayManager.isLengthMutable();
        ArrayManager.toggleMutableLength(true);
        jSlider2.setValue(calculateSliderValue(length));
        ArrayManager.toggleMutableLength(mutable);
    }

    private int getSomethingSize(String title, String message) throws Exception {
        String input = JEnhancedOptionPane.showInputDialog(title, message, new Object[] {"Ok", "Cancel"});
        int integer = Integer.parseInt(input);
        return Math.abs(integer);
    }

    private int calculateLength(int sliderValue) {
        int newLength = (int)Math.pow(2, sliderValue / 100000.0);
        return newLength;
    }

    private int calculateSliderValue(int length) {
        int sliderValue = (int)Math.ceil(Math.log(length) / Math.log(2) * 100000);
        return sliderValue;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        try {
            String os = System.getProperty("os.name");
            if (!os.equals("Linux")) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
        }

        KeyListener kListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT)
                    lockToPow2 = true;
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT)
                    lockToPow2 = false;
            }
        };
        this.addKeyListener(kListener);

        int usePower = ArrayVisualizer.MAX_LENGTH_POWER * 100000;
        int useDefault = (int)Math.min(1100000, usePower);
        this.jLabel1 = new javax.swing.JLabel();
        this.jLabel2 = new javax.swing.JLabel();
        this.jSlider1 = new javax.swing.JSlider(SwingConstants.VERTICAL, 100000, usePower, useDefault);
        this.jSlider2 = new javax.swing.JSlider(SwingConstants.VERTICAL, 100000, usePower, useDefault);

        jLabel1.setText("Array Size");
        jLabel2.setText("Unique Elements");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        int pow = 1;
        int value = ArrayVisualizer.getMinimumLength();
        while (value <= ArrayVisualizer.getMaximumLength()) {
            labels.put(pow * 100000, new JLabel(Integer.toString(value)));
            pow += 1;
            value *= 2;
        }

        jSlider1.setMajorTickSpacing(100000);
        jSlider1.setLabelTable(labels);
        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        //jSlider.setSnapToTicks(true);
        jSlider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                if (ArrayManager.isLengthMutable()) {
                    int value = jSlider1.getValue();
                    if (lockToPow2) {
                        value = (int)(Math.round(value / 100000.0) * 100000);
                        jSlider1.setValue(value);
                    }
                    int oldValue1 = calculateSliderValue(ArrayVisualizer.getCurrentLength());
                    ArrayVisualizer.setCurrentLength(calculateLength(value));
                    // double mult = (double)jSlider2.getValue() / (double)oldValue1;
                    double divver = (double)oldValue1 / (double)jSlider2.getValue();
                    jSlider2.setValue((int)(value / divver));
                    //ArrayVisualizer.setEqualItems((int) Math.pow(2, jSlider.getValue()));
                    ArrayManager.initializeArray(array);
                } else {
                    int currentLength = ArrayVisualizer.getCurrentLength();
                    jSlider1.setValue(calculateSliderValue(currentLength));
                }
                //if (ArrayVisualizer.getVisualStyles() == visuals.VisualStyles.CIRCULAR && jSlider1.getValue() == 1) jSlider1.setValue(2);

                Highlights.clearAllMarks();
            }
        });
        jSlider1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    int newSize = 0;
                    try {
                        newSize = getSomethingSize("Array Size", "Enter new array size:");
                    } catch (Exception e) {
                    }
                    if (newSize >= 2) {
                        jSlider1.setValue(calculateSliderValue(newSize));
                        ArrayVisualizer.setCurrentLength(newSize);
                        ArrayManager.initializeArray(array);
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        jSlider1.addKeyListener(kListener);

        jSlider2.setMajorTickSpacing(100000);
        jSlider2.setLabelTable(labels);
        jSlider2.setPaintLabels(true);
        jSlider2.setPaintTicks(true);
        //jSlider2.setSnapToTicks(true);
        jSlider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                if (ArrayManager.isLengthMutable()) {
                    if (jSlider2.getValue() > jSlider1.getValue()) {
                        jSlider2.setValue(jSlider1.getValue());
                    } else {
                        int value = jSlider2.getValue();
                        if (lockToPow2) {
                            value = (int)(Math.round(value / 100000.0) * 100000);
                            jSlider2.setValue(value);
                        }
                        ArrayVisualizer.setUniqueItems(calculateLength(value));
                        //ArrayVisualizer.setEqualItems((int) Math.pow(2, jSlider2.getValue()));
                        ArrayManager.initializeArray(array);
                    }
                } else {
                    int currentItems = ArrayVisualizer.getUniqueItems();
                    jSlider2.setValue(calculateSliderValue(currentItems));
                }

                Highlights.clearAllMarks();
            }
        });
        jSlider2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    int newSize = 0;
                    try {
                        newSize = getSomethingSize("Unique Elements", "Enter new number of unique elements:");
                    } catch (Exception e) {
                    }
                    if (newSize >= 2) {
                        jSlider2.setValue(calculateSliderValue(newSize));
                        ArrayVisualizer.setUniqueItems(newSize);
                        ArrayManager.initializeArray(array);
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        jSlider2.addKeyListener(kListener);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER, true)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, true)
                                .addComponent(this.jLabel1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, true)
                                        .addComponent(this.jSlider1)
                                        .addGap(0, 10, Short.MAX_VALUE))))
                .addGap(175, 175, 175)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, true)
                            .addComponent(this.jLabel2)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, true)
                                    .addComponent(this.jSlider2)
                                    .addGap(0, 10, Short.MAX_VALUE))))
                );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, true)
                .addGroup(layout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addComponent(this.jLabel1)
                    .addGap(5, 5, 5)
                    .addComponent(this.jSlider1, UtilFrame.getHeight() - 26, UtilFrame.getHeight() - 26, UtilFrame.getHeight() - 26))
                .addGroup(layout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addComponent(this.jLabel2)
                    .addGap(5, 5, 5)
                    .addComponent(this.jSlider2, UtilFrame.getHeight() - 26, UtilFrame.getHeight() - 26, UtilFrame.getHeight() - 26))
                );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    // End of variables declaration//GEN-END:variables
}