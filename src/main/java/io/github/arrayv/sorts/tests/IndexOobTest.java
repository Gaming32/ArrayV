package io.github.arrayv.sorts.tests;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(
    name = "Index OOB Test"
)
public class IndexOobTest extends Sort {

    public IndexOobTest(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int sortLength, int param) throws Exception {
        /*
           If sortLength == 32768 (the maximum), markArray works just fine, but clearAllMarks will crash.
           After that point, it crashes every time it attempts to redraw the screen.
           This is because markArray only checks for negative marker positions, not ones that exceed the array length.
           This slips by undetected in most cases because the array is always 32768 long, so you can do sneaky OOB
           writes in clearAllMarks without causing any problems if the length is any smaller.

           Once this bug is fixed, this test should ALWAYS crash, no matter the sortLength.
         */
        Highlights.markArray(1, sortLength);
        Highlights.clearAllMarks();
    }
}
