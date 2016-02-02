import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int N;
    private byte[][] array;
    private WeightedQuickUnionUF weightedQuickUnionUF;

    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        this.N = N;
        array = new byte[N][N];
        weightedQuickUnionUF = new WeightedQuickUnionUF(N * N + 2);
        // create N-by-N grid, with all sites blocked
    }

    private void setFull(int i, int j) {
        if (i < 1 || i > N || j < 1 || j > N) return;
        if (!isOpen(i, j) || isFull(i, j)) return;
        array[i - 1][j - 1] |= 1 << 1;
        setFull(i - 1, j);
        setFull(i + 1, j);
        setFull(i, j - 1);
        setFull(i, j + 1);
    }

    private void checkConnectedTop(int i, int j) {
        if (i < 1 || i > N || j < 1 || j > N) return;
        if (isFull(i, j)) return;
        if (i == 1 || (i > 1 && isFull(i - 1, j)) || (i < N && isFull(i + 1, j)) || (j > 1 && isFull(i, j - 1)) ||
                (j < N && isFull(i, j + 1))) {
            setFull(i, j);
        }
    }

    public void open(int i, int j) {
        // open site (row i, column j) if it is not open already
        if (i < 1 || i > N || j < 1 || j > N) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        if (!isOpen(i, j)) {
            array[i - 1][j - 1] |= 1;
            checkConnectedTop(i, j);
            if (i - 1 == 0) {
                weightedQuickUnionUF.union(0, j);
            }
            if (i == N) {
                weightedQuickUnionUF.union((N - 1) * N + j, N * N + 1);
            }
            if (i > 1 && isOpen(i - 1, j)) {
                weightedQuickUnionUF.union((i - 1) * N + (j - 1) + 1, (i - 2) * N + (j - 1) + 1);
            }
            if (i < N && isOpen(i + 1, j)) {
                weightedQuickUnionUF.union((i - 1) * N + (j - 1) + 1, (i) * N + (j - 1) + 1);
            }
            if (j > 1 && isOpen(i, j - 1)) {
                weightedQuickUnionUF.union((i - 1) * N + (j - 1) + 1, (i - 1) * N + (j - 2) + 1);
            }
            if (j < N && isOpen(i, j + 1)) {
                weightedQuickUnionUF.union((i - 1) * N + (j - 1) + 1, (i - 1) * N + (j) + 1);
            }
        }
    }

    public boolean isOpen(int i, int j) {
        if (i < 1 || i > N || j < 1 || j > N) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return (array[i - 1][j - 1] & 1) == 1;
        // is site (row i, column j) open?
    }

    public boolean isFull(int i, int j) {
        if (i < 1 || i > N || j < 1 || j > N) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return ((array[i - 1][j - 1] >> 1) & 1) == 1;
        // is site (row i, column j) full?
    }

    public boolean percolates() {
        return weightedQuickUnionUF.connected(0, N * N + 1);
        // does the system percolate?
    }

    public static void main(String[] args) {
        Percolation percolation = new Percolation(5);
        percolation.open(3, 1);
        percolation.open(2, 1);
        percolation.open(1, 1);
        percolation.open(3, 2);
        System.out.println(percolation.isFull(1, 1) + "");
        System.out.println(percolation.isFull(2, 1) + "");
        System.out.println(percolation.isFull(3, 2) + "");
        // test client (optional)
    }
}