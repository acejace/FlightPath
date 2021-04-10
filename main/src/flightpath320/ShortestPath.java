package flightpath320;

import java.util.ArrayList;

public interface ShortestPath<G, N> {
  public ArrayList<N> run(G g, N s, N e);
}
