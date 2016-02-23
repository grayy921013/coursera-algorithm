import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by admin on 2/23/16.
 */
public class KdTree {
    private Node root;
    private int size;

    private static class Node {
        public Node left;
        public Node right;
        public RectHV rect;
        public Point2D point2D;

        public Node(Point2D point2D, RectHV rect) {
            this.rect = rect;
            this.point2D = point2D;
        }

        public double x() {
            return point2D.x();
        }

        public double y() {
            return point2D.y();
        }
    }

    public KdTree() {
        // construct an empty set of points
        root = null;
        size = 0;
    }

    private void checkNull(Object o) {
        if (o == null) throw new NullPointerException();
    }

    public boolean isEmpty() {
        // is the set empty?
        return root == null;
    }

    public int size() {
        // number of points in the set
        return size;
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        checkNull(p);
        root = insert(root, p, true, null, true);
    }

    private Node insert(Node node, Point2D p, boolean splitUseX, Node parent, boolean isLeft) {
        if (node == null) {
            size++;
            if (parent == null) {
                return new Node(p, new RectHV(0, 0, 1, 1));
            }
            if (isLeft) {
                return new Node(p, getLeftRect(parent,!splitUseX));
            } else {
                return new Node(p, getRightRect(parent,!splitUseX));
            }
        }
        if (node.point2D.equals(p)) return node;
        if (splitUseX ? p.x() < node.x() : p.y() < node.y()) node.left =  insert(node.left, p, !splitUseX, node, true);
        else node.right = insert(node.right, p, !splitUseX, node, false);
        return node;
    }

    private Node search(Node node, Point2D p, boolean splitUseX) {
        if (node == null) return null;
        if (node.point2D.equals(p)) return node;
        if (splitUseX ? p.x() < node.x() : p.y() < node.y()) return search(node.left, p, !splitUseX);
        else return search(node.right, p, !splitUseX);
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        checkNull(p);
        return search(root, p, true) != null;
    }

    private void rangeSearch(ArrayList<Point2D> list, Node node, RectHV rect, boolean splitUseX) {
        if (node == null) return;
        if (rect.contains(node.point2D)) list.add(node.point2D);
        if (splitUseX) {
            if (rect.xmin() < node.point2D.x()) rangeSearch(list, node.left, rect, false);
            if (rect.xmax() >= node.point2D.x()) rangeSearch(list, node.right, rect, false);
        } else {
            if (rect.ymin() < node.point2D.y()) rangeSearch(list, node.left, rect, true);
            if (rect.ymax() >= node.point2D.y()) rangeSearch(list, node.right, rect, true);
        }
    }

    private RectHV getLeftRect(Node node, boolean splitUseX) {
        RectHV rect = node.rect;
        if (splitUseX) {
            return new RectHV(rect.xmin(), rect.ymin(), node.x(), rect.ymax());
        } else {
            return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.y());
        }
    }

    private RectHV getRightRect(Node node, boolean splitUseX) {
        RectHV rect = node.rect;
        if (splitUseX) {
            return new RectHV(node.x(), rect.ymin(), rect.xmax(), rect.ymax());
        } else {
            return new RectHV(rect.xmin(), node.y(), rect.xmax(), rect.ymax());
        }
    }

    private void drawPoints(Node node, boolean splitUseX) {
        if (node == null) return;
        RectHV rect = node.rect;
        StdDraw.setPenColor(Color.BLACK);
        node.point2D.draw();
        StdDraw.setPenColor(splitUseX ? Color.RED : Color.BLUE);
        if (splitUseX) StdDraw.line(node.x(), rect.ymin(), node.x(), rect.ymax());
        else StdDraw.line(rect.xmin(), node.y(), rect.xmax(), node.y());
        drawPoints(node.left, !splitUseX);
        drawPoints(node.right, !splitUseX);
    }

    public void draw() {
        // draw all points to standard draw
        drawPoints(root, true);
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle
        checkNull(rect);
        ArrayList<Point2D> list = new ArrayList<>();
        rangeSearch(list, root, rect, true);
        return list;
    }

    private Point2D findNearest(Node node, Point2D p, boolean splitUseX, Point2D closestNow, double dist) {
        if (node == null) return closestNow;
        if (closestNow == null) {
            closestNow = node.point2D;
            dist = closestNow.distanceSquaredTo(p);
        } else {
            double distThis = node.point2D.distanceSquaredTo(p);
            if (distThis < dist) {
                dist = distThis;
                closestNow = node.point2D;
            }
        }
        RectHV rectHV = node.rect;
        if (rectHV.distanceSquaredTo(p) > dist) return closestNow;
        Point2D point;
        if (splitUseX ? p.x() < node.x() : p.y() < node.y()) {
            point = findNearest(node.left, p, !splitUseX, closestNow, dist);
            if (!point.equals(closestNow)) {
                closestNow = point;
                dist = closestNow.distanceSquaredTo(p);
            }
            return findNearest(node.right, p, !splitUseX, closestNow, dist);
        } else {
            point = findNearest(node.right, p, !splitUseX, closestNow, dist);
            if (!point.equals(closestNow)) {
                closestNow = point;
                dist = closestNow.distanceSquaredTo(p);
            }
            return findNearest(node.left, p, !splitUseX, closestNow, dist);
        }
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        checkNull(p);
        return findNearest(root, p, true, null, 0);
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
        KdTree pointSET = new KdTree();
        pointSET.insert(new Point2D(0, 0));
        pointSET.insert(new Point2D(0, 1));
        pointSET.insert(new Point2D(0, 0));
        pointSET.insert(new Point2D(0, 2));
        System.out.println(pointSET.nearest(new Point2D(4,4)));
        System.out.println(pointSET.range(new RectHV(0, 0, 0.2, 0.2)));
    }
}
