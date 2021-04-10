package flightpath320;

import java.security.KeyException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.UUID;
import java.util.stream.Collectors;

// node + edge
interface Adjacent<T> {
  public T empty();

  public Node getNode();
}

class Node implements Adjacent<Node> {
  public String name;

  private Node pred;
  public int pathLength = Integer.MAX_VALUE;
  public boolean isVisited;

  public Node(String name) {
    this.pred = null;
    this.name = name;
    if (this.name == "dummy") {
      this.name = "dummy" + " " + UUID.randomUUID().toString();
    }
  }

  public Node empty() {
    return new Node();
  }

  public Node getNode() {
    return this;
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

class WeigthAdjacent implements Adjacent<WeigthAdjacent> {
  int weight;
  Node node;

  public WeigthAdjacent(Node node, int weight) {
    this.node = node;
    this.weight = weight;
  }

  public WeigthAdjacent() {
  }

  public WeigthAdjacent empty() {
    WeigthAdjacent n = new WeigthAdjacent();
    n.node = new Node();
    n.weight = Integer.MAX_VALUE;
    return n;
  }

  public Node getNode() {
    return this.node;

  }

  @Override
  public String toString() {
    return "{" + node.toString() + ", " + Integer.toString(weight) + "}";
  }
}

abstract class Graph<N extends Adjacent<N>> extends Hashtable<Node, HashSet<N>> {

  private static final long serialVersionUID = 1L;

  public Graph(ArrayList<Node> nodes) {
    super();
    for (Node n : nodes) {
      put(n, new HashSet<>());
    }
  }
}

class UnweightedGraph extends Graph<Node> {
  private static final long serialVersionUID = 1L;

  public UnweightedGraph(ArrayList<Node> nodes) {
    super(nodes);
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

    if (node1Adjs.contains(node2)) {
      throw new KeyException("edge already existsed" + node1 + " " + node2);
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
    Node dummy = node.empty();
    Node dn = dummy.getNode();
    // new Node();
    put(dn, new HashSet<>());
    addEdge(node, dummy);
    return dummy;
  }

}

// ok this is your specialization
class WeightedGraph extends Graph<WeigthAdjacent> {

  private static final long serialVersionUID = 1L;

  public WeightedGraph(ArrayList<Node> nodes) {
    super(nodes);
  }

  public void addEdge(Node node1, Node node2, int weight) throws Exception {
    HashSet<WeigthAdjacent> node1Adjs = get(node1);

    // System.out.println("=====>>");
    // System.out.println(node1);
    // System.out.println(node2);

    if (node1Adjs == null) {
      throw new KeyException("node not in graph: " + node1);
    }

    if (!containsKey(node2)) {
      throw new KeyException("node not in graph: " + node2);
    }

    if (node1Adjs.stream().map((WeigthAdjacent e) -> e.node) //
        .collect(Collectors.toSet()).contains(node2)) {
      return;
      // throw new KeyException("already in graph: " + node2);
    }

    WeigthAdjacent adj = new WeigthAdjacent();
    adj.node = node2;
    adj.weight = weight;
    node1Adjs.add(adj);
    // System.out.println("=====<<");
  }
}

public class Util {

  private static ArrayList<Node> createGraph(ArrayList<String> airports) {
    ArrayList<Node> nodes = new ArrayList<>();
    for (String a : airports) {
      Node n = new Node(a);
      if (!nodes.contains(n)) {
        nodes.add(n);
      }
    }
    return nodes;
  }

  public static UnweightedGraph createUnweightedGraph(ArrayList<String> airports) {
    ArrayList<Node> nodes = createGraph(airports);
    return new UnweightedGraph(nodes);
  }

  public static WeightedGraph createWeightedGraph(ArrayList<String> airports) {
    ArrayList<Node> nodes = createGraph(airports);
    return new WeightedGraph(nodes);
  }

  // trim dummy nodes
  public static ArrayList<Node> trimDummyNodes(ArrayList<Node> nodes) {
    ArrayList<Node> result = new ArrayList<Node>();
    for (Node n : nodes) {
      if (!n.name.startsWith("dummy")) {
        result.add(n);
      }
    }
    return result;
  }

  public static FlightInfo findCloestFlight(ArrayList<FlightInfo> infos, Node nfrom, Node nto) {
    String from = nfrom.name;
    String to = nto.name;

    FlightInfo shortest = infos.stream().filter(e -> //
    e.origin.equals(from) && e.destination.equals(to)) //
        .min((a, b) -> {
          int wa = weightFunction(a);
          int wb = weightFunction(b);
          if (wa < wb) {
            return 1;
          }
          if (wa == wb) {
            return 0;
          }
          return -1;
        }).get();

    return shortest;
  }

  // weigth function determines nodes to insert.
  public static int weightFunction(FlightInfo edge) {
    return edge.waitTime + edge.airTime + (int) edge.price;
  }

  // find the flightinfo with a pair of locations.
  public static FlightInfo findFlightInfo(ArrayList<FlightInfo> infos, Node from, Node to) {
    for (FlightInfo info : infos) {
      if (info.origin.equals(from.name) && info.destination.equals(to.name)) {
        return info;
      }
    }

    return null;
  }
}
