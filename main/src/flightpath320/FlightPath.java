package flightpath320;

public class FlightPath {
  static final int ITERATION = 10000;

  public static void main(String[] args) {
    // System.out.println("BFS test>>>start");
    // BfsTest.test(ITERATION);
    // System.out.println("BFS test<<<end");

    System.out.println("Dijkstra FS test>>>start");
    DijkstraTest.test(ITERATION);
    System.out.println("Dijkstra test<<<end");
  }
}
