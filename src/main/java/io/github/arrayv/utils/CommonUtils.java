package io.github.arrayv.utils;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;

public final class CommonUtils {
    private CommonUtils() {
    }

    public static String replace(String s, String from, String to) {
        int i = s.indexOf(from);
        if (i == -1) {
            // Fast path: no replacements necessary
            return s;
        }
        StringBuilder result = new StringBuilder(s.length() + 16);
        int last = 0;
        do {
            result.append(s, last, i);
            result.append(to);
            last = i;
            i = s.indexOf(from, i + from.length());
        } while (i != -1);
        result.append(s, last, s.length());
        return result.toString();
    }

    @SuppressWarnings("unchecked")
    public static <L, R> Map.Entry<L, R>[] createPairArray(Object... values) {
        if ((values.length & 1) != 0) {
            throw new IllegalArgumentException("Must be an even number of values");
        }
        Map.Entry<L, R>[] result = new Map.Entry[values.length >> 1];
        for (int i = 0, j = 0; i < result.length; i++, j += 2) {
            result[i] = new SimpleImmutableEntry<>((L)values[j], (R)values[j + 1]);
        }
        return result;
    }
}
