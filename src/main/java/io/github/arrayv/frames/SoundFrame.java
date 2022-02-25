package io.github.arrayv.frames;

/*
MIT License

Copyright (c) 2020 Musicombo
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
*/

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import io.github.arrayv.utils.Sounds;

public class SoundFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private int instrumentChoice;

    /**
     * Create the frame.
     */
    public SoundFrame(Sounds sounds) {
        this.instrumentChoice = sounds.getInstrumentChoice();

        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gblContentPane = new GridBagLayout();
        gblContentPane.columnWidths = new int[]{0, 0, 0};
        gblContentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        gblContentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gblContentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        contentPane.setLayout(gblContentPane);

        JLabel lblNewLabel = new JLabel("Current soundbank:");
        GridBagConstraints gbcLblNewLabel = new GridBagConstraints();
        gbcLblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbcLblNewLabel.gridx = 0;
        gbcLblNewLabel.gridy = 0;
        contentPane.add(lblNewLabel, gbcLblNewLabel);

        JButton btnNewButton = new JButton("Default sounds");
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sounds.selectDefaultSoundbank(SoundFrame.this);
            }
        });
        GridBagConstraints gbcBtnNewButton = new GridBagConstraints();
        gbcBtnNewButton.insets = new Insets(0, 0, 5, 0);
        gbcBtnNewButton.gridx = 1;
        gbcBtnNewButton.gridy = 0;
        contentPane.add(btnNewButton, gbcBtnNewButton);

        JLabel lblNewLabel1 = new JLabel(sounds.getSelectedSoundbank());
        GridBagConstraints gbcLblNewLabel1 = new GridBagConstraints();
        gbcLblNewLabel1.insets = new Insets(0, 0, 5, 5);
        gbcLblNewLabel1.gridx = 0;
        gbcLblNewLabel1.gridy = 1;
        contentPane.add(lblNewLabel1, gbcLblNewLabel1);

        JButton btnNewButton1 = new JButton("Choose soundbank...");
        btnNewButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sounds.selectCustomSoundbank(SoundFrame.this);
            }
        });
        GridBagConstraints gbcBtnNewButton1 = new GridBagConstraints();
        gbcBtnNewButton1.insets = new Insets(0, 0, 5, 0);
        gbcBtnNewButton1.gridx = 1;
        gbcBtnNewButton1.gridy = 1;
        contentPane.add(btnNewButton1, gbcBtnNewButton1);

        JSeparator separator = new JSeparator();
        GridBagConstraints gbcSeparator = new GridBagConstraints();
        gbcSeparator.insets = new Insets(0, 0, 5, 5);
        gbcSeparator.gridx = 0;
        gbcSeparator.gridy = 2;
        contentPane.add(separator, gbcSeparator);

        JLabel lblNewLabel2 = new JLabel("Choose a sample / instrument:");
        GridBagConstraints gbcLblNewLabel2 = new GridBagConstraints();
        gbcLblNewLabel2.anchor = GridBagConstraints.WEST;
        gbcLblNewLabel2.insets = new Insets(0, 0, 5, 5);
        gbcLblNewLabel2.gridx = 0;
        gbcLblNewLabel2.gridy = 3;
        contentPane.add(lblNewLabel2, gbcLblNewLabel2);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbcScrollPane = new GridBagConstraints();
        gbcScrollPane.insets = new Insets(0, 0, 5, 0);
        gbcScrollPane.gridwidth = 2;
        gbcScrollPane.fill = GridBagConstraints.BOTH;
        gbcScrollPane.gridx = 0;
        gbcScrollPane.gridy = 5;
        contentPane.add(scrollPane, gbcScrollPane);

        JList<String> list = new JList<String>();
        scrollPane.setViewportView(list);
        list.setListData(sounds.getInstrumentList());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(this.instrumentChoice);
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (SoundFrame.this.instrumentChoice != list.getSelectedIndex()) {
                    SoundFrame.this.instrumentChoice = list.getSelectedIndex();
                    sounds.testInstrument(SoundFrame.this.instrumentChoice);
                }
            }
        });

        JButton btnNewButton2 = new JButton("Select sound");
        btnNewButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sounds.setInstrumentChoice(SoundFrame.this.instrumentChoice);
            }
        });
        GridBagConstraints gbcBtnNewButton2 = new GridBagConstraints();
        gbcBtnNewButton2.anchor = GridBagConstraints.EAST;
        gbcBtnNewButton2.gridx = 1;
        gbcBtnNewButton2.gridy = 6;
        contentPane.add(btnNewButton2, gbcBtnNewButton2);
    }
}
