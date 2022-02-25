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
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import io.github.arrayv.visuals.image.CustomImage;

public class ImageFrame extends JFrame {
    private static ImageFrame defaultFrame;

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private volatile JPanel contentPane;

    private volatile JLabel lblNewLabel2;

    /**
     * Create the frame.
     */
    public ImageFrame(CustomImage visual) {
        ImageFrame.defaultFrame = this;

        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gblContentPane = new GridBagLayout();
        gblContentPane.columnWidths = new int[]{0, 0, 0};
        gblContentPane.rowHeights = new int[]{0, 32, 0, 0, 0};
        gblContentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gblContentPane.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        contentPane.setLayout(gblContentPane);

        JLabel lblNewLabel = new JLabel("Current image:");
        GridBagConstraints gbcLblNewLabel = new GridBagConstraints();
        gbcLblNewLabel.anchor = GridBagConstraints.EAST;
        gbcLblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbcLblNewLabel.gridx = 0;
        gbcLblNewLabel.gridy = 0;
        contentPane.add(lblNewLabel, gbcLblNewLabel);

        JLabel lblNewLabel1 = new JLabel(visual.getCurrentImageName());
        GridBagConstraints gbcLblNewLabel1 = new GridBagConstraints();
        gbcLblNewLabel1.anchor = GridBagConstraints.WEST;
        gbcLblNewLabel1.insets = new Insets(0, 0, 5, 0);
        gbcLblNewLabel1.gridx = 1;
        gbcLblNewLabel1.gridy = 0;
        contentPane.add(lblNewLabel1, gbcLblNewLabel1);

        this.lblNewLabel2 = new JLabel(" ");
        lblNewLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbcLblNewLabel2 = new GridBagConstraints();
        gbcLblNewLabel2.fill = GridBagConstraints.BOTH;
        gbcLblNewLabel2.insets = new Insets(0, 0, 5, 0);
        gbcLblNewLabel2.gridwidth = 2;
        gbcLblNewLabel2.gridx = 0;
        gbcLblNewLabel2.gridy = 1;
        contentPane.add(this.lblNewLabel2, gbcLblNewLabel2);

        JButton btnNewButton = new JButton("Load default artwork");
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                visual.loadDefaultArtwork(ImageFrame.this);
            }
        });
        GridBagConstraints gbcBtnNewButton = new GridBagConstraints();
        gbcBtnNewButton.fill = GridBagConstraints.VERTICAL;
        gbcBtnNewButton.gridheight = 2;
        gbcBtnNewButton.anchor = GridBagConstraints.EAST;
        gbcBtnNewButton.insets = new Insets(0, 0, 5, 5);
        gbcBtnNewButton.gridx = 0;
        gbcBtnNewButton.gridy = 2;
        contentPane.add(btnNewButton, gbcBtnNewButton);

        JButton btnNewButton1 = new JButton("Choose image...");
        btnNewButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                visual.loadCustomImage(ImageFrame.this);
            }
        });
        GridBagConstraints gbcBtnNewButton1 = new GridBagConstraints();
        gbcBtnNewButton1.fill = GridBagConstraints.VERTICAL;
        gbcBtnNewButton1.gridheight = 2;
        gbcBtnNewButton1.insets = new Insets(0, 0, 5, 0);
        gbcBtnNewButton1.anchor = GridBagConstraints.WEST;
        gbcBtnNewButton1.gridx = 1;
        gbcBtnNewButton1.gridy = 2;
        contentPane.add(btnNewButton1, gbcBtnNewButton1);
    }

    public void updatePreview(CustomImage visual) {
        BufferedImage img = visual.getImage();
        Image scaledImg = img.getScaledInstance((int) (this.lblNewLabel2.getWidth() * 0.75), this.lblNewLabel2.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(scaledImg);
        this.lblNewLabel2.setIcon(imageIcon);
        this.lblNewLabel2.setText("");
    }

    public static ImageFrame getDefaultFrame() {
        return defaultFrame;
    }
}
