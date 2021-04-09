package flightpath320;

import java.util.ArrayList;
import java.util.HashSet;

public class Util {

  public static Graph createGraph(ArrayList<String> airports) {
    ArrayList<Node> nodes = new ArrayList<>();

    for (String a : airports) {
      Node n = new Node(a);
      if (!nodes.contains(n)) {
        nodes.add(n);
      }
    }

    return new Graph(nodes);
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

    FlightInfo shortest = infos.stream().filter(e -> e.origin.equals(from) && e.destination.equals(to)).min((a, b) -> {
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
