package io.github.arrayv.prompts;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.LayoutStyle.ComponentPlacement;

import io.github.arrayv.frames.AppFrame;
import io.github.arrayv.frames.UtilFrame;
import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.visuals.VisualStyles;

/*
 *
MIT License

Copyright (c) 2019 w0rthy
Copyright (c) 2021 ArrayV 4.0 Team

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

public final class ViewPrompt extends javax.swing.JFrame implements AppFrame {
    private static final long serialVersionUID = 1L;

    private ArrayVisualizer arrayVisualizer;
    private JFrame frame;
    private UtilFrame utilFrame;

    public ViewPrompt(ArrayVisualizer arrayVisualizer, JFrame frame, UtilFrame utilFrame) {
        this.arrayVisualizer = arrayVisualizer;
        this.frame = frame;
        this.utilFrame = utilFrame;

        setAlwaysOnTop(true);
        setUndecorated(true);
        initComponents();
        reposition();
        setVisible(true);
    }

    @Override
    public void reposition(){
        setLocation(frame.getX() + ((frame.getWidth() - getWidth()) / 2), frame.getY() + ((frame.getHeight() - getHeight()) / 2));
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        this.jLabel1           = new javax.swing.JLabel();

        this.barGraph          = new javax.swing.JButton();
        this.dotGraph          = new javax.swing.JButton();
        this.colorCircle       = new javax.swing.JButton();
        this.pixelMesh         = new javax.swing.JButton();
        this.spiral            = new javax.swing.JButton();
        this.disparity         = new javax.swing.JButton();
        this.disparityDots     = new javax.swing.JButton();
        this.spiralDots        = new javax.swing.JButton();
        this.rainbow           = new javax.swing.JButton();
        this.customImage       = new javax.swing.JButton();
        this.sineWave          = new javax.swing.JButton();
        this.waveDots          = new javax.swing.JButton();
        this.hoopStack         = new javax.swing.JButton();
        this.disparityBarGraph = new javax.swing.JButton();
        this.disparityChords   = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setText("Select Visual Style");

        barGraph.setText("Bar Graph");
        barGraph.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barGraphActionPerformed(evt);
            }
        });

        dotGraph.setText("Dot Graph");
        dotGraph.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dotGraphActionPerformed(evt);
            }
        });

        colorCircle.setText("Color Circle");
        colorCircle.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorCircleActionPerformed(evt);
            }
        });

        pixelMesh.setText("Pixel Mesh");
        pixelMesh.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triangleMeshActionPerformed(evt);
            }
        });

        spiral.setText("Spiral");
        spiral.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spiralActionPerformed(evt);
            }
        });

        rainbow.setText("Rainbow");
        rainbow.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rainbowActionPerformed(evt);
            }
        });

        disparity.setText("Disparity Circle");
        disparity.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disparityActionPerformed(evt);
            }
        });

        customImage.setText("Custom Image");
        customImage.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customImageActionPerformed(evt);
            }
        });

        disparityDots.setText("Disparity Dots");
        disparityDots.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disparityDotsActionPerformed(evt);
            }
        });

        spiralDots.setText("Spiral Dots");
        spiralDots.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spiralDotsActionPerformed(evt);
            }
        });

        sineWave.setText("Sine Wave");
        sineWave.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sineWaveActionPerformed(evt);
            }
        });
        waveDots.setText("Wave Dots");
        waveDots.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                waveDotsActionPerformed(evt);
            }
        });

        hoopStack.setText("Hoop Stack");
        hoopStack.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hoopStackActionPerformed(evt);
            }
        });

        disparityBarGraph.setText("Disparity Bar Graph");
        disparityBarGraph.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disparityBarGraphActionPerformed(evt);
            }
        });

        disparityChords.setText("Disparity Chords");
        disparityChords.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disparityChordsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.CENTER)
                .addComponent(jLabel1)
                .addGroup(Alignment.LEADING, layout.createSequentialGroup()
                    .addGap(18)
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(barGraph, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                        .addComponent(rainbow, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                        .addComponent(colorCircle, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                        .addComponent(disparity, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(disparityBarGraph, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(disparityDots, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                        .addComponent(sineWave, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                        .addComponent(waveDots, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                        .addComponent(dotGraph, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                        .addComponent(hoopStack, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(pixelMesh, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                        .addComponent(spiral, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                        .addComponent(customImage, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                        .addComponent(spiralDots, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(disparityChords, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                    .addGap(7)
                    .addComponent(jLabel1)
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(barGraph)
                        .addComponent(disparityDots)
                        .addComponent(pixelMesh))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(rainbow)
                        .addComponent(sineWave)
                        .addComponent(spiral))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(colorCircle)
                        .addComponent(waveDots)
                        .addComponent(customImage))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(disparity)
                        .addComponent(dotGraph)
                        .addComponent(spiralDots))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(hoopStack)
                        .addComponent(disparityBarGraph)
                        .addComponent(disparityChords))
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setAllFieldsFalse() {
    }

    private void barGraphActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        arrayVisualizer.setVisual(VisualStyles.BARS);
        utilFrame.jButton2ResetText();
        dispose();
    }
    private void dotGraphActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        arrayVisualizer.setVisual(VisualStyles.DOTS);
        utilFrame.jButton2ResetText();
        dispose();
    }
    private void rainbowActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        arrayVisualizer.setVisual(VisualStyles.RAINBOW);
        utilFrame.jButton2ResetText();
        dispose();
    }
    private void triangleMeshActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        arrayVisualizer.setVisual(VisualStyles.PIXEL_MESH);
        utilFrame.jButton2ResetText();
        dispose();
    }
    private void colorCircleActionPerformed(java.awt.event.ActionEvent evt) {
        //TODO: Pointer as separate option
        setAllFieldsFalse();
        arrayVisualizer.setVisual(VisualStyles.COLOR_CIRCLE);
        if (arrayVisualizer.getCurrentLength() == 2) arrayVisualizer.setCurrentLength(4);
        utilFrame.jButton2ResetText();
        dispose();
    }
    private void spiralActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        arrayVisualizer.setVisual(VisualStyles.SPIRAL);
        if (arrayVisualizer.getCurrentLength() == 2) arrayVisualizer.setCurrentLength(4);
        utilFrame.jButton2ResetText();
        dispose();
    }
    private void disparityActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        arrayVisualizer.setVisual(VisualStyles.DISP_CIRCLE);
        if (arrayVisualizer.getCurrentLength() == 2) arrayVisualizer.setCurrentLength(4);
        utilFrame.jButton2ResetText();
        dispose();
    }
    private void customImageActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        arrayVisualizer.setVisual(VisualStyles.CUSTOM_IMAGE);
        utilFrame.jButton2ResetText();
        dispose();
    }
    private void disparityDotsActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        arrayVisualizer.setVisual(VisualStyles.DISP_DOTS);
        if (arrayVisualizer.getCurrentLength() == 2) arrayVisualizer.setCurrentLength(4);
        utilFrame.jButton2ResetText();
        dispose();
    }
    private void spiralDotsActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        arrayVisualizer.setVisual(VisualStyles.SPIRAL_DOTS);
        if (arrayVisualizer.getCurrentLength() == 2) arrayVisualizer.setCurrentLength(4);
        utilFrame.jButton2ResetText();
        dispose();
    }
    private void sineWaveActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        arrayVisualizer.setVisual(VisualStyles.SINE_WAVE);
        utilFrame.jButton2ResetText();
        dispose();
    }
    private void waveDotsActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        arrayVisualizer.setVisual(VisualStyles.WAVE_DOTS);
        utilFrame.jButton2ResetText();
        dispose();
    }
    private void hoopStackActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        arrayVisualizer.setVisual(VisualStyles.HOOP_STACK);
        utilFrame.jButton2ResetText();
        dispose();
    }
    private void disparityBarGraphActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        arrayVisualizer.setVisual(VisualStyles.DISP_BARS);
        utilFrame.jButton2ResetText();
        dispose();
    }
    private void disparityChordsActionPerformed(java.awt.event.ActionEvent evt) {
        setAllFieldsFalse();
        arrayVisualizer.setVisual(VisualStyles.DISP_CHORDS);
        utilFrame.jButton2ResetText();
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton barGraph;
    private javax.swing.JButton dotGraph;
    private javax.swing.JButton colorCircle;
    private javax.swing.JButton pixelMesh;
    private javax.swing.JButton spiral;
    private javax.swing.JButton spiralDots;
    private javax.swing.JButton disparity;
    private javax.swing.JButton disparityDots;
    private javax.swing.JButton rainbow;
    private javax.swing.JButton customImage;
    private javax.swing.JButton sineWave;
    private javax.swing.JButton waveDots;
    private javax.swing.JButton hoopStack;
    private javax.swing.JButton disparityBarGraph;
    private javax.swing.JButton disparityChords;
    private javax.swing.JLabel jLabel1;
}
