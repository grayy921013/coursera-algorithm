import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by admin on 2/6/16.
 */
public class BruteCollinearPoints {
    private ArrayList<LineSegment> lineSegments;
    public BruteCollinearPoints(Point[] points)   {
        // finds all line segments containing 4 points
        if (points == null) {
            throw new NullPointerException();
        }
        points = Arrays.copyOf(points, points.length);
        Arrays.sort(points);
        for (int i = 0;i<points.length-1;i++) {
            if (points[i].compareTo(points[i+1]) == 0) throw new IllegalArgumentException();
        }
        int num = 0;
        lineSegments = new ArrayList<>();
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        if (points[j].slopeTo(points[i]) == points[k].slopeTo(points[i]) && points[k].slopeTo(points[i]) == points[l].slopeTo(points[i])) {
                            lineSegments.add(new LineSegment(points[i], points[l]));
                        }
                    }
                }
            }
        }
    }
    public           int numberOfSegments()  {
        // the number of line segments
        return lineSegments.size();
    }
    public LineSegment[] segments()       {
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}
