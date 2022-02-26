package io.github.arrayv.visuals;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Renderer;

/*
 *
MIT License

Copyright (c) 2019 w0rthy

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

public enum VisualStyles {
    BARS {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
            arrayVisualizer.getVisuals()[0].drawVisual(array, arrayVisualizer, renderer, Highlights);
        }
    },
    RAINBOW {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
            arrayVisualizer.getVisuals()[1].drawVisual(array, arrayVisualizer, renderer, Highlights);
        }
    },
    DISP_BARS {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
            arrayVisualizer.getVisuals()[2].drawVisual(array, arrayVisualizer, renderer, Highlights);
        }
    },
    COLOR_CIRCLE {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
            arrayVisualizer.getVisuals()[3].drawVisual(array, arrayVisualizer, renderer, Highlights);
        }
    },
    DISP_CIRCLE {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
            arrayVisualizer.getVisuals()[4].drawVisual(array, arrayVisualizer, renderer, Highlights);
        }
    },
    DISP_CHORDS {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
            arrayVisualizer.getVisuals()[5].drawVisual(array, arrayVisualizer, renderer, Highlights);
        }
    },
    DISP_DOTS {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
            arrayVisualizer.getVisuals()[6].drawVisual(array, arrayVisualizer, renderer, Highlights);
        }
    },
    DOTS {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
            arrayVisualizer.getVisuals()[7].drawVisual(array, arrayVisualizer, renderer, Highlights);
        }
    },
    WAVE_DOTS {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
            arrayVisualizer.getVisuals()[8].drawVisual(array, arrayVisualizer, renderer, Highlights);
        }
    },
    CUSTOM_IMAGE {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
            arrayVisualizer.getVisuals()[9].drawVisual(array, arrayVisualizer, renderer, Highlights);
        }
    },
    SINE_WAVE {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
            arrayVisualizer.getVisuals()[10].drawVisual(array, arrayVisualizer, renderer, Highlights);
        }
    },
    HOOP_STACK {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
            arrayVisualizer.getVisuals()[11].drawVisual(array, arrayVisualizer, renderer, Highlights);
        }
    },
    PIXEL_MESH {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
            arrayVisualizer.getVisuals()[12].drawVisual(array, arrayVisualizer, renderer, Highlights);
        }
    },
    SPIRAL {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
            arrayVisualizer.getVisuals()[13].drawVisual(array, arrayVisualizer, renderer, Highlights);
        }
    },
    SPIRAL_DOTS {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
            arrayVisualizer.getVisuals()[14].drawVisual(array, arrayVisualizer, renderer, Highlights);
        }
    };

    public VisualStyles getCurrentVisual() {
        return this;
    }

    public abstract void drawVisual(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights);
}
