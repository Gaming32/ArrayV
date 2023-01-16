package io.github.arrayv.main;

import io.github.arrayv.dialogs.FileDialog;
import io.github.arrayv.dialogs.SaveArrayDialog;
import io.github.arrayv.frames.ArrayFrame;
import io.github.arrayv.frames.SoundFrame;
import io.github.arrayv.frames.UtilFrame;
import io.github.arrayv.groovyapi.ArrayVEventHandler;
import io.github.arrayv.groovyapi.ScriptManager;
import io.github.arrayv.panes.JErrorPane;
import io.github.arrayv.sortdata.SortInfo;
import io.github.arrayv.utils.Renderer;
import io.github.arrayv.utils.Timer;
import io.github.arrayv.utils.*;
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

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    // @checkstyle:off StaticVariableName
    private static ArrayVisualizer INSTANCE = null;
    // @checkstyle:on StaticVariableName

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

        // @checkstyle:off IndentationCheck - It doesn't like {{ syntax
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
        // @checkstyle:on IndentationCheck
    }

    final JFrame window;

    private final int minArrayVal;
    private final int maxArrayVal;

    private final Properties buildInfo;

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

    private final ArrayManager arrayManager;
    private final SortAnalyzer sortAnalyzer;

    private final UtilFrame utilFrame;
    private final ArrayFrame arrayFrame;

    private Visual[] visualClasses;

    private Thread sortingThread;
    private final Thread visualsThread;

    private volatile boolean visualsEnabled;
    private final boolean disabledStabilityCheck;

    private String category;
    private String heading;
    private String extraHeading;
    private Font typeFace;
    private final DecimalFormat formatter;

    private volatile int currentGap;

    private boolean showShuffleAnimation;

    private volatile boolean highlightAsAnalysis;

    private final Statistics statSnapshot;
    private String fontSelection;
    private double fontSelectionScale;

    private volatile boolean showStatistics;
    private volatile boolean showColor;
    private volatile boolean showLines;
    private volatile boolean showExternalArrays;

    private volatile boolean useAntiQSort;
    private volatile boolean stabilityChecking;
    private volatile boolean visualizingNetworks;
    private volatile boolean reversedComparator;

    private volatile boolean isCanceled;

    private volatile int cx;
    private volatile int cy;
    private volatile int ch;
    private volatile int cw;

    private Image img;
    private Graphics2D mainRender;
    private Graphics2D extraRender;

    private final Delays Delays;
    private final Highlights Highlights;
    private final Reads Reads;
    private final Renderer renderer;
    private final Sounds Sounds;
    private final Timer Timer;
    private final Writes Writes;
    private final AntiQSort antiQSort;
    private final ScriptManager scriptManager;

    private VisualStyles visualStyle;

    private final AtomicInteger updateVisualsForced;
    private volatile boolean benchmarking;

    private static int maxLengthPower = 15;

    private volatile boolean hidden;
    private volatile boolean frameSkipped;

    public ArrayVisualizer() throws IOException {
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
                } else if (e.getKeyCode() == KeyEvent.VK_B) {
                    Delays.beginStepping();
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
                        if (ArrayVisualizer.this.sortAnalyzer.importSort(file, false)) {
                            success++;
                        }
                    }
                    ArrayVisualizer.this.sortAnalyzer.sortSorts();
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

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() != KeyEvent.KEY_PRESSED)
                return false;
            if (e.getKeyCode() == KeyEvent.VK_S && (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0) {
                int[] snapshot = Arrays.copyOfRange(ArrayVisualizer.this.getArray(), 0, ArrayVisualizer.this.getCurrentLength());
                FileDialog selected = new SaveArrayDialog();
                ArrayFileWriter.writeArray(selected.getFile(), snapshot, snapshot.length);
                return true;
            } else if (e.getKeyCode() == KeyEvent.VK_F5) {
                ArrayVisualizer.this.updateNow();
                return true;
            }
            return false;
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
        try (InputStream is = getClass().getResourceAsStream("/buildInfo.properties")) {
            if (is != null) {
                this.buildInfo.load(is);
            }
        } catch (IOException e) {
            System.err.println("Unable to read buildInfo.properties");
            e.printStackTrace();
        }

        this.minArrayVal = 2;
        this.maxArrayVal = (int)Math.pow(2, maxLengthPower);

        int[] array;
        try {
            array = new int[this.maxArrayVal];
        } catch (OutOfMemoryError e) {
            JErrorPane.invokeCustomErrorMessage("Failed to allocate main array. The program will now exit.");
            System.exit(1);
            array = null;
        }
        this.array = array;

        this.sortLength = this.maxArrayVal;

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
                        System.err.println("Unknown statistic type: " + line.toLowerCase());
                        continue;
                    }
                    statsInfoList.add(type);
                }
            } catch (FileNotFoundException e) {
                try (InputStream in = getClass().getResourceAsStream("/stats-config.txt")) {
                    assert in != null;
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
            // @checkstyle:off IndentationCheck - There's custom indentation here to make things more readable
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
            // @checkstyle:on IndentationCheck
        } else {
            statsConfig = statsInfoList.toArray(new StatisticType[0]);
        }

        this.sortLength = Math.min(2048, this.maxArrayVal);
        this.uniqueItems = this.sortLength;

        this.formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = this.formatter.getDecimalFormatSymbols();
        this.formatter.setRoundingMode(RoundingMode.HALF_UP);
        symbols.setGroupingSeparator(',');
        this.formatter.setDecimalFormatSymbols(symbols);

        this.Highlights = new Highlights(this, this.maxArrayVal);
        this.Sounds = new Sounds(this.array, this);
        this.Delays = new Delays(this);
        this.Timer = new Timer(this);
        this.Reads = new Reads(this);
        this.renderer = new Renderer(this);
        this.Writes = new Writes(this);
        this.antiQSort = new AntiQSort(this);
        this.scriptManager = new ScriptManager();

        Highlights.postInit();

        SoundFrame test = new SoundFrame(this.Sounds);
        test.setVisible(true);

        this.arrayManager = new ArrayManager(this);
        this.sortAnalyzer = new SortAnalyzer(this);

        this.sortAnalyzer.analyzeSorts();
        this.refreshSorts();

        int[] stabilityTable, indexTable, validateArray;
        boolean disabledStabilityCheck;
        try {
            stabilityTable = new int[this.maxArrayVal];
            indexTable = new int[this.maxArrayVal];
            disabledStabilityCheck = false;
        } catch (OutOfMemoryError e) {
            JErrorPane.invokeCustomErrorMessage("Failed to allocate arrays for stability check. This feature will be disabled.");
            stabilityTable = null;
            indexTable = null;
            disabledStabilityCheck = true;
        }
        try {
            validateArray = new int[this.maxArrayVal];
        } catch (OutOfMemoryError e) {
            JErrorPane.invokeCustomErrorMessage("Failed to allocate array for improved validation. This feature will be disabled.");
            validateArray = null;
        }
        this.validateArray = validateArray;
        this.stabilityTable = stabilityTable;
        this.indexTable = indexTable;
        //noinspection ConstantValue
        this.disabledStabilityCheck = disabledStabilityCheck;
        //noinspection ConstantValue
        if (!this.disabledStabilityCheck) {
            this.resetStabilityTable();
            this.resetIndexTable();
        }

        this.category = "";
        this.heading = "";
        this.extraHeading = "";
        this.typeFace = new Font(fontSelection, Font.PLAIN, (int) (this.getWindowRatio() * fontSelectionScale));

        this.statSnapshot = new Statistics(this);

        this.utilFrame = new UtilFrame(this.array, this);
        this.arrayFrame = new ArrayFrame(this.array, this);

        this.utilFrame.reposition(this.arrayFrame);

        this.showShuffleAnimation = true;
        this.highlightAsAnalysis = false;
        this.showStatistics = true;

        this.showColor = false;
        this.showLines = false;
        this.showExternalArrays = false;

        this.useAntiQSort = false;
        this.stabilityChecking = false;
        this.visualizingNetworks = false;

        this.isCanceled = false;

        this.updateVisualsForced = new AtomicInteger();
        this.benchmarking = false;

        this.cx = 0;
        this.cy = 0;
        this.ch = 0;
        this.cw = 0;

        scriptManager.loadDefaultScripts();
        scriptManager.runEventHandlers(ArrayVEventHandler.EventType.DEFAULT_SCRIPTS_INSTALLED);

        this.arrayManager.initializeArray(this.array);

        //TODO: Overhaul visual code to properly reflect Swing (JavaFX?) style and conventions
        //DRAW THREAD
        this.visualsThread = new Thread("VisualsThread") {
            @SuppressWarnings("unused")
            @Override
            public void run() {
                ArrayVisualizer.this.visualsEnabled = true;

                io.github.arrayv.utils.Renderer.initializeVisuals(ArrayVisualizer.this);

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
                    if (ArrayVisualizer.this.updateVisualsForced.get() == 0) {
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
                        if (ArrayVisualizer.this.updateVisualsForced.get() > 0) {
                            ArrayVisualizer.this.updateVisualsForced.decrementAndGet();
                            ArrayVisualizer.this.renderer.updateVisualsStart(ArrayVisualizer.this);
                            int[][] arrays = ArrayVisualizer.this.arrays.toArray(new int[][] { });
                            ArrayVisualizer.this.renderer.drawVisual(ArrayVisualizer.this.visualStyle, arrays, ArrayVisualizer.this, ArrayVisualizer.this.Highlights);

                            if (ArrayVisualizer.this.showStatistics) {
                                ArrayVisualizer.this.statSnapshot.updateStats(ArrayVisualizer.this);
                                ArrayVisualizer.this.updateFontSize();
                                ArrayVisualizer.this.drawStats(Color.BLACK, true);
                                ArrayVisualizer.this.drawStats(Color.WHITE, false);
                            }
                            background.drawImage(ArrayVisualizer.this.img, 0, 0, null);
                            Toolkit.getDefaultToolkit().sync();
                        }
                        if (ArrayVisualizer.this.updateVisualsForced.get() > 10000) {
                            ArrayVisualizer.this.updateVisualsForced.set(100);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    long endTime = System.currentTimeMillis();
                    statSnapshot.setFrameTimeMillis(endTime - startTime);
                }
            }
        };

        this.Sounds.startAudioThread();
        this.drawWindows();

        scriptManager.runEventHandlers(ArrayVEventHandler.EventType.ARRAYV_FULLY_LOADED);
    }

    public static ArrayVisualizer getInstance() {
        return INSTANCE;
    }

    public JFrame getWindow() {
        return window;
    }

    public void refreshSorts() {
        this.sorts = this.sortAnalyzer.getSorts();
        this.invalidSorts = this.sortAnalyzer.getInvalidSorts();
        this.sortSuggestions = this.sortAnalyzer.getSuggestions();
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

        for (StatisticType statType : statsConfig) {
            // System.out.println(yPos);
            String stat;
            switch (statType) {
                case LINE_BREAK:
                    yPos += (int)(fontSelectionScale / 25.0 * 15);
                    continue;
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
        this.updateVisualsForced.addAndGet(fallback);
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
        this.updateVisualsForced.addAndGet(count);
    }

    public boolean enableBenchmarking(boolean enabled) {
        if (!enabled && this.benchmarking) {
            if (this.getCurrentLength() >= Math.pow(2, 23)) {
                int warning = JOptionPane.showOptionDialog(
                    this.getMainWindow(),
                    "Warning! "
                        + "Your computer's CPU probably can't handle more than 2^23 elements at any "
                        + "framrate not significantly less than 1. Would you still like "
                        + "to re-enable graphics?",
                    "Warning!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
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
        return this.statSnapshot.findSegments(this.array, this.sortLength, this.reversedComparator)[0] == 1;
    }

    public int[] getArray() {
        return this.array;
    }

    public ArrayList<int[]> getArrays() {
        return this.arrays;
    }

    public ArrayManager getArrayManager() {
        return this.arrayManager;
    }
    public SortAnalyzer getSortAnalyzer() {
        return this.sortAnalyzer;
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
        return this.renderer;
    }
    public Sounds getSounds() {
        return this.Sounds;
    }
    public Timer getTimer() {
        return this.Timer;
    }
    public VisualStyles getVisualStyles() {
        return this.visualStyle;
    }
    public Writes getWrites() {
        return this.Writes;
    }

    public ScriptManager getScriptManager() {
        return scriptManager;
    }

    public Visual[] getVisuals() {
        return this.visualClasses;
    }

    public UtilFrame getUtilFrame() {
        return this.utilFrame;
    }

    public ArrayFrame getArrayFrame() {
        return this.arrayFrame;
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
        return this.minArrayVal;
    }
    public int getMaximumLength() {
        return this.maxArrayVal;
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
                this.reversedComparator = false;
                this.useAntiQSort = false;
                this.stabilityChecking = false;
                this.visualizingNetworks = false;
                break;
            case 1:
                this.reversedComparator = false;
                this.useAntiQSort = true;
                this.stabilityChecking = false;
                this.visualizingNetworks = false;
                break;
            case 2:
                this.reversedComparator = false;
                this.useAntiQSort = false;
                this.stabilityChecking = true;
                this.visualizingNetworks = false;
                break;
            case 3:
                this.reversedComparator = true;
                this.useAntiQSort = false;
                this.stabilityChecking = false;
                this.visualizingNetworks = false;
                break;
            case 4:
                this.reversedComparator = false;
                this.useAntiQSort = false;
                this.stabilityChecking = false;
                this.visualizingNetworks = true;
                break;
        }
    }

    public boolean generateSortingNetworks() {
        return this.visualizingNetworks;
    }

    public boolean useAntiQSort() {
        return this.useAntiQSort;
    }
    public void initAntiQSort() {
        this.antiQSort.beginSort(this.array, this.sortLength);
    }
    public void finishAntiQSort(String name) {
        int[] result = this.antiQSort.getResult();
        this.antiQSort.hideResult();
        String outName = "antiqsort_" + name + "_" + this.sortLength;
        if (!ArrayFileWriter.writeArray(outName, result, sortLength)) {
            return;
        }
        JOptionPane.showMessageDialog(null, "Successfully saved output to file \"" + outName + "\"", "AntiQSort", JOptionPane.INFORMATION_MESSAGE);
    }
    public int antiqCompare(int left, int right) {
        int cmp = this.antiQSort.compare(left, right);
        if (cmp == 0)
            return 0;
        return cmp / Math.abs(cmp);
    }

    public boolean doingStabilityCheck() {
        return this.stabilityChecking;
    }

    public boolean reversedComparator() {
        return this.reversedComparator;
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
        return this.showShuffleAnimation;
    }

    public void toggleShuffleAnimation(boolean showShuffleAnimation) {
        this.showShuffleAnimation = showShuffleAnimation;
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

    /**
     * @deprecated No longer does anything (always returns {@code false})
     * @return {@code false}
     */
    @Deprecated
    public boolean pointerActive() {
        return false;
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

    public void toggleAnalysis(boolean highlightAsAnalysis) {
        this.highlightAsAnalysis = highlightAsAnalysis;
    }

    public boolean analysisEnabled() {
        return this.highlightAsAnalysis;
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

        int cmpVal = this.reversedComparator ? -1 : 1;

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

                    JOptionPane.showMessageDialog(this.window, "The sort was unsuccessful;\nIndices " + i + " and " + (i + 1) + " are out of order!", "Error", JOptionPane.ERROR_MESSAGE, null);
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
        if (this.stabilityChecking && success && !stable) {
            boolean tempSound = this.Sounds.isEnabled();
            this.Sounds.toggleSound(false);
            this.Highlights.toggleFancyFinish(false);

            for (int j = unstableIdx; j < this.sortLength; j++) {
                this.Highlights.markArray(j, j);
                this.Delays.sleep(sleepRatio);
            }

            JOptionPane.showMessageDialog(this.window, "This sort is not stable;\nIndices " + unstableIdx + " and " + (unstableIdx + 1) + " are out of order!", "Error", JOptionPane.ERROR_MESSAGE, null);

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

            JOptionPane.showMessageDialog(this.window, "The sort was unsuccessful;\narray[" + invalidateIdx + "] != validateArray[" + invalidateIdx + "]", "Error", JOptionPane.ERROR_MESSAGE, null);

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
        StringBuilder result = new StringBuilder();

        Hashtable<String, Double> categoricalTimes = this.Timer.getCategoricalTimes();
        for (Map.Entry<String, Double> keyValuePair : categoricalTimes.entrySet()) {
            result.append(keyValuePair.getKey()).append(":\t").append(this.Timer.prettifyTime(keyValuePair.getValue())).append("\n");
        }

        String totalTime = this.Timer.getRealTime();
        result.append("--------------------\nTotal:\t").append(totalTime);

        return result.toString();
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

    /**
     * @deprecated No longer does anything
     */
    @Deprecated
    public void togglePointer(boolean showPointer) {
    }

    /**
     * @deprecated No longer does anything
     */
    @Deprecated
    public void toggleDistance(boolean unused) {
    }

    /**
     * @deprecated No longer does anything
     */
    @Deprecated
    public void togglePixels(boolean usePixels) {
    }

    /**
     * @deprecated No longer does anything
     */
    @Deprecated
    public void toggleRainbow(boolean rainbow) {
    }

    /**
     * @deprecated No longer does anything
     */
    @Deprecated
    public void toggleSpiral(boolean spiral) {
    }

    public void toggleLinkedDots(boolean showLines) {
        this.showLines = showLines;
    }

    public void toggleStatistics(boolean showStatistics) {
        this.showStatistics = showStatistics;
    }

    public void toggleColor(boolean showColor) {
        this.showColor = showColor;
    }

    /**
     * @deprecated No longer does anything
     */
    @Deprecated
    public void toggleWave(boolean useWave) {
    }

    public void toggleExternalArrays(boolean showExternalArrays) {
        this.showExternalArrays = showExternalArrays;
    }

    public void setVisual(VisualStyles choice) {
        if (choice == io.github.arrayv.visuals.VisualStyles.CUSTOM_IMAGE) {
            ((CustomImage) this.visualClasses[9]).enableImgMenu();
        }
        this.visualStyle = choice;
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
        this.arrayFrame.reposition();
        this.utilFrame.reposition(this.arrayFrame);
    }

    /**
     * @deprecated No longer does anything (always returns {@code false})
     * @return {@code false}
     */
    @Deprecated
    public boolean rainbowEnabled() {
        return false;
    }

    public boolean colorEnabled() {
        return this.showColor;
    }

    /**
     * @deprecated No longer does anything (always returns {@code false})
     * @return {@code false}
     */
    @Deprecated
    public boolean spiralEnabled() {
        return false;
    }

    /**
     * @deprecated No longer does anything (always returns {@code false})
     * @return {@code false}
     */
    @Deprecated
    public boolean distanceEnabled() {
        return false;
    }

    /**
     * @deprecated No longer does anything (always returns {@code false})
     * @return {@code false}
     */
    @Deprecated
    public boolean pixelsEnabled() {
        return false;
    }

    public boolean linesEnabled() {
        return this.showLines;
    }

    /**
     * @deprecated No longer does anything (always returns {@code false})
     * @return {@code false}
     */
    @Deprecated
    public boolean waveEnabled() {
        return false;
    }

    public boolean externalArraysEnabled() {
        return this.showExternalArrays;
    }

    public DecimalFormat getNumberFormat() {
        return this.formatter;
    }

    private static String parseStringArray(String[] stringArray) {
        StringBuilder parsed = new StringBuilder();
        for (String s : stringArray) {
            parsed.append(s).append("\n");
        }
        return parsed.toString();
    }

    private void drawWindows() {
        this.visualStyle = io.github.arrayv.visuals.VisualStyles.BARS;
        this.category = "Select a Sort";

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.window.setSize((int) (screenSize.getWidth() / 2), (int) (screenSize.getHeight() / 2));

        StringBuilder title = new StringBuilder("w0rthy's Array Visualizer - ");
        title.append(this.sorts.length);
        title.append(" Sorts, 15 Visual Styles, and Infinite Inputs to Sort");
        String versionName = buildInfo.getProperty("version");
        String commitSha = buildInfo.getProperty("commitId");
        if (commitSha != null || versionName != null) {
            title.append(" (");
            if (versionName != null) {
                title.append("version ").append(versionName);
                if (commitSha != null) {
                    title.append(", ");
                }
            }
            if (commitSha != null) {
                title.append("commit ").append(commitSha);
            }
            title.append(')');
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
        this.utilFrame.setVisible(true);
        this.arrayFrame.setVisible(true);

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

    public boolean isDisabledStabilityCheck() {
        return disabledStabilityCheck;
    }

    public static int getMaxLengthPower() {
        return maxLengthPower;
    }

    public static void main(String[] args) throws IOException {
        System.setProperty("sun.java2d.d3d", "false");
        if (args.length > 0) {
            ArrayVisualizer.maxLengthPower = Integer.parseInt(args[0]);
        }
        new ArrayVisualizer();
    }
}
