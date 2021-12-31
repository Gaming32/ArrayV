package utils;

public class IndexedRotations {
    public static void griesMills(int[] array, int start, int mid, int end, double pause, boolean mark, boolean auxwrite) {
        Rotations.griesMills(array, start, mid - start, end - mid, pause, mark, auxwrite);
    }

    public static void threeReversal(int[] array, int start, int mid, int end, double pause, boolean mark, boolean auxwrite) {
        Rotations.threeReversal(array, start, mid - start, end - mid, pause, mark, auxwrite);
    }

    public static void juggling(int[] array, int start, int mid, int end, double pause, boolean mark, boolean auxwrite) {
        Rotations.juggling(array, start, mid - start, end - mid, pause, mark, auxwrite);
    }

    public static void holyGriesMills(int[] array, int start, int mid, int end, double pause, boolean mark, boolean auxwrite) {
        Rotations.holyGriesMills(array, start, mid - start, end - mid, pause, mark, auxwrite);
    }

    public static void helium(int[] array, int start, int mid, int end, double pause, boolean mark, boolean auxwrite) {
        Rotations.helium(array, start, mid - start, end - mid, pause, mark, auxwrite);
    }

    public static void cycleReverse(int[] array, int start, int mid, int end, double pause, boolean mark, boolean auxwrite) {
        Rotations.cycleReverse(array, start, mid - start, end - mid, pause, mark, auxwrite);
    }

    public static void bridge(int[] array, int start, int mid, int end, double pause, boolean mark, boolean auxwrite) {
        Rotations.bridge(array, start, mid - start, end - mid, pause, mark, auxwrite);
    }
}
