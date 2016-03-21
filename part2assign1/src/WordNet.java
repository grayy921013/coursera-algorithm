import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Zhehui Zhou on 3/21/16.
 */
public class WordNet {

    private Digraph G;
    private SAP sap;
    private HashMap<String, List<Integer>> nounMap; //map noun to a list of id
    private String[] synsetsList;
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        checkNotNull(synsets, hypernyms);
        In in = new In(synsets);
        int count = 0;
        nounMap = new HashMap<>();
        String[] lines = in.readAllLines();
        synsetsList = new String[lines.length];
        for(String line : lines) {
            String[] records = line.split(",");
            Integer id = Integer.parseInt(records[0]);
            String[] nouns = records[1].split(" ");
            for(String noun : nouns) {
                if (nounMap.get(noun) == null) nounMap.put(noun, new ArrayList<>());
                List<Integer> ids = nounMap.get(noun);
                ids.add(id);
            }
            synsetsList[count] = records[1];
            count++;
        }
        G = new Digraph(count);
        in = new In(hypernyms);
        while(in.hasNextLine()) {
            String line = in.readLine();
            String[] records = line.split(",");
            for (int i = 1; i < records.length; i++) {
                G.addEdge(Integer.parseInt(records[0]), Integer.parseInt(records[i]));
            }
        }
        //check one root
        int root = -1;
        for(int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0) {
                if (root < 0) root = i;
                else throw new IllegalArgumentException();
            }
        }
        if (root == -1) throw new IllegalArgumentException();
        //check no cycle, do a dfs
        int[] mark = new int[G.V()]; //0 for not visited, 1 for visited, 2 for visiting
        for(int i = 0; i < mark.length; i++) {
            if (mark[i] == 0) {
                dfs(G, i, mark);
            }
        }
        sap = new SAP(G);
    }

    private boolean dfs(Digraph G, int node, int[] mark) {
        mark[node] = 2;
        for(int adj : G.adj(node)) {
            if(mark[adj] == 0) {
                dfs(G, adj, mark);
            } else if (mark[adj] == 2) throw new IllegalArgumentException();
        }
        mark[node] = 1;
        return true;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        checkNotNull(word);
        return nounMap.containsKey(word);
    }

    private void checkIsNoun(String... nouns) {
        for (String noun : nouns) {
            if (!isNoun(noun)) throw new IllegalArgumentException();
        }
    }
    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        checkIsNoun(nounA, nounB);
        return sap.length(nounMap.get(nounA), nounMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        checkIsNoun(nounA, nounB);
        int ancestor = sap.ancestor(nounMap.get(nounA), nounMap.get(nounB));
        return synsetsList[ancestor];
    }

    private void checkNotNull(String... objects) {
        for(Object o : objects) {
            if (o == null) throw new NullPointerException();
        }
    }
    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("/Users/admin/Downloads/wordnet/synsets3.txt","/Users/admin/Downloads/wordnet/hypernyms3InvalidCycle.txt");
    }
}

