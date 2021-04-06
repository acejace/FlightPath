package flightpath320;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.HashSet;

import java.util.UUID;

class ShortestPathBFS {
  static ArrayList<Node> run(Graph g, Node s, Node e) {
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

class Node {
  public String name;
  private Node pred;
  public boolean isVisited;

  public Node(String name) {
    this.pred = null;
    this.name = name;
    if (this.name == "dummy") {
      this.name = "dummy" + " " + UUID.randomUUID().toString();
    }
  }

  public Node() {
    this("dummy");
  }

  public Node getPred() {
    return pred;
  }

  public void setPred(Node node) {
    pred = node;
  }

  public ArrayList<Node> traceback(Node goal) {
    assert (pred != null);

    Node pred = this.pred;
    ArrayList<Node> path = new ArrayList<Node>();
    path.add(this);

    try {
      while (pred != goal) {
        if (goal != null && pred == null) {
          String msg = "node " + this + " has no predecessor ";
          msg += " and is not the starting node";
          throw new Exception(msg);
        }

        path.add(pred);
        pred = pred.pred;
      }
      path.add(goal);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return path;
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object node) {

    return name.equals(((Node) node).name);
  }

  @Override
  public String toString() {
    String str = "<Node ";
    str += name;
    str += ">";
    return str;
  }

}

class Graph extends Hashtable<Node, HashSet<Node>> {

  private static final long serialVersionUID = 1L;

  Graph(ArrayList<Node> nodes) {
    super();

    for (Node n : nodes) {
      put(n, new HashSet<>());
    }

  }

  void addEdge(Node node1, Node node2) {
    HashSet<Node> node1Adjs = get(node1);
    if (!node1Adjs.contains(node2)) {
      node1Adjs.add(node2);
    }
  }

  void addEdge(Node node1, Node node2, int weight) throws Exception {
    HashSet<Node> node1Adjs = get(node1);
    if (node1Adjs == null) {
      throw new KeyException("node not in graph: " + node1);
    }

    if (!containsKey(node2)) {
      throw new KeyException("node not in graph: " + node2);
    }

    Node dummy = node1;
    while (weight != 0) {
      dummy = addDummy(dummy);
      weight -= 1;
    }
    assert (weight == 0);
    addEdge(dummy, node2);
  }

  Node addDummy(Node node) {
    Node dummy = new Node();
    put(dummy, new HashSet<>());
    addEdge(node, dummy);
    return dummy;
  }
}
