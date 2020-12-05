package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.KeyboardFocusManager;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import dialogs.CustomImageDialog;
import frames.ArrayFrame;
import frames.SoundFrame;
import frames.UtilFrame;
import panes.JErrorPane;
import threads.RunScriptedSorts;
import utils.Delays;
import utils.Highlights;
import utils.MultipleScript;
import utils.Reads;
import utils.Renderer;
import utils.Sounds;
import utils.Statistics;
import utils.Timer;
import utils.Writes;
import visuals.Bars;
import visuals.Circular;
import visuals.CustomImage;
import visuals.Mesh;
import visuals.Pixels;
import visuals.Visual;
import visuals.VisualStyles;

/*
 * 
The MIT License (MIT)

Copyright (c) 2019 w0rthy
Copyright (c) 2019 Luke Hutchison
Copyright (c) 2020 MusicTheorist

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

final public class ArrayVisualizer {
    final JFrame window;

    final private int MIN_ARRAY_VAL;
    final private int MAX_ARRAY_VAL;

    final int[] array;
    private int[] shadowArray;
    final ArrayList<int[]> arrays;
    final ArrayList<ArrayList<Integer>> arrayLists;

    private String[][] ComparisonSorts; // First row of Comparison/DistributionSorts arrays consists of class names
    private String[][] DistributionSorts; // Second row consists of user-friendly names
    private String[] InvalidSorts;
    private String[] sortSuggestions;

    private volatile int sortLength;
    private volatile int uniqueItems;

    private ArrayManager ArrayManager;
    private SortAnalyzer SortAnalyzer;

    private UtilFrame UtilFrame;
    private ArrayFrame ArrayFrame;

    private Visual[] visualClasses;

    private Thread sortingThread;
    private Thread visualsThread;

    private volatile boolean visualsEnabled;

    private String category;
    private String heading;
    private String extraHeading;
    private Font typeFace;
    private DecimalFormat formatter;
    private DecimalFormatSymbols symbols;

    private volatile int currentGap;

    private boolean SHUFFLEANIM;

    private volatile boolean ANALYZE;

    private volatile boolean POINTER;

    private Statistics statSnapshot;
    private String fontSelection;

    private volatile boolean TEXTDRAW;
    private volatile boolean COLOR;
    private volatile boolean DISPARITYDRAW;
    private volatile boolean LINEDRAW;
    private volatile boolean PIXELDRAW;
    private volatile boolean RAINBOW;
    private volatile boolean SPIRALDRAW;
    private volatile boolean WAVEDRAW;

    private volatile int cx;
    private volatile int cy;
    private volatile int ch;
    private volatile int cw;

    private Image img;
    private Graphics2D mainRender;
    private Graphics2D extraRender;
    private Stroke thickStroke;

    private Delays Delays;
    private Highlights Highlights;
    private MultipleScript MultipleScript;
    private Reads Reads;
    private Renderer Renderer;
    private Sounds Sounds;
    private Timer Timer;
    private VisualStyles VisualStyles;
    private Writes Writes;

    private volatile boolean updateVisuals;

    public ArrayVisualizer() {
        this.window = new JFrame();
        this.window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == ' ' || e.getKeyChar() == 'k') {
                    ArrayVisualizer.this.getDelays().togglePaused();
                }
            }
            @Override
            public void keyPressed(KeyEvent e) { }
            @Override
            public void keyReleased(KeyEvent e) { }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_O && (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0) {
                    if (ArrayVisualizer.this.isActive())
                        return false;
                    new Thread(){
                        @Override
                        public void run(){
                            RunScriptedSorts RunScriptedSorts = new RunScriptedSorts(ArrayVisualizer.this);
                            try {
                                RunScriptedSorts.runThread(ArrayVisualizer.this.getArray(), 0, 0, false);
                            }
                            catch (Exception e) {
                                JErrorPane.invokeErrorMessage(e);
                            }
                        }
                    }.start();
                    return true;
                }
                return false;
            }
        });
        
        this.MIN_ARRAY_VAL = 2;
        this.MAX_ARRAY_VAL = 32768;
        
        this.array = new int[this.MAX_ARRAY_VAL];
        this.shadowArray = new int[this.array.length];
        this.arrays = new ArrayList<>();
        this.arrays.add(this.array);
        this.arrayLists = new ArrayList<>();
        
        this.sortLength = 2048;
        this.uniqueItems = 2048;
        
        this.formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        this.symbols = this.formatter.getDecimalFormatSymbols();
        this.formatter.setRoundingMode(RoundingMode.HALF_UP);
        this.symbols.setGroupingSeparator(',');
        this.formatter.setDecimalFormatSymbols(this.symbols);
        
        this.Highlights = new Highlights(this, this.MAX_ARRAY_VAL);
        this.Sounds = new Sounds(this.array, this);
        this.Delays = new Delays(this);
        this.Timer = new Timer(this);
        this.Reads = new Reads(this);
        this.Renderer = new Renderer(this);
        this.Writes = new Writes(this);
        
        SoundFrame test = new SoundFrame(this.Sounds);
        test.setVisible(true);
        
        this.ArrayManager = new ArrayManager(this);
        this.SortAnalyzer = new SortAnalyzer(this);
        
        this.SortAnalyzer.analyzeSorts();
        this.ComparisonSorts = this.SortAnalyzer.getComparisonSorts();
        this.DistributionSorts = this.SortAnalyzer.getDistributionSorts();
        this.InvalidSorts = this.SortAnalyzer.getInvalidSorts();
        this.sortSuggestions = this.SortAnalyzer.getSuggestions();

        this.MultipleScript = new MultipleScript(this);
        
        this.category = "";
        this.heading = "";
        this.extraHeading = "";
        this.fontSelection = "Times New Roman";
        this.typeFace = new Font(this.fontSelection, Font.PLAIN, (int) (this.getWindowRatio() * 25));
        
        this.statSnapshot = new Statistics(this);
        
        this.UtilFrame = new UtilFrame(this.array, this);
        this.ArrayFrame = new ArrayFrame(this.array, this);
       
        this.UtilFrame.reposition(this.ArrayFrame);
        
        this.SHUFFLEANIM = true;
        this.ANALYZE = false;
        this.POINTER = false;
        this.TEXTDRAW = true;
        
        this.COLOR = false;
        this.DISPARITYDRAW = false;
        this.LINEDRAW = false;
        this.PIXELDRAW = false;
        this.RAINBOW = false;
        this.SPIRALDRAW = false;
 
        this.cx = 0;
        this.cy = 0;
        this.ch = 0;
        this.cw = 0;

        this.ArrayManager.initializeArray(this.array);
        
        //TODO: Overhaul visual code to properly reflect Swing (JavaFX?) style and conventions
        this.toggleVisualUpdates(true);
        //DRAW THREAD
        this.visualsThread = new Thread() {
            private int[] fancyToArray(ArrayList<Integer> aList) {
                int[] result = new int[aList.size()];
                for (int i = 0; i < result.length; i++) {
                    result[i] = aList.get(i);
                }
                return result;
            }

            @SuppressWarnings("unused")
            @Override
            public void run() {
                ArrayVisualizer.this.visualsEnabled = true;
                
                utils.Renderer.initializeVisuals(ArrayVisualizer.this);
                
                Graphics background = ArrayVisualizer.this.window.getGraphics();
                background.setColor(Color.BLACK);
                int coltmp = 255;
                
                ArrayVisualizer.this.visualClasses = new Visual[5];
                ArrayVisualizer.this.visualClasses[0] = new Bars(ArrayVisualizer.this);
                ArrayVisualizer.this.visualClasses[1] = new Circular(ArrayVisualizer.this);
                ArrayVisualizer.this.visualClasses[2] = new CustomImage(ArrayVisualizer.this);
                ArrayVisualizer.this.visualClasses[3] = new Mesh(ArrayVisualizer.this);
                ArrayVisualizer.this.visualClasses[4] = new Pixels(ArrayVisualizer.this);
                
                while(ArrayVisualizer.this.visualsEnabled) {
                    if(ArrayVisualizer.this.updateVisuals) {
                        @SuppressWarnings("unchecked")
                        ArrayList<int[]> copied = (ArrayList<int[]>)ArrayVisualizer.this.arrays.clone();
                        // for (ArrayList<Integer> aList : ArrayVisualizer.this.arrayLists) {
                        //     copied.add(this.fancyToArray(aList));
                        // }
                        int[][] arrays = copied.toArray(new int[][] { });
                        ArrayVisualizer.this.Renderer.updateVisualsStart(ArrayVisualizer.this, arrays);
                        ArrayVisualizer.this.Renderer.drawVisual(ArrayVisualizer.this.VisualStyles, arrays, ArrayVisualizer.this, ArrayVisualizer.this.Highlights);

                        if(ArrayVisualizer.this.TEXTDRAW) {
                            ArrayVisualizer.this.statSnapshot.updateStats(ArrayVisualizer.this);
                            ArrayVisualizer.this.updateFontSize();
                            ArrayVisualizer.this.drawStats(Color.BLACK, true);
                            ArrayVisualizer.this.drawStats(Color.WHITE, false);
                        }
                        background.drawImage(ArrayVisualizer.this.img, 0, 0, null);
                        Toolkit.getDefaultToolkit().sync();
                    }
                    // See: https://stackoverflow.com/questions/580419/how-can-i-stop-a-java-while-loop-from-eating-50-of-my-cpu/583537#583537
                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                }}};
                
                this.Sounds.startAudioThread();
                this.drawWindows();
    }
    
    private void drawStats(Color textColor, boolean dropShadow) {
        int xOffset = 15;
        int yOffset = 30;
        if(dropShadow) {
            xOffset += 3;
            yOffset += 3;
        }
        
        double windowRatio = this.getWindowRatio();
        
        this.mainRender.setColor(textColor);
        
        this.mainRender.drawString(this.statSnapshot.getSortIdentity(),    xOffset, (int) (windowRatio *  30) + yOffset);
        this.mainRender.drawString(this.statSnapshot.getArrayLength(),     xOffset, (int) (windowRatio *  55) + yOffset);
        this.mainRender.drawString(this.statSnapshot.getSortDelay(),       xOffset, (int) (windowRatio *  95) + yOffset);
        this.mainRender.drawString(this.statSnapshot.getVisualTime(),      xOffset, (int) (windowRatio * 120) + yOffset);
        this.mainRender.drawString(this.statSnapshot.getEstSortTime(),     xOffset, (int) (windowRatio * 145) + yOffset);
        this.mainRender.drawString(this.statSnapshot.getComparisonCount(), xOffset, (int) (windowRatio * 185) + yOffset);
        this.mainRender.drawString(this.statSnapshot.getSwapCount(),       xOffset, (int) (windowRatio * 210) + yOffset);
        this.mainRender.drawString(this.statSnapshot.getReversalCount(),   xOffset, (int) (windowRatio * 235) + yOffset);
        this.mainRender.drawString(this.statSnapshot.getMainWriteCount(),  xOffset, (int) (windowRatio * 275) + yOffset);
        this.mainRender.drawString(this.statSnapshot.getAuxWriteCount(),   xOffset, (int) (windowRatio * 300) + yOffset);
        this.mainRender.drawString(this.statSnapshot.getAuxAllocAmount(),  xOffset, (int) (windowRatio * 325) + yOffset);
        this.mainRender.drawString(this.statSnapshot.getSegments(),        xOffset, (int) (windowRatio * 355) + yOffset);
    }
    
    public void toggleVisualUpdates(boolean bool) {
        this.updateVisuals = bool;
    }
    
    public int[] getShadowArray() {
        return this.shadowArray;
    }
    public void setShadowArray() {
        for(int i = 0; i < this.sortLength; i++) {
            this.shadowArray[this.array[i]] = i;
        }
    }

    public boolean isSorted() {
        return this.statSnapshot.findSegments(this.array, this.sortLength)[0] == 1;
    }

    public int[] getArray() {
        return this.array;
    }

    public ArrayList<int[]> getArrays() {
        return this.arrays;
    }

    public ArrayList<ArrayList<Integer>> getArrayLists() {
        return this.arrayLists;
    }
    
    public ArrayManager getArrayManager() {
        return this.ArrayManager;
    }
    public Delays getDelays() {
        return this.Delays;
    }
    public Highlights getHighlights() {
        return this.Highlights;
    }
    public Reads getReads() {
        return this.Reads;
    }
    public Renderer getRender() {
        return this.Renderer;
    }
    public Sounds getSounds() {
        return this.Sounds;
    }
    public Timer getTimer() {
        return this.Timer;
    }
    public VisualStyles getVisualStyles() {
        return this.VisualStyles;
    }
    public Writes getWrites() {
        return this.Writes;
    }
    public MultipleScript getScriptParser() {
        return this.MultipleScript;
    }
    
    public Visual[] getVisuals() {
        return this.visualClasses;
    }
    
    public UtilFrame getUtilFrame() {
        return this.UtilFrame;
    }
    
    public String[][] getComparisonSorts() {
        return this.ComparisonSorts;
    }
    public String[][] getDistributionSorts() {
        return this.DistributionSorts;
    }
    
    public Thread getSortingThread() {
        return this.sortingThread;
    }
    public void setSortingThread(Thread thread) {
        this.sortingThread = thread;
    }
    public void runSortingThread() {
        this.sortingThread.start();
    }
    
    public int getMinimumLength() {
        return this.MIN_ARRAY_VAL;
    }
    public int getMaximumLength() {
        return this.MAX_ARRAY_VAL;
    }
    
    public void resetAllStatistics() {
        this.Reads.resetStatistics();
        this.Writes.resetStatistics();
        this.Timer.manualSetTime(0);
    }

    public boolean isActive() {
        return this.getSortingThread() != null && this.getSortingThread().isAlive();
    }
    
    // These next five methods should be part of ArrayManager
    public int getCurrentLength() {
        return this.sortLength;
    }
    public void setCurrentLength(int newLength) {
        this.sortLength = newLength;
    }

    public void setUniqueItems(int newCount) {
        int length = this.getCurrentLength();
        if (newCount <= length) {
            this.uniqueItems = newCount;
        } else {
            System.out.println("Too many unique items!");
        }
    }
    public int getUniqueItems() {
        return uniqueItems;
    }
    
    public int getLogBaseNOfLength(int base) {
        return (int) (Math.log(this.sortLength) / Math.log(base)); 
    }
    public int getLogBaseTwoOfLength() {
        return getLogBaseNOfLength(2); 
    }
    
    public boolean shuffleEnabled() {
        return this.SHUFFLEANIM;
    }
    public void toggleShuffleAnimation(boolean Bool) {
        this.SHUFFLEANIM = Bool;
    }
    
    public String getCategory() {
        return this.category;
    }
    public String getHeading() {
        return this.heading;
    }
    public String getExtraHeading() {
        return this.extraHeading;
    }
    public void setHeading(String text) {
        this.heading = text;
    }
    public void setCategory(String text) {
        this.category = text;
    }
    public void setExtraHeading(String text) {
        this.extraHeading = text;
    }

    public boolean pointerActive() {
        return this.POINTER;
    }
    
    public JFrame getMainWindow() {
        return this.window;
    }
    
    public void setWindowHeight() {
        this.ch = this.window.getHeight();
    }
    public void setWindowWidth() {
        this.cw = this.window.getWidth();
    }
    
    // TODO:
    // CURRENT HEIGHT/WIDTH/X/Y SHOULD CORRESPOND TO "C" VARIABLES
    // AND WINDOW HEIGHT/WIDTH/X/Y SHOULD CORRESPOND TO WINDOW FIELD
    
    public int currentHeight() {
        return this.window.getHeight();
    }
    public int currentWidth() {
        return this.window.getWidth();
    }
    public int currentX() {
        return this.window.getX();
    }
    public int currentY() {
        return this.window.getY();
    }
    
    public int windowHeight() {
        return this.ch;
    }
    public int windowWidth() {
        return this.cw;
    }
    public int windowHalfHeight() {
        return (this.ch / 2);
    }
    public int windowHalfWidth() {
        return (this.cw / 2);
    }
    public int windowXCoordinate() {
        return this.cx;
    }
    public int windowYCoordinate() {
        return this.cy;
    }
    
    public void createVolatileImage() {
        this.img = this.window.createVolatileImage(this.cw, this.ch);
    }
    public void setThickStroke(Stroke stroke) {
        this.thickStroke = stroke;
    }
    public Stroke getThickStroke() {
        return this.thickStroke;
    }
    public Stroke getDefaultStroke() {
        return new BasicStroke((float) (3 * this.getWindowRatio()));
    }
    public Graphics2D getMainRender() {
        return this.mainRender;
    }
    public Graphics2D getExtraRender() {
        return this.extraRender;
    }
    public void setMainRender() {
        this.mainRender = (Graphics2D) this.img.getGraphics();
    }
    public void setExtraRender() {
        this.extraRender = (Graphics2D) this.img.getGraphics();
    }
    public void updateVisuals() {
        for(Visual visual : this.visualClasses) {
            visual.updateRender(this);
        }
    }
    public void resetMainStroke() {
        this.mainRender.setStroke(this.getDefaultStroke());
    }
    
    public void renderBackground() {
        this.mainRender.setColor(new Color(0, 0, 0)); // Pure black
        this.mainRender.fillRect(0, 0, this.img.getWidth(null), this.img.getHeight(null));
    }
    
    public void updateCoordinates() {
        this.cx = this.window.getX();
        this.cy = this.window.getY();
    }
    public void updateDimensions() {
        this.cw = this.window.getWidth();
        this.ch = this.window.getHeight();
    }
    public double getWindowRatio() {
        return this.cw / 1280d;
    }
    public void updateFontSize() {
        this.typeFace = new Font(this.fontSelection, Font.PLAIN, (int) (this.getWindowRatio() * 25));
        this.mainRender.setFont(this.typeFace);
    }
    
    public void toggleAnalysis(boolean Bool) {
        this.ANALYZE = Bool;
    }
    public boolean analysisEnabled() {
        return this.ANALYZE;
    }
    
    public int halfCircle() {
        return (this.sortLength / 2);
    }
    
    //TODO: This method is *way* too long. Break it apart.
    public synchronized void verifySortAndSweep() {
        this.Highlights.toggleFancyFinish(true);
        this.Highlights.resetFancyFinish();

        this.Delays.setSleepRatio(1);

        double sleepRatio = 0;
        
        //TODO: Rewrite as continuous function and DON'T CHANGE SLEEP RATIOS, FOR THE LOVE OF GOD !!!
        switch(this.getLogBaseTwoOfLength()) {
        case 15: sleepRatio =  0.125; break;
        case 14: sleepRatio =  0.25;  break;
        case 13: sleepRatio =  0.5;   break;
        case 12: sleepRatio =  1;     break;
        case 11: sleepRatio =  2;     break;
        case 10: sleepRatio =  4;     break;
        case 9:  sleepRatio =  6;     break;
        case 8:  sleepRatio =  8;     break;
        case 7:  sleepRatio = 16;     break;
        case 6:  sleepRatio = 24;     break;
        case 5:
        case 4:  sleepRatio = 32;     break;
        case 3:
        case 2:
        default: sleepRatio = 64;
        }
        
        long tempComps = this.Reads.getComparisons();
        this.Reads.setComparisons(0);
        
        String temp = this.heading;
        this.heading = "Verifying sort...";
        
        for(int i = 0; i < this.sortLength + this.getLogBaseTwoOfLength(); i++) {
            if(i < this.sortLength) this.Highlights.markArray(1, i);
            this.Highlights.incrementFancyFinishPosition();
            
            if(i < this.sortLength - 1) {
                if(this.Reads.compareValues(this.array[i], this.array[i + 1]) == 1) {
                    this.Highlights.clearMark(1);
                    
                    this.Sounds.toggleSound(false);
                    this.Highlights.toggleFancyFinish(false);
                    
                    for(int j = i + 1; j < this.sortLength; j++) {
                        this.Highlights.markArray(j, j);
                        this.Delays.sleep(sleepRatio / this.getLogBaseTwoOfLength());
                    }
                    
                    JOptionPane.showMessageDialog(this.window, "The sort was unsuccessful;\nIndices " + i + " and " + (i + 1) + " are out of order!", "Error", JOptionPane.OK_OPTION, null);
                    
                    this.Highlights.clearAllMarks();
                    
                    i = this.sortLength + this.getLogBaseTwoOfLength();
                    
                    this.Sounds.toggleSound(true);
                }
            }
            
            if(this.Highlights.fancyFinishEnabled()) {
                this.Delays.sleep(sleepRatio / this.getLogBaseTwoOfLength());
            }
        }
        this.Highlights.clearMark(1);

        this.heading = temp;
        this.Reads.setComparisons(tempComps);
        
        if(this.Highlights.fancyFinishActive()) {
            this.Highlights.toggleFancyFinish(false);
        }
        this.Highlights.resetFancyFinish();
    }

    public void endSort() {
        this.Timer.disableRealTimer();
        this.Highlights.clearAllMarks();

        this.Delays.changeSkipped(false);
        double speed = this.Delays.getSleepRatio(); 
        this.verifySortAndSweep();
        this.Delays.setSleepRatio(speed);

        this.Highlights.clearAllMarks();
    }
    
    public void togglePointer(boolean Bool) {
        this.POINTER = Bool;
    }
    public void toggleDistance(boolean Bool) {
        this.DISPARITYDRAW = Bool;
    }
    public void togglePixels(boolean Bool) {
        this.PIXELDRAW = Bool;
    }
    public void toggleRainbow(boolean Bool) {
        this.RAINBOW = Bool;
    }
    public void toggleSpiral(boolean Bool) {
        this.SPIRALDRAW = Bool;
    }
    public void toggleLinkedDots(boolean Bool) {
        this.LINEDRAW = Bool;
    }
    public void toggleStatistics(boolean Bool) {
        this.TEXTDRAW = Bool;
    }
    public void toggleColor(boolean Bool) {
        this.COLOR = Bool;
    }
    public void toggleWave(boolean Bool) {
        this.WAVEDRAW = Bool;
    }
    
    public void setVisual(VisualStyles choice) {
        if(choice == visuals.VisualStyles.CUSTOMIMAGE) {
            ((CustomImage) this.visualClasses[2]).enableImgMenu();
        }
        this.VisualStyles = choice;
    }
    
    public int getCurrentGap() {
        return this.currentGap;
    }
    public void setCurrentGap(int gap) {
        this.currentGap = gap;
    }
    
    public void repositionFrames() {
        this.ArrayFrame.reposition();
        this.UtilFrame.reposition(this.ArrayFrame);
    }
    
    public boolean rainbowEnabled() {
        return this.RAINBOW;
    }
    public boolean colorEnabled() {
        return this.COLOR;
    }
    public boolean spiralEnabled() {
        return this.SPIRALDRAW;
    }
    public boolean distanceEnabled() {
        return this.DISPARITYDRAW;
    }
    public boolean pixelsEnabled() {
        return this.PIXELDRAW;
    }
    public boolean linesEnabled() {
        return this.LINEDRAW;
    }
    public boolean waveEnabled() {
        return this.WAVEDRAW;
    }
    
    public DecimalFormat getNumberFormat() {
        return this.formatter;
    }
    
    private static String parseStringArray(String[] stringArray) {
        String parsed = "";
        for(int i = 0; i < stringArray.length; i++) {
            parsed += stringArray[i] + "\n";
        }
        return parsed;
    }
    
    private void drawWindows() {
        this.VisualStyles = visuals.VisualStyles.BARS;
        this.category = "Select a Sort";
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.window.setSize((int) (screenSize.getWidth() / 2), (int) (screenSize.getHeight() / 2));
        
        this.window.setLocation(0, 0);
        this.window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.window.setTitle("w0rthy's Array Visualizer - " + (this.ComparisonSorts[0].length + this.DistributionSorts[0].length) + " Sorting Algorithms with 12 Different Visual Styles");
        this.window.setBackground(Color.BLACK);
        this.window.setIgnoreRepaint(true);
        
        this.window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent close) {
                ArrayVisualizer.this.Sounds.closeSynth();
                ArrayVisualizer.this.visualsEnabled = false;
                if(ArrayVisualizer.this.getSortingThread() != null && ArrayVisualizer.this.getSortingThread().isAlive()) {
                    ArrayVisualizer.this.sortingThread.interrupt();
                }
            }
        });
        
        //TODO: Consider removing insets from window size
        this.cw = this.window.getWidth();
        this.ch = this.window.getHeight();

        this.window.setVisible(true);
        this.visualsThread.start();
        this.UtilFrame.setVisible(true);
        this.ArrayFrame.setVisible(true);
        
        this.window.createBufferStrategy(2);
        
        if(this.InvalidSorts != null) {
            String output = parseStringArray(this.InvalidSorts);
            JOptionPane.showMessageDialog(this.window, "The following algorithms were not loaded:\n" + output, "Warning", JOptionPane.WARNING_MESSAGE);
        }
        if(this.sortSuggestions != null) {
            String output = parseStringArray(this.sortSuggestions);
            JOptionPane.showMessageDialog(this.window, "Here's a list of suggestions based on your custom sorts:\n" + output, "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        System.setProperty("sun.java2d.d3d", "false");
        new ArrayVisualizer();
    }
}