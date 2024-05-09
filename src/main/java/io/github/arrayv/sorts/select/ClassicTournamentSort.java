package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2021 aphitorite

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
@SortMeta(name = "Classic Tournament")
public final class ClassicTournamentSort extends Sort {
	public ClassicTournamentSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private int[] array;
	private int[] tmp;
	private int[] tree;

	private int size;

	private int ceilPow2(int n) {
		int r = 1;
		while (r < n)
			r *= 2;
		return r;
	}

	private boolean treeCompare(int a, int b) {
		return Reads.compareIndices(this.array, this.tree[a], this.tree[b], 1, true) <= 0;
	}

	private void buildTree(int n) {
		this.size = this.ceilPow2(n) - 1;
		int mod = n & 1;
		int treeSize = n + this.size + mod;

		this.tree = Writes.createExternalArray(treeSize);

		for (int i = 0; i < treeSize; i++)
			Writes.write(this.tree, i, -1, 0, false, true);

		for (int i = this.size; i < treeSize - mod; i++) {
			Highlights.markArray(1, i - this.size);
			Writes.write(this.tree, i, i - this.size, 0.5, false, true);
		}

		for (int i, j = this.size, k = treeSize - mod; j > 0; j /= 2, k /= 2) {
			for (i = j; i + 1 < k; i += 2) {
				int val = this.treeCompare(i, i + 1) ? this.tree[i] : this.tree[i + 1];

				Writes.write(this.tree, i / 2, val, 0, false, true);
			}
			if (i < k)
				Writes.write(this.tree, i / 2, this.tree[i], 0, false, true);
		}
	}

	private int peek() {
		return this.array[this.tree[0]];
	}

	private int findNext() {
		int root = this.tree[0] + this.size;

		for (int i = root; i > 0; i = (i - 1) / 2)
			Writes.write(this.tree, i, -1, 0, false, true);

		for (int i = root; i > 0;) {
			int j = i + ((i & 1) << 1) - 1;

			int c1 = this.tree[i] >> 31;
			int c2 = this.tree[j] >> 31;

			int nVal = (c1 & ((c2 & -1) + (~c2 & this.tree[j]))) + (~c1 & ((c2 & this.tree[i]) + (~c2 & -2)));

			if (nVal == -2) {
				if (i < j)
					nVal = this.treeCompare(i, j) ? this.tree[i] : this.tree[j];
				else
					nVal = this.treeCompare(j, i) ? this.tree[j] : this.tree[i];
			}

			i = (i - 1) / 2;
			if (nVal != -1)
				Writes.write(this.tree, i, nVal, 0, false, true);
		}

		return this.peek();
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		this.array = array;

		this.buildTree(length);
		this.tmp = Writes.createExternalArray(length);

		Highlights.markArray(3, 0);
		Writes.write(this.tmp, 0, this.peek(), 1, false, true);

		for (int i = 1; i < length; i++) {
			int val = this.findNext();

			Highlights.markArray(3, i);
			Writes.write(this.tmp, i, val, 1, false, true);
		}
		Highlights.clearAllMarks();
		Writes.arraycopy(this.tmp, 0, array, 0, length, 1, true, false);

		Writes.deleteExternalArray(this.tree);
		Writes.deleteExternalArray(this.tmp);
	}
}
