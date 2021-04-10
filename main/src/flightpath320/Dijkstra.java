package flightpath320;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Collections;

class Dijkstra implements ShortestPath<Graph<WeigthAdjacent>, Node> {

  @Override
  public ArrayList<Node> run(Graph<WeigthAdjacent> g, Node s, Node e) {

    // System.out.println("in dijkstra" + s.name + " " + e.name);
    // System.out.println("in dijkstra" + s + " " + e);

    Comparator<Node> comp = (Node a, Node b) -> {
      return b.pathLength - a.pathLength;
    };

    PriorityQueue<Node> queue = new PriorityQueue<Node>(comp);

    s.isVisited = true;
    s.pathLength = 0;
    queue.add(s);
    while (!queue.isEmpty()) {
      Node n = queue.poll();

      // System.out.println("in dstra, dequeue: " + n);
      for (WeigthAdjacent adj : g.get(n)) {
        int weight = adj.weight;
        Node adjnode = adj.node;

        queue.add(adjnode);
        if (adjnode.pathLength > n.pathLength + weight) {
          adjnode.pathLength = n.pathLength + weight;
          adjnode.setPred(n);

          // System.out.println("------>");
          // System.out.println("  | in dstra: " + n);
          // System.out.println("  | in dstra: adj" + adjnode.name);
          // System.out.println("  | in dstra: dest" + e.name);
          // System.out.println("  | in dstra: am I in? " + adjnode.name == e.name);

          if (adjnode.name.equals(e.name)) {

            // System.out.println("    | in dstra found: " + adjnode + " " + n);
            ArrayList<Node> out = adjnode.traceback(s);
            return out;
          }
        }
      }
      // System.out.println("in dstra, " + n + "is searched");

    }
    return null;
  }
}
