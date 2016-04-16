import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Zhehui Zhou on 4/16/16.
 */
public class BoggleSolver {
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    private static class TrieNode {
        public TrieNode[] array = new TrieNode[26];
        public String word;

        public void insert(String s, int index) {
            if (index == s.length()) {
                word = s;
                return;
            }
            char c = s.charAt(index);
            if (c == 'Q') {
                if (index < s.length() - 1 && s.charAt(index + 1) == 'U') index += 2;
                else return;
                ;
            } else {
                index++;
            }
            if (array[c - 'A'] == null) array[c - 'A'] = new TrieNode();
            array[c - 'A'].insert(s, index);
        }
    }

    private TrieNode root;

    public BoggleSolver(String[] dictionary) {
        root = new TrieNode();
        for (String s : dictionary) {
            if (s.length() > 2) root.insert(s, 0);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> set = new HashSet<>();
        boolean[][] visited = new boolean[board.rows()][board.cols()];
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                dfs(set, board, visited, i, j, root);
            }
        }
        return set;
    }

    private void dfs(Set<String> set, BoggleBoard board, boolean[][] visited, int row, int col, TrieNode node) {
        if (row < 0 || row >= board.rows() || col < 0 || col >= board.cols() || visited[row][col]) return;
        node = node.array[board.getLetter(row, col) - 'A'];
        if (node == null) return;
        if (node.word != null) set.add(node.word);
        visited[row][col] = true;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                dfs(set, board, visited, row + i, col + j, node);
            }
        }
        visited[row][col] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int i = 0;
        TrieNode node = root;
        while (i < word.length() && node != null) {
            char c = word.charAt(i);
            if (c == 'Q') {
                if (i < word.length() - 1 && word.charAt(i + 1) == 'U') {
                    node = node.array['Q' - 'A'];
                    i += 2;
                } else node = null;
            } else {
                node = node.array[c - 'A'];
                i++;
            }
        }
        if (node != null && node.word != null) {
            int length = node.word.length();
            if (length <= 2) return 0;
            else if (length <= 4) return 1;
            else if (length == 5) return 2;
            else if (length == 6) return 3;
            else if (length == 7) return 5;
            else return 11;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
