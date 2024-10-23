/* *****************************************************************************
 *  Name:              Yun Zhu
 *  Coursera User ID:
 *  Last modified:     August 16, 2024
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufForFull;
    private final boolean[] openSites;
    private int openSiteCount;
    private final int topVirtualSite;
    private final int bottomVirtualSite;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("N must be positive.");
        this.n = n;
        this.uf = new WeightedQuickUnionUF(n * n + 2);
        this.ufForFull = new WeightedQuickUnionUF(n * n + 1);
        this.openSites = new boolean[n * n];
        this.openSiteCount = 0;
        this.topVirtualSite = n * n;
        this.bottomVirtualSite = n * n + 1;
    }

    // convert 2D coordinate to 1D
    private int xyTo1D(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException("Row and col must be between 1 and n.");
        }
        return (row - 1) * n + (col - 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int index = xyTo1D(row, col);
        if (!openSites[index]) {
            openSites[index] = true;
            openSiteCount++;
            if (row == 1) {
                uf.union(index, topVirtualSite);
                ufForFull.union(index, topVirtualSite);
            }
            if (row == n) uf.union(index, bottomVirtualSite);
            connectIfOpen(row, col, row - 1, col);
            connectIfOpen(row, col, row + 1, col);
            connectIfOpen(row, col, row, col - 1);
            connectIfOpen(row, col, row, col + 1);
        }
    }

    // connect point 1 and point 2 if point 2 is open
    private void connectIfOpen(int row1, int col1, int row2, int col2) {
        if (row2 >= 1 && row2 <= n && col2 >= 1 && col2 <= n && isOpen(row2, col2)) {
            uf.union(xyTo1D(row1, col1), xyTo1D(row2, col2));
            ufForFull.union(xyTo1D(row1, col1), xyTo1D(row2, col2));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return openSites[xyTo1D(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return ufForFull.find(xyTo1D(row, col)) == ufForFull.find(topVirtualSite);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSiteCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(topVirtualSite) == uf.find(bottomVirtualSite);
    }

    // test client (optional)
    public static void main(String[] args) {
    }
}
