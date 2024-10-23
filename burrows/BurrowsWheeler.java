/* *****************************************************************************
 *  Name: Yun Zhu
 *  Date: September 24, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;

public class BurrowsWheeler {

    // Apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int n = csa.length();
        char[] t = new char[n];

        for (int i = 0; i < n; i++) {
            int index = csa.index(i);
            if (index == 0) {
                BinaryStdOut.write(i);
            }
            t[i] = s.charAt((index - 1 + n) % n);
        }

        for (char c : t) {
            BinaryStdOut.write(c);
        }
        BinaryStdOut.close();
    }

    // Apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        int n = t.length();

        int[] next = new int[n];
        char[] firstColumn = t.toCharArray();
        Arrays.sort(firstColumn);

        int[] count = new int[256];
        for (int i = 0; i < n; i++) {
            count[t.charAt(i)]++;
        }
        for (int r = 1; r < 256; r++) {
            count[r] += count[r - 1];
        }
        for (int i = n - 1; i >= 0; i--) {
            next[--count[t.charAt(i)]] = i;
        }

        for (int i = 0, row = first; i < n; i++) {
            BinaryStdOut.write(firstColumn[row]);
            row = next[row];
        }
        BinaryStdOut.close();
    }

    // If args[0] is "-", apply Burrows-Wheeler transform
    // If args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException(
                    "Expected '-' for transform or '+' for inverse transform.");
        }
        if (args[0].equals("-")) {
            transform();
        }
        else if (args[0].equals("+")) {
            inverseTransform();
        }
        else {
            throw new IllegalArgumentException(
                    "Expected '-' for transform or '+' for inverse transform.");
        }
    }
}
