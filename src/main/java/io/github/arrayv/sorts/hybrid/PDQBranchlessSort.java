package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.select.MaxHeapSort;
import io.github.arrayv.sorts.templates.PDQSorting;

/*
 *
pdqsort.h - Pattern-defeating quicksort.
Copyright (c) 2015 Orson Peters
This software is provided 'as-is', without any express or implied warranty. In no event will the
authors be held liable for any damages arising from the use of this software.
Permission is granted to anyone to use this software for any purpose, including commercial
applications, and to alter it and redistribute it freely, subject to the following restrictions:
1. The origin of this software must not be misrepresented; you must not claim that you wrote the
   original software. If you use this software in a product, an acknowledgment in the product
   documentation would be appreciated but is not required.
2. Altered source versions must be plainly marked as such, and must not be misrepresented as
   being the original software.
3. This notice may not be removed or altered from any source distribution.
 *
 */
@SortMeta(listName = "Pattern-Defeating Quick (Branchless)", runName = "Branchless Pattern-Defeating Quick Sort")
public final class PDQBranchlessSort extends PDQSorting {
    public PDQBranchlessSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        visualizeAux();
        this.newHeapSorter(new MaxHeapSort(this.arrayVisualizer));
        pdqLoop(array, 0, currentLength, true, pdqLog(currentLength));
        deleteAux();
    }
}
