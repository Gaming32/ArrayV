package io.github.arrayv.main;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import io.github.arrayv.panes.JErrorPane;
import io.github.arrayv.sortdata.SortComparator;
import io.github.arrayv.sortdata.SortInfo;
import io.github.arrayv.sortdata.SortNameType;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.CommonUtils;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

/*
 *
The MIT License (MIT)

Copyright (c) 2019 Luke Hutchison
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

public final class SortAnalyzer {
    private static final URL EXTRA_SORTS_DOWNLOAD;
    private static final String EXTRA_SORTS_JAR_NAME = "ArrayV-Extra-Sorts.jar";
    private static final File EXTRA_SORTS_FILE = new File("cache", EXTRA_SORTS_JAR_NAME);
    private static final URLClassLoader EXTRA_SORTS_CLASS_LOADER;
    private static final Map.Entry<String, String>[] IMPORT_REPLACEMENTS = CommonUtils.createPairArray(
        "import dialogs.", "import io.github.arrayv.dialogs.",
        "import frames.", "import io.github.arrayv.frames.",
        "import main.", "import io.github.arrayv.main.",
        "import panels.", "import io.github.arrayv.panels.",
        "import panes.", "import io.github.arrayv.panes.",
        "import prompts.", "import io.github.arrayv.prompts.",
        "import sorts.", "import io.github.arrayv.sorts.",
        "import threads.", "import io.github.arrayv.threads.",
        "import utils.", "import io.github.arrayv.utils.",
        "import visuals.", "import io.github.arrayv.visuals.",
        "import sorts.templates.SortComparator;", "import io.github.arrayv.sortdata.SortComparator;"
    );

    private final Set<Class<?>> extraSorts = new HashSet<>();
    private final Map<SortNameType, Map<String, SortInfo>> sortsByName = new EnumMap<>(SortNameType.class);

    static {
        try {
            EXTRA_SORTS_DOWNLOAD = new URL("https://nightly.link/Gaming32/ArrayV-Extra-Sorts/workflows/build/main/extra-sorts-jar.zip");
            EXTRA_SORTS_CLASS_LOADER = new URLClassLoader(new URL[] {
                EXTRA_SORTS_FILE.toURI().toURL()
            });
        } catch (MalformedURLException e) {
            throw new Error(e);
        }
    }

    private ArrayList<SortInfo> sorts;
    private ArrayList<String> invalidSorts;
    private ArrayList<String> suggestions;

    private String sortErrorMsg;

    private ArrayVisualizer arrayVisualizer;

    SortAnalyzer(ArrayVisualizer arrayVisualizer) {
        this.sorts = new ArrayList<>();
        this.invalidSorts = new ArrayList<>();
        this.suggestions = new ArrayList<>();

        this.arrayVisualizer = arrayVisualizer;
    }

    public boolean didSortComeFromExtra(Sort sort) {
        return didSortComeFromExtra(sort.getClass());
    }

    public boolean didSortComeFromExtra(Class<?> sort) {
        return extraSorts.contains(sort);
    }

    public SortInfo addSort(SortInfo sort) {
        sort = sort.withId(sorts.size());
        sorts.add(sort);
        addSortByName(sort);
        return sort;
    }

    /**
     * Like {@link #addSort}, but also sorts it.
     * This is equivalent to, but more efficient than:
     * <pre>
     * addSort(sort);
     * sortSorts();
     * </pre>
     */
    public SortInfo insortSort(SortInfo sort) {
        int position = Collections.binarySearch(sorts, sort, new SortComparator());
        if (position < 0) {
            // Not found (good)
            position = -position - 1;
        }
        sort = sort.withId(position);
        sorts.add(null);
        for (int i = sorts.size() - 1; i > position; i--) {
            sorts.set(i, sorts.get(i - 1).withId(i));
        }
        sorts.set(position, sort);
        addSortByName(sort);
        return sort;
    }

    private void setSortCameFromExtra(Class<?> sort) {
        extraSorts.add(sort);
    }

    @SuppressWarnings("unchecked")
    private boolean compileSingle(String name, ClassLoader loader) {
        Class<?> sortClass;
        try {
            if (loader == null)
                sortClass = Class.forName(name);
            else
                sortClass = Class.forName(name, true, loader);
            if (!Sort.class.isAssignableFrom(sortClass)) {
                throw new IllegalArgumentException(sortClass + " does not subclass Sort");
            }
        } catch (ClassNotFoundException e) {
            System.err.println(e);
            return true;
        }
        return compileSingle((Class<? extends Sort>)sortClass);
    }

    private boolean compileSingle(Class<? extends Sort> sortClass) {
        try {
            if (sortClass.getClassLoader() == EXTRA_SORTS_CLASS_LOADER) {
                setSortCameFromExtra(sortClass);
            }
            SortInfo sort = new SortInfo(sorts.size(), sortClass);

            try {
                if (verifySort(sort)) {
                    String suggestion = checkForSuggestions(sort);
                    if (!suggestion.isEmpty()) {
                        suggestions.add(suggestion);
                    }
                    sorts.add(sort);
                    addSortByName(sort);
                } else {
                    throw new Exception(sortErrorMsg);
                }
            } catch (Exception e) {
                invalidSorts.add(sort.getClass().getName() + " (" + e.getMessage() + ")");
                return false;
            }
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e, "Could not load " + sortClass.getName());
            invalidSorts.add(sortClass.getName() + " (failed to load)");
            return false;
        }
        return true;
    }

    private Map<String, SortInfo> getSortNameCategory(SortNameType type) {
        return sortsByName.computeIfAbsent(type, k -> new HashMap<>());
    }

    private void addSortByName(SortInfo sort) {
        if (sort.getInternalName() != null) {
            getSortNameCategory(SortNameType.INTERNAL_NAME).put(sort.getInternalName(), sort);
        }
        getSortNameCategory(SortNameType.LIST_NAME).put(sort.getListName(), sort);
        getSortNameCategory(SortNameType.RUN_NAME).put(sort.getRunName(), sort);
        getSortNameCategory(SortNameType.SHOWCASE_NAME).put(sort.getRunAllName(), sort);
    }

    public SortInfo getSortByName(SortNameType nameType, String name) {
        return getSortNameCategory(nameType).get(name);
    }

    private ClassGraph classGraph(boolean includeExtras) {
        ClassGraph classGraph = new ClassGraph()
            .acceptPackages("sorts", "io.github.arrayv.sorts")
            .rejectPackages("sorts.templates", "io.github.arrayv.sorts.templates")
            .initializeLoadedClasses();
        if (includeExtras && extraSortsInstalled()) {
            classGraph.addClassLoader(EXTRA_SORTS_CLASS_LOADER);
        }
        return classGraph;
    }

    public void analyzeSorts() {
        analyzeSorts(true);
    }

    public void analyzeSorts(boolean includeExtras) {
        this.sorts.clear();
        this.invalidSorts.clear();
        this.suggestions.clear();
        this.sortErrorMsg = null;
        this.sortsByName.clear();
        analyzeSorts(classGraph(includeExtras));
    }

    public void analyzeSortsExtrasOnly() {
        if (!extraSortsInstalled()) return;
        // analyzeSorts(
        //     classGraph(true)
        //         .whitelistJars(EXTRA_SORTS_FILE.getName())
        // );
        // Custom sort analysis so sorts don't get duplicated
        try {
            try (ZipFile zf = new ZipFile(EXTRA_SORTS_FILE)) {
                for (final Enumeration<? extends ZipEntry> en = zf.entries(); en.hasMoreElements();) {
                    ZipEntry entry = en.nextElement();
                    if (entry.isDirectory()) continue;
                    String name = entry.getName();
                    if (!name.endsWith(".class")) continue;
                    String nameNoExt = name.substring(0, name.length() - 6);
                    if (!nameNoExt.startsWith("sorts") && !nameNoExt.startsWith("io/github/arrayv/sorts")) continue;
                    if (nameNoExt.contains("$")) continue;
                    String className = nameNoExt.replace('/', '.');
                    this.compileSingle(className, EXTRA_SORTS_CLASS_LOADER);
                }
            }
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException)e; // rethrow
            }
            throw new RuntimeException(e);
        }
    }

    public void analyzeSorts(ClassGraph classGraph) {
        try (ScanResult scanResult = classGraph.scan()) {
            List<ClassInfo> sortFiles;
            sortFiles = scanResult.getAllClasses();

            for (int i = 0; i < sortFiles.size(); i++) {
                ClassInfo sortFile = sortFiles.get(i);
                if (sortFile.getName().contains("$")) continue; // Ignore inner classes
                this.compileSingle(sortFile.loadClass(Sort.class));
            }
            sortSorts();
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
        }
    }

    public boolean extraSortsInstalled() {
        return EXTRA_SORTS_FILE.isFile();
    }

    public void installOrUpdateExtraSorts() throws IOException {
        installOrUpdateExtraSorts(null);
    }

    public void unloadAllExtraSorts() {
        int j = 0;
        for (SortInfo sort : sorts) {
            if (!sort.isFromExtra()) {
                sorts.set(j++, sort);
            }
        }
        sorts.subList(j, sorts.size()).clear();
        extraSorts.clear();
    }

    public void installOrUpdateExtraSorts(ProgressMonitor monitor) throws IOException {
        final File CACHE_DIR = EXTRA_SORTS_FILE.getParentFile();
        CACHE_DIR.mkdirs();
        final File DOWNLOAD_TEMP_FILE = File.createTempFile("avdownload-", ".zip", CACHE_DIR);
        DOWNLOAD_TEMP_FILE.deleteOnExit(); // Really just a safeguard in case installOrUpdateExtraSorts fails
        URLConnection connection = EXTRA_SORTS_DOWNLOAD.openConnection();
        int totalProgress = 0;
        int partProgress = 0;
        int partLength = 0;
        if (monitor != null) {
            monitor.setMinimum(0);
            final int contentLength = (int)connection.getContentLengthLong();
            if (contentLength > 0) { // Negative if size unknown or overflow
                partLength = contentLength;
                monitor.setMaximum(contentLength * 2);
            } else if (EXTRA_SORTS_FILE.isFile()) {
                // We can estimate the download size from the previous file if this is an update
                final int fileLength = (int)EXTRA_SORTS_FILE.length();
                if (fileLength > 0) {
                    partLength = fileLength;
                    monitor.setMaximum(fileLength * 2);
                } else {
                    partLength = -1;
                }
            } else {
                partLength = -1;
            }
            monitor.setNote("Downloading...");
            monitor.setProgress(0);
        }
        try (
            InputStream is = connection.getInputStream();
            OutputStream os = new FileOutputStream(DOWNLOAD_TEMP_FILE);
        ) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = is.read(buffer)) != -1) {
                if (monitor != null && partLength != -1) {
                    totalProgress += len;
                    partProgress += len;
                    monitor.setProgress(totalProgress);
                    monitor.setNote("Downloading... (" + partProgress / 1024 + " KB/" + partLength / 1024 + " KB)");
                }
                os.write(buffer, 0, len);
            }
        }
        try (ZipFile zf = new ZipFile(DOWNLOAD_TEMP_FILE)) {
            final ZipEntry EXTRA_SORTS_JAR_ENTRY = zf.getEntry(EXTRA_SORTS_JAR_NAME);
            if (monitor != null) {
                final int size = (int)EXTRA_SORTS_JAR_ENTRY.getSize();
                if (size > 0) { // Negative if size unknown or overflow (extremely unlikely)
                    partLength = size;
                    monitor.setMaximum(totalProgress + size);
                } else {
                    partLength = -1;
                }
                partProgress = 0;
                monitor.setNote("Extracting...");
            }
            try (
                InputStream is = zf.getInputStream(EXTRA_SORTS_JAR_ENTRY);
                OutputStream os = new FileOutputStream(EXTRA_SORTS_FILE);
            ) {
                byte[] buffer = new byte[8192];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    if (monitor != null && partLength != -1) {
                        totalProgress += len;
                        partProgress += len;
                        monitor.setProgress(totalProgress);
                        monitor.setNote("Extracting... (" + partProgress / 1024 + " KB/" + partLength / 1024 + " KB)");
                    }
                    os.write(buffer, 0, len);
                }
            }
        }
        DOWNLOAD_TEMP_FILE.delete(); // Might as well do it now
    }

    private static final class JavaPackageNameFinder {
        final String source;
        int i, c;

        JavaPackageNameFinder(String source) {
            this.source = source;
        }

        String findPackageName() {
            i = 0;
            while (true) {
                if (skipWhitespace()) break;

                if (c == '/') {
                    if (advance()) break;
                    if (c == '/') {
                        while (true) {
                            if (advance() || c == '\n') break;
                        }
                    } else if (c == '*') {
                        while (true) {
                            if (advance()) break;
                            if (c == '*' && (advance() || c == '/')) break;
                        }
                    } else {
                        throw new IllegalArgumentException("/ by itself (not part of a comment) before class declaration");
                    }
                    continue;
                }

                if (c == 'p') {
                    if (advance() || c == 'u') break; // public
                    if (c != 'a') throw new IllegalArgumentException("Except u or a after p");
                    expectWord("ckage", "package");
                    if (skipWhitespace()) throw new IllegalArgumentException("Expect package name");
                    return readPackageName();
                }

                if (c == 'i' || c == 'a' || c == 'c' || c == 'r' || c == 'e' || c == 'f') break;
            }
            return "";
        }

        private String readPackageName() {
            StringBuilder result = new StringBuilder();
            boolean isAfterDot = true;
            while (true) {
                // @checkstyle:off IndentationCheck - It looks nicer how it is
                if (isAfterDot) {
                    if (Character.isJavaIdentifierStart(c)) {
                        result.appendCodePoint(c);
                    } else {
                        throw new IllegalArgumentException("Illegal character in package name: " + new String(Character.toChars(c)));
                    }
                    isAfterDot = false;
                } else {
                    if (Character.isJavaIdentifierPart(c)) {
                        result.appendCodePoint(c);
                    } else if (c == '.') {
                        result.append('.');
                        isAfterDot = true;
                    } else {
                        throw new IllegalArgumentException("Illegal character in package name: " + new String(Character.toChars(c)));
                    }
                }
                // @checkstyle:on IndentationCheck
                if (advance()) {
                    throw new IllegalArgumentException("EOF");
                }
                if (c == ';') break;
            }
            return result.toString();
        }

        private boolean skipWhitespace() {
            while (true) {
                if (advance()) return true;
                if (!Character.isWhitespace(c)) break;
            }
            return false;
        }

        private boolean advance() {
            // True indicates EOF
            if (i >= source.length()) return true;
            c = source.codePointAt(i);
            i += Character.charCount(c);
            return false;
        }

        private void expectWord(String word, String fullWord) {
            for (int j = 0; j < word.length(); j++) {
                if (advance() || c != word.charAt(j)) throw new IllegalArgumentException("EOL or unexpected character in word '" + fullWord + "'");
            }
        }
    }

    private String findPackageName(String source) {
        return new JavaPackageNameFinder(source).findPackageName();
    }

    private static String performImportReplacements(String source) {
        for (Map.Entry<String, String> replacement : IMPORT_REPLACEMENTS) {
            source = CommonUtils.replace(source, replacement.getKey(), replacement.getValue());
        }
        return source;
    }

    public static JavaCompiler tryGetJavaCompiler() {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            if (
                JOptionPane.showConfirmDialog(
                    null,
                    "You must install a JDK on your system in order to import sorts.\n" +
                        "Would you like to download one now?",
                    "Import Sort",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE
                ) == JOptionPane.YES_OPTION
            ) {
                try {
                    Desktop.getDesktop().browse(new URI("https://adoptium.net/temurin/releases"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (URISyntaxException e) {
                    throw new Error(e);
                }
            }
        }
        return compiler;
    }

    public boolean importSort(File file, boolean showConfirmation) {
        // SLightly modified from https://stackoverflow.com/a/40772073/8840278
        // Pattern packagePattern = Pattern.compile("package (([a-zA-Z]{1}[a-zA-Z\\d_]*\\.)*[a-zA-Z][a-zA-Z\\d_]*);");
        String originalContents;
        try {
            originalContents = new String(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
            return false;
        }
        String packageName;
        try {
            packageName = findPackageName(originalContents);
        } catch (IllegalArgumentException e) {
            JErrorPane.invokeCustomErrorMessage("Invalid Java syntax detected: " + e.getMessage());
            return false;
        }
        if (!packageName.startsWith("sorts") && !packageName.startsWith("io.github.arrayv.sorts")) {
            JErrorPane.invokeCustomErrorMessage("Sort package must be either sorts or io.github.arrayv.sorts");
            return false;
        }

        final File CACHE_DIR = new File("./cache/");
        CACHE_DIR.mkdirs();
        final Path CACHE_PATH = CACHE_DIR.toPath();

        String contents = performImportReplacements(originalContents);
        File useFile;
        if (!contents.equals(originalContents)) {
            useFile = new File(CACHE_DIR, file.getName());
            try (Writer writer = new FileWriter(useFile)) {
                writer.write(contents);
            } catch (IOException e) {
                JErrorPane.invokeErrorMessage(e, "Sort Import");
                return false;
            }
        } else {
            useFile = file;
        }

        JavaCompiler compiler = tryGetJavaCompiler();
        if (compiler == null) {
            return false;
        }
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> jFiles = fileManager.getJavaFileObjects(useFile);
        final File logFile = new File(CACHE_DIR, "compile-" + System.currentTimeMillis() + ".log").getAbsoluteFile();
        int success;
        try (Writer output = new FileWriter(logFile)) {
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(CACHE_DIR));
            // @checkstyle:off ParenPadCheck
            CompilationTask task = compiler.getTask(
                output,        // out
                fileManager,   // fileManager
                null,          // diagnosticListener
                Arrays.asList( // options
                    "-classpath", System.getProperty("java.class.path")
                ),
                null,          // classes
                jFiles         // compilationUnits
            );
            // @checkstyle:on ParenPadCheck
            try {
                // Code that would work except that reflection is safer (I think) when using APIs that may be removed
                // com.sun.tools.javac.main.Main.Result result = ((com.sun.tools.javac.api.JavacTaskImpl)task).doCall();
                // success = result.exitCode;
                Method doCall = task.getClass().getDeclaredMethod("doCall");
                Object result = doCall.invoke(task);
                success = (int)result.getClass().getDeclaredField("exitCode").get(result);
            } catch (Exception e) {
                success = task.call() ? 0 : 1;
            }
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e, "Sort Import");
            success = -1;
        }
        if (success != 0) {
            if (
                JOptionPane.showConfirmDialog(
                    null,
                    "Failed to compile: " + file +
                    "\nError code " + success + "\n" +
                    "Would you like to open the log file?",
                    "Import Sort",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE
                ) == JOptionPane.YES_OPTION
            ) {
                try {
                    Desktop.getDesktop().open(logFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        final String rawClassName;
        {
            String baseName = file.getName();
            rawClassName = baseName.substring(0, baseName.lastIndexOf('.'));
        }

        String[][] maybeNames;
        try {
            maybeNames = StreamSupport.stream(
                fileManager.list(StandardLocation.CLASS_OUTPUT, "", Collections.singleton(JavaFileObject.Kind.CLASS), true).spliterator(),
                false
            )
                .map(fobj -> {
                    File jioFile = new File(fobj.getName());
                    Path relativePath = CACHE_PATH.relativize(jioFile.getParentFile().toPath());
                    String baseName = jioFile.getName();
                    return new String[] {
                        relativePath.toString().replace(File.separatorChar, '.'),
                        baseName.substring(0, baseName.lastIndexOf('.'))
                    };
                })
                .filter(fc ->
                    packageName.equals(fc[0]) && rawClassName.equals(fc[1])
                )
                .toArray(String[][]::new);
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e, "Sort Import");
            return false;
        }
        if (maybeNames.length == 2) {
            JErrorPane.invokeCustomErrorMessage(
                "Multiple sorts found that match this file (" +
                Arrays.deepToString(maybeNames) +
                "). Please contact the sort devs of all sorts named " +
                file.getName()
            );
            return false;
        }
        if (maybeNames.length == 0) {
            JErrorPane.invokeCustomErrorMessage("The resulting sort file could not be located. Please report this on the bug tracker.");
            return false;
        }
        final String name;
        if (maybeNames[0][0].isEmpty()) {
            name = rawClassName;
        } else {
            name = maybeNames[0][0] + "." + rawClassName;
        }

        try {
            if (!compileSingle(name, URLClassLoader.newInstance(new URL[] { CACHE_DIR.toURI().toURL() })))
                return false;
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
            return false;
        }

        if (showConfirmation) {
            sortSorts();
            arrayVisualizer.refreshSorts();
            JOptionPane.showMessageDialog(null, "Successfully imported sort " + name, "Import Sort", JOptionPane.INFORMATION_MESSAGE);
        }
        return true;
    }

    public boolean importSort(File file) {
        return importSort(file, true);
    }

    public void sortSorts() {
        SortComparator sortComparator = new SortComparator();
        Collections.sort(sorts, sortComparator);
        // This loop fixes all the sort IDs to match up with the indices again
        for (int i = 0; i < sorts.size(); i++) {
            SortInfo sort = sorts.get(i);
            if (sort.getId() != i) {
                sorts.set(i, sort.withId(i));
            }
        }
    }

    private boolean verifySort(SortInfo sort) {
        if (sort.isDisabled()) {
            this.sortErrorMsg = "manually disabled";
            return false;
        }
        if (sort.getListName().isEmpty()) {
            this.sortErrorMsg = "missing 'Choose Sort' name";
            return false;
        }
        if (sort.getRunAllName().isEmpty()) {
            this.sortErrorMsg = "missing 'Run All' name";
            return false;
        }
        if (sort.getRunName().isEmpty()) {
            this.sortErrorMsg = "missing 'Run Sort' name";
            return false;
        }
        if (sort.getCategory().isEmpty()) {
            this.sortErrorMsg = "missing category";
            return false;
        }

        return true;
    }

    private static String checkForSuggestions(SortInfo sort) {
        StringBuilder suggestions = new StringBuilder();
        boolean warned = false;

        if (sort.isBogoSort() && !sort.isSlowSort()) {
            suggestions.append("- " + sort.getRunName() + " is a bogosort. It should be marked 'unreasonably slow'.\n");
            warned = true;
        }
        if (sort.isSlowSort() && sort.getUnreasonableLimit() == 0) {
            suggestions.append("- A warning will pop up every time you select " + sort.getRunName() + ". You might want to change its 'unreasonable limit'.\n");
            warned = true;
        }
        if (!sort.isSlowSort() && sort.getUnreasonableLimit() != 0) {
            suggestions.append("- You might want to set " + sort.getRunName() + "'s 'unreasonable limit' to 0. It's not marked 'unreasonably slow'.\n");
            warned = true;
        }
        if (sort.isRadixSort() && !sort.isBucketSort()) {
            suggestions.append("- " + sort.getRunName() + " is a radix sort and should also be classified as a bucket sort.\n");
            warned = true;
        }

        if (warned) {
            suggestions.deleteCharAt(suggestions.length() - 1);
        }
        return suggestions.toString();
    }

    public SortInfo[] getSorts() {
        return sorts.toArray(new SortInfo[this.sorts.size()]);
        // SortInfo[] sorts = new SortInfo[this.sorts.size()];

        // for (int i = 0; i < sorts.length; i++) {
        //     sorts[i] = new SortInfo(i, this.sorts.get(i));
        // }

        // return sorts;
    }

    public String[] getInvalidSorts() {
        if (invalidSorts.size() < 1) {
            return null;
        }

        String[] invalidSorts = new String[this.invalidSorts.size()];
        invalidSorts = this.invalidSorts.toArray(invalidSorts);

        return invalidSorts;
    }
    public String[] getSuggestions() {
        if (suggestions.size() < 1) {
            return null;
        }

        String[] allSuggestions = new String[suggestions.size()];
        allSuggestions = suggestions.toArray(allSuggestions);

        return allSuggestions;
    }
}
