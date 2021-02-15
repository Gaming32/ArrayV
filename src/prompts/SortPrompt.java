/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prompts;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JList;

import dialogs.ImportSortDialog;
import frames.AppFrame;
import frames.UtilFrame;
import main.ArrayVisualizer;
import main.SortAnalyzer.SortPair;
import panes.JErrorPane;
import threads.*;

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

final public class SortPrompt extends javax.swing.JFrame implements AppFrame {

    private static int lastCategory = 0;

    private static final long serialVersionUID = 1L;

    private Hashtable<String, MultipleSortThread> categorySortThreads;
    
    private int[] array;
    
    private ArrayVisualizer ArrayVisualizer;
    private JFrame Frame;
    private UtilFrame UtilFrame;
    
    public SortPrompt(int[] array, ArrayVisualizer arrayVisualizer, JFrame frame, UtilFrame utilFrame) {
        this.array = array;
        this.ArrayVisualizer = arrayVisualizer;
        this.Frame = frame;
        this.UtilFrame = utilFrame;
        
        setAlwaysOnTop(true);
        setUndecorated(true);
        loadSortThreads();
        initComponents();
        jComboBox1.setSelectedIndex(lastCategory);
        loadSorts();
        reposition();
        setVisible(true);
    }

    @Override
    public void reposition() {
        setLocation(Frame.getX()+(Frame.getWidth()-getWidth())/2,Frame.getY()+(Frame.getHeight()-getHeight())/2);
    }

    private void loadSortThreads() {
        this.categorySortThreads = new Hashtable<>();
        categorySortThreads.put("Concurrent Sorts",    new RunConcurrentSorts   (ArrayVisualizer));
        categorySortThreads.put("Distribution Sorts",  new RunDistributionSorts (ArrayVisualizer));
        categorySortThreads.put("Exchange Sorts",      new RunExchangeSorts     (ArrayVisualizer));
        categorySortThreads.put("Hybrid Sorts",        new RunHybridSorts       (ArrayVisualizer));
        categorySortThreads.put("Impractical Sorts",   new RunImpracticalSorts  (ArrayVisualizer));
        categorySortThreads.put("Insertion Sorts",     new RunInsertionSorts    (ArrayVisualizer));
        categorySortThreads.put("Merge Sorts",         new RunMergeSorts        (ArrayVisualizer));
        categorySortThreads.put("Miscellaneous Sorts", new RunMiscellaneousSorts(ArrayVisualizer));
        categorySortThreads.put("Selection Sorts",     new RunSelectionSorts    (ArrayVisualizer));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        this.jComboBox1 = new javax.swing.JComboBox();
        this.jScrollPane1 = new javax.swing.JScrollPane();
        this.jList1 = new javax.swing.JList();
        this.jButton1 = new javax.swing.JButton();
        this.jButton2 = new javax.swing.JButton();
        this.jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jComboBox1.setModel(new DefaultComboBoxModel<>(SortPair.getCategories(ArrayVisualizer.getAllSorts())));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        jList1.setModel(new javax.swing.AbstractListModel() {
            
            private static final long serialVersionUID = 1L;

            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            @Override
            public int getSize() { return strings.length; }
            @Override
            public Object getElementAt(int i) { return strings[i]; }
        });

        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });

        jScrollPane1.setViewportView(this.jList1);

        jComboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1SelectionChanged(evt);
            }
        });

        jButton1.setText("Run All Sorts (approx. 30-90 minutes)");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed();
            }
        });
        
        jButton2.setText("Import Sort");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed();
            }
        });
        
        jButton3.setText("Run All in Selected Category");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed();
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(25, 25, 25)
                    .addComponent(this.jComboBox1)
                    .addGap(25, 25, 25))
                .addGroup(layout.createSequentialGroup()
                    .addGap(25, 25, 25)
                    .addComponent(this.jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(25, 25, 25))
                .addGroup(javax.swing.GroupLayout.Alignment.CENTER, layout.createSequentialGroup()
                    .addComponent(this.jButton3))
                .addGroup(javax.swing.GroupLayout.Alignment.CENTER, layout.createSequentialGroup()
                    .addComponent(this.jButton1))
                .addGroup(javax.swing.GroupLayout.Alignment.CENTER, layout.createSequentialGroup()
                    .addComponent(this.jButton2))
                );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(this.jComboBox1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(this.jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(this.jButton3)
                        .addGap(5, 5, 5)
                        .addComponent(this.jButton1)
                        .addGap(5, 5, 5)
                        .addComponent(this.jButton2)
                        .addGap(5, 5, 5))
                );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed() {//GEN-FIRST:event_jButton1ActionPerformed
        new Thread(){
            @Override
            public void run(){
                RunAllSorts RunAllSorts = new RunAllSorts(ArrayVisualizer);
                RunAllSorts.reportAllSorts(array);
            }
        }.start();
        UtilFrame.jButton1ResetText();
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed() {//GEN-FIRST:event_jButton1ActionPerformed
        new Thread(){
            @Override
            public void run(){
                File f = new ImportSortDialog().getFile();
                if (f == null) {
                    return;
                }
                ArrayVisualizer.getSortAnalyzer().importSort(f);
            }
        }.start();
        UtilFrame.jButton1ResetText();
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed() {//GEN-FIRST:event_jButton1ActionPerformed
        if (categorySortThreads.containsKey(jComboBox1.getSelectedItem())) {
            MultipleSortThread thread = categorySortThreads.get(jComboBox1.getSelectedItem());
            try {
                thread.reportAllSorts(array, 1, thread.getSortCount());
            }
            catch (Exception e) {
                JErrorPane.invokeErrorMessage(e);
            }
        }
        UtilFrame.jButton1ResetText();
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        @SuppressWarnings("rawtypes")
        String sortName = (String)((JList)evt.getSource()).getSelectedValue();
        SortPair sortNotFinal = new SortPair();
        for (SortPair sort : ArrayVisualizer.getAllSorts()) {
            if (sort.listName.equals(sortName)) {
                sortNotFinal = sort;
                break;
            }
        }
        final SortPair selection = sortNotFinal;
        new Thread(){
            @Override
            public void run() {
                if (selection.usesComparisons) {
                    RunComparisonSort sortThread = new RunComparisonSort(ArrayVisualizer);
                    sortThread.ReportComparativeSort(array, selection.id);
                }
                else {
                    RunDistributionSort sortThread = new RunDistributionSort(ArrayVisualizer);
                    sortThread.ReportDistributionSort(array, selection.id);
                }
            }
        }.start();
        UtilFrame.jButton1ResetText();
        dispose();
    }//GEN-LAST:event_jList1ValueChanged

    private void loadSorts() {
        String category = (String)jComboBox1.getSelectedItem();
        ArrayList<String> sorts = new ArrayList<>();
        for (SortPair sort : ArrayVisualizer.getAllSorts()) {
            if (sort.category.equals(category)) {
                sorts.add(sort.listName);
            }
        }
        jList1.setListData(sorts.toArray());
        jButton3.setText("Run All ".concat(category));
        jButton3.setEnabled(categorySortThreads.containsKey(category));
    }

    private void jComboBox1SelectionChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        loadSorts();
        SortPrompt.lastCategory = jComboBox1.getSelectedIndex();
    }//GEN-LAST:event_jList1ValueChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    @SuppressWarnings("rawtypes")
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}