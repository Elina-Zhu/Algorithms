/* *****************************************************************************
 *  Name: Yun Zhu
 *  Date: August 27, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final List<LineSegment> segmentsList = new ArrayList<>();

    // Finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Argument is null.");
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException("Found null point in the array.");
        }

        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);

        // check for duplicate points
        for (int i = 1; i < sortedPoints.length; i++) {
            if (sortedPoints[i].compareTo(sortedPoints[i - 1]) == 0) {
                throw new IllegalArgumentException("Detected duplicate points.");
            }
        }

        int n = sortedPoints.length;
        for (int i = 0; i < n - 3; i++) {
            for (int j = i + 1; j < n - 2; j++) {
                for (int k = j + 1; k < n - 1; k++) {
                    for (int l = k + 1; l < n; l++) {
                        Point p = sortedPoints[i];
                        Point q = sortedPoints[j];
                        Point r = sortedPoints[k];
                        Point s = sortedPoints[l];

                        if (p.slopeTo(q) == p.slopeTo(r) && p.slopeTo(r) == p.slopeTo(s)) {
                            segmentsList.add(new LineSegment(p, s));
                        }
                    }
                }
            }
        }
    }

    // The number of line segments
    public int numberOfSegments() {
        return segmentsList.size();
    }

    // The line segments
    public LineSegment[] segments() {
        return segmentsList.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
