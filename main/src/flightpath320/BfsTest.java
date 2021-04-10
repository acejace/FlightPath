package flightpath320;

import java.util.ArrayList;

class BfsTest {

	// old
	public static void main(String[] args) {
		System.out.println("Testing BFS:\n");
		test(2000);
	}


	public static void test(int iterations) {
		ArrayList<Long> a1 = new ArrayList<>();
		ArrayList<Integer> a2 = new ArrayList<>();
		ArrayList<Integer> a3 = new ArrayList<>();

		for (int n = 20; n <= iterations; n += 20) {
			System.out.printf("n=%d\n", n);
			Metric metric = testRealData(n);
			a1.add(metric.timeTaken);
			a2.add(metric.E);
			a3.add(metric.V);
		}
		System.out.println(a1.toString());
		System.out.println(a2.toString());
		System.out.println(a3.toString());

	}

	public static Metric testRealData(int inputSize) {
		try {
			ArrayList<FlightInfo> flights = FlightData.loadFlights(inputSize);
			ArrayList<String> airports = FlightData.airportsFromFlights(flights);
			// Graph g = Util.createGraph(airports);
			UnweightedGraph g = Util.createUnweightedGraph(airports);

			System.out.println("======================");

			for (FlightInfo info : flights) {
				int weight = info.origin == info.destination ? 0 : Util.weightFunction(info);
				g.addEdge(new Node(info.origin), new Node(info.destination), weight);
			}

			Node from = new Node("LAX");
			Node to = new Node("MIA");

			long startTime = System.currentTimeMillis();

			ArrayList<Node> result = (new ShortestPathBFS()).run(g, from, to);

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

			Metric metric = new Metric();
			metric.timeTaken = (endTime - startTime);
			metric.E = flights.size();
			metric.V = airports.size();

			return metric;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Metric metric = new Metric();
		return metric;
	}

	public static void test1(ShortestPathBFS algo) {
		try {
			ArrayList<Node> nodes = new ArrayList<>();
			nodes.add(new Node("a"));
			nodes.add(new Node("b"));
			nodes.add(new Node("c"));
			nodes.add(new Node("d"));
			UnweightedGraph g = new UnweightedGraph(nodes);
			g.addEdge(new Node("a"), new Node("c"), 3);
			g.addEdge(new Node("b"), new Node("c"), 2);
			g.addEdge(new Node("c"), new Node("d"), 2);
			g.addEdge(new Node("a"), new Node("d"), 4);

			System.out.println(g.toString());
			System.out.println();

			ArrayList<Node> result = algo.run(g, new Node("a"), new Node("d"));
			System.out.println(result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
