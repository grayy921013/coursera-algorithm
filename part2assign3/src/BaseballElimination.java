import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zhehui Zhou on 4/4/16.
 */
public class BaseballElimination {
    private int num;
    private String[] names;
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] against;
    private int winMax = 0;
    private String winMaxName = "";
    private HashMap<String, Integer> map; //map name to index
    //cache result
    private HashMap<String, Boolean> isEliminated = new HashMap<>();
    private HashMap<String, Iterable<String>> certificate = new HashMap<>();
    public BaseballElimination(String filename) {
        // create a baseball division from given filename in format specified below
        In in = new In(filename);
        num = in.readInt();
        names = new String[num];
        wins = new int[num];
        losses = new int[num];
        remaining = new int[num];
        against = new int[num][num];
        map = new HashMap<>();
        for(int i = 0; i < num; i++) {
            names[i] = in.readString();
            map.put(names[i], i);
            wins[i] = in.readInt();
            if (wins[i] > winMax) {
                winMax = wins[i];
                winMaxName = names[i];
            }
            losses[i] = in.readInt();
            remaining[i] = in.readInt();
            for(int j = 0; j < num; j++) against[i][j] = in.readInt();
        }
    }

    public int numberOfTeams() {
        // number of teams
        return num;
    }

    public Iterable<String> teams() {
        // all teams
        return map.keySet();
    }

    private void checkTeam(String... teams) {
        for(String team : teams) {
            if (map.get(team) == null) throw new IllegalArgumentException();
        }
    }

    public int wins(String team) {
        // number of wins for given team
        checkTeam(team);
        return wins[map.get(team)];
    }

    public int losses(String team) {
        // number of losses for given team
        checkTeam(team);
        return losses[map.get(team)];
    }

    public int remaining(String team) {
        // number of remaining games for given team
        checkTeam(team);
        return remaining[map.get(team)];
    }

    public int against(String team1, String team2) {
        // number of remaining games between team1 and team2
        checkTeam(team1, team2);
        return against[map.get(team1)][map.get(team2)];
    }

    public boolean isEliminated(String team) {
        // is given team eliminated?
        checkTeam(team);
        if (isEliminated.get(team) != null) return isEliminated.get(team);
        int win = wins(team);
        int remaining = remaining(team);
        if (win + remaining < winMax) {
            //trivial
            isEliminated.put(team, true);
            ArrayList<String> list = new ArrayList<>();
            list.add(winMaxName);
            certificate.put(team, list);
            return true;
        }
        // check non-trivial, s is v[0], t is v[count - 1]
        int index = 1;
        int matchSum = 0;
        int[] indexToV = new int[num]; // map team to vertex
        for(int i = 0; i < num; i++) {
            if (i != map.get(team)) indexToV[i] = index++;
        }
        int count = 0;
        for(int i = 0; i < num; i++) {
            if (i == map.get(team)) continue;;
            for(int j = i + 1; j < num; j++) {
                if (j == map.get(team)) continue;
                if (against[i][j] > 0) {
                    count++;
                    matchSum += against[i][j];
                }
            }
        }
        FlowNetwork network = new FlowNetwork(count + num + 1);
        for(int i = 0; i < num; i++) {
            if (i != map.get(team)) network.addEdge(new FlowEdge(indexToV[i], count + num, win + remaining - wins[i]));
        }
        //team vertex start from index
        for(int i = 0; i < num; i++) {
            if (i == map.get(team)) continue;;
            for(int j = i + 1; j < num; j++) {
                if (j == map.get(team)) continue;
                if (against[i][j] > 0) {
                    network.addEdge(new FlowEdge(0, index, against[i][j]));
                    network.addEdge(new FlowEdge(index, indexToV[i], Double.POSITIVE_INFINITY));
                    network.addEdge(new FlowEdge(index, indexToV[j], Double.POSITIVE_INFINITY));
                    index++;
                }
            }
        }
        FordFulkerson fulkerson = new FordFulkerson(network, 0 , count + num);
        if (fulkerson.value() == matchSum) {
            isEliminated.put(team, false);
            return false;
        }
        else {
            isEliminated.put(team, true);
            ArrayList<String> list = new ArrayList<>();
            for(int i = 0; i < num; i++) {
                if(i != map.get(team) && fulkerson.inCut(indexToV[i])) list.add(names[i]);
            }
            certificate.put(team, list);
            return true;
        }
    }

    public Iterable<String> certificateOfElimination(String team) {
        // subset R of teams that eliminates given team; null if not eliminated
        checkTeam(team);
        if (!isEliminated(team)) return null;
        return certificate.get(team);
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
