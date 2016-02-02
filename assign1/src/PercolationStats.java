import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int T;
    private double[] percenatge;

    public PercolationStats(int N, int T) {
        // perform T independent experiments on an N-by-N grid
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        this.T = T;
        this.percenatge = new double[T];
        for (int i = 0; i < T; i++) {
            int count = 0;
            Percolation percolation = new Percolation(N);
            while (!percolation.percolates()) {
                int x, y;
                do {
                    x = StdRandom.uniform(N) + 1;
                    y = StdRandom.uniform(N) + 1;
                } while (percolation.isOpen(x, y));
                percolation.open(x, y);
                count++;
            }
            percenatge[i] = (double) count / N / N;
        }
    }

    public double mean() {
        // sample mean of percolation threshold
        return StdStats.mean(percenatge);
    }

    public double stddev() {
        // sample standard deviation of percolation threshold
        return StdStats.stddev(percenatge);
    }

    public double confidenceLo() {
        // low  endpoint of 95% confidence interval
        return StdStats.mean(percenatge) - 1.96 * StdStats.stddev(percenatge) / Math.sqrt(T);
    }

    public double confidenceHi() {
        // high endpoint of 95% confidence interval
        return StdStats.mean(percenatge) + 1.96 * StdStats.stddev(percenatge) / Math.sqrt(T);
    }

    public static void main(String[] args) {
        // test client (described below)
        int n = 2;
        int t = 10000;
        PercolationStats stats = new PercolationStats(n, t);
        System.out.println(stats.mean() + "");
        System.out.println(stats.stddev() + "");
        System.out.println(stats.confidenceLo() + " " + stats.confidenceHi());
    }
}