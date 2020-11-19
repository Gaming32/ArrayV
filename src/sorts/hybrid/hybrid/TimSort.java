package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import sorts.templates.TimSorting;

/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class TimSort extends Sort {
    private TimSorting timSortInstance; // TimSort cannot be simply written off as an abstract class, as it creates an instance of itself
                                        // in order to track its state. Plus, it contains both instance and static methods, requiring even
                                        // more refactoring, which would be just doing unnecessary busy work. Instead of what we've done for
                                        // the rest of the algorithms, we'll favor composition over inheritance here and pass "util" objects
                                        // to it.
    
    public TimSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Tim");
        this.setRunAllSortsName("Tim Sort");
        this.setRunSortName("Timsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.timSortInstance = new TimSorting(array, currentLength, this.arrayVisualizer);
        
        TimSorting.sort(this.timSortInstance, array, currentLength);
    }
}