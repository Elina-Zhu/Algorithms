/* *****************************************************************************
 *  Name:              Yun Zhu
 *  Coursera User ID:
 *  Last modified:     August 12, 2024
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String champion = null;
        int i = 0;

        while (!StdIn.isEmpty()) {
            String currentWord = StdIn.readString();
            i++;
            if (StdRandom.bernoulli(1.0 / i)) {
                champion = currentWord;
            }
        }

        if (champion != null) {
            StdOut.println(champion);
        }
    }
}
