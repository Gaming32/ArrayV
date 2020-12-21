package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/* MIT License
Copyright (c) 2020 Walker Gray
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
*/

final public class SmartBogoSort extends BogoSorting {
	
	private int max;
	
    public SmartBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Smart Bogo");
        this.setRunAllSortsName("Deterministic Bogo Sort");
        this.setRunSortName("Deterministic Bogosort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(11);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLen, int bucketCount) {
    	max = currentLen - 1;
        permutationSort(array, 0);
    }

    private boolean permutationSort(int[] array, int min)
    {
        boolean sorted = false;
        int i;
        for(i = max; i > min; i--)
        {
            if(max > min+1)
            {
                sorted = permutationSort(array, min+1); //permutation = recurrence relation
            }
            if(sorted || this.bogoIsSorted(array, max+1))
            {
                return true;
            }
			if((max+1-min)%2 == 0)
			{
				Writes.swap(array, min, i, 0, true, false);
			} else {
				Writes.swap(array, min, max, 0, true, false);
			}
		}
        if(max > min+1)
        {
            return permutationSort(array, min+1); //permutation = recurrence relation
        }
        return false;
    }
}