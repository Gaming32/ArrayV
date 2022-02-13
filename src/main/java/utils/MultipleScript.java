package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import io.github.arrayv.panes.JErrorPane;
import io.github.arrayv.sortdata.SortInfo;
import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class MultipleScript {
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
        private final Sort algortitm;
        private final int bucketCount;
        private final int defaultLength;
        private final double defaultSpeedMultiplier;
        private final boolean slowSort;

        public SortCallInfo(ArrayVisualizer arrayVisualizer, SortInfo sort, int bucketCount, int defaultLength, double defaultSpeedMultiplier) {
            this.algortitm = sort.getFreshInstance();
            this.bucketCount = bucketCount;
            this.defaultLength = defaultLength;
            this.defaultSpeedMultiplier = defaultSpeedMultiplier;
            this.slowSort = sort.isSlowSort();
        }

        public Sort getAlgortitm() {
            return algortitm;
        }

        public int getBucketCount() {
            return bucketCount;
        }

        public int getDefaultLength() {
            return defaultLength;
        }

        public double getDefaultSpeedMultiplier() {
            return defaultSpeedMultiplier;
        }

        public boolean isSlowSort() {
            return slowSort;
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

    private final Hashtable<String, SortInfo> sortNames;

    public MultipleScript(ArrayVisualizer arrayVisualizer) {
        this.arrayVisualizer = arrayVisualizer;

        sortNames = new Hashtable<>();
        populateSortTable(this.arrayVisualizer.getSorts(), sortNames);
    }

    private void populateSortTable(SortInfo[] array, Hashtable<String, SortInfo> table) {
        for (SortInfo sort : array) {
            String className = sort.getInternalName();
            if (className == null) continue;
            String[] classParts = className.split("\\.");

            table.put(classParts[classParts.length - 1].toLowerCase(), sort);
            table.put(sort.getListName().toLowerCase(), sort);
            table.put(sort.getRunName().toLowerCase(), sort);
            table.put(sort.getRunAllName().toLowerCase(), sort);
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
                SortInfo sort = sortNames.get(sortName);

                int bucketCount = commands.length > 1 ? Integer.parseInt(commands[1]) : 0;
                int defaultLength = commands.length > 2 ? Integer.parseInt(commands[2]) : 2048;
                double defaultSpeedMultiplier = commands.length > 3 ? Double.parseDouble(commands[3]) : 1;

                commandType = ScriptCommand.CommandType.SortCall;
                argument = new SortCallInfo(this.arrayVisualizer, sort, bucketCount, defaultLength, defaultSpeedMultiplier);
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
