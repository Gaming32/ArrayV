package utils;

import main.ArrayVisualizer;

public class Rotations {
    private static Writes Writes;

    public Rotations(ArrayVisualizer arrayVisualizer) {
        Rotations.Writes = arrayVisualizer.getWrites();
    }

    // utility functions
    private static void swapBlocksBackwards(int[] array, int a, int b, int len, double pause, boolean mark, boolean auxwrite) {
        for (int i = 0; i < len; i++) {
            Writes.swap(array, a + len - i - 1, b + len - i - 1, pause, mark, auxwrite);
        }
    }

    private static void blockSwap(int[] array, int a, int b, int len, double pause, boolean mark, boolean auxwrite) {
        for (int i = 0; i < len; i++) {
            Writes.swap(array, a + i, b + i, pause, mark, auxwrite);
        }
    }

    private static void shiftForwards(int[] array, int start, int length, double pause, boolean mark, boolean auxwrite) {
        int temp = array[start];
        for(int i = 0; i < length; i++) {
            Writes.write(array, start + i, array[start + i + 1], pause, mark, auxwrite);
        }
        Writes.write(array, start + length, temp, pause, mark, auxwrite);
    }

    private static void shiftBackwards(int[] array, int start, int length, double pause, boolean mark, boolean auxwrite) {    
        int temp = array[start + length];
        for(int i = length; i > 0; i--) {
            Writes.write(array, start + i, array[start + i - 1], pause, mark, auxwrite);
        }
        Writes.write(array, start, temp, pause, mark, auxwrite);
    }

    private static int mapIndex(int index, int n, int length) {
        return (index - n + length) % length;
    }

    private static int swap(int[] arr, int a, int v, double pause, boolean mark, boolean auxwrite) {
        int old = arr[a];
        Writes.write(arr, a, v, pause, mark, auxwrite);
        return old;
    }


    // rotation algorithms
    public static void griesMills(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
        while(lenA != 0 && lenB != 0) {
            if(lenA <= lenB) {
                blockSwap(array, pos, pos + lenA, lenA, pause, mark, auxwrite);
                pos += lenA;
                lenB -= lenA;
            }
            else {
                blockSwap(array, pos + (lenA - lenB), pos + lenA, lenB, pause, mark, auxwrite);
                lenA -= lenB;
            }
        }
    }

    public static void threeReversal(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
        Writes.reversal(array, pos, pos + lenA - 1, pause, mark, auxwrite);
        Writes.reversal(array, pos + lenA, pos + lenA + lenB - 1, pause, mark, auxwrite);
        Writes.reversal(array, pos, pos + lenA + lenB - 1, pause, mark, auxwrite);
    }

    public static void holyGriesMills(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
        while(lenA > 1 && lenB > 1) {
            while(lenA <= lenB) {
                blockSwap(array, pos, pos + lenA, lenA, pause, mark, auxwrite);
                pos  += lenA;
                lenB -= lenA;
            }
            
            if(lenA <= 1 || lenB <= 1) break;
            
            while(lenA > lenB) {
                swapBlocksBackwards(array, pos + lenA - lenB, pos + lenA, lenB, pause, mark, auxwrite);
                lenA -= lenB;
            }
        }
        
        if(lenA == 1) {
            shiftForwards(array, pos, lenB, pause, mark, auxwrite);
        }
        else if(lenB == 1) {
            shiftBackwards(array, pos, lenA, pause, mark, auxwrite);
        }
    }

    // by thatsOven
    public static void helium(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
        while (lenB > 1 && lenA > 1) {
            if (lenB < lenA) {
                blockSwap(array, pos, pos + lenA, lenB, pause, mark, auxwrite);
                pos  += lenB;
                lenA -= lenB;
            } else {
                swapBlocksBackwards(array, pos, pos + lenB, lenA, pause, mark, auxwrite);
                lenB -= lenA;
            }
        }
        
        if      (lenB == 1) shiftBackwards(array, pos, lenA, pause, mark, auxwrite);
        else if (lenA == 1) shiftForwards(array, pos, lenB, pause, mark, auxwrite);
    }

    // by Scandum and Control
    public static void cycleReverse(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
        int a = pos,
            b = pos + lenA - 1,
            c = pos + lenA,
            d = pos + lenA + lenB - 1;
        int swap;

        while (a < b && c < d) {
            swap = array[b];
            Writes.write(array, b--, array[a], pause, mark, auxwrite);
            Writes.write(array, a++, array[c], pause, mark, auxwrite);
            Writes.write(array, c++, array[d], pause, mark, auxwrite);
            Writes.write(array, d--, swap, pause, mark, auxwrite);
        }
        while (a < b) {
            swap = array[b];
            Writes.write(array, b--, array[a], pause, mark, auxwrite);
            Writes.write(array, a++, array[d], pause, mark, auxwrite);
            Writes.write(array, d--, swap, pause, mark, auxwrite);
        }
        while (c < d) {
            swap = array[c];
            Writes.write(array, c++, array[d], pause, mark, auxwrite);
            Writes.write(array, d--, array[a], pause, mark, auxwrite);
            Writes.write(array, a++, swap, pause, mark, auxwrite);
        }
        while (a < d) {
            swap = array[a];
            Writes.write(array, a++, array[d], pause, mark, auxwrite);
            Writes.write(array, d--, swap, pause, mark, auxwrite);
        }
    }

    public static void juggling(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
        int length = lenA + lenB;
        lenA %= length;

        if (lenA == 0) return;

        for (int cnt = 0, 
                 index = 0, 
                 value = array[pos + index], 
                 startIndex = index; 
            cnt < length; cnt++) {
                int nextIndex = mapIndex(index, lenA, length);

                value = swap(array, pos + nextIndex, value, pause, mark, auxwrite);

                if (nextIndex == startIndex) {
                    startIndex = index = mapIndex(index, 1, length);
                    value = array[pos + index];
                } else {
                    index = nextIndex;
                }
        }
    }
}
