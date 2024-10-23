/* *****************************************************************************
 *  Name: Yun Zhu
 *  Date: September 9, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class SAP {
    private final Digraph graph;  // The input digraph

    // Constructor that takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Digraph cannot be null.");
        }
        this.graph = new Digraph(G);  // Create a copy of the digraph
    }

    // Length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return findSAP(v, w)[0];  // Return the shortest path length
    }

    // A common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return findSAP(v, w)[1];  // Return the common ancestor
    }

    // Length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("Variable can not be null.");
        }
        if (!hasValue(v) || !hasValue(w)) {
            return -1;
        }
        return findSAP(v, w)[0];  // Return the shortest path length
    }

    // A common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("Variable can not be null.");
        }
        if (!hasValue(v) || !hasValue(w)) {
            return -1;
        }
        return findSAP(v, w)[1];  // Return the common ancestor
    }

    private boolean hasValue(Iterable<Integer> iterable) {
        Iterator<Integer> it = iterable.iterator();
        return it.hasNext();
    }

    // Helper method to find the shortest ancestral path and common ancestor between two vertices
    private int[] findSAP(int v, int w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);
        return findSAP(bfsV, bfsW);
    }

    // Helper method to find the shortest ancestral path and common ancestor between two sets of vertices
    private int[] findSAP(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);
        return findSAP(bfsV, bfsW);
    }

    // Helper method to find the length and ancestor using two BFS objects
    private int[] findSAP(BreadthFirstDirectedPaths bfsV, BreadthFirstDirectedPaths bfsW) {
        int minLength = Integer.MAX_VALUE;  // Initialize to infinity
        int commonAncestor = -1;  // Initialize to -1 if no common ancestor is found

        // Check all vertices to find the shortest ancestral path
        for (int vertex = 0; vertex < graph.V(); vertex++) {
            if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
                int length = bfsV.distTo(vertex) + bfsW.distTo(vertex);
                if (length < minLength) {
                    minLength = length;
                    commonAncestor = vertex;
                }
            }
        }

        // If no ancestral path found, return -1 for both
        if (minLength == Integer.MAX_VALUE) {
            return new int[] {-1, -1};
        } else {
            return new int[] {minLength, commonAncestor};
        }
    }

    // Unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
