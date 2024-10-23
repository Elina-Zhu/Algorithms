/* *****************************************************************************
 *  Name: Yun Zhu
 *  Date: August 29, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private static class Node {
        private Point2D point;
        private RectHV rect;
        private int size;
        private Node left, right;
        private boolean isVertical;

        public Node(Point2D point, RectHV rect, int size, boolean isVertical) {
            this.point = point;
            this.rect = rect;
            this.size = size;
            this.isVertical = isVertical;
        }

        public boolean isVertical() {
            return isVertical;
        }

        public RectHV leftRect() {
            if (isVertical) {
                return new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
            }
            else {
                return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
            }
        }

        public RectHV rightRect() {
            if (isVertical) {
                return new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            }
            else {
                return new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
            }
        }
    }

    private Node root;
    private double dist;
    private Point2D nearestPoint;

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) {
            return 0;
        }
        else {
            return node.size;
        }
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument to insert() is null");
        root = insert(p, root, null, true);
    }

    private Node insert(Point2D p, Node node, Node parent, boolean isLeft) {
        if (node == null) {
            if (parent == null) {
                return new Node(p, new RectHV(0, 0, 1, 1), 1, true);
            }
            else {
                if (isLeft) {
                    return new Node(p, parent.leftRect(), 1, !parent.isVertical());
                }
                else {
                    return new Node(p, parent.rightRect(), 1, !parent.isVertical());
                }
            }
        }
        if (node.point.equals(p)) return node;

        if (node.isVertical) {
            if (p.x() < node.point.x()) {
                node.left = insert(p, node.left, node, true);
            }
            else {
                node.right = insert(p, node.right, node, false);
            }
        }
        else {
            if (p.y() < node.point.y()) {
                node.left = insert(p, node.left, node, true);
            }
            else {
                node.right = insert(p, node.right, node, false);
            }
        }
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument to contains() is null");
        return contains(root, p);
    }

    private boolean contains(Node node, Point2D p) {
        if (node == null) return false;
        if (node.point.equals(p)) return true;

        if (node.isVertical()) {
            if (p.x() < node.point.x()) return contains(node.left, p);
            else return contains(node.right, p);
        }
        else {
            if (p.y() < node.point.y()) return contains(node.left, p);
            else return contains(node.right, p);
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node node) {
        if (node == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();

        StdDraw.setPenRadius();
        if (node.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
        }

        draw(node.left);
        draw(node.right);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Argument to range() is null");
        List<Point2D> pointsInRange = new ArrayList<>();
        range(root, rect, pointsInRange);
        return pointsInRange;
    }

    private void range(Node node, RectHV rect, List<Point2D> pointsInRange) {
        if (node == null) return;
        if (rect.contains(node.point)) pointsInRange.add(node.point);

        if (node.left != null && rect.intersects(node.left.rect)) {
            range(node.left, rect, pointsInRange);
        }
        if (node.right != null && rect.intersects(node.right.rect)) {
            range(node.right, rect, pointsInRange);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument to nearest() is null");
        dist = Double.POSITIVE_INFINITY;
        nearestPoint = null;
        nearest(p, root);
        return nearestPoint;
    }

    private void nearest(Point2D p, Node node) {
        if (node == null) return;
        if (p.distanceSquaredTo(node.point) < dist) {
            nearestPoint = node.point;
            dist = p.distanceSquaredTo(node.point);
        }

        boolean searchLeft = false, searchRight = false;
        if (node.left != null && node.left.rect.distanceSquaredTo(p) < dist) {
            searchLeft = true;
        }
        if (node.right != null && node.right.rect.distanceSquaredTo(p) < dist) {
            searchRight = true;
        }
        if (searchLeft && searchRight) {
            if (node.isVertical()) {
                if (p.x() < node.point.x()) {
                    nearest(p, node.left);
                    if (node.right.rect.distanceSquaredTo(p) < dist) {
                        nearest(p, node.right);
                    }
                }
                else {
                    nearest(p, node.right);
                    if (node.left.rect.distanceSquaredTo(p) < dist) {
                        nearest(p, node.left);
                    }
                }
            }
            else {
                if (p.y() < node.point.y()) {
                    nearest(p, node.left);
                    if (node.right.rect.distanceSquaredTo(p) < dist) {
                        nearest(p, node.right);
                    }
                }
                else {
                    nearest(p, node.right);
                    if (node.left.rect.distanceSquaredTo(p) < dist) {
                        nearest(p, node.left);
                    }
                }
            }
        }
        else {
            if (searchLeft) {
                nearest(p, node.left);
            }
            if (searchRight) {
                nearest(p, node.right);
            }
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}
