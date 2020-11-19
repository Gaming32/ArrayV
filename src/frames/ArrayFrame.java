/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.MouseEvent;

import main.ArrayManager;
import main.ArrayVisualizer;
import panes.JEnhancedOptionPane;
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
        if(this.abstractFrame != null && abstractFrame.isVisible())
            abstractFrame.reposition();
    }

    private int getSomethingSize(String title, String message) throws Exception {
        String input = JEnhancedOptionPane.showInputDialog(title, message, new Object[] {"Ok", "Cancel"});
        int integer = Integer.parseInt(input);
        return Math.abs(integer);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        this.jLabel1 = new javax.swing.JLabel();
        this.jLabel2 = new javax.swing.JLabel();
        this.jSlider1 = new javax.swing.JSlider(SwingConstants.VERTICAL, 2, 32768, 2048);
        this.jSlider2 = new javax.swing.JSlider(SwingConstants.VERTICAL, 1, 32768, 1);
        
        jLabel1.setText("Array Size");
        jLabel2.setText("Unique Elements");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(2, new JLabel("2"));
        int value = 2048;
        while(value <= 32768) {
            labels.put(value, new JLabel(Integer.toString(value)));
            value += 2048;
        }

        jSlider1.setMajorTickSpacing(2048);
        jSlider1.setLabelTable(labels);
        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        //jSlider.setSnapToTicks(true);
        jSlider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                if(ArrayManager.isLengthMutable()) {
                    ArrayVisualizer.setCurrentLength(jSlider1.getValue());
                    //ArrayVisualizer.setEqualItems((int) Math.pow(2, jSlider.getValue()));
                    ArrayManager.initializeArray(array);
                }
                else jSlider1.setValue(ArrayVisualizer.getCurrentLength());
                if(ArrayVisualizer.getVisualStyles() == visuals.VisualStyles.CIRCULAR && jSlider1.getValue() == 1) jSlider1.setValue(2);
                
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
                    }
                    catch(Exception e) { }
                    if (newSize >= 2) {
                        ArrayVisualizer.setCurrentLength(newSize);
                        ArrayManager.initializeArray(array);
                        jSlider1.setValue(newSize);
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        });

        jSlider2.setMajorTickSpacing(2048);
        jSlider2.setLabelTable(labels);
        jSlider2.setPaintLabels(true);
        jSlider2.setPaintTicks(true);
        //jSlider2.setSnapToTicks(true);
        jSlider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                if(ArrayManager.isLengthMutable()) {
                    ArrayVisualizer.setEqualItems(jSlider2.getValue());
                    //ArrayVisualizer.setEqualItems((int) Math.pow(2, jSlider2.getValue()));
                    ArrayManager.initializeArray(array);
                }
                else jSlider2.setValue(ArrayVisualizer.getEqualItems());
                
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
                    }
                    catch(Exception e) { }
                    if (newSize >= 2) {
                        ArrayVisualizer.setEqualItems(newSize);
                        ArrayManager.initializeArray(array);
                        jSlider2.setValue(newSize);
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        });

        // java.awt.GridLayout layout = new java.awt.GridLayout(2, 2);
        // layout.setHgap(0);
        // layout.setVgap(0);
        // getContentPane().setLayout(layout);
        // getContentPane().add(jLabel1);
        // getContentPane().add(jLabel2);
        // getContentPane().add(jSlider1);
        // getContentPane().add(jSlider2);

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