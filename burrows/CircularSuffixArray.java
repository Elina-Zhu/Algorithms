/* *****************************************************************************
 *  Name: Yun Zhu
 *  Date: September 24, 2024
 *  Description:
 **************************************************************************** */

public class CircularSuffixArray {
    private final int n;
    private final Integer[] index;
    private final String s;

    // Circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Input string cannot be null");
        }

        this.s = s;
        this.n = s.length();
        this.index = new Integer[n];

        for (int i = 0; i < n; i++) {
            index[i] = i;
        }

        sortCircularSuffixes();
    }

    private void sortCircularSuffixes() {
        java.util.Arrays.sort(index, this::compareCircularSuffixes);
    }

    private int compareCircularSuffixes(int a, int b) {
        for (int i = 0; i < n; i++) {
            char charA = s.charAt((a + i) % n);
            char charB = s.charAt((b + i) % n);
            if (charA < charB) return -1;
            if (charA > charB) return 1;
        }
        return 0;
    }

    // Length of s
    public int length() {
        return n;
    }

    // Returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= n) {
            throw new IllegalArgumentException("Index out of bounds.");
        }
        return index[i];
    }

    // Unit testing
    public static void main(String[] args) {
        String input = "ABRACADABRA!";
        CircularSuffixArray csa = new CircularSuffixArray(input);

        System.out.println("Length of input: " + csa.length());
        System.out.println("Sorted suffix indices:");
        for (int i = 0; i < csa.length(); i++) {
            System.out.println("index[" + i + "] = " + csa.index(i));
        }
    }
}
