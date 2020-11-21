package threads;

import java.io.File;

import dialogs.RunScriptDialog;
import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.templates.Sort;
import utils.MultipleScript;

/*
 * 
MIT License

Copyright (c) 2020 Gaming32

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

final public class RunScriptedSorts extends MultipleSortThread {
    private RunScriptDialog fileDialog;

    public RunScriptedSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        fileDialog = new RunScriptDialog();
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        this.executeSortList(new MultipleScript.SortCallInfo[] { }, array);
    }

    protected synchronized void executeSortList(MultipleScript.SortCallInfo[] sorts, int[] array) throws Exception {
        for (MultipleScript.SortCallInfo info : sorts) {
            RunScriptedSorts.this.runIndividualSort(info.algortitm,
                info.bucketCount,
                array,
                info.defaultLength,
                info.defaultSpeedMultiplier,
                info.slowSort);
        }
    }
    
    @Override
    public synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if(arrayVisualizer.getSortingThread() != null && arrayVisualizer.getSortingThread().isAlive())
            return;

        Sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(new Thread() {
            @Override
            public void run() {
                try{
                    File file = fileDialog.getFile();
                    MultipleScript.SortCallInfo[] sorts = RunScriptedSorts.this.arrayVisualizer.getScriptParser().runScript(file);

                    RunScriptedSorts.this.sortNumber = 1;
                    RunScriptedSorts.this.sortCount = sorts.length;

                    arrayManager.toggleMutableLength(false);

                    arrayVisualizer.setCategory("Scripted Sorts");

                    RunScriptedSorts.this.executeSortList(sorts, array);
                    
                    if(!runAllActive) {
                        arrayVisualizer.setCategory("Run Scripted Sorts");
                        arrayVisualizer.setHeading("Done");
                    }
                    
                    arrayManager.toggleMutableLength(true);
                }
                catch (Exception e) {
                    JErrorPane.invokeErrorMessage(e);
                }
                Sounds.toggleSound(false);
                arrayVisualizer.setSortingThread(null);
            }
        });

        arrayVisualizer.runSortingThread();
    }
}