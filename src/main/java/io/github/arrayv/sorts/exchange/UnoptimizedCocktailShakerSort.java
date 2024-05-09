package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2019 w0rthy

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
@SortMeta(name = "Unoptimized Cocktail Shaker")
public final class UnoptimizedCocktailShakerSort extends Sort {
    public UnoptimizedCocktailShakerSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void cocktailShaker(int[] array, int start, int end, double sleep) {
        int i = start;
        while (i < ((end / 2) + start)) {
            for (int j = i; j < end + start - i - 1; j++) {
                if (Reads.compareIndices(array, j, j + 1, sleep / 2, true) == 1) {
                    Writes.swap(array, j, j + 1, sleep, true, false);
                }
            }
            for (int j = end + start - i - 1; j > i; j--) {
                if (Reads.compareIndices(array, j, j - 1, sleep / 2, true) == -1) {
                    Writes.swap(array, j, j - 1, sleep, true, false);
                }
            }

            i++;
        }
    }

    public void customSort(int[] array, int start, int end) {
        this.cocktailShaker(array, start, end, 1);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.cocktailShaker(array, 0, sortLength, 0.1);
    }
}
