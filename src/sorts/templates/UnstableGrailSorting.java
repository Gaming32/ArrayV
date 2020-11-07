package sorts.templates;

import main.ArrayVisualizer;
import sorts.exchange.SmartGnomeSort;

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
/* Define int / SortComparator                           */
/* and then call GrailSort() function                    */
/*                                                       */
/* For sorting w/ fixed external buffer (512 items)      */
/* use GrailSortWithBuffer()                             */
/*                                                       */
/* For sorting w/ dynamic external buffer (sqrt(length)) */
/* use GrailSortWithDynBuffer()                          */
/*                                                       */
/*********************************************************/

public abstract class UnstableGrailSorting extends Sort {
    private SmartGnomeSort grailInsertSorter;
    
    protected UnstableGrailSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }
    
    private void grailSwap(int[] arr, int a, int b) {
        Writes.swap(arr, a, b, 1, true, false);
    }
    
    private void grailMultiSwap(int[] arr, int a, int b, int swapsLeft) {        
        while(swapsLeft != 0) { 
            this.grailSwap(arr, a++, b++);
            swapsLeft--;
        }
    }
    
    protected void grailRotate(int[] array, int pos, int lenA, int lenB) {
        while(lenA != 0 && lenB != 0) {
            if(lenA <= lenB) {
                this.grailMultiSwap(array, pos, pos + lenA, lenA);
                pos += lenA;
                lenB -= lenA;
            } 
            else {
                this.grailMultiSwap(array, pos + (lenA - lenB), pos + lenA, lenB);
                lenA -= lenB;
            }
        }
    }

    private void grailInsertSort(int[] arr, int pos, int len) {
        grailInsertSorter.customSort(arr, pos, pos + len, 0.75);
    }

    //boolean argument determines direction
    private int grailBinSearch(int[] arr, int pos, int len, int keyPos, boolean isLeft) {
        int left = -1, right = len;
        while(left < right - 1) {
            int mid = left + ((right - left) >> 1);
            if(isLeft) {
                if(Reads.compareValues(arr[pos + mid], arr[keyPos]) >= 0) {
                    right = mid;
                } else {
                    left = mid;
                }
            } else {
                if(Reads.compareValues(arr[pos + mid], arr[keyPos]) > 0) {
                    right = mid;
                } else left = mid;
            }
            Highlights.markArray(1, pos + mid);
        }
        return right;
    }

    // cost: min(len1, len2)^2 + max(len1, len2)
    private void grailMergeWithoutBuffer(int[] arr, int pos, int len1, int len2) {
        if(len1 < len2) {
            while(len1 != 0) {
                //Binary Search left
                int loc = this.grailBinSearch(arr, pos + len1, len2, pos, true);
                if(loc != 0) {
                    this.grailRotate(arr, pos, len1, loc);
                    pos += loc;
                    len2 -= loc;
                }
                if(len2 == 0) break;
                do {
                    pos++;
                    len1--;
                } while(len1 != 0 && Reads.compareValues(arr[pos], arr[pos + len1]) <= 0);
            }
        } else {
            while(len2 != 0) {
                //Binary Search right
                int loc = this.grailBinSearch(arr, pos, len1, pos + (len1 + len2 - 1), false);
                if(loc != len1) {
                    this.grailRotate(arr, pos + loc, len1 - loc, len2);
                    len1 = loc;
                }
                if(len1 == 0) break;
                do {
                    len2--;
                } while(len2 != 0 && Reads.compareValues(arr[pos + len1 - 1], arr[pos + len1 + len2 - 1]) <= 0);
            }
        }
    }

    // arr - starting array. arr[0 - regBlockLen..-1] - buffer (if havebuf).
    // regBlockLen - length of regular blocks. First blockCount blocks are stable sorted by 1st elements and key-coded
    // keysPos - arrays of keys, in same order as blocks. keysPos < midkey means stream A
    // aBlockCount are regular blocks from stream A.
    // lastLen is length of last (irregular) block from stream B, that should go before nblock2 blocks.
    // lastLen = 0 requires aBlockCount = 0 (no irregular blocks). lastLen > 0, aBlockCount = 0 is possible.
    private void grailMergeBuffersLeft(int[] arr, int pos, int blockCount, int blockLen, int aBlockCount, int lastLen) {
        if(blockCount == 0) {
            int aBlocksLen = aBlockCount * blockLen;
            this.grailMergeLeft(arr, pos, aBlocksLen, lastLen, 0 - blockLen);
            return;
        }

        int leftOverLen = blockLen;
        int processIndex = blockLen;
        int restToProcess;

        for(int keyIndex = 1; keyIndex < blockCount; keyIndex++, processIndex += blockLen) {
            restToProcess = processIndex - leftOverLen;
            leftOverLen = this.grailSmartMergeWithBuffer(arr, pos + restToProcess, leftOverLen, blockLen);
        }
        restToProcess = processIndex - leftOverLen;

        if(lastLen != 0) {
            leftOverLen += blockLen * aBlockCount;
            this.grailMergeLeft(arr, pos + restToProcess, leftOverLen, lastLen, -blockLen);
            
        } else {
            this.grailMultiSwap(arr, pos + restToProcess, pos + (restToProcess - blockLen), leftOverLen);
        }
    }
    
    // arr[dist..-1] - buffer, arr[0, leftLen - 1] ++ arr[leftLen, leftLen + rightLen - 1]
    // -> arr[dist, dist + leftLen + rightLen - 1]
    private void grailMergeLeft(int[] arr, int pos, int leftLen, int rightLen, int dist) {
        int left = 0;
        int right = leftLen;

        rightLen += leftLen;

        while(right < rightLen) {
            if(left == leftLen || Reads.compareValues(arr[pos + left], arr[pos + right]) > 0) {
                this.grailSwap(arr, pos + (dist++), pos + (right++));
            } 
            else this.grailSwap(arr, pos + (dist++), pos + (left++));       
            Highlights.markArray(3, pos + left);
            Highlights.markArray(4, pos + right);
        }
        Highlights.clearMark(3);
        Highlights.clearMark(4);
        
        if(dist != left) this.grailMultiSwap(arr, pos + dist, pos + left, leftLen - left);
    }
    private void grailMergeRight(int[] arr, int pos, int leftLen, int rightLen, int dist) {
        int mergedPos = leftLen + rightLen + dist - 1;
        int right = leftLen + rightLen - 1;
        int left = leftLen - 1;

        while(left >= 0) {
            if(right < leftLen || Reads.compareValues(arr[pos + left], arr[pos + right]) > 0) {
                this.grailSwap(arr, pos + (mergedPos--), pos + (left--));
            } 
            else this.grailSwap(arr, pos + (mergedPos--), pos + (right--));
            if(pos + left >= 0) Highlights.markArray(3, pos + left);
            Highlights.markArray(4, pos + right);
        }
        Highlights.clearMark(3);
        Highlights.clearMark(4);
        
        if(right != mergedPos) {
            while(right >= leftLen) this.grailSwap(arr, pos + (mergedPos--), pos + (right--));
        }
    }

    //returns just the leftover length
    private int grailSmartMergeWithBuffer(int[] arr, int pos, int leftOverLen, int blockLen) {
        int dist = 0 - blockLen, left = 0, right = leftOverLen, leftEnd = right, rightEnd = right + blockLen;

        while(left < leftEnd && right < rightEnd) {
            if(Reads.compareValues(arr[pos + left], arr[pos + right]) <= 0) {
                this.grailSwap(arr, pos + (dist++), pos + (left++));
            }
            else this.grailSwap(arr, pos + (dist++), pos + (right++));
            Highlights.markArray(3, pos + left);
            Highlights.markArray(4, pos + right);
        }
        Highlights.clearMark(3);
        Highlights.clearMark(4);
        
        int length;
        if(left < leftEnd) {
            length = leftEnd - left;
            while(left < leftEnd) this.grailSwap(arr, pos + (--leftEnd), pos + (--rightEnd));
        } else {
            length = rightEnd - right;
        }
        return length;
    }

    // build blocks of length buildLen
    // input: [-buildLen, -1] elements are buffer
    // output: first buildLen elements are buffer, blocks 2 * buildLen and last subblock sorted
    private void grailBuildBlocks(int[] arr, int pos, int len, int buildLen) {
        int extraDist, part;
            for(int dist = 1; dist < len; dist += 2) {
                extraDist = 0;
                if(Reads.compareValues(arr[pos + (dist - 1)], arr[pos + dist]) > 0) extraDist = 1;
                this.grailSwap(arr, pos + (dist - 3), pos + (dist - 1 + extraDist));
                this.grailSwap(arr, pos + (dist - 2), pos + (dist - extraDist));
            }
            if(len % 2 != 0) this.grailSwap(arr, pos + (len - 1), pos + (len - 3));
            pos -= 2;
            part = 2;

        for(; part < buildLen; part *= 2) {
            int left = 0;
            int right = len - 2 * part;
            while(left <= right) {
                this.grailMergeLeft(arr, pos + left, part, part, 0 - part);
                left += 2 * part;
            }
            int rest = len - left;
            if(rest > part) {
                this.grailMergeLeft(arr, pos + left, part, rest - part, 0 - part);
            } else {
                this.grailRotate(arr, pos + left - part, part, rest);
            }
            pos -= part;
        }
        int restToBuild = len % (2 * buildLen);
        int leftOverPos = len - restToBuild;

        if(restToBuild <= buildLen) this.grailRotate(arr, pos + leftOverPos, restToBuild, buildLen);
        else this.grailMergeRight(arr, pos + leftOverPos, buildLen, restToBuild - buildLen, buildLen);

        while(leftOverPos > 0) {
            leftOverPos -= 2 * buildLen;
            this.grailMergeRight(arr, pos + leftOverPos, buildLen, buildLen, buildLen);
        }
    }

    // keys are on the left of arr. Blocks of length buildLen combined. We'll combine them in pairs
    // buildLen and nkeys are powers of 2. (2 * buildLen / regBlockLen) keys are guaranteed
    private void grailCombineBlocks(int[] arr, int pos, int len, int buildLen, int regBlockLen) {
        int combineLen = len / (2 * buildLen);
        int leftOver = len % (2 * buildLen);
        if(leftOver <= buildLen) {
            len -= leftOver;
            leftOver = 0;
        }

        for(int i = 0; i <= combineLen; i++) {
            if(i == combineLen && leftOver == 0) break;

            int blockPos = pos + i * 2 * buildLen;
            int blockCount = (i == combineLen ? leftOver : 2 * buildLen) / regBlockLen;

            for(int index = 1; index < blockCount; index++) {
                int leftIndex = index - 1;

                for(int rightIndex = index; rightIndex < blockCount; rightIndex++) {
                    int rightComp = Reads.compareValues(arr[blockPos + leftIndex * regBlockLen],
                                                        arr[blockPos + rightIndex * regBlockLen]);
                    if(rightComp >= 0) {
                        leftIndex = rightIndex;
                    }
                }
                if(leftIndex != index - 1) {
                    this.grailMultiSwap(arr, blockPos + (index - 1) * regBlockLen, blockPos + leftIndex * regBlockLen, regBlockLen);
                }
            }

            int aBlockCount = 0;
            int lastLen = 0;
            if(i == combineLen) lastLen = leftOver % regBlockLen;

            if(lastLen != 0) {
                while(aBlockCount < blockCount && Reads.compareValues(arr[blockPos + blockCount * regBlockLen],
                                                                      arr[blockPos + (blockCount - aBlockCount - 1) * regBlockLen])
																	  < 0) {
                    aBlockCount++;
                }
            }
            this.grailMergeBuffersLeft(arr, blockPos, blockCount - aBlockCount, regBlockLen, aBlockCount, lastLen);
        }
        while(--len >= 0) {
            this.grailSwap(arr, pos + len, pos + len - regBlockLen);
        }
    }

    protected void grailCommonSort(int[] arr, int pos, int len) {
        this.grailInsertSorter = new SmartGnomeSort(this.arrayVisualizer);
        
        if(len <= 16) {
            this.grailInsertSort(arr, pos, len);
            return;
        }
        
        int blockLen = 1;
        while(blockLen * blockLen < len) blockLen *= 2;
        int buildLen = blockLen;

        this.grailBuildBlocks(arr, pos + blockLen, len - blockLen, buildLen);

        // 2 * buildLen are built
        while(len - blockLen > (buildLen *= 2)) {
            this.grailCombineBlocks(arr, pos + blockLen, len - blockLen, buildLen, blockLen);
            
            Highlights.clearMark(2);
        }

        this.grailInsertSort(arr, pos, blockLen);
        this.grailMergeWithoutBuffer(arr, pos, blockLen, len - blockLen);
    }
}