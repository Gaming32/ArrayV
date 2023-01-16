package io.github.arrayv.utils;

import io.github.arrayv.main.ArrayVisualizer;

import java.util.ArrayList;
import java.util.List;

public final class ShuffleInfo {
    final boolean isDistribution;
    final Distributions distribution;
    final Shuffles shuffle;
    final boolean warpDistribution;

    public ShuffleInfo(Distributions distribution, boolean warpDistribution) {
        this.isDistribution = true;
        this.distribution = distribution;
        this.shuffle = null;
        this.warpDistribution = warpDistribution;
    }

    public ShuffleInfo(Distributions distribution) {
        this(distribution, false);
    }

    public ShuffleInfo(Shuffles shuffle) {
        this.isDistribution = false;
        this.distribution = null;
        this.shuffle = shuffle;
        this.warpDistribution = false;
    }

    public static ShuffleInfo[] fromDistributionIterable(Iterable<Distributions> iterable, boolean warped) {
        List<ShuffleInfo> result = new ArrayList<>();
        iterable.forEach(dist -> result.add(new ShuffleInfo(dist, warped)));
        return result.toArray(new ShuffleInfo[0]);
    }

    public static ShuffleInfo[] fromDistributionIterable(Iterable<Distributions> iterable) {
        return fromDistributionIterable(iterable, false);
    }

    public static ShuffleInfo[] fromShuffleIterable(Iterable<Shuffles> iterable) {
        List<ShuffleInfo> result = new ArrayList<>();
        iterable.forEach(shuff -> result.add(new ShuffleInfo(shuff)));
        return result.toArray(new ShuffleInfo[0]);
    }

    public boolean isDistribution() {
        return this.isDistribution;
    }

    public Distributions getDistribution() {
        return this.distribution;
    }

    public Shuffles getShuffle() {
        return this.shuffle;
    }

    public boolean isDistributionWarped() {
        return this.warpDistribution;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof ShuffleInfo) {
            ShuffleInfo other = (ShuffleInfo)o;
            if (this.isDistribution != other.isDistribution) {
                return false;
            }
            if (isDistribution) {
                return this.distribution == other.distribution && this.warpDistribution == other.warpDistribution;
            } else {
                return this.shuffle == other.shuffle;
            }
        } else if (o instanceof Shuffles) {
            return !this.isDistribution && this.shuffle == o;
        } else if (o instanceof Distributions) {
            return this.isDistribution && this.distribution == o;
        }
        return false;
    }

    public String getName() {
        if (this.isDistribution) {
            assert this.distribution != null;
            if (warpDistribution)
                return "Warped " + this.distribution.getName();
            return this.distribution.getName();
        }
        assert this.shuffle != null;
        return this.shuffle.getName();
    }

    public void shuffle(int[] array, ArrayVisualizer arrayVisualizer) {
        if (this.isDistribution) {
            assert distribution != null;
            Writes Writes = arrayVisualizer.getWrites();
            int currentLen = arrayVisualizer.getCurrentLength();
            double sleep = arrayVisualizer.shuffleEnabled() ? 1 : 0;
            int[] copy = new int[currentLen];
            int[] tmp = new int[currentLen];
            Writes.arraycopy(array, 0, copy, 0, currentLen, sleep, true, true);
            this.distribution.initializeArray(tmp, arrayVisualizer);
            if (warpDistribution) {
                for (int i = 0; i < currentLen; i++) {
                    Writes.write(array, i, copy[tmp[i]], sleep, true, false);
                }
            } else {
                for (int i = 0; i < currentLen; i++) {
                    Writes.write(array, i, tmp[copy[i]], sleep, true, false);
                }
            }
        } else {
            assert shuffle != null;
            Delays Delays = arrayVisualizer.getDelays();
            Highlights Highlights = arrayVisualizer.getHighlights();
            Writes Writes = arrayVisualizer.getWrites();
            this.shuffle.shuffleArray(array, arrayVisualizer, Delays, Highlights, Writes);
        }
    }
}
