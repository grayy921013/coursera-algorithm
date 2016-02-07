import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

/**
 * Created by admin on 2/6/16.
 */
public class Point implements Comparable<Point> {
    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Point o) {
        if (y != o.y) {
            return y - o.y;
        }
        return x - o.x;
    }

    public double slopeTo(Point that) {
        // the slope between this point and that point
        if (that.y == y && that.x != x) return 0;
        if (that.x == x && that.y != y) return Double.POSITIVE_INFINITY;
        if (that.x == x && that.y == y) return Double.NEGATIVE_INFINITY;
        return (double)(that.y - y)/(that.x - x);
    }

    public Comparator<Point> slopeOrder() {
        // compare two points by slopes they make with this point
        return new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                if (slopeTo(o1) < slopeTo(o2)) {
                    return -1;
                } else if (slopeTo(o1) == slopeTo(o2)) {
                    return 0;
                } else {
                    return 1;
                }
            }
        };
    }
    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
    }
}
