import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

public class FastCollinearPoints {
    private ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points
        if (points == null) {
            throw new NullPointerException();
        }
        Point[] copy = Arrays.copyOf(points, points.length);
        Arrays.sort(copy);
        for (int i = 0; i < copy.length - 1; i++) {
            if (copy[i].compareTo(copy[i + 1]) == 0) throw new IllegalArgumentException();
        }
        for (int i = 0; i < points.length; i++) {
            Point start = points[i];
            Arrays.sort(copy, 0, i, start.slopeOrder());
            Arrays.sort(copy, i, copy.length, start.slopeOrder());
            int j = 1;
            ArrayList<Point> list = new ArrayList<>();
            int count = 1;
            int pos = 0;
            while (j < points.length - 2) {
                if (j + i + count < points.length && copy[j + i].slopeTo(start) == copy[j + i + count].slopeTo(start)) {
                    list.add(copy[j + i + count]);
                    count++;
                } else {
                    if (count >= 3) {
                        list.add(copy[i]);
                        list.add(copy[j + i]);
                        Collections.sort(list);
                        double slope = list.get(list.size() - 1).slopeTo(list.get(0));
                        boolean exist = false;
                        for(;pos<i;pos++) {
                            if (copy[pos].slopeTo(start) >= slope) {
                                break;
                            }
                        }
                        if (pos >= i || copy[pos].slopeTo(start) > slope) {
                            if (pos > 0 && pos < i) {
                                pos--;
                            }
                            lineSegments.add(new LineSegment(list.get(0), list.get(list.size() - 1)));
                        }
                    }
                    list.clear();
                    j += count;
                    count = 1;
                }
            }
        }
    }

    public int numberOfSegments() {
        // the number of line segments
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        // the line segments
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }

    public static void main(String[] args) {

        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
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
    }
}