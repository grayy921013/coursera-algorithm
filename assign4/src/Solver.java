import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by admin on 2/17/16.
 */
public class Solver {
    private int moves;
    private MinPQ<Node> pq;
    private MinPQ<Node> pq2;
    private ArrayList<Board> solution;
    private boolean solvable = false;

    private static class Node {
        public Node parent;
        public Board board;
        public int moves;

        public Node(Node parent, Board board, int moves) {
            this.parent = parent;
            this.board = board;
            this.moves = moves;
        }
    }

    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        if (initial == null) {
            throw new NullPointerException();
        }
        Comparator<Node> comparator = new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.board.manhattan() + o1.moves - o2.board.manhattan() - o2.moves;
            }
        };
        pq = new MinPQ<>(comparator);
        pq.insert(new Node(null, initial, 0));
        pq2 = new MinPQ<>(comparator);
        pq2.insert(new Node(null, initial.twin(), 0));
        Node node;
        while (true) {
            node = pq.delMin();
            if (node.board.isGoal()) {
                solvable = true;
                break;
            }
            for (Board neighbor : node.board.neighbors()) {
                if (node.parent != null && node.parent.board.equals(neighbor)) continue;
                Node node1 = new Node(node, neighbor, node.moves + 1);
                pq.insert(node1);
            }
            node = pq2.delMin();
            if (node.board.isGoal()) {
                solvable = false;
                break;
            }
            for (Board neighbor : node.board.neighbors()) {
                if (node.parent != null && node.parent.board.equals(neighbor)) continue;
                Node node1 = new Node(node, neighbor, node.moves + 1);
                pq2.insert(node1);
            }
        }
        solution = null;
        if (solvable) {
            solution = new ArrayList<>();
            while (node != null) {
                moves++;
                solution.add(node.board);
                node = node.parent;
            }
            Collections.reverse(solution);
        }
    }

    public boolean isSolvable() {
        // is the initial board solvable?
        return solvable;
    }

    public int moves() {
        // min number of moves to solve initial board; -1 if unsolvable
        return solvable ? moves - 1 : -1;
    }

    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if unsolvable
        return solution;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
