package io.github.arrayv.prompts;

import io.github.arrayv.dialogs.ImportSortDialog;
import io.github.arrayv.frames.AppFrame;
import io.github.arrayv.frames.UtilFrame;
import io.github.arrayv.groovyapi.GroovyLocals;
import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.main.RunSort;
import io.github.arrayv.main.SortAnalyzer;
import io.github.arrayv.panes.JErrorPane;
import io.github.arrayv.sortdata.SortInfo;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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

public final class SortPrompt extends javax.swing.JFrame implements AppFrame {
    public static class PlaceholderTextField extends JTextField {
        /**
         * Shamelessly copied from https://stackoverflow.com/a/16229082/8840278
         */
        private static final long serialVersionUID = 1L;
        private String placeholder;

        public String getPlaceholder() {
            return placeholder;
        }

        @Override
        protected void paintComponent(final Graphics pG) {
            super.paintComponent(pG);

            if (placeholder == null || placeholder.length() == 0 || getText().length() > 0) {
                return;
            }

            final Graphics2D g = (Graphics2D) pG;
            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(getDisabledTextColor());
            g.drawString(placeholder, getInsets().left, pG.getFontMetrics()
                .getMaxAscent() + getInsets().top);
        }

        public void setPlaceholder(final String s) {
            placeholder = s;
        }
    }

    private static int lastCategory = -1;
    private static boolean showExtraSorts = true;

    private static final long serialVersionUID = 1L;

    private static final Map<String, Map.Entry<Runnable, Integer>> CATEGORY_SORT_THREADS = new LinkedHashMap<>();

    private final int[] array;

    private final ArrayVisualizer arrayVisualizer;
    private final JFrame frame;
    private final UtilFrame utilFrame;

    public SortPrompt(int[] array, ArrayVisualizer arrayVisualizer, JFrame frame, UtilFrame utilFrame) {
        this.array = array;
        this.arrayVisualizer = arrayVisualizer;
        this.frame = frame;
        this.utilFrame = utilFrame;

        setAlwaysOnTop(true);
        setUndecorated(true);
        initComponents();
        if (lastCategory == -1) {
            for (lastCategory = 1; ; lastCategory++) {
                jComboBox1.setSelectedIndex(lastCategory);
                //noinspection DataFlowIssue
                if (jComboBox1.getSelectedItem().equals("Hybrid Sorts")) {
                    break;
                }
            }
        } else {
            jComboBox1.setSelectedIndex(lastCategory);
        }
        jTextField1.requestFocusInWindow();
        loadSorts();
        reposition();
        setVisible(true);
    }

    public static void setSortThreadForCategory(String category, int sortCount, Runnable sortThread) {
        synchronized (CATEGORY_SORT_THREADS) {
            CATEGORY_SORT_THREADS.put(category, new SimpleImmutableEntry<>(sortThread, sortCount));
        }
    }

    @Override
    public void reposition() {
        setLocation(frame.getX()+(frame.getWidth()-getWidth())/2, frame.getY()+(frame.getHeight()-getHeight())/2);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        this.jComboBox1 = new javax.swing.JComboBox();
        JScrollPane jScrollPane1 = new JScrollPane();
        this.jList1 = new javax.swing.JList();
        JButton jButton1 = new JButton();
        JButton jButton2 = new JButton();
        this.jButton3 = new javax.swing.JButton();
        this.jTextField1 = new PlaceholderTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jComboBox1.setModel(new DefaultComboBoxModel<>(SortInfo.getCategories(arrayVisualizer.getSorts())));
        jComboBox1.insertItemAt("All Sorts", 0);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jList1.addListSelectionListener(this::jList1ValueChanged);

        jScrollPane1.setViewportView(this.jList1);

        jComboBox1.addActionListener(this::jComboBox1SelectionChanged);

        jTextField1.setPlaceholder("Search");
        jTextField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                jTextField1TextChanged(e);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                jTextField1TextChanged(e);
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                jTextField1TextChanged(e);
            }
        });

        jButton1.setText("Showcase Sorts (approx. 30-90 minutes)");
        jButton1.addActionListener(evt -> jButton1ActionPerformed());

        jButton2.setText("Import Sort");
        jButton2.addActionListener(evt -> jButton2ActionPerformed());

        jButton3.setText("Run All in Selected Category");
        jButton3.addActionListener(evt -> jButton3ActionPerformed());

        JPanel extraSortsManagementPanel = new JPanel();
        JButton installExtraSortPackButton = new JButton(
            arrayVisualizer.getSortAnalyzer().extraSortsInstalled() ?
                "Update Extra Sorts Pack" :
                "Install Extra Sorts Pack"
        );
        installExtraSortPackButton.addActionListener(e -> {
            utilFrame.jButton1ResetText();
            utilFrame.jButton1Disable();
            dispose();
            new Thread(() -> {
                ProgressMonitor installProgress = new ProgressMonitor(
                    arrayVisualizer.getMainWindow(),
                    "Installing...",
                    "Installing...",
                    0, 1
                );
                installProgress.setMillisToDecideToPopup(500);
                installProgress.setMillisToPopup(500);
                SortAnalyzer analyzer = arrayVisualizer.getSortAnalyzer();
                boolean success;
                try {
                    analyzer.installOrUpdateExtraSorts(installProgress);
                    success = true;
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JErrorPane.invokeCustomErrorMessage("Failed to install: " + e1.getMessage());
                    JErrorPane.invokeErrorMessage(e1, "Install Extra Sorts Pack");
                    success = false;
                }
                installProgress.close();
                if (success) {
                    analyzer.unloadAllExtraSorts();
                    analyzer.analyzeSortsExtrasOnly();
                    analyzer.sortSorts();
                    arrayVisualizer.refreshSorts();
                    utilFrame.jButton1Enable();
                    JOptionPane.showMessageDialog(
                        arrayVisualizer.getMainWindow(),
                        "Successfully installed and loaded extra sorts pack!",
                        "Install Extra Sorts Pack",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    utilFrame.jButton1Enable();
                }
            }, "ExtraSortsInstall").start();
        });
        extraSortsManagementPanel.add(installExtraSortPackButton);
        if (arrayVisualizer.getSortAnalyzer().extraSortsInstalled()) {
            JCheckBox showExtraSorts = new JCheckBox("Show Extra Sorts");
            showExtraSorts.setSelected(SortPrompt.showExtraSorts);
            showExtraSorts.addActionListener(e -> {
                SortPrompt.showExtraSorts = showExtraSorts.isSelected();
                loadSorts();
            });
            extraSortsManagementPanel.add(showExtraSorts);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(this.jComboBox1)
                .addGap(25, 25, 25))
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(this.jTextField1)
                .addGap(45, 45, 45))
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
            .addGroup(Alignment.CENTER, layout.createSequentialGroup()
                .addComponent(this.jButton3))
            .addGroup(Alignment.CENTER, layout.createSequentialGroup()
                .addComponent(jButton1))
            .addGroup(Alignment.CENTER, layout.createSequentialGroup()
                .addComponent(extraSortsManagementPanel))
            .addGroup(Alignment.CENTER, layout.createSequentialGroup()
                .addComponent(jButton2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(this.jComboBox1))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(this.jTextField1))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(Alignment.LEADING, false)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(this.jButton3)
                    .addGap(5, 5, 5)
                    .addComponent(jButton1)
                    .addComponent(extraSortsManagementPanel)
                    .addComponent(jButton2)
                    .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed() {//GEN-FIRST:event_jButton1ActionPerformed
        GroovyLocals.runGroupInThread(
            CATEGORY_SORT_THREADS.values().stream().map(Map.Entry::getValue).reduce(Integer::sum).get(),
            () ->
                CATEGORY_SORT_THREADS.values().stream().map(Map.Entry::getKey).forEach(Runnable::run),
            true
        );
        utilFrame.jButton1ResetText();
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed() {//GEN-FIRST:event_jButton1ActionPerformed
        new Thread("ImportSort") {
            @Override
            public void run() {
                if (SortAnalyzer.tryGetJavaCompiler() == null) {
                    return;
                }
                File f = new ImportSortDialog().getFile();
                if (f == null) {
                    return;
                }
                arrayVisualizer.getSortAnalyzer().importSort(f);
            }
        }.start();
        utilFrame.jButton1ResetText();
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed() {//GEN-FIRST:event_jButton1ActionPerformed\
        final String category = (String)jComboBox1.getSelectedItem();
        Map.Entry<Runnable, Integer> groupInfo = CATEGORY_SORT_THREADS.get(category);
        if (groupInfo != null) {
            arrayVisualizer.setCategory(category);
            GroovyLocals.runGroupInThread(groupInfo.getValue(), groupInfo.getKey());
        }
        utilFrame.jButton1ResetText();
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        @SuppressWarnings("rawtypes")
        String sortName = (String)((JList)evt.getSource()).getSelectedValue();
        SortInfo sortNotFinal = null;
        for (SortInfo sort : arrayVisualizer.getSorts()) {
            if (sort.getListName().equals(sortName)) {
                sortNotFinal = sort;
                break;
            }
        }
        final SortInfo selection = sortNotFinal;
        new Thread("SortingThread") {
            @Override
            public void run() {
                RunSort sortThread = new RunSort(arrayVisualizer);
                sortThread.runSort(array, selection.getId());
            }
        }.start();
        utilFrame.jButton1ResetText();
        dispose();
    }//GEN-LAST:event_jList1ValueChanged

    @SuppressWarnings("unchecked")
    private void loadSorts() {
        int index = jComboBox1.getSelectedIndex();
        String category = (String)jComboBox1.getSelectedItem();
        assert category != null;
        ArrayList<String> sorts = new ArrayList<>();
        String searchTerms = jTextField1.getText().toLowerCase();
        boolean isSearching = !searchTerms.isEmpty();
        for (SortInfo sort : arrayVisualizer.getSorts()) {
            if (index == 0 || sort.getCategory().equals(category)) {
                if (!showExtraSorts && sort.isFromExtra()) continue;
                if (isSearching && !sort.getListName().toLowerCase().contains(searchTerms)) continue;
                sorts.add(sort.getListName());
            }
        }
        jList1.setListData(sorts.toArray());
        if (index == 0) {
            jButton3.setEnabled(false);
        } else {
            jButton3.setText("Run All ".concat(category));
            jButton3.setEnabled(CATEGORY_SORT_THREADS.containsKey(category));
        }
    }

    private void jComboBox1SelectionChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        loadSorts();
        SortPrompt.lastCategory = jComboBox1.getSelectedIndex();
    }//GEN-LAST:event_jList1ValueChanged

    private void jTextField1TextChanged(DocumentEvent e) {//GEN-FIRST:event_jList1ValueChanged
        if (e.getLength() == jTextField1.getText().length())
            jComboBox1.setSelectedIndex(0);
        loadSorts();
    }//GEN-LAST:event_jList1ValueChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JButton jButton3;
    @SuppressWarnings("rawtypes")
    private javax.swing.JList jList1;
    private PlaceholderTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
