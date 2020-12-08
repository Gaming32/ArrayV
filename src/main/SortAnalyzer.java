package main;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.tools.ToolProvider;
import javax.swing.JOptionPane;
import javax.tools.JavaCompiler;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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

final public class SortAnalyzer {
    private ArrayList<Sort> comparisonSorts;
    private ArrayList<Sort> distributionSorts;
    private ArrayList<String> invalidSorts;
    private ArrayList<String> suggestions;
    
    private String sortErrorMsg;
    
    private ArrayVisualizer arrayVisualizer;
    
    public SortAnalyzer(ArrayVisualizer arrayVisualizer) {
        this.comparisonSorts = new ArrayList<>();
        this.distributionSorts = new ArrayList<>();
        this.invalidSorts = new ArrayList<>();
        this.suggestions = new ArrayList<>();
        
        this.arrayVisualizer = arrayVisualizer;
    }

    private boolean compileSingle(String name, ClassLoader loader) {
        try {
            Class<?> sortClass;
            if (loader == null)
                sortClass = Class.forName(name);
            else
                sortClass = Class.forName(name, true, loader);
            Constructor<?> newSort = sortClass.getConstructor(new Class[] {ArrayVisualizer.class});
            Sort sort = (Sort) newSort.newInstance(this.arrayVisualizer);
            
            try {
                if(verifySort(sort)) {
                    String suggestion = checkForSuggestions(sort);
                    if(!suggestion.isEmpty()) {
                        suggestions.add(suggestion);
                    }
                    if(sort.isComparisonBased()) {
                        comparisonSorts.add(sort);
                    }
                    else {
                        distributionSorts.add(sort);
                    }
                }
                else {
                    throw new Exception();
                }
            }
            catch(Exception e) {
                invalidSorts.add(sort.getClass().getName() + " (" + this.sortErrorMsg + ")");
                return false;
            }
        }
        catch(Exception e) {
            JErrorPane.invokeErrorMessage(e, "Could not compile " + name);
            invalidSorts.add(name + " (failed to compile)");
            return false;
        }
        return true;
    }
    
    @SuppressWarnings("unused")
    public void analyzeSorts() {
        ClassGraph classGraph = new ClassGraph();
        classGraph.whitelistPackages("sorts");
        classGraph.blacklistPackages("sorts.templates");
        
        try (ScanResult scanResult = classGraph.scan()) {
            List<ClassInfo> sortFiles;
            sortFiles = scanResult.getAllClasses();
            
            for(int i = 0; i < sortFiles.size(); i++) {
                this.compileSingle(sortFiles.get(i).getName(), null);
            }
            SortComparator sortComparator = new SortComparator();
            Collections.sort(comparisonSorts, sortComparator);
            Collections.sort(distributionSorts, sortComparator);
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
        }
    }

    public void importSort(File file) {
        Pattern packagePattern = Pattern.compile("^\\s*package ([a-zA-Z\\.]+);");
        String contents;
        try {
            contents = new String(Files.readAllBytes(file.toPath()));
        }
        catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
            return;
        }
        Matcher matcher = packagePattern.matcher(contents);
        if (!matcher.find()) {
            JErrorPane.invokeCustomErrorMessage("No package specifed");
            return;
        }
        String packageName = matcher.group(1);
        String name = packageName + "." + file.getName().split("\\.")[0];
        File tempPath = new File(String.join("/", packageName.split("\\.")));
        tempPath.mkdirs();
        File destPath = new File(tempPath.getAbsolutePath() + "/" + file.getName());
        try {
            Files.copy(file.toPath(), destPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
            return;
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int success = compiler.run(null, null, null, destPath.getAbsolutePath());
        if (success != 0) {
            JErrorPane.invokeCustomErrorMessage("Failed to compile: " + destPath.getPath() + "\nError code " + success);
            return;
        }

        try {
            if (!compileSingle(name, URLClassLoader.newInstance(new URL[] { new File(".").toURI().toURL() })))
                return;
        }
        catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
            return;
        }

        SortComparator sortComparator = new SortComparator();
        Collections.sort(comparisonSorts, sortComparator);
        Collections.sort(distributionSorts, sortComparator);

        arrayVisualizer.refreshSorts();
        JOptionPane.showMessageDialog(null, "Successfully imported sort " + name, "Import Sort", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private boolean verifySort(Sort sort) {
        if(!sort.isSortEnabled()) {
            this.sortErrorMsg = "manually disabled";
            return false;
        }
        if(sort.getSortListName().equals("")) {
            this.sortErrorMsg = "missing 'Choose Sort' name";
            return false;
        }
        if(sort.getRunAllSortsName().equals("")) {
            this.sortErrorMsg = "missing 'Run All' name";
            return false;
        }
        if(sort.getRunSortName().equals("")) {
            this.sortErrorMsg = "missing 'Run Sort' name";
            return false;
        }
        if(sort.getCategory().equals("")) {
            this.sortErrorMsg = "missing category";
            return false;
        }
        
        return true;
    }
    
    private static String checkForSuggestions(Sort sort) {
        StringBuilder suggestions = new StringBuilder();
        boolean warned = false;
        
        if(sort.isBogoSort() && !sort.isUnreasonablySlow()) {
            suggestions.append("- " + sort.getRunSortName() + " is a bogosort. It should be marked 'unreasonably slow'.\n");
            warned = true;
        }
        if(sort.isUnreasonablySlow() && sort.getUnreasonableLimit() == 0) {
            suggestions.append("- A warning will pop up every time you select " + sort.getRunSortName() + ". You might want to change its 'unreasonable limit'.\n");
            warned = true;
        }
        if(!sort.isUnreasonablySlow() && sort.getUnreasonableLimit() != 0) {
            suggestions.append("- You might want to set " + sort.getRunSortName() + "'s 'unreasonable limit' to 0. It's not marked 'unreasonably slow'.\n");
            warned = true;
        }
        if(sort.isRadixSort() && !sort.usesBuckets()) {
            suggestions.append("- " + sort.getRunSortName() + " is a radix sort and should also be classified as a bucket sort.\n");
            warned = true;
        }
        if(sort.isRadixSort() && sort.isComparisonBased()) {
            suggestions.append("- " + sort.getRunSortName() + " is a radix sort. It probably shouldn't be labelled as a comparison-based sort.\n");
            warned = true;
        }
        
        if(warned) {
            suggestions.deleteCharAt(suggestions.length() - 1);
        }
        return suggestions.toString();
    }
    
    public String[][] getComparisonSorts() {
        String[][] ComparisonSorts = new String[2][comparisonSorts.size()];
        
        for(int i = 0; i < ComparisonSorts[0].length; i++) {
            ComparisonSorts[0][i] = comparisonSorts.get(i).getClass().getName();
            ComparisonSorts[1][i] = comparisonSorts.get(i).getSortListName();
        }
        
        return ComparisonSorts;
    }
    public String[][] getDistributionSorts() {
        String[][] DistributionSorts = new String[2][distributionSorts.size()];
        
        for(int i = 0; i < DistributionSorts[0].length; i++) {
            DistributionSorts[0][i] = distributionSorts.get(i).getClass().getName();
            DistributionSorts[1][i] = distributionSorts.get(i).getSortListName();
        }
        
        return DistributionSorts;
    }
    public String[] getInvalidSorts() {
        if(invalidSorts.size() < 1) {
            return null;
        }
        
        String[] InvalidSorts = new String[invalidSorts.size()];
        InvalidSorts = invalidSorts.toArray(InvalidSorts);
        
        return InvalidSorts;
    }
    public String[] getSuggestions() {
        if(suggestions.size() < 1) {
            return null;
        }
        
        String[] allSuggestions = new String[suggestions.size()];
        allSuggestions = suggestions.toArray(allSuggestions);
        
        return allSuggestions;
    }
}