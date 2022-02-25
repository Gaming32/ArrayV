package io.github.arrayv.utils;

// @checkstyle:off TypeNameCheck
public final class m {
// @checkstyle:on TypeNameCheck
    private m() {
    }

    /**
     * Returns the logarithm base BASE of the number X
     * @param x
     * @param base
     * @return The resulting logarithm
     */
    public static double log(double x, double base) {
        return Math.log(x) / Math.log(base);
    }

    /**
     * Returns the truncated logarithm base BASE of the number X
     * @param x
     * @param base
     * @return The resulting truncated logarithm
     */
    public static int flog(double x, double base) {
        return (int)m.log(x, base);
    }

    /**
     * Returns the logarithm base 2 of the number X
     * @param x
     * @return The resulting logarithm
     */
    public static double log2(double x) {
        return m.log(x, 2);
    }

    /**
     * Returns the truncated logarithm base 2 of the number X
     * @param x
     * @return The resulting truncated logarithm
     */
    public static int flog2(double x) {
        return m.flog(x, 2);
    }

    /**
     * Returns the logarithm base 10 of the number X
     * @param x
     * @return The resulting logarithm
     */
    public static double log10(double x) {
        return m.log(x, 10);
    }

    /**
     * Returns the truncated logarithm base 10 of the number X
     * @param x
     * @return The resulting truncated logarithm
     */
    public static int flog10(double x) {
        return m.flog(x, 10);
    }

    /**
     * Returns the logarithm base e of the number X
     * @param x
     * @return The resulting logarithm
     */
    public static double ln(double x) {
        return Math.log(x);
    }

    /**
     * Returns X raised to the power of Y
     * @param x
     * @param y
     * @return X raised to the power of Y
     */
    public static double pow(double x, double y) {
        return Math.pow(x, y);
    }

    /**
     * Returns X raised to the power of Y and truncated to the nearest integer closest to zero
     * @param x
     * @param y
     * @return X raised to the power of Y and truncated to the nearest integer closest to zero
     */
    public static int fpow(double x, double y) {
        return (int)Math.pow(x, y);
    }

    /**
     * Rounds X down to the nearest value closest to -inf
     * @param x
     * @return X rounded down to the nearest value closest to -inf
     */
    public static int lowFloor(double x) {
        return (int)Math.floor(x);
    }

    /**
     * Truncates X
     * @param x
     * @return The truncated value of X
     */
    public static int floor(double x) {
        return (int)x;
    }

    /**
     * Rounds X up to the nearest value closest to +inf
     * @param x
     * @return X rounded up to the nearest value closest to +inf
     */
    public static int ceil(double x) {
        return (int)Math.ceil(x);
    }

    // @checkstyle:off ConstantNameCheck
    public static final double inf = Double.POSITIVE_INFINITY;
    public static final double NaN = Double.NaN;
    // @checkstyle:on ConstantNameCheck
}
