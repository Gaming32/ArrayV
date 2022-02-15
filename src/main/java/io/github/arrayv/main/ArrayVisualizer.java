package io.github.arrayv.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import io.github.arrayv.dialogs.FileDialog;
import io.github.arrayv.dialogs.SaveArrayDialog;
import io.github.arrayv.frames.ArrayFrame;
import io.github.arrayv.frames.SoundFrame;
import io.github.arrayv.frames.UtilFrame;
import io.github.arrayv.panes.JErrorPane;
import io.github.arrayv.sortdata.SortInfo;
import io.github.arrayv.threads.RunScriptedSorts;
import io.github.arrayv.visuals.Visual;
import io.github.arrayv.visuals.VisualStyles;
import io.github.arrayv.visuals.bars.BarGraph;
import io.github.arrayv.visuals.bars.DisparityBarGraph;
import io.github.arrayv.visuals.bars.Rainbow;
import io.github.arrayv.visuals.bars.SineWave;
import io.github.arrayv.visuals.circles.ColorCircle;
import io.github.arrayv.visuals.circles.DisparityChords;
import io.github.arrayv.visuals.circles.DisparityCircle;
import io.github.arrayv.visuals.circles.Spiral;
import io.github.arrayv.visuals.dots.DisparityDots;
import io.github.arrayv.visuals.dots.ScatterPlot;
import io.github.arrayv.visuals.dots.SpiralDots;
import io.github.arrayv.visuals.dots.WaveDots;
import io.github.arrayv.visuals.image.CustomImage;
import io.github.arrayv.visuals.misc.HoopStack;
import io.github.arrayv.visuals.misc.PixelMesh;
import utils.AntiQSort;
import utils.ArrayFileWriter;
import utils.Delays;
import utils.Highlights;
import utils.MultipleScript;
import utils.Reads;
import utils.Renderer;
import utils.Sounds;
import utils.Statistics;
import utils.Timer;
import utils.Writes;

/*
 *
The MIT License (MIT)

Copyright (c) 2019 w0rthy
Copyright (c) 2019 Luke Hutchison
Copyright (c) 2020 MusicTheorist
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

public final class ArrayVisualizer {
    private static ArrayVisualizer INSTANCE = null;

    private enum StatisticType {
        LINE_BREAK,
        SORT_IDENTITY,
        ARRAY_LENGTH,
        FRAMERATE,
        SORT_DELAY,
        VISUAL_TIME,
        EST_SORT_TIME,
        COMPARISONS,
        SWAPS,
        REVERSALS,
        MAIN_WRITE,
        AUX_WRITE,
        AUX_ALLOC,
        SEGMENTS;

        private static final Map<String, StatisticType> CONFIG_KEYS = Collections.unmodifiableMap(new HashMap<String, StatisticType>() {{
            put("",         LINE_BREAK);
            put("sort",     SORT_IDENTITY);
            put("length",   ARRAY_LENGTH);
            put("fps",      FRAMERATE);
            put("delay",    SORT_DELAY);
            put("vtime",    VISUAL_TIME);
            put("stime",    EST_SORT_TIME);
            put("comps",    COMPARISONS);
            put("swaps",    SWAPS);
            put("revs",     REVERSALS);
            put("wmain",    MAIN_WRITE);
            put("waux",     AUX_WRITE);
            put("auxlen",   AUX_ALLOC);
            put("segments", SEGMENTS);
        }});
    }

    final JFrame window;

    final private int MIN_ARRAY_VAL;
    final private int MAX_ARRAY_VAL;

    final private Properties buildInfo;

    final int[] array;
    final int[] validateArray;
    final int[] stabilityTable;
    final int[] indexTable;
    final ArrayList<int[]> arrays;
    private final StatisticType[] statsConfig;

    private SortInfo[] sorts;
    private String[] invalidSorts;
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
    public final boolean disabledStabilityCheck;

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
    private double fontSelectionScale;

    private volatile boolean TEXTDRAW;
    private volatile boolean COLOR;
    private volatile boolean DISPARITYDRAW;
    private volatile boolean LINEDRAW;
    private volatile boolean PIXELDRAW;
    private volatile boolean RAINBOW;
    private volatile boolean SPIRALDRAW;
    private volatile boolean WAVEDRAW;
    private volatile boolean EXTARRAYS;

    private volatile boolean ANTIQSORT;
    private volatile boolean STABILITY;
    private volatile boolean NETWORKS;
    private volatile boolean REVERSED;

    private volatile boolean isCanceled;

    private volatile int cx;
    private volatile int cy;
    private volatile int ch;
    private volatile int cw;

    private Image img;
    private Graphics2D mainRender;
    private Graphics2D extraRender;

    private Delays Delays;
    private Highlights Highlights;
    private MultipleScript MultipleScript;
    private Reads Reads;
    private Renderer Renderer;
    private Sounds Sounds;
    private Timer Timer;
    private VisualStyles VisualStyles;
    private Writes Writes;
    private AntiQSort AntiQSort;

    private volatile int updateVisualsForced;
    public  volatile boolean benchmarking;

    public static int MAX_LENGTH_POWER = 15;

    private volatile boolean hidden;
    private volatile boolean frameSkipped;

    public ArrayVisualizer() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Cannot create more than one ArrayVisualizer");
        }
        INSTANCE = this;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        UIManager.getLookAndFeelDefaults().put("Slider.paintValue", Boolean.FALSE); // GTK PLAF fix

        this.window = new JFrame();
        this.window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_K || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    ArrayVisualizer.this.getDelays().togglePaused();
                } else if (e.getKeyCode() == KeyEvent.VK_F12) {
                    System.gc();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        this.window.setDropTarget(new DropTarget() {
            @SuppressWarnings("unchecked")
            public synchronized void drop(DropTargetDropEvent e) {
                try {
                    e.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>)e.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    int success = 0;
                    for (File file : droppedFiles) {
                        if (ArrayVisualizer.this.SortAnalyzer.importSort(file, false)) {
                            success++;
                        }
                    }
                    ArrayVisualizer.this.SortAnalyzer.sortSorts();
                    ArrayVisualizer.this.refreshSorts();
                    if (success == 0) {
                        JErrorPane.invokeCustomErrorMessage("Failed to import all " + droppedFiles.size() + " sorts");
                    } else {
                        String message = "Successfully imported " + success + " sorts";
                        if (success < droppedFiles.size()) {
                            message += " and failed to import " + (droppedFiles.size() - success);
                        }
                        JOptionPane.showMessageDialog(null, message, "Import Sorts", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() != KeyEvent.KEY_PRESSED)
                    return false;
                if (e.getKeyCode() == KeyEvent.VK_O && (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0) {
                    if (ArrayVisualizer.this.isActive())
                        return false;
                    Thread thread = new Thread("ScriptSortThread") {
                        @Override
                        public void run(){
                            RunScriptedSorts RunScriptedSorts = new RunScriptedSorts(ArrayVisualizer.this);
                            try {
                                RunScriptedSorts.runThread(ArrayVisualizer.this.getArray(), 0, 0, false);
                            } catch (Exception e) {
                                JErrorPane.invokeErrorMessage(e);
                            }
                        }
                    };
                    thread.start();
                    return true;
                } else if (e.getKeyCode() == KeyEvent.VK_S && (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0) {
                    int[] snapshot = Arrays.copyOfRange(ArrayVisualizer.this.getArray(), 0, ArrayVisualizer.this.getCurrentLength());
                    FileDialog selected = new SaveArrayDialog();
                    ArrayFileWriter.writeArray(selected.getFile(), snapshot, snapshot.length);
                    return true;
                } else if (e.getKeyCode() == KeyEvent.VK_F5) {
                    ArrayVisualizer.this.updateNow();
                    return true;
                }
                return false;
            }
        });

        this.window.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                ArrayVisualizer.this.updateNow();
            }
            @Override
            public void componentMoved(ComponentEvent e) {
                ArrayVisualizer.this.updateNow();
            }
            @Override
            public void componentShown(ComponentEvent e) {
                ArrayVisualizer.this.hidden = false;
                if (ArrayVisualizer.this.frameSkipped) {
                    frameSkipped = false;
                    ArrayVisualizer.this.updateNow();
                }
            }
            @Override
            public void componentHidden(ComponentEvent e) {
                ArrayVisualizer.this.hidden = true;
            }
        });

        new Thread("FileDialogInitializer") {
            @Override
            public void run() {
                FileDialog.initialize();
            }
        }.start();

        this.buildInfo = new Properties();
        this.buildInfo.setProperty("commitId", "unknown"); // Put default
        try (InputStream is = getClass().getResourceAsStream("/buildInfo.properties")) {
            if (is != null) {
                this.buildInfo.load(is);
            }
        } catch (IOException e) {
            System.err.println("Unable to read buildInfo.properties");
            e.printStackTrace();
        }

        this.MIN_ARRAY_VAL = 2;
        this.MAX_ARRAY_VAL = (int)Math.pow(2, MAX_LENGTH_POWER);

        int[] array;
        try {
            array = new int[this.MAX_ARRAY_VAL];
        } catch (OutOfMemoryError e) {
            JErrorPane.invokeCustomErrorMessage("Failed to allocate main array. The program will now exit.");
            System.exit(1);
            array = null;
        }
        this.array = array;

        this.sortLength = this.MAX_ARRAY_VAL;

        this.arrays = new ArrayList<>();
        this.arrays.add(this.array);

        this.fontSelection = "Times New Roman";
        this.fontSelectionScale = 25;
        List<StatisticType> statsInfoList = new ArrayList<>();
        Throwable statsLoadException = null;
        while (true) {
            try (Scanner statsScanner = new Scanner(new File("stats-config.txt"))) {
                while (statsScanner.hasNextLine()) {
                    String line = statsScanner.nextLine().trim();
                    if (line.length() > 0 && line.charAt(0) == '#') continue;
                    if (line.startsWith("FONT:")) {
                        String font = line.substring(5);
                        int starIndex;
                        if ((starIndex = font.indexOf('*')) != -1) {
                            fontSelectionScale = Double.parseDouble(font.substring(starIndex + 1).trim());
                            font = font.substring(0, starIndex);
                        }
                        fontSelection = font.trim();
                        continue;
                    }
                    StatisticType type = StatisticType.CONFIG_KEYS.get(line.toLowerCase());
                    if (type == null) {
                        System.err.println("Unknown statistic type: " + type);
                        continue;
                    }
                    statsInfoList.add(type);
                }
            } catch (FileNotFoundException e) {
                try (InputStream in = getClass().getResourceAsStream("/stats-config.txt")) {
                    try (OutputStream out = new FileOutputStream("stats-config.txt")) {
                        byte[] buf = new byte[8192];
                        int length;
                        while ((length = in.read(buf)) > 0) {
                            out.write(buf, 0, length);
                        }
                    } catch (Exception e2) {
                        statsLoadException = e2;
                    }
                } catch (Exception e2) {
                    statsLoadException = e2;
                }
                continue;
            } catch (Exception e) {
                statsLoadException = e;
            }
            break;
        }
        if (statsLoadException != null) {
            JErrorPane.invokeErrorMessage(statsLoadException, "ArrayVisualizer");
            JOptionPane.showMessageDialog(
                this.window,
                "Unable to load stats-config, using default config",
                "ArrayVisualizer",
                JOptionPane.WARNING_MESSAGE
            );
            statsConfig = new StatisticType[] {
                StatisticType.SORT_IDENTITY,
                StatisticType.ARRAY_LENGTH,
                    StatisticType.LINE_BREAK,
                StatisticType.SORT_DELAY,
                StatisticType.VISUAL_TIME,
                StatisticType.EST_SORT_TIME,
                    StatisticType.LINE_BREAK,
                StatisticType.COMPARISONS,
                StatisticType.SWAPS,
                StatisticType.REVERSALS,
                    StatisticType.LINE_BREAK,
                StatisticType.MAIN_WRITE,
                StatisticType.AUX_WRITE,
                StatisticType.AUX_ALLOC,
                StatisticType.SEGMENTS
            };
        } else {
            statsConfig = statsInfoList.toArray(new StatisticType[statsInfoList.size()]);
        }

        this.sortLength = Math.min(2048, this.MAX_ARRAY_VAL);
        this.uniqueItems = this.sortLength;

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
        this.AntiQSort = new AntiQSort(this);

        SoundFrame test = new SoundFrame(this.Sounds);
        test.setVisible(true);

        this.ArrayManager = new ArrayManager(this);
        this.SortAnalyzer = new SortAnalyzer(this);

        this.SortAnalyzer.analyzeSorts();
        this.refreshSorts();

        int[] stabilityTable, indexTable, validateArray;
        boolean disabledStabilityCheck;
        try {
            stabilityTable = new int[this.MAX_ARRAY_VAL];
            indexTable = new int[this.MAX_ARRAY_VAL];
            disabledStabilityCheck = false;
        } catch (OutOfMemoryError e) {
            JErrorPane.invokeCustomErrorMessage("Failed to allocate arrays for stability check. This feature will be disabled.");
            stabilityTable = null;
            indexTable = null;
            disabledStabilityCheck = true;
        }
        try {
            validateArray = new int[this.MAX_ARRAY_VAL];
        } catch (OutOfMemoryError e) {
            JErrorPane.invokeCustomErrorMessage("Failed to allocate array for improved validation. This feature will be disabled.");
            validateArray = null;
        }
        this.validateArray = validateArray;;
        this.stabilityTable = stabilityTable;
        this.indexTable = indexTable;
        this.disabledStabilityCheck = disabledStabilityCheck;
        if (!this.disabledStabilityCheck) {
            this.resetStabilityTable();
            this.resetIndexTable();
        }

        this.MultipleScript = new MultipleScript(this);

        this.category = "";
        this.heading = "";
        this.extraHeading = "";
        this.typeFace = new Font(fontSelection, Font.PLAIN, (int) (this.getWindowRatio() * fontSelectionScale));

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
        this.EXTARRAYS = false;

        this.ANTIQSORT = false;
        this.STABILITY = false;
        this.NETWORKS = false;

        this.isCanceled = false;

        this.updateVisualsForced = 0;
        this.benchmarking = false;

        this.cx = 0;
        this.cy = 0;
        this.ch = 0;
        this.cw = 0;

        this.ArrayManager.initializeArray(this.array);

        //TODO: Overhaul visual code to properly reflect Swing (JavaFX?) style and conventions
        this.toggleVisualUpdates(false);
        //DRAW THREAD
        this.visualsThread = new Thread("VisualsThread") {
            @SuppressWarnings("unused")
            @Override
            public void run() {
                ArrayVisualizer.this.visualsEnabled = true;

                utils.Renderer.initializeVisuals(ArrayVisualizer.this);

                Graphics background = ArrayVisualizer.this.window.getGraphics();
                background.setColor(Color.BLACK);
                int coltmp = 255;

                ArrayVisualizer.this.visualClasses = new Visual[15];

                ArrayVisualizer.this.visualClasses[0]  = new          BarGraph(ArrayVisualizer.this);
                ArrayVisualizer.this.visualClasses[1]  = new           Rainbow(ArrayVisualizer.this);
                ArrayVisualizer.this.visualClasses[2]  = new DisparityBarGraph(ArrayVisualizer.this);
                ArrayVisualizer.this.visualClasses[3]  = new       ColorCircle(ArrayVisualizer.this);
                ArrayVisualizer.this.visualClasses[4]  = new   DisparityCircle(ArrayVisualizer.this);
                ArrayVisualizer.this.visualClasses[5]  = new   DisparityChords(ArrayVisualizer.this);
                ArrayVisualizer.this.visualClasses[6]  = new     DisparityDots(ArrayVisualizer.this);
                ArrayVisualizer.this.visualClasses[7]  = new       ScatterPlot(ArrayVisualizer.this);
                ArrayVisualizer.this.visualClasses[8]  = new          WaveDots(ArrayVisualizer.this);
                ArrayVisualizer.this.visualClasses[9]  = new       CustomImage(ArrayVisualizer.this);
                ArrayVisualizer.this.visualClasses[10] = new          SineWave(ArrayVisualizer.this);
                ArrayVisualizer.this.visualClasses[11] = new         HoopStack(ArrayVisualizer.this);
                ArrayVisualizer.this.visualClasses[12] = new         PixelMesh(ArrayVisualizer.this);
                ArrayVisualizer.this.visualClasses[13] = new            Spiral(ArrayVisualizer.this);
                ArrayVisualizer.this.visualClasses[14] = new        SpiralDots(ArrayVisualizer.this);

                while (ArrayVisualizer.this.visualsEnabled) {
                    if (ArrayVisualizer.this.updateVisualsForced == 0) {
                        try {
                            synchronized (ArrayVisualizer.this) {
                                ArrayVisualizer.this.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    long startTime = System.currentTimeMillis();
                    try {
                        if (ArrayVisualizer.this.updateVisualsForced > 0) {
                            ArrayVisualizer.this.updateVisualsForced--;
                            ArrayVisualizer.this.Renderer.updateVisualsStart(ArrayVisualizer.this);
                            int[][] arrays = ArrayVisualizer.this.arrays.toArray(new int[][] { });
                            ArrayVisualizer.this.Renderer.drawVisual(ArrayVisualizer.this.VisualStyles, arrays, ArrayVisualizer.this, ArrayVisualizer.this.Highlights);

                            if (ArrayVisualizer.this.TEXTDRAW) {
                                ArrayVisualizer.this.statSnapshot.updateStats(ArrayVisualizer.this);
                                ArrayVisualizer.this.updateFontSize();
                                ArrayVisualizer.this.drawStats(Color.BLACK, true);
                                ArrayVisualizer.this.drawStats(Color.WHITE, false);
                            }
                            background.drawImage(ArrayVisualizer.this.img, 0, 0, null);
                            Toolkit.getDefaultToolkit().sync();
                        }
                        if (ArrayVisualizer.this.updateVisualsForced > 10000) {
                            ArrayVisualizer.this.updateVisualsForced = 100;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    long endTime = System.currentTimeMillis();
                    statSnapshot.frameTimeMillis = endTime - startTime;
                }
            }
        };

        this.Sounds.startAudioThread();
        this.drawWindows();
    }

    public static ArrayVisualizer getInstance() {
        return INSTANCE;
    }

    public JFrame getWindow() {
        return window;
    }

    public void refreshSorts() {
        this.sorts = this.SortAnalyzer.getSorts();
        this.invalidSorts = this.SortAnalyzer.getInvalidSorts();
        this.sortSuggestions = this.SortAnalyzer.getSuggestions();
    }

    private void drawStats(Color textColor, boolean dropShadow) {
        int xOffset = 15;
        int yOffset = 30;
        if (dropShadow) {
            xOffset += 3;
            yOffset += 3;
        }

        double windowRatio = this.getWindowRatio();
        int yPos = (int)(fontSelectionScale / 25.0 * 30);

        this.mainRender.setColor(textColor);

        statLoop:
        for (StatisticType statType : statsConfig) {
            // System.out.println(yPos);
            String stat;
            switch (statType) {
                case LINE_BREAK:
                    yPos += (int)(fontSelectionScale / 25.0 * 15);
                    continue statLoop;
                case SORT_IDENTITY:
                    stat = statSnapshot.getSortIdentity();
                    break;
                case ARRAY_LENGTH:
                    stat = statSnapshot.getArrayLength();
                    break;
                case FRAMERATE:
                    stat = statSnapshot.getFramerate();
                    break;
                case SORT_DELAY:
                    stat = statSnapshot.getSortDelay();
                    break;
                case VISUAL_TIME:
                    stat = statSnapshot.getVisualTime();
                    break;
                case EST_SORT_TIME:
                    stat = statSnapshot.getEstSortTime();
                    break;
                case COMPARISONS:
                    stat = statSnapshot.getComparisonCount();
                    break;
                case SWAPS:
                    stat = statSnapshot.getSwapCount();
                    break;
                case REVERSALS:
                    stat = statSnapshot.getReversalCount();
                    break;
                case MAIN_WRITE:
                    stat = statSnapshot.getMainWriteCount();
                    break;
                case AUX_WRITE:
                    stat = statSnapshot.getAuxWriteCount();
                    break;
                case AUX_ALLOC:
                    stat = statSnapshot.getAuxAllocAmount();
                    break;
                case SEGMENTS:
                    stat = statSnapshot.getSegments();
                    break;
                default:
                    stat = null; // Unreachable
            }
            mainRender.drawString(stat, xOffset, (int)(windowRatio * yPos) + yOffset);
            yPos += fontSelectionScale;
        }
    }

    public void updateNow() {
        this.updateNow(1);
    }
    public void updateNow(int fallback) {
        if (hidden) {
            frameSkipped = true;
            return;
        }
        this.updateVisualsForced += fallback;
        synchronized (this) {
            this.notify();
        }
    }

    /**
     * @deprecated This method no longer does anything!
     */
    @Deprecated
    public void toggleVisualUpdates(boolean bool) {
    }

    public void forceVisualUpdate(int count) {
        this.updateVisualsForced += count;
    }

    public boolean enableBenchmarking(boolean enabled) {
        if (enabled) {

        } else if (this.benchmarking) {
            if (this.getCurrentLength() >= Math.pow(2, 23)) {
                int warning = JOptionPane.showOptionDialog(this.getMainWindow(), "Warning! "
                + "Your computer's GPU probably can't handle more than 2^23 elements at any "
                + "framrate not significantly less than 1. Would you still like "
                + "to re-enable graphics?", "Warning!", 2, JOptionPane.WARNING_MESSAGE,
                null, new String[] { "Yes", "Please save my GPU!" }, "Please save my GPU!");
                if (warning != 0) {
                    enabled = true;
                }
            }
        }
        this.benchmarking = enabled;
        return this.benchmarking;
    }

    public int[] getValidationArray() {
        return this.validateArray;
    }

    public int getStabilityValue(int n) {
        n = Math.max(0, Math.min(n, this.sortLength-1));

        return this.stabilityTable[n];
    }

    public int[] getStabilityTable() {
        return this.stabilityTable;
    }

    public void resetStabilityTable() {
        for (int i = 0; i < this.sortLength; i++) {
            this.stabilityTable[i] = i;
        }
    }

    public int getIndexValue(int n) {
        n = Math.max(0, Math.min(n, this.sortLength-1));

        return this.indexTable[n];
    }

    public int[] getIndexTable() {
        return this.indexTable;
    }

    public void setIndexTable() {
        for (int i = 0; i < this.sortLength; i++) {
            this.indexTable[array[i]] = i;
        }
    }

    public void resetIndexTable() {
        for (int i = 0; i < this.sortLength; i++) {
            this.indexTable[i] = i;
        }
    }

    public boolean isSorted() {
        return this.statSnapshot.findSegments(this.array, this.sortLength, this.REVERSED)[0] == 1;
    }

    public int[] getArray() {
        return this.array;
    }

    public ArrayList<int[]> getArrays() {
        return this.arrays;
    }

    public ArrayManager getArrayManager() {
        return this.ArrayManager;
    }
    public SortAnalyzer getSortAnalyzer() {
        return this.SortAnalyzer;
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

    public ArrayFrame getArrayFrame() {
        return this.ArrayFrame;
    }

    public SortInfo[] getSorts() {
        return this.sorts;
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

    public void setComparator(int comparator) {
        switch (comparator) {
            case 0:
                this.REVERSED = false;
                this.ANTIQSORT = false;
                this.STABILITY = false;
                this.NETWORKS = false;
                break;
            case 1:
                this.REVERSED = false;
                this.ANTIQSORT = true;
                this.STABILITY = false;
                this.NETWORKS = false;
                break;
            case 2:
                this.REVERSED = false;
                this.ANTIQSORT = false;
                this.STABILITY = true;
                this.NETWORKS = false;
                break;
            case 3:
                this.REVERSED = true;
                this.ANTIQSORT = false;
                this.STABILITY = false;
                this.NETWORKS = false;
                break;
            case 4:
                this.REVERSED = false;
                this.ANTIQSORT = false;
                this.STABILITY = false;
                this.NETWORKS = true;
                break;
        }
    }

    public boolean generateSortingNetworks() {
        return this.NETWORKS;
    }

    public boolean useAntiQSort() {
        return this.ANTIQSORT;
    }
    public void initAntiQSort() {
        this.AntiQSort.beginSort(this.array, this.sortLength);
    }
    public void finishAntiQSort(String name) {
        int[] result = this.AntiQSort.getResult();
        this.AntiQSort.hideResult();
        String outName = "antiqsort_" + name + "_" + this.sortLength;
        if (!ArrayFileWriter.writeArray(outName, result, sortLength)) {
            return;
        }
        JOptionPane.showMessageDialog(null, "Successfully saved output to file \"" + outName + "\"", "AntiQSort", JOptionPane.INFORMATION_MESSAGE);
    }
    public int antiqCompare(int left, int right) {
        int cmp = this.AntiQSort.compare(left, right);
        if (cmp == 0)
            return 0;
        return cmp / Math.abs(cmp);
    }

    public boolean doingStabilityCheck() {
        return this.STABILITY;
    }

    public boolean reversedComparator() {
        return this.REVERSED;
    }

    // These next five methods should be part of ArrayManager
    public int getCurrentLength() {
        return this.sortLength;
    }
    public void setCurrentLength(int newLength) {
        this.sortLength = newLength;
        this.Delays.setSleepRatio(this.sortLength/1024d);
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

    public Color getHighlightColor() {
        if (this.colorEnabled()) {
            if (this.analysisEnabled())
                return Color.LIGHT_GRAY;
            else
                return Color.WHITE;
        } else {
            if (this.analysisEnabled())
                return Color.BLUE;
            else
                return Color.RED;
        }
    }

    public void createVolatileImage() {
        this.img = this.window.getGraphicsConfiguration().createCompatibleVolatileImage(this.cw, this.ch);
    }
    public Stroke getThickStroke() {
        return new BasicStroke((float) (5 * this.getWindowRatio()));
    }
    public Stroke getDefaultStroke() {
        return new BasicStroke((float) (3 * this.getWindowRatio()));
    }
    public Stroke getThinStroke() {
        return new BasicStroke((float) (this.getWindowRatio()));
    }
    public Stroke getCustomStroke(double size) {
        return new BasicStroke((float) (size * this.getWindowRatio()));
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
        for (Visual visual : this.visualClasses) {
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
        this.typeFace = new Font(fontSelection, Font.PLAIN, (int) (this.getWindowRatio() * fontSelectionScale));
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

        double sleepRatio = 256d/this.sortLength;
        long tempComps = this.Reads.getComparisons();
        this.Reads.setComparisons(0);

        String temp = this.heading;
        this.heading = "Verifying sort...";

        int cmpVal = this.REVERSED ? -1 : 1;

        boolean success = true, stable = true;
        int unstableIdx = 0;

        boolean validate = this.validateArray != null;
        boolean validateFailed = false;
        int invalidateIdx = 0;

        for (int i = 0; i < this.sortLength + this.getLogBaseTwoOfLength(); i++) {
            if (i < this.sortLength) this.Highlights.markArray(1, i);
            this.Highlights.incrementFancyFinishPosition();

            if (i < this.sortLength - 1) {
                if (validate && !validateFailed && this.Reads.compareOriginalValues(this.array[i], this.validateArray[i]) != 0) {
                    validateFailed = true;
                    invalidateIdx = i;
                }
                if (stable && this.Reads.compareOriginalValues(this.array[i], this.array[i + 1]) == cmpVal) {
                    stable = false;
                    unstableIdx = i;
                }
                if (this.Reads.compareValues(this.array[i], this.array[i + 1]) == cmpVal) {
                    this.Highlights.clearMark(1);

                    boolean tempSound = this.Sounds.isEnabled();
                    this.Sounds.toggleSound(false);
                    this.Highlights.toggleFancyFinish(false);

                    for (int j = i + 1; j < this.sortLength; j++) {
                        this.Highlights.markArray(j, j);
                        this.Delays.sleep(sleepRatio);
                    }

                    JOptionPane.showMessageDialog(this.window, "The sort was unsuccessful;\nIndices " + i + " and " + (i + 1) + " are out of order!", "Error", JOptionPane.OK_OPTION, null);
                    success = false;

                    this.Highlights.clearAllMarks();

                    i = this.sortLength + this.getLogBaseTwoOfLength();

                    this.Sounds.toggleSound(tempSound);
                }
            }

            if (this.Highlights.fancyFinishEnabled()) {
                this.Delays.sleep(sleepRatio);
            }
        }
        this.Highlights.clearMark(1);

        // if (tempStability && success)
        //     JOptionPane.showMessageDialog(this.window, "This sort is stable!", "Information", JOptionPane.OK_OPTION, null);
        if (this.STABILITY && success && !stable) {
            boolean tempSound = this.Sounds.isEnabled();
            this.Sounds.toggleSound(false);
            this.Highlights.toggleFancyFinish(false);

            for (int j = unstableIdx; j < this.sortLength; j++) {
                this.Highlights.markArray(j, j);
                this.Delays.sleep(sleepRatio);
            }

            JOptionPane.showMessageDialog(this.window, "This sort is not stable;\nIndices " + unstableIdx + " and " + (unstableIdx + 1) + " are out of order!", "Error", JOptionPane.OK_OPTION, null);

            this.Highlights.clearAllMarks();
            this.Sounds.toggleSound(tempSound);
        } else if (success && validateFailed) {
            boolean tempSound = this.Sounds.isEnabled();
            this.Sounds.toggleSound(false);
            this.Highlights.toggleFancyFinish(false);

            for (int j = invalidateIdx + 1; j < this.sortLength; j++) {
                this.Highlights.markArray(j, j);
                this.Delays.sleep(sleepRatio);
            }

            JOptionPane.showMessageDialog(this.window, "The sort was unsuccessful;\narray[" + invalidateIdx + "] != validateArray[" + invalidateIdx + "]", "Error", JOptionPane.OK_OPTION, null);

            this.Highlights.clearAllMarks();
            this.Sounds.toggleSound(tempSound);
        }

        this.heading = temp;
        this.Reads.setComparisons(tempComps);

        if (this.benchmarking) {
            JOptionPane.showMessageDialog(this.window, "The sort took a total of " + this.Timer.getRealTime());
        }

        if (this.Highlights.fancyFinishActive()) {
            this.Highlights.toggleFancyFinish(false);
        }
        this.Highlights.resetFancyFinish();
    }

    public String formatTimes() {
        String result = "";

        Hashtable<String, Double> categoricalTimes = this.Timer.getCategoricalTimes();
        for (Map.Entry<String, Double> keyValuePair : categoricalTimes.entrySet()) {
            result += keyValuePair.getKey() + ":\t" + this.Timer.prettifyTime(keyValuePair.getValue()) + "\n";
        }

        String totalTime = this.Timer.getRealTime();
        result += "--------------------\nTotal:\t" + totalTime;

        return result;
    }

    public void endSort() {
        this.Timer.disableRealTimer();
        this.Highlights.clearAllMarks();
        System.out.println(formatTimes());

        this.isCanceled = false;
        this.Delays.changeSkipped(false);
        double speed = this.Delays.getSleepRatio();
        this.verifySortAndSweep();
        this.Delays.setSleepRatio(speed);

        this.arrays.subList(1, this.arrays.size()).clear();
        this.Writes.clearAllocAmount();

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
    public void toggleExternalArrays(boolean Bool) {
        this.EXTARRAYS = Bool;
    }

    public void setVisual(VisualStyles choice) {
        if (choice == io.github.arrayv.visuals.VisualStyles.CUSTOM_IMAGE) {
            ((CustomImage) this.visualClasses[9]).enableImgMenu();
        }
        this.VisualStyles = choice;
        synchronized (this) {
            this.updateNow();
        }
    }

    public int getCurrentGap() {
        return this.currentGap;
    }
    public void setCurrentGap(int gap) {
        this.currentGap = gap;
    }

    public boolean sortCanceled() {
        return this.isCanceled;
    }
    public void setCanceled(boolean canceled) {
        this.isCanceled = canceled;
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
    public boolean externalArraysEnabled() {
        return this.EXTARRAYS;
    }

    public DecimalFormat getNumberFormat() {
        return this.formatter;
    }

    private static String parseStringArray(String[] stringArray) {
        String parsed = "";
        for (int i = 0; i < stringArray.length; i++) {
            parsed += stringArray[i] + "\n";
        }
        return parsed;
    }

    private void drawWindows() {
        this.VisualStyles = io.github.arrayv.visuals.VisualStyles.BARS;
        this.category = "Select a Sort";

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.window.setSize((int) (screenSize.getWidth() / 2), (int) (screenSize.getHeight() / 2));

        StringBuilder title = new StringBuilder("w0rthy's Array Visualizer - ");
        title.append(this.sorts.length);
        title.append(" Sorts, 15 Visual Styles, and Infinite Inputs to Sort");
        String versionSha = buildInfo.getProperty("commitId");
        if (!versionSha.equals("@git.sha@") && !versionSha.equals("unknown")) { // Hash not loaded
            title.append(" (commit ").append(versionSha).append(")");
        }

        this.window.setLocation(0, 0);
        this.window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.window.setTitle(title.toString());
        this.window.setBackground(Color.BLACK);
        this.window.setIgnoreRepaint(true);

        this.window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent close) {
                ArrayVisualizer.this.Sounds.closeSynth();
                ArrayVisualizer.this.visualsEnabled = false;
                if (ArrayVisualizer.this.isActive()) {
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

        if (this.invalidSorts != null) {
            String output = parseStringArray(this.invalidSorts);
            JOptionPane.showMessageDialog(this.window, "The following algorithms were not loaded:\n" + output, "Warning", JOptionPane.WARNING_MESSAGE);
        }
        if (this.sortSuggestions != null) {
            String output = parseStringArray(this.sortSuggestions);
            JOptionPane.showMessageDialog(this.window, "Here's a list of suggestions based on your sorts:\n" + output, "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.d3d", "false");
        if (args.length > 0) {
            ArrayVisualizer.MAX_LENGTH_POWER = Integer.parseInt(args[0]);
        }
        new ArrayVisualizer();
    }
}
