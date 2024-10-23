/* *****************************************************************************
 *  Name: Yun Zhu
 *  Date: September 24, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // Apply move-to-front encoding,
    // reading from standard input and writing to standard output
    public static void encode() {
        char[] sequence = new char[R];
        for (int i = 0; i < R; i++) {
            sequence[i] = (char) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = 0;

            for (int i = 0; i < R; i++) {
                if (sequence[i] == c) {
                    index = i;
                    break;
                }
            }

            BinaryStdOut.write((char) index);

            for (int i = index; i > 0; i--) {
                sequence[i] = sequence[i - 1];
            }
            sequence[0] = c;
        }

        BinaryStdOut.close();
    }

    // Apply move-to-front decoding,
    // reading from standard input and writing to standard output
    public static void decode() {
        char[] sequence = new char[R];
        for (int i = 0; i < R; i++) {
            sequence[i] = (char) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar();
            char c = sequence[index];

            BinaryStdOut.write(c);

            for (int i = index; i > 0; i--) {
                sequence[i] = sequence[i - 1];
            }
            sequence[0] = c;
        }

        BinaryStdOut.close();
    }

    // If args[0] is "-", apply move-to-front encoding
    // If args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Expected '+' for decoding or '-' for encoding.");
        }
        if (args[0].equals("-")) {
            encode();
        }
        else if (args[0].equals("+")) {
            decode();
        }
        else {
            throw new IllegalArgumentException("Expected '+' for decoding or '-' for encoding.");
        }
    }
}
