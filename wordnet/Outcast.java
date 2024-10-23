/* *****************************************************************************
 *  Name: Yun Zhu
 *  Date: September 9, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordNet;

    // Constructor takes a WordNet object
    public Outcast(WordNet wordNet) {
        if (wordNet == null) {
            throw new IllegalArgumentException("WordNet object cannot be null.");
        }
        this.wordNet = wordNet;
    }

    // Given an array of WordNet nouns, return the outcast
    public String outcast(String[] nouns) {
        if (nouns == null || nouns.length < 2) {
            throw new IllegalArgumentException("Nouns array must contain at least two nouns.");
        }

        String outcast = null;
        int maxDistanceSum = -1;

        // Calculate the sum of distances for each noun
        for (String nounA : nouns) {
            int distanceSum = 0;
            for (String nounB : nouns) {
                if (!nounA.equals(nounB)) {
                    distanceSum += wordNet.distance(nounA, nounB);
                }
            }

            // If the sum of distances is greater than the current max, update the outcast
            if (distanceSum > maxDistanceSum) {
                maxDistanceSum = distanceSum;
                outcast = nounA;
            }
        }

        return outcast;
    }

    // Test client for the Outcast class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
