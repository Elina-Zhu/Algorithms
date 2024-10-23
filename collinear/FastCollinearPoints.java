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

public class FastCollinearPoints {
    private final List<LineSegment> segmentsList = new ArrayList<>();

    // Finds all line segments containing 4 points
    public FastCollinearPoints(Point[] points) {
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
        for (int i = 0; i < n; i++) {
            Point origin = sortedPoints[i];
            Point[] sortedBySlope = sortedPoints.clone();

            // sort by slope relative to origin
            Arrays.sort(sortedBySlope, origin.slopeOrder());

            int count = 0;
            for (int j = 1; j < n; j++) {
                if (j == n - 1 || origin.slopeTo(sortedBySlope[j]) != origin.slopeTo(
                        sortedBySlope[j + 1])) {
                    if (count >= 2) {
                        // Found a segment of 4 or more points
                        Point start = sortedBySlope[j - count];
                        Point end = sortedBySlope[j];
                        if (origin.compareTo(start) < 0) {
                            segmentsList.add(new LineSegment(origin, end));
                        }
                    }
                    count = 0;
                }
                else {
                    count++;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
