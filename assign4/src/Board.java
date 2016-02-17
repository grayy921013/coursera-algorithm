import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Board {
    private int[][] blocks;
    private int N;

    public Board(int[][] blocks) {
        // construct a board from an N-by-N array of blocks
        // (where blocks[i][j] = block in row i, column j)
        this.N = blocks.length;
        int[][] copy = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                copy[i][j] = blocks[i][j];
            }
        }
        this.blocks = copy;
    }

    public int dimension() {
        // board dimension N
        return N;
    }

    public int hamming() {
        // number of blocks out of place
        int dist = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] != 0 && blocks[i][j] != i * N + j + 1) {
                    dist++;
                }
            }
        }
        return dist;
    }

    public int manhattan() {
        // sum of Manhattan distances between blocks and goal
        int dist = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] != 0 && blocks[i][j] != i * N + j + 1) {
                    dist += Math.abs(i - (blocks[i][j] - 1) / N) + Math.abs(j - (blocks[i][j] - 1) % N);
                }
            }
        }
        return dist;
    }

    public boolean isGoal() {
        // is this board the goal board?
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] != 0 && blocks[i][j] != i * N + j + 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public Board twin() {
        // a board that is obtained by exchanging any pair of blocks
        int[][] copy = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                copy[i][j] = blocks[i][j];
            }
        }
        boolean find = false;
        int i = 0,j = 0;
        for (i = 0; i < N ; i++) {
            for (j = 0; j < N - 1; j++) {
                if(copy[i][j] != 0 && copy[i][j+1] !=0) {
                    find = true;
                    break;
                }
            }
            if (find) break;
        }
        int temp = copy[i][j];
        copy[i][j] = copy[i][j+1];
        copy[i][j+1] = temp;
        return new Board(copy);
    }

    public boolean equals(Object y) {
        // does this board equal y?
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (N != that.N) return false;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (that.blocks[i][j] != blocks[i][j]) return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        // all neighboring boards
        ArrayList<Board> list = new ArrayList<>();
        int i = 0, j = 0;
        boolean flag = false;
        for (i = 0; i < N; i++) {
            for (j = 0; j < N; j++) {
                if (blocks[i][j] == 0) {
                    flag = true;
                    break;
                }
            }
            if (flag) break;
        }
        if (i > 0) {
            blocks[i][j] = blocks[i - 1][j];
            blocks[i - 1][j] = 0;
            list.add(new Board(blocks));
            blocks[i - 1][j] = blocks[i][j];
            blocks[i][j] = 0;
        }
        if (i < N - 1) {
            blocks[i][j] = blocks[i + 1][j];
            blocks[i + 1][j] = 0;
            list.add(new Board(blocks));
            blocks[i + 1][j] = blocks[i][j];
            blocks[i][j] = 0;
        }
        if (j > 0) {
            blocks[i][j] = blocks[i][j - 1];
            blocks[i][j - 1] = 0;
            list.add(new Board(blocks));
            blocks[i][j - 1] = blocks[i][j];
            blocks[i][j] = 0;
        }
        if (j < N - 1) {
            blocks[i][j] = blocks[i][j + 1];
            blocks[i][j + 1] = 0;
            list.add(new Board(blocks));
            blocks[i][j + 1] = blocks[i][j];
            blocks[i][j] = 0;
        }
        return list;
    }

    public String toString() {
        // string representation of this board (in the output format specified below)
        StringBuilder builder = new StringBuilder();
        builder.append(Integer.toString(N));
        builder.append("\n");
        for (int i = 0; i < N; i++) {
            builder.append(" ");
            for (int j = 0; j < N; j++) {
                builder.append(blocks[i][j]);
                if (j != N - 1)
                    builder.append(" ");
                else
                    builder.append("\n");
            }
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        // unit tests (not graded)
        int N = StdIn.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = StdIn.readInt();
        Board initial = new Board(blocks);
        StdOut.println(initial.hamming() + "");
        StdOut.println(initial.manhattan() + "");
        StdOut.println(initial.twin() + "");
        for (Board board : initial.neighbors()) {
            StdOut.println(board.toString());
        }
    }
}