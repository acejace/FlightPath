**Shortest Flight Path**


Data is too large to commit to git, to use the data:

1. Download zip file from https://www.kaggle.com/usdot/flight-delays
2. Unzip files and place all 3 files in the `data` folder

**#Objective**

Identifying the optimal route for a flight from A to B in a situation where there is no direct connection between the them in the most efficient way possible. The task is to identify the best route, where best route is identified by a few parameters as follows. A customer requires the cheapest flight, with minimum wait time in all connectionairports (the summation of the wait times in all connection airports), and minimum flight duration. Therefore, the customer can go from A to B as fast as possible with the minimum cost
 

We can think of the problem as a single source shortest path problem on an undirected weighted graph, where airports are vertices of the graph, and a flight between two airports represents an edge. The weight of an edge will be a cost function of the score of pricing, wait time, and duration of the flight. The task of our algorithm is to find the path that minimizes the total cost. More specifically, say we have an undirected weighted graph G(V, E)  , a path is given as a sequence:                   P=vi, 1 < i < n, viV,and  j, j+1< i, eE such that (vj, vj+1) = e.
We define the function c : E R x R x R as the cost function from an edge to a tuple of price, wait time, and duration; h:R x R x R Ras the function takes these parameters and gives us a real number as the aggregated score, we have f:ER, f = h c that gives us the weight of a particular edge. Let the set T contains all path from vto v', v and v' V, so the problem of finding the shortest path between v, v' is to find the path p= (vi), p T that minimize the function :
g:T  R, g(vi) = i=1n|f(ei) , where v1=v, vn=v', ei = (vi,vi+1)  .
The algorithm design will be a combination of finding the shortest path and finding the most appropriate c and g that represents the requirement  properly.


**Full presentation can be viewed:** https://www.youtube.com/watch?v=pq--YBAsUVs