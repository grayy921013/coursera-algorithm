import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * Created by Zhehui Zhou on 4/24/16.
 */
public class CircularSuffixArray {
    private Integer[] index;
    private int[] aux;
    private String s;
    private static final int R = 256;
    private static final int CUTOFF = 15;


    public CircularSuffixArray(String s) {
        // circular suffix array of s
        if (s == null) throw new NullPointerException();
        this.s = s;
        index = new Integer[s.length()];
        aux = new int[s.length()];
        for (int i = 0; i < s.length(); i++) index[i] = i;
        sort(0, index.length - 1, 0);

    }

    private char charAt(int i, int d) {
        return s.charAt((i + d) % s.length());
    }

    private void swap(int i, int j) {
        int temp = index[i];
        index[i] = index[j];
        index[j] = temp;
    }

    private void insertionSort(int start, int end, int d) {
        //use system sort if the range is short
        Arrays.sort(index, start, end + 1, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                for (int i = d; i < s.length(); i++) {
                    char c1 = charAt(o1, i);
                    char c2 = charAt(o2, i);
                    if (c1 != c2) return c1 - c2;
                }
                return 0;
            }
        });
    }

    private void sort(int start, int end, int d) {
        //sort similar to MSD radix sort, sort based on dth character
        if (start >= end || d >= s.length()) return;
        if (end <= start + CUTOFF) {
            insertionSort(start, end, d);
            return;
        }
        int[] count = new int[R + 1];
        for (int i = start; i <= end; i++) count[charAt(index[i], d) + 1]++;
        for (int i = 1; i <= R; i++) count[i] += count[i - 1];
        for (int i = start; i <= end; i++) {
            int c = charAt(index[i], d);
            aux[start + count[c]++] = index[i];
        }
        for (int i = start; i <= end; i++) index[i] = aux[i];
        sort(start, start + count[0] - 1, d + 1);
        for (int i = 0; i < R; i++) sort(start + count[i], start + count[i + 1] - 1, d + 1);
    }

    public int length() {
        // length of s
        return index.length;
    }

    public int index(int i) {
        // returns index of ith sorted suffix
        if (i < 0 || i >= length()) throw new IndexOutOfBoundsException();
        return index[i];
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
        CircularSuffixArray array = new CircularSuffixArray(BinaryStdIn.readString());
        System.out.println(Arrays.toString(array.index));
    }
}
