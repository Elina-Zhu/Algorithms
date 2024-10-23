/* *****************************************************************************
 *  Name: Yun Zhu
 *  Date: September 12, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private static final int[] SCORE_TABLE = { 0, 0, 0, 1, 1, 2, 3, 5, 11 };
    private final TrieNode root;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        root = new TrieNode();
        for (String word : dictionary) {
            addWordToTrie(word);
        }
    }

    // TrieNode for the dictionary
    private static class TrieNode {
        TrieNode[] next = new TrieNode[26];
        boolean isWord;
    }

    private void addWordToTrie(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int index = c - 'A';
            if (node.next[index] == null) {
                node.next[index] = new TrieNode();
            }
            node = node.next[index];
        }
        node.isWord = true;
    }

    private boolean isWordInTrie(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.next[c - 'A'];
            if (node == null) return false;
        }
        return node.isWord;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> validWords = new HashSet<>();
        boolean[][] visited = new boolean[board.rows()][board.cols()];
        StringBuilder prefix = new StringBuilder();

        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                dfs(board, i, j, root, visited, prefix, validWords);
            }
        }

        return validWords;
    }

    // Performs DFS to explore all possible paths from (i, j)
    private void dfs(BoggleBoard board, int i, int j, TrieNode node, boolean[][] visited,
                     StringBuilder prefix, Set<String> validWords) {
        if (visited[i][j]) return;

        char letter = board.getLetter(i, j);
        node = node.next[letter - 'A'];
        if (node != null && letter == 'Q') node = node.next['U' - 'A'];
        if (node == null) {
            return;
        }

        visited[i][j] = true;
        if (letter == 'Q') {
            prefix.append("QU");
        }
        else {
            prefix.append(letter);
        }

        if (node.isWord && prefix.length() >= 3) {
            validWords.add(prefix.toString());
        }

        // Explore all 8 possible neighbors
        for (int row = i - 1; row <= i + 1; row++) {
            for (int col = j - 1; col <= j + 1; col++) {
                if (row >= 0 && row < board.rows() && col >= 0 && col < board.cols() && !(row == i
                        && col == j)) {
                    dfs(board, row, col, node, visited, prefix, validWords);
                }
            }
        }

        visited[i][j] = false;
        if (letter == 'Q') {
            prefix.setLength(prefix.length() - 2);
        }
        else {
            prefix.setLength(prefix.length() - 1);
        }
        // return;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word.length() <= 2 || !isWordInTrie(word)) return 0;
        int len = word.length();
        if (len >= SCORE_TABLE.length) return 11;
        return SCORE_TABLE[len];
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
            // score += 1;
        }
        StdOut.println("Score = " + score);
    }
}
