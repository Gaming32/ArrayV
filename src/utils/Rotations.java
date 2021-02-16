package utils;

import main.ArrayVisualizer;

public class Rotations {
    private static Writes Writes;

    public Rotations(ArrayVisualizer arrayVisualizer) {
        Rotations.Writes = arrayVisualizer.getWrites();
    }

    // utility functions
    public static void blockSwap(int[] array, int a, int b, int len, double pause, boolean mark, boolean auxwrite) {
        for (int i = 0; i < len; i++) {
            Writes.swap(array, a + i, b + i, pause, mark, auxwrite);
        }
    }

    private static void grailForwardShift(int[] array, int start, int length, double pause, boolean mark, boolean auxwrite) {
        int temp = array[start];
        for(int i = 0; i < length; i++) {
            Writes.write(array, start + i, array[start + i + 1], pause, mark, auxwrite);
        }
        Writes.write(array, start + length, temp, pause, mark, auxwrite);
    }

    private static void grailBackwardShift(int[] array, int start, int length, double pause, boolean mark, boolean auxwrite) {    
        int temp = array[start + length];
        for(int i = length; i > 0; i--) {
            Writes.write(array, start + i, array[start + i - 1], pause, mark, auxwrite);
        }
        Writes.write(array, start, temp, pause, mark, auxwrite);
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
        Writes.reversal(array, pos, pos + lenA, pause, mark, auxwrite);
        Writes.reversal(array, pos + lenA, pos + lenA + lenB, pause, mark, auxwrite);
        Writes.reversal(array, pos, pos + lenA + lenB, pause, mark, auxwrite);
    }

    // public static void juggling(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {

    // }

    public static void holyGriesMills(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
        boolean swaps = false;

        while(lenA > 1 && lenB > 1) {
            while(lenA <= lenB) {
                blockSwap(array, pos, pos + lenA, lenA, pause, mark, auxwrite);
                pos    += lenA;
                lenB -= lenA;
                swaps     = true;
            }
            if(!swaps || (lenB > 1 && lenA > 1)) {
                while(lenA > lenB) {
                    blockSwap(array, pos + lenA - lenB, pos + lenA, lenB, pause, mark, auxwrite); //backward swap here
                    lenA -= lenB;
                }
            }
        }

        if(lenA == 1) {
            grailForwardShift(array, pos, lenB, pause, mark, auxwrite);
        }
        else if(lenB == 1) {
            grailBackwardShift(array, pos, lenA, pause, mark, auxwrite);
        }
    }

    // public static void helium(int[] array, int pos, int lenA, int lenB, double pause, boolean mark, boolean auxwrite) {
        
    // }
}
