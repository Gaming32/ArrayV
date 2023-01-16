package io.github.arrayv.utils;

import io.github.arrayv.main.ArrayVisualizer;

public class AntiQSort {
    private final ArrayVisualizer arrayVisualizer;
    private int[] data;
    private int nmemb;
    private int gas;
    private int frozen;

    private boolean hasCandidate;
    private int candidate;

    public AntiQSort(ArrayVisualizer arrayVisualizer) {
        this.arrayVisualizer = arrayVisualizer;
    }

    public int compare(int ap, int bp) {
        int a, b;
        if (!this.hasCandidate) {
            this.candidate = 0;
            this.hasCandidate = true;
        }

        a = ap;
        b = bp;

        if (data[a] == gas && data[b] == gas)
            if (a == candidate)
                data[a] = frozen++;
            else
                data[b] = frozen++;

        if (data[a] == gas) {
            candidate = a;
            return 1;
        }

        if (data[b] == gas) {
            candidate = b;
            return -1;
        }

        return Integer.compare(data[a], data[b]);
    }

    public void beginSort(int[] refs, int nmemb) {
        this.hasCandidate = false;
        this.frozen = 1;

        this.nmemb = nmemb;
        this.data = arrayVisualizer.getWrites().createExternalArray(nmemb);
        arrayVisualizer.getWrites().changeAllocAmount(-nmemb);
        this.gas = nmemb;
        for (int i = 0; i < nmemb; i++) {
            refs[i] = i;
            data[i] = gas;
        }
    }

    public int[] getResult() {
        return this.data;
    }

    public void hideResult() {
        arrayVisualizer.getWrites().changeAllocAmount(nmemb);
        arrayVisualizer.getWrites().deleteExternalArray(this.data);
    }
}
