package io.github.arrayv.frames;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import io.github.arrayv.main.ArrayManager;
import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.panes.JEnhancedOptionPane;
import io.github.arrayv.utils.Highlights;

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

public final class ArrayFrame extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;

    private int[] array;

    private ArrayManager arrayManager;
    private ArrayVisualizer arrayVisualizer;
    private AppFrame abstractFrame;
    private Highlights Highlights;
    private JFrame frame;
    private UtilFrame utilFrame;

    private boolean lockToPow2;

    public ArrayFrame(int[] array, ArrayVisualizer arrayVisualizer) {
        this.array = array;

        this.arrayVisualizer = arrayVisualizer;
        this.arrayManager = arrayVisualizer.getArrayManager();

        this.Highlights = arrayVisualizer.getHighlights();
        this.frame = arrayVisualizer.getMainWindow();
        this.utilFrame = arrayVisualizer.getUtilFrame();

        setUndecorated(true);
        initComponents();
        setBounds(
            Math.min((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth(), frame.getX() + frame.getWidth()),
            frame.getY() + 29,
            getWidth(),
            arrayVisualizer.getUtilFrame().getHeight()
        );
        setAlwaysOnTop(false);
        setVisible(true);
    }

    public void reposition(){
        toFront();
        setLocation(Math.min((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth() - utilFrame.getWidth(), frame.getX() + frame.getWidth()), frame.getY() + 29);
        if (this.abstractFrame != null && abstractFrame.isVisible())
            abstractFrame.reposition();
    }

    public void setLengthSlider(int length) {
        boolean mutable = arrayManager.isLengthMutable();
        arrayManager.toggleMutableLength(true);
        jSlider1.setValue(calculateSliderValue(length));
        arrayManager.toggleMutableLength(mutable);
    }

    public void setUniqueSlider(int length) {
        boolean mutable = arrayManager.isLengthMutable();
        arrayManager.toggleMutableLength(true);
        jSlider2.setValue(calculateSliderValue(length));
        arrayManager.toggleMutableLength(mutable);
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

        int usePower = ArrayVisualizer.getMaxLengthPower() * 100000;
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
        int value = arrayVisualizer.getMinimumLength();
        while (value <= arrayVisualizer.getMaximumLength()) {
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
                if (arrayManager.isLengthMutable()) {
                    int value = jSlider1.getValue();
                    if (lockToPow2) {
                        value = (int)(Math.round(value / 100000.0) * 100000);
                        jSlider1.setValue(value);
                    }
                    int oldValue1 = calculateSliderValue(arrayVisualizer.getCurrentLength());
                    arrayVisualizer.setCurrentLength(calculateLength(value));
                    // double mult = (double)jSlider2.getValue() / (double)oldValue1;
                    double divver = (double)oldValue1 / (double)jSlider2.getValue();
                    jSlider2.setValue((int)(value / divver));
                    //ArrayVisualizer.setEqualItems((int) Math.pow(2, jSlider.getValue()));
                    arrayManager.initializeArray(array);
                } else {
                    int currentLength = arrayVisualizer.getCurrentLength();
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
                        arrayVisualizer.setCurrentLength(newSize);
                        arrayManager.initializeArray(array);
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
                if (arrayManager.isLengthMutable()) {
                    if (jSlider2.getValue() > jSlider1.getValue()) {
                        jSlider2.setValue(jSlider1.getValue());
                    } else {
                        int value = jSlider2.getValue();
                        if (lockToPow2) {
                            value = (int)(Math.round(value / 100000.0) * 100000);
                            jSlider2.setValue(value);
                        }
                        arrayVisualizer.setUniqueItems(calculateLength(value));
                        //ArrayVisualizer.setEqualItems((int) Math.pow(2, jSlider2.getValue()));
                        arrayManager.initializeArray(array);
                    }
                } else {
                    int currentItems = arrayVisualizer.getUniqueItems();
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
                        arrayVisualizer.setUniqueItems(newSize);
                        arrayManager.initializeArray(array);
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

        final int sliderGap = UIManager.getLookAndFeel().getClass().getName().equals("com.sun.java.swing.plaf.gtk.GTKLookAndFeel") ? 250 : 175;

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
                .addGap(sliderGap, sliderGap, sliderGap)
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
                    .addComponent(this.jSlider1, utilFrame.getHeight() - 26, utilFrame.getHeight() - 26, utilFrame.getHeight() - 26))
                .addGroup(layout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addComponent(this.jLabel2)
                    .addGap(5, 5, 5)
                    .addComponent(this.jSlider2, utilFrame.getHeight() - 26, utilFrame.getHeight() - 26, utilFrame.getHeight() - 26))
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
