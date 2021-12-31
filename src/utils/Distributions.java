package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import dialogs.LoadCustomDistributionDialog;
import main.ArrayVisualizer;
import panes.JErrorPane;

/*
 *
MIT License

Copyright (c) 2020 ArrayV 4.0 Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

public enum Distributions {
    LINEAR {
        public String getName() {
            return "Linear";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();

            for (int i = 0; i < currentLen; i++)
                array[i] = i;
        }
    },
    SIMILAR {
        public String getName() {
            return "Few Unique";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            Random random = new Random();

            int l = 0, r, t = Math.min(currentLen, 8);
            for (int i = 0; i < t; i++)
                if (random.nextDouble() < 0.5) l++;
            r = currentLen-(t-l);

            int i = 0;
            for (; i < l; i++)          array[i] = (int) (currentLen * 0.25);
            for (; i < r; i++)          array[i] = currentLen / 2;
            for (; i < currentLen; i++) array[i] = (int) (currentLen * 0.75);
        }
    },
    EQUAL {
        public String getName() {
            return "No Unique";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            int val = currentLen / 2;

            for (int i = 0; i < currentLen; i++) {
                array[i] = val;
            }
        }
    },
    RANDOM {
        public String getName() {
            return "Random";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            Random random = new Random();

            for (int i = 0; i < currentLen; i++)
                array[i] = random.nextInt(currentLen);
        }
    },
    SQUARE {
        public String getName() {
            return "Quadratic";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();

            for (int i = 0; i < currentLen; i++)
                array[i] = (int)(Math.pow(i, 2)/currentLen);
        }
    },
    SQRT {
        public String getName() {
            return "Square Root";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();

            for (int i = 0; i < currentLen; i++)
                array[i] = (int)(Math.sqrt(i)*Math.sqrt(currentLen));
        }
    },
    CUBIC {
        public String getName() {
            return "Cubic (Centered)";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            int power = 3;
            double mid = (currentLen-1)/2d;

            for (int i = 0; i < currentLen; i++)
                array[i] = (int)(Math.pow(i - mid, power)/Math.pow(mid, power-1) + mid);
        }
    },
    QUINTIC {
        public String getName() {
            return "Quintic (Centered)";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            int power = 5;
            double mid = (currentLen-1)/2d;

            for (int i = 0; i < currentLen; i++)
                array[i] = (int)(Math.pow(i - mid, power)/Math.pow(mid, power-1) + mid);
        }
    },
    CBRT {
        public String getName() {
            return "Cube Root (Centered)";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            int p = 3;
            double h = currentLen/2d;

            for (int i = 0; i < currentLen; i++) {
                double val  = i/h - 1,
                       root = val < 0 ? -Math.pow(-val, 1d/p) : Math.pow(val, 1d/p);

                array[i] = (int)(h * (root + 1));
            }
        }
    },
    QTRT {
        public String getName() {
            return "Fifth Root (Centered)";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            int p = 5;
            double h = currentLen/2d;

            for (int i = 0; i < currentLen; i++) {
                double val  = i/h - 1,
                       root = val < 0 ? -Math.pow(-val, 1d/p) : Math.pow(val, 1d/p);

                array[i] = (int)(h * (root + 1));
            }
        }
    },
    SINE {
        public String getName() {
            return "Sine Wave";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            int n = currentLen-1;
            double c = 2*Math.PI/n;

            for (int i = 0; i < currentLen; i++)
                array[i] = (int)(n * (Math.sin(c * i)+1)/2);
        }
    },
    COSINE {
        public String getName() {
            return "Cosine Wave";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            int n = currentLen-1;
            double c = 2*Math.PI/n;

            for (int i = 0; i < currentLen; i++)
                array[i] = (int)(n * (Math.cos(c * i)+1)/2);
        }
    },
    PERLIN_NOISE {
        public String getName() {
            return "Perlin Noise";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            Random random = new Random();

            int[] perlinNoise = new int[currentLen];

            float step = 1f / currentLen;
            float randomStart = (float) (random.nextInt(currentLen));
            int octave = (int) (Math.log(currentLen) / Math.log(2));

            for (int i = 0; i < currentLen; i++) {
                int value = (int) (PerlinNoise.returnFracBrownNoise(randomStart, octave) * currentLen);
                perlinNoise[i] = value;
                randomStart += step;
            }

            int minimum = Integer.MAX_VALUE;
            for (int i = 0; i < currentLen; i++) {
                if (perlinNoise[i] < minimum) {
                    minimum = perlinNoise[i];
                }
            }
            minimum = Math.abs(minimum);
            for (int i = 0; i < currentLen; i++) {
                perlinNoise[i] += minimum;
            }

            double maximum = Double.MIN_VALUE;
            for (int i = 0; i < currentLen; i++) {
                if (perlinNoise[i] > maximum) {
                    maximum = perlinNoise[i];
                }
            }
            double scale = currentLen / maximum;
            if (scale < 1.0 || scale > 1.8) {
                for (int i = 0; i < currentLen; i++) {
                    perlinNoise[i] = (int) (perlinNoise[i] * scale);
                }
            }

            for (int i = 0; i < currentLen; i++) {
                array[i] = Math.min(perlinNoise[i], currentLen-1);
            }
        }
    },
    PERLIN_NOISE_CURVE {
        public String getName() {
            return "Perlin Noise Curve";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();

            for (int i = 0; i < currentLen; i++) {
                int value = 0 - (int) (PerlinNoise.returnNoise((float) i / currentLen) * currentLen);
                array[i] = Math.min(value, currentLen-1);
            }
        }
    },
    BELL_CURVE {
        public String getName() {
            return "Bell Curve";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            double step = 8d / currentLen;
            double position = -4;
            int constant = 1264;
            double factor = currentLen / 512d;

            for (int i = 0; i < currentLen; i++) {
                double square = Math.pow(position, 2);
                double negativeSquare = 0 - square;
                double halfNegSquare = negativeSquare / 2d;
                double numerator = constant * factor * Math.pow(Math.E, halfNegSquare);

                double doublePi = 2 * Math.PI;
                double denominator = Math.sqrt(doublePi);

                array[i] = (int) (numerator / denominator);
                position += step;
            }
        }
    },
    RULER {
        public String getName() {
            return "Ruler";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            int step = Math.max(1, currentLen/256);
            int floorLog2 = (int)(Math.log(currentLen/step)/Math.log(2));
            int lowest;
            for (lowest = step; 2*lowest <= currentLen/4; lowest*=2);
            boolean[] digits = new boolean[floorLog2+2];

            int i, j;
            for (i = 0; i+step <= currentLen; i+=step) {
                for (j = 0; digits[j]; j++);
                digits[j] = true;

                for (int k = 0; k < step; k++) {
                    int value = currentLen/2 - Math.min((1 << j)*step, lowest);
                    array[i+k] = value;
                }

                for (int k = 0; k < j; k++) digits[k] = false;
            }

            for (j = 0; digits[j]; j++);
            digits[j] = true;
            while (i < currentLen) {
                int value = Math.max(currentLen/2 - (1 << j)*step, currentLen/4);
                array[i++] = value;
            }
        }
    },
    BLANCMANGE {
        public String getName() {
            return "Blancmange Curve";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            int floorLog2 = (int)(Math.log(currentLen)/Math.log(2));

            for (int i = 0; i < currentLen; i++) {
                int value = (int)(currentLen * curveSum(floorLog2, (double)i/currentLen));
                array[i] = value;
            }
        }

        public double curveSum(int n, double x) {
            double sum = 0;
            while (n >= 0)
                sum += curve(n--, x);
            return sum;
        }

        public double curve(int n, double x) {
            return triangleWave((1 << n) * x) / (1 << n);
        }

        public double triangleWave(double x) {
            return Math.abs(x - (int)(x + 0.5));
        }
    },
    CANTOR {
        public String getName() {
            return "Cantor Function";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();

            cantor(array, 0, currentLen, 0, currentLen-1);
        }

        public void cantor(int[] array, int a, int b, int min, int max) {
            if (b-a < 1 || max == min) return;

            int mid = (min+max)/2;
            if (b-a == 1) {
                array[a] = mid;
                return;
            }

            int t1 = (a+a+b)/3, t2 = (a+b+b+2)/3;

            for (int i = t1; i < t2; i++)
                array[i] = mid;

            this.cantor(array, a, t1, min, mid);
            this.cantor(array, t2, b, mid+1, max);
        }
    },
    DIVISORS {//O(n^1.5)
        public String getName() {
            return "Sum of Divisors";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            int[] n = new int[currentLen];

            n[0] = 0;
            n[1] = 1;
            double max = 1;

            for (int i = 2; i < currentLen; i++) {
                n[i] = sumDivisors(i);
                if (n[i] > max) max = n[i];
            }

            double scale = Math.min((currentLen-1)/max, 1);
            for (int i = 0; i < currentLen; i++) {
                array[i] = (int)(n[i]*scale);
            }
        }

        public int sumDivisors(int n) {
            int sum = n+1;
            for (int i = 2; i <= (int)Math.sqrt(n); i++) {
                if (n % i == 0) {
                    if (i == n/i) sum += i;
                    else          sum += i + n/i;
                }
            }
            return sum;
        }
    },
    FSD {// fly straight dangit (OEIS A133058)
        public String getName() {
            return "Fly Straight, Damnit!";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            int[] fsd = new int[currentLen];

            double max;
            max = fsd[0] = fsd[1] = 1;
            for (int i = 2; i < currentLen; i++) {
                int g = gcd(fsd[i-1], i);
                fsd[i] = fsd[i-1]/g + (g==1 ? i+1 : 0);
                if (fsd[i] > max) max = fsd[i];
            }

            double scale = Math.min((currentLen-1)/max, 1);
            for (int i = 0; i < currentLen; i++)
                array[i] = (int)(fsd[i]*scale);
        }

        public int gcd(int a, int b) {
            if (b==0) return a;
            return gcd(b,a%b);
        }
    },
    REVLOG {
        public String getName() {
            return "Decreasing Random";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            Random random = new Random();

            for (int i = 0; i < currentLen; i++){
                int r = random.nextInt(currentLen - i) + i;
                array[i] = r;
            }
        }
    },
    MODULO {
        public String getName() {
            return "Modulo Function";
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int n = ArrayVisualizer.getCurrentLength();

            for (int i = 0; i < n; i++) array[i] = 2*(n%(i+1));
        }
    },
    TOTIENT { // probably o(n^1.5)
        @Override
        public String getName() {
            return "Euler Totient Function";
        }

        class PrimeFactorization {
            List<Integer> primeFactors;
            List<Integer> exponentList;

            public PrimeFactorization(int i, List<Integer> allPrimes) {
                primeFactors = new ArrayList<>();
                exponentList = new ArrayList<>();

                int sqrt = (int) Math.sqrt(i);
                for (int prime : allPrimes) {
                    // all prime factors are found (unless i itself is prime, handled below)
                    if (prime > sqrt || i == 1) break;

                    if (i % prime == 0) { // p is a prime factor of i
                        i /= prime;
                        int exponent = 1;
                        while (i % prime == 0) {
                            i /= prime;
                            exponent++;
                        }

                        primeFactors.add(prime);
                        exponentList.add(exponent);

                        sqrt = (int) Math.sqrt(i);
                    }
                }
                if (i != 1) {
                    primeFactors.add(i);
                    exponentList.add(1);
                }
            }
        }

        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int[] smallTotients = new int[] {
                0, 1, 1, 2, 2, 4, 2, 6, 4, 6, 4, 10, 4, 12, 6, 8, 8, 16, 6,
                18, 8, 12, 10, 22, 8, 20, 12, 18, 12, 28, 8, 30, 16, 20, 16, 24, 12, 36, 18,
                24, 16, 40, 12, 42, 20, 24, 22, 46, 16, 42, 20, 32, 24, 52, 18, 40, 24, 36,
                28, 58, 16, 60, 30, 36, 32, 48, 20, 66, 32, 44
            };

            // important: have largest prime larger than number of precomputed totients
            List<Integer> smallPrimes = Arrays.asList(
                2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59,
                61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139,
                149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227,
                229, 233, 239, 241, 251, 257, 263, 269, 271
            );

            ArrayList<Integer> primes = new ArrayList<>(smallPrimes);

            int m = smallTotients.length;
            int n = ArrayVisualizer.getCurrentLength();
            System.arraycopy(smallTotients, 0, array, 0, m > n ? n : m);

            for (int i = m; i < n; i++) {
                PrimeFactorization pf = new PrimeFactorization(i, primes);
                if (pf.primeFactors.size() == 1) { // i is prime
                    if (i > smallPrimes.get(smallPrimes.size() - 1)) primes.add(i);
                    array[i] = i - 1;
                    continue;
                }

                int totient = 1;
                for (int j = 0; j < pf.primeFactors.size(); j++) {
                    int    prime = pf.primeFactors.get(j);
                    int exponent = pf.exponentList.get(j);
                    totient *= prime - 1;
                    for (int k = 1; k < exponent; k++) totient *= prime;
                }
                array[i] = totient;
            }
        }
    },
    CUSTOM {
        private int[] refarray;
        private int length;
        public String getName() {
            return "Custom";
        }
        @Override
        public void selectDistribution(int[] array, ArrayVisualizer ArrayVisualizer) {
            LoadCustomDistributionDialog dialog = new LoadCustomDistributionDialog();
            File file = dialog.getFile();
            Scanner scanner;
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
                JErrorPane.invokeErrorMessage(e);
                return;
            }
            scanner.useDelimiter("\\s+");
            this.refarray = new int[ArrayVisualizer.getMaximumLength()];
            int current = 0;
            while (scanner.hasNext()) {
                this.refarray[current++] = Integer.parseInt(scanner.next());
            }
            this.length = current;
            scanner.close();
        }
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            double scale = (double)this.length / currentLen;
            for (int i = 0; i < currentLen; i++) {
                array[i] = (int)(this.refarray[(int)(i * scale)] / scale);
            }
        }
    };

    public abstract String getName();
    public void selectDistribution(int[] array, ArrayVisualizer ArrayVisualizer) {
    }
    public abstract void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer);
}