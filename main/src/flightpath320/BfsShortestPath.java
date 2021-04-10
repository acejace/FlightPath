package flightpath320;

import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;

class ShortestPathBFS implements ShortestPath<UnweightedGraph, Node> {
  @Override
  public ArrayList<Node> run(UnweightedGraph g, Node s, Node e) {
    Queue<Node> queue = new LinkedList<>();
    queue.add(s);

    s.isVisited = true;
    while (!queue.isEmpty()) {
      Node v = queue.remove();
      for (Node u : g.get(v)) {
        if (!u.isVisited) {
          u.setPred(v);
          u.isVisited = true;
          queue.add(u);
        }
        if (u.equals(e)) {
          return u.traceback(s);
        }
      }
    }
    return null;
  }
}
