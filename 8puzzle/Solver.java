/* *****************************************************************************
 *  Name: Yun Zhu
 *  Date: August 30, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {
    private final boolean solvable;
    private final int moves;
    private final List<Board> solution;

    // Search node class to keep track of board, number of moves and previous node
    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final int priority;
        private final SearchNode previous;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            this.priority = board.manhattan() + moves;
        }

        @Override
        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority, that.priority);
        }
    }

    // Constructor to find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Initial board cannot be null");
        }

        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> twinPQ = new MinPQ<>();

        pq.insert(new SearchNode(initial, 0, null));
        twinPQ.insert(new SearchNode(initial.twin(), 0, null));

        SearchNode solutionNode = null;

        while (solutionNode == null && processNextNode(twinPQ) == null) {
            solutionNode = processNextNode(pq);
        }

        if (solutionNode == null) {
            this.solvable = false;
            this.moves = -1;
            this.solution = null;
        }
        else {
            this.solvable = true;
            this.moves = solutionNode.moves;
            this.solution = new ArrayList<>();
            while (solutionNode != null) {
                solution.add(solutionNode.board);
                solutionNode = solutionNode.previous;
            }
            Collections.reverse(solution);
        }
    }

    // Helper method to process the next node in the priority queue
    private SearchNode processNextNode(MinPQ<SearchNode> pq) {
        if (pq.isEmpty()) return null;

        SearchNode node = pq.delMin();
        if (node.board.isGoal()) return node;

        for (Board neighbor : node.board.neighbors()) {
            if (node.previous == null || !neighbor.equals(node.previous.board)) {
                pq.insert(new SearchNode(neighbor, node.moves + 1, node));
            }
        }

        return null;
    }

    // Is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // Min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // Sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    // Test client
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
