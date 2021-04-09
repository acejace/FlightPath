package flightpath320;

import java.util.ArrayList;
import java.util.Arrays;

public class FlightPath {

  public static void main(String[] args) {
    long[] a = new long[10];
    for (int n = 100; n <= 1000; n += 100) {
      System.out.printf("n=%d\n", n);
      a[(n - 100) / 100] = test(n);
    }
    System.out.println(Arrays.toString(a));
  }

  public static long test(int inputSize) {
    try {
      ArrayList<FlightInfo> flights = FlightData.loadFlights(inputSize);
      ArrayList<String> airports = FlightData.airportsFromFlights(flights);
      // Graph g = Util.createGraph(airports);
      Graph g = Util.createGraph(airports);

      System.out.println("======================");

      for (FlightInfo info : flights) {
        int weight = info.origin == info.destination ? 0 : Util.weightFunction(info);
        g.addEdge(new Node(info.origin), new Node(info.destination), weight);
      }

      // System.out.println(airports);
      // System.out.println(flights);
      // System.out.println(g);

      Node from = new Node("LAS");
      Node to = new Node("MIA");

      long startTime = System.currentTimeMillis();

      ArrayList<Node> result = ShortestPathBFS.run(g, from, to);

      if (result == null) {
        String msg = "No connection between ";
        msg += from.name;
        msg += " and ";
        msg += to.name;
        throw new Exception(msg);
      }

      result = Util.trimDummyNodes(result);
      System.out.println("Path: " + result.toString());

      for (int i = result.size() - 1; i != 0; --i) {
        Node f = result.get(i);
        Node t = result.get(i - 1);
        FlightInfo info = Util.findCloestFlight(flights, f, t);
        if (i != 0) {
          System.out.println("flight: " + info.toString());
        }
      }

      long endTime = System.currentTimeMillis();

      System.out.println(result.toString());

      return (endTime - startTime);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  public static void test1() {
    try {
      ArrayList<Node> nodes = new ArrayList<>();
      nodes.add(new Node("a"));
      nodes.add(new Node("b"));
      nodes.add(new Node("c"));
      nodes.add(new Node("d"));
      Graph g = new Graph(nodes);
      g.addEdge(new Node("a"), new Node("c"), 3);
      g.addEdge(new Node("b"), new Node("c"), 2);
      g.addEdge(new Node("c"), new Node("d"), 2);
      g.addEdge(new Node("a"), new Node("d"), 4);

      System.out.println(g.toString());
      System.out.println();

      ArrayList<Node> result = ShortestPathBFS.run(g, new Node("a"), new Node("d"));
      System.out.println(result);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
