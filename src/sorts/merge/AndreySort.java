package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class AndreySort extends Sort {
    public AndreySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Andrey's Merge");
        this.setRunAllSortsName("Andrey Astrelin's In-Place Merge Sort");
        this.setRunSortName("Andreysort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private void sort(int[] arr, int a, int b) {
        while(b > 1) {
            int k = 0;
            for(int i = 1; i < b; i++) {
                if(Reads.compareValues(arr[a + k], arr[a + i]) > 0) {
                    k = i;
                }
            }
            Writes.swap(arr, a, a + k, 1, true, false);
            a++;
            b--;
        }
    }
    
    private void aswap(int[] arr, int arr1, int arr2, int l) { 
        while(l-- > 0) {
            Writes.swap(arr, arr1++, arr2++, 1, true, false);
        }
    }
    
    // arr1(-l1..0] :merge: arr2(-l2..0] -> arr2(-l2..l1]
    private int backmerge(int[] arr, int arr1, int l1, int arr2, int l2) {
        int arr0 = arr2 + l1;
        for(;;) {
            if(Reads.compareValues(arr[arr1], arr[arr2]) > 0) { 
                Writes.swap(arr, arr1--, arr0--, 1, true, false);
                if(--l1 == 0) {
                    return 0;
                }
            }
            else {
                Writes.swap(arr, arr2--, arr0--, 1, true, false);
                if(--l2 == 0) {
                    break;
                }
            }
        }
        int res = l1;
        do {
            Writes.swap(arr, arr1--, arr0--, 1, true, false);
        } while(--l1 != 0);
        return res;
    }
    
    // merge arr[p0..p1) by buffer arr[p1..p1+r)
    private void rmerge(int[] arr, int a, int l, int r) {
        for(int i = 0; i < l; i += r) {
            // select smallest arr[p0+n*r]
            int q = i;
            for(int j = i + r; j < l; j += r) {
                if(Reads.compareValues(arr[a + q], arr[a + j]) > 0) {
                    q = j;
                }
            }       
            if(q != i) {
                aswap(arr, a + i, a + q, r); // swap it with current position
            }
            if(i != 0) {
                aswap(arr, a + l, a + i, r);  // swap current position with buffer
                backmerge(arr, a + (l + r - 1), r, a + (i - 1), r); // buffer :merge: arr[i-r..i) -> arr[i-r..i+r)
            }
        }
    }
    
    private int rbnd(int len) {
        len = len / 2;
        int k = 0;
        for(int i = 1; i < len; i *= 2) {
            k++;
        }
        len /= k;
        for(k = 1; k <= len; k *= 2);
        return k;
    }

    private void msort(int[] arr, int a, int len) {
        if(len < 12) {
            sort(arr, a, len);
            return;
        }
        
        int r = rbnd(len);
        int lr = (len / r - 1) * r;
        
        for(int p = 2; p <= lr; p += 2) {
            if(Reads.compareValues(arr[a + (p - 2)], arr[a + (p - 1)]) > 0) {
                Writes.swap(arr, a + (p - 2), a + (p - 1), 1, true, false);
            }
            if((p & 2) != 0) {
                continue;
            }
            
            aswap(arr, a + (p - 2), a + p, 2);
            
            int m = len - p;
            int q = 2;
            
            for(;;) {
                int q0 = 2 * q;
                if(q0 > m || (p & q0) != 0) {
                    break;
                }
                backmerge(arr, a + (p - q - 1), q, a + (p + q - 1), q);
                q = q0;
            }
            
            backmerge(arr, a + (p + q - 1), q, a + (p - q - 1), q);
            int q1 = q;
            q *= 2;
            
            while((q & p) == 0) {
                q *= 2;
                rmerge(arr, a + (p - q), q, q1);
            }
        }
        
        int q1 = 0;
        for(int q = r; q< lr; q *= 2) {
            if((lr & q) != 0) {
                q1 += q;
                if(q1 != q) {
                    rmerge(arr, a + (lr - q1), q1, r);
                }
            }
        }
        
        int s = len - lr;
        msort(arr, a + lr, s);
        aswap(arr, a, a + lr, s);
        s += backmerge(arr, a + (s - 1), s, a + (lr - 1), lr - s);
        msort(arr, a, s);
    }
    
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.msort(array, 0, sortLength);
    }
}