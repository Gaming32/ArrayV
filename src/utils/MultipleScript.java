package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import main.ArrayVisualizer;
import main.SortAnalyzer.SortInfo;
import sorts.templates.Sort;
import panes.JErrorPane;

final public class MultipleScript {
    public static class ScriptCommand {
        public static enum CommandType {
            SortCall,
            SetCategory
        }

        public CommandType type;
        public Object argument;

        public ScriptCommand(CommandType type, Object argument) {
            this.type = type;
            this.argument = argument;
        }
    }

    public static class SortCallInfo {
        public Sort algortitm;
        public int bucketCount;
        public int defaultLength;
        public double defaultSpeedMultiplier;
        public boolean slowSort;

        public SortCallInfo(ArrayVisualizer arrayVisualizer, Class<?> sortClass, int bucketCount, int defaultLength, double defaultSpeedMultiplier) {
            Sort inst = null;
            try {
                Constructor<?> newSort = sortClass.getConstructor(new Class[] {ArrayVisualizer.class});
                inst = (Sort) newSort.newInstance(arrayVisualizer);
            } catch (Exception e) {
            }
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
            } else if (character == '"' && last == ' ' && !inBlock) {
                inBlock = true;
            } else if (character == ' ' && last == '"' && inBlock) {
                result.add(current);
                current = "";
                inBlock = false;
            } else if (character == '"' && inBlock) {
            } else if (character == ' ' && last == ' ' && !inBlock) {
            } else {
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

    private void populateSortTable(SortInfo[] array, Hashtable<String, Class<?>> table) {
        for (SortInfo sort : array) {
            Class<?> sortClass = sort.sortClass;
            Sort inst;
            try {
                Constructor<?> newSort = sortClass.getConstructor(new Class[] {ArrayVisualizer.class});
                inst = (Sort) newSort.newInstance(this.arrayVisualizer);
            } catch (Exception e) {
                continue;
            }

            String[] classParts = sortClass.getName().split("\\.");
            table.put(classParts[classParts.length - 1].toLowerCase(), sortClass);
            table.put(inst.getSortListName().toLowerCase(), sortClass);
            table.put(inst.getRunSortName().toLowerCase(), sortClass);
            table.put(inst.getRunAllSortsName().toLowerCase(), sortClass);
        }
    }

    public ScriptCommand[] runScript(Scanner scanner) {
        ArrayList<ScriptCommand> result = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] commands = simpleCommandLineParse(line);
            if (commands.length == 0 || (commands.length == 1 && commands[0].length() == 0))
                continue;

            ScriptCommand.CommandType commandType = null;
            Object argument = null;

            String commandLabel = commands[0].toLowerCase();
            if (commandLabel.compareTo("setcategory") == 0) {
                commandType = ScriptCommand.CommandType.SetCategory;
                argument = commands.length > 1 ? commands[1] : "Scripted Sorts";
            } else {
                String sortName = commandLabel;
                if (!sortNames.containsKey(sortName)) {
                    continue;
                }
                Class<?> sortClass = sortNames.get(sortName);

                int bucketCount = commands.length > 1 ? Integer.parseInt(commands[1]) : 0;
                int defaultLength = commands.length > 2 ? Integer.parseInt(commands[2]) : 2048;
                double defaultSpeedMultiplier = commands.length > 3 ? Double.parseDouble(commands[3]) : 1;

                commandType = ScriptCommand.CommandType.SortCall;
                argument = new SortCallInfo(this.arrayVisualizer, sortClass, bucketCount, defaultLength, defaultSpeedMultiplier);
            }
            result.add(new ScriptCommand(commandType, argument));
        }

        return result.toArray(new ScriptCommand[result.size()]);
    }

    public ScriptCommand[] runScript(String[] code) {
        Scanner scanner = new Scanner(String.join("\n", code));
        return runScript(scanner);
    }

    public ScriptCommand[] runScript(File file) {
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            JErrorPane.invokeCustomErrorMessage("The file \"" + file.getPath() + "\" does not exist");
            return null;
        } catch (NullPointerException e) {
            return null;
        }
        return runScript(scanner);
    }

    public ScriptCommand[] runScript(String path) {
        return runScript(new File(path));
    }
}
