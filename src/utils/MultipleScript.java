package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import panes.JErrorPane;

final public class MultipleScript {
    public class SortCallInfo {
        public Sort algortitm;
        public int bucketCount;
        public int defaultLength;
        public int defaultSpeedMultiplier;
        public boolean slowSort;

        public SortCallInfo(ArrayVisualizer arrayVisualizer, Class<?> sortClass, int bucketCount, int defaultLength, int defaultSpeedMultiplier) {
            Sort inst = null;
            try {
                Constructor<?> newSort = sortClass.getConstructor(new Class[] {ArrayVisualizer.class});
                inst = (Sort) newSort.newInstance(arrayVisualizer);
            }
            catch (Exception e) { }
            algortitm = inst;
            
            this.bucketCount = bucketCount;
            this.defaultLength = defaultLength;
            this.defaultSpeedMultiplier = defaultSpeedMultiplier;
        }
    }

    private static String[] simpleCommandLineParse(String commandLine) {
        ArrayList<String> result = new ArrayList<>();

        char last = ' ';
        String current = "";
        boolean inBlock = false;

        for (char character : commandLine.toCharArray()) {
            if (character == ' ' && !inBlock) {
                if (current != "") {
                    result.add(current);
                    current = "";
                }
            }
            else if (character == '"' && last == ' ' && !inBlock) {
                inBlock = true;
            }
            else if (character == ' ' && last == '"' && inBlock) {
                result.add(current);
                current = "";
                inBlock = false;
            }
            else if (character == '"' && inBlock) { }
            else if (character == ' ' && last == ' ' && !inBlock) { }
            else {
                current += character;
            }
            last = character;
        }
        result.add(current);

        return result.toArray(new String[result.size()]);
    }

    private ArrayVisualizer arrayVisualizer;

    private final Hashtable<String, Class<?>> sortNames;

    public MultipleScript(ArrayVisualizer arrayVisualizer) {
        this.arrayVisualizer = arrayVisualizer;

        sortNames = new Hashtable<>();
        populateSortTable(this.arrayVisualizer.getComparisonSorts(), sortNames);
        populateSortTable(this.arrayVisualizer.getDistributionSorts(), sortNames);
    }

    private void populateSortTable(String[][] array, Hashtable<String, Class<?>> table) {
        for (String className : array[0]) {
            Class<?> sortClass;
            Sort inst;
            try {
                sortClass = Class.forName(className);
                Constructor<?> newSort = sortClass.getConstructor(new Class[] {ArrayVisualizer.class});
                inst = (Sort) newSort.newInstance(this.arrayVisualizer);
            }
            catch (Exception e) {
                continue;
            }

            String[] classParts = className.split("\\.");
            table.put(classParts[classParts.length - 1].toLowerCase(), sortClass);
            table.put(inst.getSortListName().toLowerCase(), sortClass);
            table.put(inst.getRunSortName().toLowerCase(), sortClass);
            table.put(inst.getRunAllSortsName().toLowerCase(), sortClass);
        }
    }

    public SortCallInfo[] runScript(Scanner scanner) {
        ArrayList<SortCallInfo> result = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] commands = simpleCommandLineParse(line);

            String sortName = commands[0].toLowerCase();
            if (!sortNames.containsKey(sortName)) {
                continue;
            }
            Class<?> sortClass = sortNames.get(sortName);

            int bucketCount = Integer.parseInt(commands[1]);
            int defaultLength = Integer.parseInt(commands[2]);
            int defaultSpeedMultiplier = Integer.parseInt(commands[3]);

            result.add(new SortCallInfo(this.arrayVisualizer, sortClass, bucketCount, defaultLength, defaultSpeedMultiplier));
        }

        return result.toArray(new SortCallInfo[result.size()]);
    }

    public SortCallInfo[] runScript(String[] code) {
        Scanner scanner = new Scanner(String.join("\n", code));
        return runScript(scanner);
    }

    public SortCallInfo[] runScript(File file) {
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            JErrorPane.invokeErrorMessage(e, "Run Script");
            return null;
        }
        return runScript(scanner);    
    }

    public SortCallInfo[] runScript(String path) {
        return runScript(new File(path));        
    }
}
