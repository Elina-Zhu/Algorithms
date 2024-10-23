/* *****************************************************************************
 *  Name: Yun Zhu
 *  Date: September 9, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordNet {
    private final Map<Integer, String> idToSynset;
    private final Map<String, Set<Integer>> nounToIds;
    private final Digraph wordNetGraph;
    private final SAP sapHelper;

    // Constructor that takes the names of synsets and hypernyms input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }

        idToSynset = new HashMap<>();
        nounToIds = new HashMap<>();
        readSynsets(synsets);

        wordNetGraph = new Digraph(idToSynset.size());
        readHypernyms(hypernyms);

        validateRootedDAG();

        sapHelper = new SAP(wordNetGraph);
    }

    // Parses the synsets file and fills idToSynset and nounToIds mappings
    private void readSynsets(String synsets) {
        In in = new In(synsets);
        while (in.hasNextLine()) {
            String[] fields = in.readLine().split(",");
            int id = Integer.parseInt(fields[0]);
            String synset = fields[1];
            idToSynset.put(id, synset);

            for (String noun : synset.split(" ")) {
                nounToIds.computeIfAbsent(noun, value -> new HashSet<>()).add(id);
            }
        }
    }

    // Parses the hypernyms file and constructs the WordNet graph
    private void readHypernyms(String hypernyms) {
        In in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] fields = in.readLine().split(",");
            int synsetId = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int hypernymId = Integer.parseInt(fields[i]);
                wordNetGraph.addEdge(synsetId, hypernymId);
            }
        }
    }

    // Validates that the digraph has a single root
    // i.e. check it there's exactly one vertex with no outgoing edges
    private void validateRootedDAG() {
        int roots = 0;
        for (int v = 0; v < wordNetGraph.V(); v++) {
            if (wordNetGraph.outdegree(v) == 0) {
                roots++;
            }
        }

        if (roots != 1) {
            throw new IllegalArgumentException("The input does not form a rooted DAG.");
        }
    }

    // Returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToIds.keySet();
    }

    // Is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Argument cannot be null.");
        }
        return nounToIds.containsKey(word);
    }

    // Distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Invalid noun(s).");
        }
        Set<Integer> idsA = nounToIds.get(nounA);
        Set<Integer> idsB = nounToIds.get(nounB);
        return sapHelper.length(idsA, idsB);
    }

    // A synset that is the common ancestor of nounA and nounB in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Invalid noun(s).");
        }
        Set<Integer> idsA = nounToIds.get(nounA);
        Set<Integer> idsB = nounToIds.get(nounB);
        int ancestorId = sapHelper.ancestor(idsA, idsB);
        return idToSynset.get(ancestorId);
    }

    public static void main(String[] args) {
        // Unit testing
    }
}
