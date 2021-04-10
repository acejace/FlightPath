# using bfs on unweighted graph to simulate weighted graph and
# to find the shortest path.


import collections as c
import typing as t
import uuid


class Node:
    def __init__(self, name="dummy"):
        self.pred: t.Optional['Node'] = None
        self.name: str = name
        self.is_airport: bool = False

        if name == "dummy":
            # dummies have unique nodes
            self.name = self.name + " " + str(uuid.uuid4())

    def traceback(self, goal: 'Node' = None) -> t.List['Node']:
        path = [self]
        pred = self.pred

        def stopping(v):
            if goal is not None and pred is None:
                raise RuntimeError(
                    f"node {self} has no predecessor " +
                    " and is not the starting node")

            return v is None if goal is None else v == goal

        while not stopping(pred):
            path.append(pred)
            pred = pred.pred
        path.append(goal)
        return path

    def __hash__(self):
        """
        Node will be unique by their name on a set
        """
        return hash(self.name)

    def __str__(self):
        return f"<Node {self.name}>"

    def __eq__(self, other):
        """
        Although if two nodes have the same name we consider them
        equal, only the one in the graph matters.
        """
        return self.name == other.name

    __repr__ = __str__


class Graph(c.UserDict):
    def __init__(self, nodes: t.List):
        super().__init__()

        for n in nodes:
            self.__setitem__(Node(n), set())

    def add_edge(self, node1: Node, node2: Node, weight=0):
        """
        Never add nodes here. Nodes need to exists before adding edges.
        """

        # node1_adjacents: t.Set = self.get(node1)
        node1_adjacents: t.Set = self[node1]
        if node1_adjacents is None:
            raise KeyError(f"node not in graph. Node: {node1}")
        if node2 not in self:
            raise KeyError(f"node not in graph. Node: {node2}")
        if weight == 0:
            if node2 not in node1_adjacents:
                node1_adjacents.add(node2)
        else:
            dummy = node1
            while weight != 0:
                dummy = self.add_dummy(dummy)
                weight -= 1
            assert weight == 0
            self.add_edge(dummy, node2)

    def add_dummy(self, node: Node) -> Node:
        """
        Add dummy nodes to simulate the distance.
        """
        dummy = Node()
        self[dummy] = set()
        self.add_edge(node, dummy)
        return dummy


def bfs(g: Graph, s: Node, e: Node):
    queue = [s]
    visited = [s]

    while queue != []:
        v = queue.pop(0)
        for u in g[v]:
            if u not in visited:
                u.pred = v
                visited.append(u)
                queue.append(u)
            if u == e:
                return u.traceback(s)
    return []


if __name__ == "__main__":
    g = Graph([1, 2, 3, 4])

    g.add_edge(Node(1), Node(3), weight=10)
    g.add_edge(Node(2), Node(3), weight=3)
    g.add_edge(Node(3), Node(4), weight=3)
    g.add_edge(Node(1), Node(4), weight=30)

    print("graph: ")
    __import__('pprint').pprint(g)

    visited = bfs(g, Node(1), Node(4))
    print("shortest path: ")
    __import__('pprint').pprint(visited)
