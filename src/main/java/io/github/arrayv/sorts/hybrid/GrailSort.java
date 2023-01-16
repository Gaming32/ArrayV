package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.GrailSorting;

/*
 *
The MIT License (MIT)

Copyright (c) 2013 Andrey Astrelin

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

/********* Grail sorting *********************************/
/*                                                       */
/* (c) 2013 by Andrey Astrelin                           */
/* Refactored by MusicTheorist                           */
/*                                                       */
/* Stable sorting that works in O(N*log(N)) worst time   */
/* and uses O(1) extra memory                            */
/*                                                       */
/* Define int / SortComparator                     	     */
/* and then call GrailSort() function                    */
/*                                                       */
/* For sorting w/ fixed external buffer (512 items)      */
/* use GrailSortWithBuffer()                             */
/*                                                       */
/* For sorting w/ dynamic external buffer (sqrt(length)) */
/* use GrailSortWithDynBuffer()                          */
/*                                                       */
/*********************************************************/

@SortMeta(
    name = "Grail",
    category = "Hybrid Sorts",
    question = "Enter external buffer type (0 = in-place, 1 = static, 2 = dynamic):",
    defaultAnswer = 0
)
public final class GrailSort extends GrailSorting {
    public GrailSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void rotateLength(int[] array, int leftLength, int rightLength) {
        this.grailRotate(array, 0, leftLength, rightLength);
    }

    public void customSort(int[] array, int start, int end) {
        this.grailCommonSort(array, start, end, null, 0, 0);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		switch(bucketCount) {
		case 1:
            int[] ExtBuf = Writes.createExternalArray(this.getStaticBuffer());
            this.grailCommonSort(array, 0, length, ExtBuf, 0, this.getStaticBuffer());
            Writes.deleteExternalArray(ExtBuf);
			break;

		case 2:
            int tempLen = 1;
            while(tempLen * tempLen < length) tempLen *= 2;
            int[] DynExtBuf = Writes.createExternalArray(tempLen);
            this.grailCommonSort(array, 0, length, DynExtBuf, 0, tempLen);
            Writes.deleteExternalArray(DynExtBuf);
			break;

		default:
			this.grailCommonSort(array, 0, length, null, 0, 0);
		}
    }
}
