import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Zhehui Zhou on 3/21/16.
 */
public class Outcast {
    private WordNet wordNet;
    public Outcast(WordNet wordnet) {
        // constructor takes a WordNet object
        this.wordNet = wordnet;
    }

    public String outcast(String[] nouns) {
        // given an array of WordNet nouns, return an outcast
        int maxDist = 0;
        int index = -1;
        for(int i = 0; i < nouns.length; i++) {
            int dist = 0;
            for(int j = 0; j < nouns.length; j++) {
                dist += wordNet.distance(nouns[i], nouns[j]);
            }
            if (dist > maxDist) {
                maxDist = dist;
                index = i;
            }
        }
        return nouns[index];
    }

    public static void main(String[] args) {
        // see test client below
        args = new String[]{"/Users/admin/Downloads/wordnet/synsets.txt", "/Users/admin/Downloads/wordnet/hypernyms.txt"
        ,"/Users/admin/Downloads/wordnet/outcast5.txt", "/Users/admin/Downloads/wordnet/outcast8.txt"
                ,"/Users/admin/Downloads/wordnet/outcast11.txt"};
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
