package utils;

import main.ArrayVisualizer;

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
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
        }
    },
	SIMILAR {
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
			int currentLen = ArrayVisualizer.getCurrentLength();
            
			int i;
            for(i = 0; i < currentLen - 8; i++) {
                array[i] = currentLen / 2;
            }
            for(; i < currentLen; i++) {
                array[i] = (int) (Math.random() < 0.5 ? currentLen * 0.75 : currentLen * 0.25);
            }
        }
    },
	RANDOM {
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
			int[] temp = new int[currentLen];
			
            for(int i = 0; i < currentLen; i++)
				temp[i] = array[(int)(Math.random()*currentLen)];
			
			for(int i = 0; i < currentLen; i++)
				array[i] = temp[i];
        }
    },
	SQUARE {
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			
			for(int i = 0; i < currentLen; i++)
				array[i] = (int)(Math.pow(array[i], 2)/currentLen);
        }
    },
	SQRT {
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			
			for(int i = 0; i < currentLen; i++)
				array[i] = (int)(Math.sqrt(array[i])*Math.sqrt(currentLen));
        }
    },
    CUBIC {
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			int power = 3;
            double mid = (currentLen-1)/2d;
			
			for(int i = 0; i < currentLen; i++)
				array[i] = (int)(Math.pow(array[i] - mid, power)/Math.pow(mid, power-1) + mid);
        }
    },
	QUINTIC {
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			int power = 5;
            double mid = (currentLen-1)/2d;
			
			for(int i = 0; i < currentLen; i++)
				array[i] = (int)(Math.pow(array[i] - mid, power)/Math.pow(mid, power-1) + mid);
        }
    },
	PERLIN_NOISE {
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			int[] perlinNoise = new int[currentLen];
			int[] temp = new int[currentLen];

			float step = 1f / currentLen;
			float randomStart = (float) (Math.random() * currentLen);
			int octave = (int) (Math.log(currentLen) / Math.log(2));

			for(int i = 0; i < currentLen; i++) {
				int value = (int) (PerlinNoise.returnFracBrownNoise(randomStart, octave) * currentLen);
				perlinNoise[i] = value;
				randomStart += step;
			}

			int minimum = Integer.MAX_VALUE;
			for(int i = 0; i < currentLen; i++) {
				if(perlinNoise[i] < minimum) {
					minimum = perlinNoise[i];
				}
			}
			minimum = Math.abs(minimum);
			for(int i = 0; i < currentLen; i++) {
				perlinNoise[i] += minimum;
			}

			double maximum = Double.MIN_VALUE;
			for(int i = 0; i < currentLen; i++) {
				if(perlinNoise[i] > maximum) {
					maximum = perlinNoise[i];
				}
			}
			double scale = currentLen / maximum;
			if(scale < 1.0 || scale > 1.8) {
				for(int i = 0; i < currentLen; i++) {
					perlinNoise[i] = (int) (perlinNoise[i] * scale);
				}
			}

			for(int i = 0; i < currentLen; i++) {
				temp[i] = array[i];
			}
			for(int i = 0; i < currentLen; i++) {
				array[i] = temp[Math.min(perlinNoise[i], currentLen-1)];
			}
        }
    },
	PERLIN_NOISE_CURVE {
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			
			int[] temp = new int[currentLen];
			for(int i = 0; i < currentLen; i++) {
				temp[i] = array[i];
			}
			
			for(int i = 0; i < currentLen; i++) {
				int value = 0 - (int) (PerlinNoise.returnNoise((float) array[i] / currentLen) * currentLen);
				array[i] = temp[Math.min(value, currentLen-1)];
			}
        }
    },
	BELL_CURVE {
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			double step = 8d / currentLen;
			double position = -4;
			int constant = 1264;
			double factor = currentLen / 512d;
			
			int[] temp = new int[currentLen];
			for(int i = 0; i < currentLen; i++) {
				temp[i] = array[i];
			}
			
			for(int i = 0; i < currentLen; i++) {
				double square = Math.pow(position, 2);
				double negativeSquare = 0 - square;
				double halfNegSquare = negativeSquare / 2d;
				double numerator = constant * factor * Math.pow(Math.E, halfNegSquare);
				
				double doublePi = 2 * Math.PI;
				double denominator = Math.sqrt(doublePi);
				
				array[i] = temp[Math.min((int) (numerator / denominator), currentLen-1)];
				position += step;
			}
        }
    },
	RULER {
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			int step = Math.max(1, currentLen/256);
			int floorLog2 = (int)(Math.log(currentLen/step)/Math.log(2));
			int lowest;
			for(lowest = step; 2*lowest <= currentLen/4; lowest*=2);
			boolean[] digits = new boolean[floorLog2+2];

			int i, j;
			for(i = 0; i+step <= currentLen; i+=step) {
				for(j = 0; digits[j]; j++);
				digits[j] = true;
				
				for(int k = 0; k < step; k++) {
					int value = currentLen/2 - Math.min((1 << j)*step, lowest);
					array[i+k] = value;
				}
				
				for(int k = 0; k < j; k++) digits[k] = false;
			}

			for(j = 0; digits[j]; j++);
			digits[j] = true;
			while(i < currentLen) {
				int value = Math.max(currentLen/2 - (1 << j)*step, currentLen/4);
				array[i++] = value;
			}
        }
    },
	BLANCMANGE {
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			int floorLog2 = (int)(Math.log(currentLen)/Math.log(2));
			
			int[] temp = new int[currentLen];
			for(int i = 0; i < currentLen; i++) {
				temp[i] = array[i];
			}
			
			for(int i = 0; i < currentLen; i++) {
				int value = (int)(currentLen * curveSum(floorLog2, (double)i/currentLen));
				array[i] = temp[value];
			}
        }
		
		public double curveSum(int n, double x) {
			double sum = 0;
			while(n >= 0)
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
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			
			int[] temp = new int[currentLen];
			for(int i = 0; i < currentLen; i++) {
				temp[i] = array[i];
			}
			cantor(array, temp, 0, currentLen, 0, currentLen-1);
        }
		
		public void cantor(int[] array, int[] temp, int a, int b, int min, int max) {
			if(b-a < 1 || max == min) return;
			
			int mid = (min+max)/2;
			if(b-a == 1) {
				array[a] = temp[mid];
				return;
			}
			
			int t1 = (a+a+b)/3, t2 = (a+b+b+2)/3;
			
			for(int i = t1; i < t2; i++)
				array[i] = temp[mid];
			
			this.cantor(array, temp, a, t1, min, mid);
			this.cantor(array, temp, t2, b, mid+1, max);
		}
    },
	DIVISORS {//O(n^1.5)
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			int[] n = new int[currentLen];
			int[] temp = new int[currentLen];
			
			n[0] = 0;
			n[1] = 1;
			double max = 1;
			
			for(int i = 2; i < currentLen; i++) {
				n[i] = sumDivisors(i);
				if(n[i] > max) max = n[i];
			}
			
			for(int i = 0; i < currentLen; i++) {
				temp[i] = array[i];
			}
			
			double scale = Math.min((currentLen-1)/max, 1);
			for(int i = 0; i < currentLen; i++) {
				array[i] = temp[(int)(n[i]*scale)];
			}
        }
		
		public int sumDivisors(int n) {
			int sum = n+1;
			for(int i = 2; i <= (int)Math.sqrt(n); i++) {
				if(n % i == 0) {
					if(i == n/i) sum += i;
					else         sum += i + n/i;
				}
			}
			return sum;
		}
    },
	FSD {// fly straight dangit (OEIS A133058)
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
			int currentLen = ArrayVisualizer.getCurrentLength();
			int[] fsd = new int[currentLen];
			int[] temp = new int[currentLen];
			
			double max;
			max = fsd[0] = fsd[1] = 1;
			for(int i = 2; i < currentLen; i++) {
				int g = gcd(fsd[i-1], i);
				fsd[i] = fsd[i-1]/g + (g==1 ? i+1 : 0);
				if(fsd[i] > max) max = fsd[i];
			}
			
			for(int i = 0; i < currentLen; i++)
				temp[i] = array[i];
			
			double scale = Math.min((currentLen-1)/max, 1);
			for(int i = 0; i < currentLen; i++)
				array[i] = temp[(int)(fsd[i]*scale)];
        }
		
		public int gcd(int a, int b) {
			if (b==0) return a;
			return gcd(b,a%b);
		}
    },
	REVLOG {
        @Override
        public void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer) {
            int currentLen = ArrayVisualizer.getCurrentLength();
			
			int[] temp = new int[currentLen];
			for(int i = 0; i < currentLen; i++) {
				temp[i] = array[i];
			}
			
            for(int i = 0; i < currentLen; i++){
                int random = (int) (Math.random() * (currentLen - i)) + i;
                array[i] = temp[random];
            }
        }
    };
	
    public abstract void initializeArray(int[] array, ArrayVisualizer ArrayVisualizer);
}