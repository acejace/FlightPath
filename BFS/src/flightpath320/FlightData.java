package flightpath320;

import java.util.*;
import java.io.*;

class FlightInfo {
	public String flightNumber; // flight ID
	public String origin; // Origin Airport code
	public String destination; // Destination airport code

	// Times in minutes
	public int elapsedTime; // Total time
	public int airTime; // Time spent in flight
	public int waitTime; // Time spent at airport
	public double price;

	@Override
	public String toString() {
		// Print the values we're interested in (project description mentions that we
		// need to minimize
		// wait time, duration, price.
		return String.format("(%s, %s, (duration: %d, wait: %d, price: %f))", origin, destination, airTime, waitTime,
				price);
	}
}

public class FlightData {
	public static final String FLIGHTS_CSV = "data/flights.csv";
	public static final String AIRPORTS_CSV = "data/airports.csv";

	// Use this instead of loadAirports to ensure that there are no unconnected
	// nodes
	// This lets us vary the number of nodes by varying the number of flights.
	public static ArrayList<String> airportsFromFlights(ArrayList<FlightInfo> flights) {
		HashSet<String> airportsMap = new HashSet<>();

		for (FlightInfo flight : flights) {
			airportsMap.add(flight.origin);
			airportsMap.add(flight.destination);
		}

		String[] airports = new String[airportsMap.size()];
		return new ArrayList<String>(Arrays.asList(airportsMap.toArray(airports)));
	}

	// Use airports for nodes in the graph
	public static ArrayList<String> loadAirports() {
		ArrayList<Hashtable<String, String>> csv = read(AIRPORTS_CSV, -1);
		ArrayList<String> airports = new ArrayList<>();

		for (Hashtable<String, String> row : csv) {
			airports.add(row.get("IATA_CODE"));
		}
		return airports;
	}

	public static ArrayList<FlightInfo> loadFlights(int numberOfRows) {
		// Load an extra amount of data since some of the data has missing rows
		ArrayList<Hashtable<String, String>> flightsCSV = read(FLIGHTS_CSV, (int) (numberOfRows * 1.5));
		ArrayList<FlightInfo> flights = new ArrayList<>();
		Random rng = new Random();
		int i = 0;

		for (Hashtable<String, String> row : flightsCSV) {
			FlightInfo flight = new FlightInfo();

			try {
				flight.flightNumber = row.get("FLIGHT_NUMBER");
				flight.origin = row.get("ORIGIN_AIRPORT");
				flight.destination = row.get("DESTINATION_AIRPORT");
				flight.elapsedTime = Integer.parseInt(row.get("ELAPSED_TIME"));
				flight.airTime = Integer.parseInt(row.get("AIR_TIME"));
			} catch (Exception e) {
				continue;
			}

			flight.waitTime = flight.elapsedTime - flight.airTime;

			// flight price has some randomness as the dataset doesn't include pricing
			// information
			flight.price = flight.airTime * (rng.nextDouble() + 0.5);
			flights.add(flight);

			if (++i >= numberOfRows)
				break;
		}
		return flights;
	}

	public static ArrayList<Hashtable<String, String>> read(String filename, int numberOfRows) {
		ArrayList<Hashtable<String, String>> rows = new ArrayList<>();
		try {
			BufferedReader csv = new BufferedReader(new FileReader(filename));
			String[] header = csv.readLine().split(",");

			String row;
			int line = 0;
			while ((row = csv.readLine()) != null) {
				String[] columns = row.split(",");
				Hashtable<String, String> values = new Hashtable<>();

				for (int i = 0; i < columns.length; i++) {
					values.put(header[i], columns[i]);
				}
				rows.add(values);

				if (numberOfRows > 0 && line++ >= numberOfRows) {
					break;
				}
			}

			csv.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rows;
	}
}
