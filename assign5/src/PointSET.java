import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by admin on 2/23/16.
 */
public class PointSET {
    private TreeSet<Point2D> pointSet;
    public PointSET() {
        // construct an empty set of points
        pointSet = new TreeSet<>();
    }

    private void checkNull(Object o) {
        if (o == null) throw new NullPointerException();
    }
    public boolean isEmpty() {
        // is the set empty?
        return pointSet.isEmpty();
    }

    public int size() {
        // number of points in the set
        return pointSet.size();
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        checkNull(p);
        pointSet.add(p);
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        checkNull(p);
        return pointSet.contains(p);
    }

    public void draw() {
        // draw all points to standard draw
        for (Point2D point2D : pointSet) {
            point2D.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle
        checkNull(rect);
        ArrayList<Point2D> list = new ArrayList<>();
        for (Point2D point2D : pointSet) {
            if (rect.contains(point2D)) list.add(point2D);
        }
        return list;
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        checkNull(p);
        double dist = Double.POSITIVE_INFINITY;
        Point2D nearest = null;
        for (Point2D point2D : pointSet) {
            if (point2D.distanceTo(p) < dist) {
                nearest = point2D;
                dist = point2D.distanceTo(p);
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
        PointSET pointSET = new PointSET();
        pointSET.insert(new Point2D(0,0));
        pointSET.insert(new Point2D(0,1));
        pointSET.insert(new Point2D(0,0));
        pointSET.insert(new Point2D(0,2));
        pointSET.draw();
    }
}
