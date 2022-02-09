package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
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

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import panes.JErrorPane;
import sorts.templates.Sort;
import sorts.templates.SortComparator;

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

    private final Set<Class<?>> EXTRA_SORTS = new HashSet<>();

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

    private ArrayList<Sort> comparisonSorts;
    private ArrayList<Sort> distributionSorts;
    private ArrayList<String> invalidSorts;
    private ArrayList<String> suggestions;

    private String sortErrorMsg;

    private ArrayVisualizer arrayVisualizer;

    public static class SortInfo {
        public int id;
        public Class<?> sortClass;
        public String listName;
        public String category;
        public boolean usesComparisons;

        public SortInfo(int id, Sort sort) {
            this.id = id;
            this.sortClass = sort.getClass();
            this.listName = sort.getSortListName();
            this.category = sort.getCategory();
            this.usesComparisons = true;
        }

        public SortInfo(Sort sort) {
            this(-1, sort);
        }

        public static String[] getListNames(SortInfo[] sorts) {
            String[] result = new String[sorts.length];
            for (int i = 0; i < sorts.length; i++) {
                result[i] = sorts[i].listName;
            }
            return result;
        }

        public static String[] getCategories(SortInfo[] sorts) {
            HashSet<String> result = new HashSet<>();
            for (int i = 0; i < sorts.length; i++) {
                result.add(sorts[i].category);
            }
            String[] resultArray = result.toArray(new String[result.size()]);
            Arrays.sort(resultArray);
            return resultArray;
        }
    }

    SortAnalyzer(ArrayVisualizer arrayVisualizer) {
        this.comparisonSorts = new ArrayList<>();
        this.distributionSorts = new ArrayList<>();
        this.invalidSorts = new ArrayList<>();
        this.suggestions = new ArrayList<>();

        this.arrayVisualizer = arrayVisualizer;
    }

    public boolean didSortComeFromExtra(Sort sort) {
        return didSortComeFromExtra(sort.getClass());
    }

    public boolean didSortComeFromExtra(Class<?> sort) {
        return EXTRA_SORTS.contains(sort);
    }

    private void setSortCameFromExtra(Class<?> sort) {
        EXTRA_SORTS.add(sort);
    }

    private boolean compileSingle(String name, ClassLoader loader) {
        Class<?> sortClass;
        try {
            if (loader == null)
                sortClass = Class.forName(name);
            else
                sortClass = Class.forName(name, true, loader);
        } catch (ClassNotFoundException e) {
            System.err.println(e);
            return true;
        }
        return compileSingle(sortClass);
    }

    private boolean compileSingle(Class<?> sortClass) {
        try {
            if (sortClass.getClassLoader() == EXTRA_SORTS_CLASS_LOADER) {
                setSortCameFromExtra(sortClass);
            }
            Constructor<?> newSort = sortClass.getConstructor(new Class[] {ArrayVisualizer.class});
            Sort sort = (Sort) newSort.newInstance(this.arrayVisualizer);

            try {
                if (verifySort(sort)) {
                    String suggestion = checkForSuggestions(sort);
                    if (!suggestion.isEmpty()) {
                        suggestions.add(suggestion);
                    }
                    if (sort.isComparisonBased()) {
                        comparisonSorts.add(sort);
                    } else {
                        distributionSorts.add(sort);
                    }
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

    private ClassGraph classGraph(boolean includeExtras) {
        ClassGraph classGraph = new ClassGraph()
            .whitelistPackages("sorts", "io.github.arrayv.sorts")
            .blacklistPackages("sorts.templates", "io.github.arrayv.sorts.templates")
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
        this.comparisonSorts.clear();
        this.distributionSorts.clear();
        this.invalidSorts.clear();
        this.suggestions.clear();
        this.sortErrorMsg = null;
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
                this.compileSingle(sortFile.loadClass());
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

    @SuppressWarnings("unchecked")
    public void unloadAllExtraSorts() {
        for (List<Sort> sortsList : new List[] {comparisonSorts, distributionSorts}) {
            int j = 0;
            for (Sort sort : sortsList) {
                if (!didSortComeFromExtra(sort.getClass())) {
                    sortsList.set(j++, sort);
                }
            }
            sortsList.subList(j, sortsList.size()).clear();
        }
        EXTRA_SORTS.clear();
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
                if (isAfterDot) {
                    if (
                        (c >= 'a' && c <= 'z') ||
                        (c >= 'A' && c <= 'Z') ||
                        c == '_' || c == '$') {
                        result.appendCodePoint(c);
                    } else {
                        throw new IllegalArgumentException("Illegal character in package name: " + new String(Character.toChars(c)));
                    }
                    isAfterDot = false;
                } else {
                    if (
                        (c >= 'a' && c <= 'z') ||
                        (c >= 'A' && c <= 'Z') ||
                        (c >= '0' && c <= '9') ||
                        c == '_' || c == '$') {
                        result.appendCodePoint(c);
                    } else if (c == '.') {
                        result.append('.');
                        isAfterDot = true;
                    } else {
                        throw new IllegalArgumentException("Illegal character in package name: " + new String(Character.toChars(c)));
                    }
                }
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

    public boolean importSort(File file, boolean showConfirmation) {
        // SLightly modified from https://stackoverflow.com/a/40772073/8840278
        // Pattern packagePattern = Pattern.compile("package (([a-zA-Z]{1}[a-zA-Z\\d_]*\\.)*[a-zA-Z][a-zA-Z\\d_]*);");
        String contents;
        try {
            contents = new String(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
            return false;
        }
        String packageName;
        try {
            packageName = findPackageName(contents);
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

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> jFiles = fileManager.getJavaFileObjects(file);
        int success;
        try {
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(CACHE_DIR));
            CompilationTask task = compiler.getTask(
                null,          // out
                fileManager,   // fileManager
                null,          // diagnosticListener
                Arrays.asList( // options
                    "-classpath", System.getProperty("java.class.path")
                ),
                null,          // classes
                jFiles         // compilationUnits
            );
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
            JErrorPane.invokeCustomErrorMessage("Failed to compile: " + file + "\nError code " + success);
            return false;
        }

        final String rawClassName;
        {
            String baseName = file.getName();
            rawClassName = baseName.substring(0, baseName.lastIndexOf('.'));
        }

        String[] maybeNames;
        try {
            maybeNames = StreamSupport.stream(
                fileManager.list(StandardLocation.CLASS_OUTPUT, "", Collections.singleton(JavaFileObject.Kind.CLASS), true).spliterator(),
                false
            )
                .map(fobj -> {
                    Path relativePath = CACHE_PATH.relativize(new File(fobj.getName()).getParentFile().toPath());
                    return relativePath.toString().replace(File.separatorChar, '.');
                })
                .filter(packageName::equals)
                .toArray(String[]::new);
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e, "Sort Import");
            return false;
        }
        if (maybeNames.length == 2) {
            JErrorPane.invokeCustomErrorMessage("Multiple sorts found that match this file. Please contact the sort devs of all sorts named " + file.getName());
            return false;
        }
        if (maybeNames.length == 0) {
            JErrorPane.invokeCustomErrorMessage("The resulting sort file could not be located. Please report this on the bug tracker.");
            return false;
        }
        final String name;
        if (maybeNames[0].isEmpty()) {
            name = rawClassName;
        } else {
            name = maybeNames[0] + "." + rawClassName;
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
        Collections.sort(comparisonSorts, sortComparator);
        Collections.sort(distributionSorts, sortComparator);
    }

    private boolean verifySort(Sort sort) {
        if (!sort.isSortEnabled()) {
            this.sortErrorMsg = "manually disabled";
            return false;
        }
        if (sort.getSortListName().equals("")) {
            this.sortErrorMsg = "missing 'Choose Sort' name";
            return false;
        }
        if (sort.getRunAllSortsName().equals("")) {
            this.sortErrorMsg = "missing 'Run All' name";
            return false;
        }
        if (sort.getRunSortName().equals("")) {
            this.sortErrorMsg = "missing 'Run Sort' name";
            return false;
        }
        if (sort.getCategory().equals("")) {
            this.sortErrorMsg = "missing category";
            return false;
        }

        return true;
    }

    private static String checkForSuggestions(Sort sort) {
        StringBuilder suggestions = new StringBuilder();
        boolean warned = false;

        if (sort.isBogoSort() && !sort.isUnreasonablySlow()) {
            suggestions.append("- " + sort.getRunSortName() + " is a bogosort. It should be marked 'unreasonably slow'.\n");
            warned = true;
        }
        if (sort.isUnreasonablySlow() && sort.getUnreasonableLimit() == 0) {
            suggestions.append("- A warning will pop up every time you select " + sort.getRunSortName() + ". You might want to change its 'unreasonable limit'.\n");
            warned = true;
        }
        if (!sort.isUnreasonablySlow() && sort.getUnreasonableLimit() != 0) {
            suggestions.append("- You might want to set " + sort.getRunSortName() + "'s 'unreasonable limit' to 0. It's not marked 'unreasonably slow'.\n");
            warned = true;
        }
        if (sort.isRadixSort() && !sort.usesBuckets()) {
            suggestions.append("- " + sort.getRunSortName() + " is a radix sort and should also be classified as a bucket sort.\n");
            warned = true;
        }
        if (sort.isRadixSort() && sort.isComparisonBased()) {
            suggestions.append("- " + sort.getRunSortName() + " is a radix sort. It probably shouldn't be labelled as a comparison-based sort.\n");
            warned = true;
        }

        if (warned) {
            suggestions.deleteCharAt(suggestions.length() - 1);
        }
        return suggestions.toString();
    }

    public SortInfo[] getComparisonSorts() {
        SortInfo[] ComparisonSorts = new SortInfo[comparisonSorts.size()];

        for (int i = 0; i < ComparisonSorts.length; i++) {
            ComparisonSorts[i] = new SortInfo(i, comparisonSorts.get(i));
        }

        return ComparisonSorts;
    }

    public SortInfo[] getDistributionSorts() {
        SortInfo[] DistributionSorts = new SortInfo[distributionSorts.size()];

        for (int i = 0; i < DistributionSorts.length; i++) {
            DistributionSorts[i] = new SortInfo(i, distributionSorts.get(i));
        }

        return DistributionSorts;
    }

    public String[] getInvalidSorts() {
        if (invalidSorts.size() < 1) {
            return null;
        }

        String[] InvalidSorts = new String[invalidSorts.size()];
        InvalidSorts = invalidSorts.toArray(InvalidSorts);

        return InvalidSorts;
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
