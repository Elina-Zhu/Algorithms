/* *****************************************************************************
 *  Name: Yun Zhu
 *  Date: September 10, 2024
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseballElimination {
    private final int numberOfTeams;
    private final Map<String, Integer> teamToIndex;
    private final String[] teams;
    private final int[] wins, losses, remaining;
    private final int[][] games;
    private final Map<String, List<String>> certificates;

    // Create a baseball division from given filename
    public BaseballElimination(String filename) {
        In in = new In(filename);
        numberOfTeams = in.readInt();
        teams = new String[numberOfTeams];
        wins = new int[numberOfTeams];
        losses = new int[numberOfTeams];
        remaining = new int[numberOfTeams];
        games = new int[numberOfTeams][numberOfTeams];
        teamToIndex = new HashMap<>();
        certificates = new HashMap<>();

        for (int i = 0; i < numberOfTeams; i++) {
            teams[i] = in.readString();
            teamToIndex.put(teams[i], i);
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < numberOfTeams; j++) {
                games[i][j] = in.readInt();
            }
        }
    }

    // Number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }

    // All teams
    public Iterable<String> teams() {
        return Arrays.asList(teams);
    }

    // Number of wins for given team
    public int wins(String team) {
        validateTeam(team);
        return wins[teamToIndex.get(team)];
    }

    // Number of losses for given team
    public int losses(String team) {
        validateTeam(team);
        return losses[teamToIndex.get(team)];
    }

    // Number of remaining games for given team
    public int remaining(String team) {
        validateTeam(team);
        return remaining[teamToIndex.get(team)];
    }

    // Number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);
        return games[teamToIndex.get(team1)][teamToIndex.get(team2)];
    }

    // Is given team eliminated?
    public boolean isEliminated(String team) {
        validateTeam(team);
        int x = teamToIndex.get(team);
        int maxWinsPossible = wins[x] + remaining[x];

        // Trivial elimination check
        for (int i = 0; i < numberOfTeams; i++) {
            if (wins[i] > maxWinsPossible) {
                certificates.put(team, Collections.singletonList(teams[i]));
                return true;
            }
        }

        // Non-trivial elimination check using max-flow
        int gameVertices = (numberOfTeams - 1) * (numberOfTeams - 2) / 2;
        int totalVertices = gameVertices + (numberOfTeams - 1) + 2;
        FlowNetwork network = new FlowNetwork(totalVertices);
        int s = totalVertices - 2;
        int t = totalVertices - 1;
        int vertexIndex = 0;

        // Add edges for game vertices
        for (int i = 0; i < numberOfTeams; i++) {
            if (i == x) continue;
            for (int j = i + 1; j < numberOfTeams; j++) {
                if (j == x) continue;

                network.addEdge(new FlowEdge(s, vertexIndex, games[i][j]));
                network.addEdge(new FlowEdge(vertexIndex, gameVertices + (i > x ? i - 1 : i),
                                             Double.POSITIVE_INFINITY));
                network.addEdge(new FlowEdge(vertexIndex, gameVertices + (j > x ? j - 1 : j),
                                             Double.POSITIVE_INFINITY));
                vertexIndex++;
            }
        }

        // Add edges for team vertices
        for (int i = 0; i < numberOfTeams; i++) {
            if (i == x) continue;
            network.addEdge(
                    new FlowEdge(gameVertices + (i > x ? i - 1 : i), t, maxWinsPossible - wins[i]));
        }

        // Solve max-flow problem
        FordFulkerson ff = new FordFulkerson(network, s, t);
        for (FlowEdge e : network.adj(s)) {
            if (e.flow() != e.capacity()) {
                List<String> certificate = new ArrayList<>();
                for (int i = 0; i < numberOfTeams; i++) {
                    if (i == x) continue;
                    int teamVertex = gameVertices + (i > x ? i - 1 : i);
                    if (ff.inCut(teamVertex)) {
                        certificate.add(teams[i]);
                    }
                }
                certificates.put(team, certificate);
                return true;
            }
        }
        return false;
    }

    // Subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);

        if (!certificates.containsKey(team)) {
            isEliminated(team);
        }

        return certificates.getOrDefault(team, null);
    }

    private void validateTeam(String team) {
        if (!teamToIndex.containsKey(team)) {
            throw new IllegalArgumentException("Invalid team.");
        }
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
