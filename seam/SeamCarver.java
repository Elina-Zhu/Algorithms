/* *****************************************************************************
 *  Name: Yun Zhu
 *  Date: September 10, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private int width;
    private int height;

    // Create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("Picture cannot be null.");
        this.picture = new Picture(picture);
        this.width = picture.width();
        this.height = picture.height();
    }

    // Current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // Width of the current picture
    public int width() {
        return width;
    }

    // Height of the current picture
    public int height() {
        return height;
    }

    // Energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Invalid pixel coordinates.");
        }

        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
            return 1000.0;
        }

        double deltaX = gradient(x - 1, y, x + 1, y);
        double deltaY = gradient(x, y - 1, x, y + 1);

        return Math.sqrt(deltaX + deltaY);
    }

    // Compute gradient for energy calculation
    private double gradient(int x1, int y1, int x2, int y2) {
        Color c1 = picture.get(x1, y1);
        Color c2 = picture.get(x2, y2);
        double rDiff = c1.getRed() - c2.getRed();
        double gDiff = c1.getGreen() - c2.getGreen();
        double bDiff = c1.getBlue() - c2.getBlue();
        return rDiff * rDiff + gDiff * gDiff + bDiff * bDiff;
    }

    // Sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        Picture tPicture = transposePicture(picture);
        SeamCarver tSeamCarver = new SeamCarver(tPicture);
        return tSeamCarver.findVerticalSeam();
    }

    // Sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energies = computeEnergyMatrix();
        double[][] distTo = new double[width][height];
        int[][] edgeTo = new int[width][height];

        // Initialize distTo
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (y == 0) {
                    distTo[x][y] = 0;
                }
                else {
                    distTo[x][y] = Double.POSITIVE_INFINITY;
                }
            }
        }

        // Relax the vertices in topological order
        for (int y = 1; y < height; y++) {
            for (int x = 0; x < width; x++) {
                relax(x, y, energies, distTo, edgeTo);
            }
        }

        // Find the minimum energy path to the bottom row
        double minDist = Double.POSITIVE_INFINITY;
        int minIndex = 0;
        for (int x = 0; x < width; x++) {
            if (distTo[x][height - 1] < minDist) {
                minDist = distTo[x][height - 1];
                minIndex = x;
            }
        }

        // Construct the seam
        int[] seam = new int[height];
        for (int y = height - 1; y >= 0; y--) {
            seam[y] = minIndex;
            minIndex = edgeTo[minIndex][y];
        }

        return seam;
    }

    // Relax vertices to find minimum vertical seam
    private void relax(int x, int y, double[][] energies, double[][] distTo, int[][] edgeTo) {
        for (int dx = -1; dx <= 1; dx++) {
            int prevX = x + dx;
            if (prevX >= 0 && prevX < width) {
                if (distTo[x][y] > distTo[prevX][y - 1] + energies[x][y]) {
                    distTo[x][y] = distTo[prevX][y - 1] + energies[x][y];
                    edgeTo[x][y] = prevX;
                }
            }
        }
    }

    // Compute the energy matrix
    private double[][] computeEnergyMatrix() {
        double[][] energies = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                energies[x][y] = energy(x, y);
            }
        }
        return energies;
    }

    // Remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width || height <= 1) {
            throw new IllegalArgumentException("Invalid seam");
        }
        validateSeam(seam, height);

        Picture newPicture = new Picture(width, height - 1);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < seam[x]; y++) {
                newPicture.set(x, y, picture.get(x, y));
            }
            for (int y = seam[x] + 1; y < height; y++) {
                newPicture.set(x, y - 1, picture.get(x, y));
            }
        }

        picture = newPicture;
        height--;
    }

    // Remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != height || width <= 1) {
            throw new IllegalArgumentException("Invalid seam.");
        }
        validateSeam(seam, width);

        Picture newPicture = new Picture(width - 1, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < seam[y]; x++) {
                newPicture.set(x, y, picture.get(x, y));
            }
            for (int x = seam[y] + 1; x < width; x++) {
                newPicture.set(x - 1, y, picture.get(x, y));
            }
        }

        picture = newPicture;
        width--;
    }

    // Validate the seam for correctness
    private void validateSeam(int[] seam, int length) {
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= length) {
                throw new IllegalArgumentException("Seam out of bounds.");
            }
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException(
                        "Invalid seam: adjacent elements differ by more than 1.");
            }
        }
    }

    // Transpose the picture to find horizontal seams
    private Picture transposePicture(Picture original) {
        Picture transposed = new Picture(original.height(), original.width());
        for (int x = 0; x < original.width(); x++) {
            for (int y = 0; y < original.height(); y++) {
                transposed.set(y, x, original.get(x, y));
            }
        }
        return transposed;
    }

    public static void main(String[] args) {
        // Unit testing
    }

}
