import java.util.*;
import java.io.*;
public class NetworkGraph
{
	private int V; // # of vertices
	private int E; // # of edges
	private ArrayList<LinkedList<DirectedEdge>> adj; // representing graphs using LinkedList of DirectedEdge
	private ArrayList<Boolean> online; // ArrayList that holds info about whether the current node is online or down

	private class PathFinder
	{
		private ArrayList<ArrayList<DirectedEdge>> paths;
		private double maxWeight;
		private int source;
		private int destination;
		private NetworkGraph graph;
		public PathFinder(NetworkGraph G, int givenSource, int givenDestination)
		{
			paths = new ArrayList<>(1);
			maxWeight = 30;
			source = givenSource;
			destination = givenDestination;
			graph = G;

			find(source);
		}

		public PathFinder(NetworkGraph G, int givenSource, int givenDestination, double weight)
		{
			paths =  new ArrayList<>(1); // initialize all my jawns
			maxWeight = weight;
			source = givenSource;
			destination = givenDestination;
			graph = G;

			find(source); // Use helper method to have all paths ready
		}

		private void find(int vertex)
		{
			for(DirectedEdge e : graph.adj(vertex)) // start each path in a different direction
			{
				if(!graph.isOnline(e.to())) continue;
				ArrayList<DirectedEdge> path = new ArrayList<>(1);
				path.add(e);
				find_permutations(path, e.weight()); 
			}
		}

		private void find_permutations(ArrayList<DirectedEdge> currentPath, double currentWeight)
		{
			if(currentWeight > maxWeight) { return; } // base case is passing the weight threshold

			DirectedEdge e = currentPath.get(currentPath.size()-1); // get the latest edge to be added

			if(e.to() == destination) // this is a path (add it)!
			{
				paths.add(currentPath);
			}

			for(DirectedEdge neighbor : graph.adj(e.to())) // all of the other vertices we can visit!
			{
				if(!graph.isOnline(neighbor.from())) continue;

				ArrayList<DirectedEdge> newPath = deep_copy(currentPath);
				newPath.add(neighbor); // add the edge to our new path
				double updatedWeight = currentWeight + neighbor.weight();
				find_permutations(newPath, updatedWeight); // keep going down the rabbit hole.
			}
		}

		// helper method to create deep copies of ArrayLists
		private ArrayList<DirectedEdge> deep_copy(ArrayList<DirectedEdge> path) 
		{
			ArrayList<DirectedEdge> newList = new ArrayList<>();
			for(DirectedEdge e : path)
			{
				newList.add(e);
			}
			return newList;
		}

		// returns all paths that we got
		public Iterable<ArrayList<DirectedEdge>> paths()
		{
			return paths;
		}
	}

	private class UniquePathFinder
	{
		private boolean[] marked;
		private ArrayList<ArrayList<DirectedEdge>> paths;
		private double maxWeight;
		private int source;
		private int destination;
		private NetworkGraph graph;
		public UniquePathFinder(NetworkGraph G, int givenSource, int givenDestination)
		{
			paths = new ArrayList<>(1);
			maxWeight = 30;
			source = givenSource;
			destination = givenDestination;
			graph = G;
			marked = new boolean[G.V()];

			find(source);
		}

		public UniquePathFinder(NetworkGraph G, int givenSource, int givenDestination, double weight)
		{
			paths =  new ArrayList<>(1); // initialize all my jawns
			maxWeight = weight;
			source = givenSource;
			destination = givenDestination;
			graph = G;
			marked = new boolean[G.V()];

			find(source); // Use helper method to have all paths ready
		}

		private void find(int vertex)
		{
			for(DirectedEdge e : graph.adj(vertex)) // start each path in a different direction
			{
				if(!graph.isOnline(e.to()) || marked[e.to()]) continue;
				marked[e.to()] = true;
				ArrayList<DirectedEdge> path = new ArrayList<>(1);
				path.add(e);
				find_permutations(path, e.weight()); 
			}
		}

		private void find_permutations(ArrayList<DirectedEdge> currentPath, double currentWeight)
		{
			if(currentWeight > maxWeight) { return; } // base case is passing the weight threshold

			DirectedEdge e = currentPath.get(currentPath.size()-1); // get the latest edge to be added

			if(e.to() == destination) // this is a path (add it)!
			{
				paths.add(currentPath);
			}

			for(DirectedEdge neighbor : graph.adj(e.to())) // all of the other vertices we can visit!
			{
				if(!graph.isOnline(neighbor.to()) || marked[neighbor.to()]) continue;

				marked[neighbor.to()] = true;
				ArrayList<DirectedEdge> newPath = deep_copy(currentPath);
				newPath.add(neighbor); // add the edge to our new path
				double updatedWeight = currentWeight + neighbor.weight();
				find_permutations(newPath, updatedWeight); // keep going down the rabbit hole.
			}
		}

		// helper method to create deep copies of ArrayLists
		private ArrayList<DirectedEdge> deep_copy(ArrayList<DirectedEdge> path) 
		{
			ArrayList<DirectedEdge> newList = new ArrayList<>();
			for(DirectedEdge e : path)
			{
				newList.add(e);
			}
			return newList;
		}

		// returns all paths that we got
		public Iterable<ArrayList<DirectedEdge>> paths()
		{
			return paths;
		}
	}


	// Dijsktra private class
	private class DijkstraSP
	{
		private DirectedEdge[] edgeTo;
		private double[] distTo;
		private IndexMinPQ<Double> pq;

		public DijkstraSP(NetworkGraph G, int s)
		{
			edgeTo = new DirectedEdge[G.V()];
			distTo = new double[G.V()];
			pq = new IndexMinPQ<Double>(G.V());

			for(int v = 0; v < G.V(); v++)
			{
				distTo[v] = Double.POSITIVE_INFINITY; // originally initalize every index to infinity
			}
			distTo[s] = 0.0;

			pq.insert(s,0.0);
			while(!pq.isEmpty())
			{
				relax(G, pq.delMin()); // our helper method
			}
		}

		private void relax(NetworkGraph G, int v)
		{
			for(DirectedEdge e : G.adj(v)) // check every edge to see the quickest route.
			{
				int w = e.to();
				if(G.isOnline(w))
				{
					if(distTo[w] > distTo[v] + e.weight()) // key of Dijkstras
					{
						distTo[w] = distTo[v] + e.weight();
						edgeTo[w] = e;
						if(pq.contains(w))
						{
							pq.change(w, distTo[w]); // update an edge
						}
						else
						{
							pq.insert(w, distTo[w]); // add an edge
						}
					}
				}
			}
		}

		public boolean hasPathTo(int v)
		{
			return distTo[v] < Double.POSITIVE_INFINITY;
		}

		public Iterable<DirectedEdge> pathTo(int v)
		{
			if (!hasPathTo(v)) return null;
			Stack<DirectedEdge> path = new Stack<>(); // a stack is the person data structure because we add the edges in a Last ->First manner

			for(DirectedEdge e = edgeTo[v];  e != null ; e = edgeTo[e.from()]) // traverse through the array
			{
				path.push(e); // really cool stuff
			}
			return path;
		}
	}

    private class ConnectedComponent
    {
    	private boolean[] marked;
    	private int[] id;
    	private int count;

    	public ConnectedComponent(NetworkGraph G)
    	{
    		marked = new boolean[G.V()];
    		id = new int[G.V()];
    		for(int s = 0; s < G.V(); s++)
    		{
    			if(!marked[s] && G.isOnline(s)) // If not marked that means it was not reached during the dfs
    			{
    				dfs(G,s);
    				count++; 
    			}
    		}
    	}	

    	// Depth first search, connected component id's are assigned here
    	private void dfs(NetworkGraph G, int v)
    	{
    		marked[v] = true; // Mark the vertex we currently are at 
    		id[v] = count; // Mark the correct component #
    		for(DirectedEdge w : G.adj(v))
    		{
    			if(G.isOnline(w.to())) // if a vertex isn't marked let's explore it!
    			{
    				if(!marked[w.to()])
    					dfs(G, w.to()); // explore
    			}
    		}
    	}

    	// return if two verties are on the same component
		public boolean connected(int v, int w)
		{ 
			return id[v] == id[w]; 
		}
		
		// The component of a certain vertex
		public int id(int v)
		{ 
			return id[v]; 
		}

		// Number of Connected Components
		public int count()
		{ 
			return count; 
		}
    }

    private class DepthSearch
    {
    	private boolean[] marked;
    	private int count = 0;

    	public DepthSearch(NetworkGraph G, int s)
    	{
    		marked = new boolean[G.V()];
    		dfs(G, s);
    	}

    	private void dfs(NetworkGraph G, int v)
    	{
    		marked[v] = true; // mark dat shit
    		count++; // A new vertex has been counted
    		for(DirectedEdge w : G.adj(v)) // Check every edge
    		{
    			if(G.isOnline(w.to())) // Let's explore
    			{
    				if(!marked[w.to()])
    					dfs(G, w.to());
    			}
    		}
    	}

    	public boolean marked(int w)
    	{
    		return marked[w]; // Did we visit a vertex?
    	}

    	public int count()
    	{
    		return count; // # of vertices in the DFS
    	}
    }

    private class MST
    {
    	private double weight;
    	private Queue<DirectedEdge> mst;
    	private MinPQ<DirectedEdge> pq;
    	private boolean[] marked;
    	public MST(NetworkGraph G, int source)
    	{
    		pq = new MinPQ<DirectedEdge>();
    		mst = new LinkedList<DirectedEdge>();
    		marked = new boolean[G.V()];

    		visit(G,source);
    		while(!pq.isEmpty())
    		{
    			DirectedEdge e = pq.delMin();
    			int v = e.from(), w = e.to();
    			if((marked[v] && marked[w]) || (!G.isOnline(v) && !G.isOnline(w)))
    			{
    				continue;
    			}
    			mst.add(e);
    			weight += e.weight();
    			if(!marked[v] && G.isOnline(v)) visit(G, v);
    			if(!marked[w] && G.isOnline(w)) visit(G, w);
    		}
    	}

    	// Adding all the relevant edges to the PQ
    	private void visit(NetworkGraph G, int v)
    	{
    		marked[v] = true;
    		for(DirectedEdge e : G.adj(v))
    		{
    			if(!G.isOnline(e.from()))
    			{
    				continue;
    			}

    			if(G.isOnline(e.to()))
    			{
    				if(!marked[e.to()])
    					pq.insert(e);
    			}
    		}
    	}

    	// Returns a queue of all the edges
    	public Iterable<DirectedEdge> edges()
    	{
    		return mst;
    	}

    	// Damn the MST low key thicc
    	public double weight()
    	{
    		return weight;
    	}
    }

    public NetworkGraph(int V) 
    {
        if (V < 0) throw new RuntimeException("Number of vertices must be nonnegative"); // yuh
        this.V = V; 
        this.E = 0;
        adj = new ArrayList<LinkedList<DirectedEdge>>(V);

        online = new ArrayList<Boolean>(V);
        for (int v = 0; v < V; v++) 
        {
        	adj.add(new LinkedList<DirectedEdge>()); // Initializing each spot of the ArrayList
    		online.add(true);
    	}
    }

	public NetworkGraph(In in)
	{
		this(in.readInt()); // waow
		int E = in.readInt(); // Read in the E value

		for(int i = 0; i < E; i++)
		{
			int v = in.readInt();
			int w = in.readInt();
			double weight = in.readDouble();
			addEdge(v,w,weight); // Read in each edge
		}
	}

	// Returns # of vertices
	public int V()
	{
		return V;
	}

	// Returns # of Edges
	public int E()
	{
		return E;
	}

	// Edge adding method
	public void addEdge(int v, int w, double weight)
	{
		// We can only add edges if both nodes are online, or if were are adding a new edge.
		// v > V || w > V should cover adding a new edge because if v or w were greater than V, then they wouldn't be online
		if((isOnline(v) && isOnline(w)) || (v > V || w > V))
		{
			adj.get(v).add(new DirectedEdge(v,w,weight)); // Because the edges are directed we have to add 
			adj.get(w).add(new DirectedEdge(w,v,weight)); // v -> w and w -> v
			E++; // Though we add two items to adj, we are in fact only adding one edge
		} else{ // If there's are problem then shit's going down
			System.out.println("Node is currently shut down! Please try again later.");
		}
	}

	// Method to remove an edge. helper for the "C" command
	private void removeEdge(int v, int w)
	{
		boolean edgeFound = false; // for if we had to remove a non-existent edge
		if((isOnline(v) && isOnline(w)))
		{	
			for(DirectedEdge e : adj.get(v)) // first edge
			{
				if(e.from()==v && e.to()==w)
				{
					adj.get(v).remove(e);
					edgeFound = true;
					break;
				}
			}
			for(DirectedEdge e : adj.get(w)) // second edge
			{
				if(e.from() == w && e.to() == v)
				{
					adj.get(w).remove(e);
					break;
				}
			}
			if (edgeFound)E--;
			else System.out.println("Edge does not exist!");
		} else{
			System.out.println("At least one node is currently down! Please try again later.");
		}
	}

	// Look for an edge, if it is there we store it in a tuple and change the edge's weights.
	// If the edge isn't there we return an empty array, this will indicate that we have to add an edge
	private DirectedEdge[] findEdges(int v, int w, double weight)
	{
		DirectedEdge[] tuple = new DirectedEdge[2];
		boolean edgeFound = false;
		if(isOnline(v) && isOnline(w)) // only exist if they are both online
		{
			for(DirectedEdge e : adj.get(v)) // find the first edge
			{
				if(e.from()==v && e.to()==w)
				{
					tuple[0] = e;
					e.setWeight(weight);
					edgeFound = true;
					break;
				}
			}

			for(DirectedEdge e : adj.get(w)) // find the second edge
			{
				if(e.from()==w && e.to()==v)
				{
					tuple[1]=e;
					e.setWeight(weight);
					edgeFound = edgeFound & true; // bit manipulation ??
					break;
				}
			}
			if(edgeFound) return tuple;
			else{
				System.out.println("Edge Not Found! Adding it now!");
				return new DirectedEdge[2];
			}

		} else{
			System.out.println("At least one node is currently down! Please try again later");
			return tuple;
		}
	}

	// Return all the edges that connect with vertex v
	public Iterable<DirectedEdge> adj(int v)
	{
		return adj.get(v); 
	}

	public boolean isOnline(int v)
	{
		return online.get(v); // very helpful helper
	}

	public Iterable<DirectedEdge> edges()
	{
		ArrayList<DirectedEdge> list = new ArrayList<DirectedEdge>();
		for(int v = 0; v < V; v++) // visit every vertex
		{
			int selfLoops = 0;
			for(DirectedEdge e : adj(v)) // loop through every edge adjacent to vertice v
			{
				if((e.to() > v) && isOnline(v) && isOnline(e.to())){ // We are only interested in edges that have two online nodes
					list.add(e);
				}

				else if ((e.to() == v) && isOnline(v) && isOnline(e.to())){ // Visit each self-loop vertex
					if(selfLoops % 2 == 0) list.add(e);
					selfLoops++;
				}
			}
		}
		return list;
	}

    /**
     * Return a string representation of this graph.
     */
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (DirectedEdge e : adj.get(v)) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    // set the node's status to offline
    public void shut_down(String[] tokens)
    {
    	if(tokens.length<2)
    	{
    		System.out.println("Not enough parameters for command!");
    		return;
    	}
    	int v = Integer.parseInt(tokens[1]);
    	
		if(v >= V)
    	{
    		System.out.println("Invalid vertex number!");
    		return;
    	}    	

		if(!isOnline(v))
		{
	    	System.out.println("Node is already shut down!");
	    	return;
		}

   		online.set(v, false);
   		V-=1; // decrement V 
   		System.out.println("Node " + v +  " is now down!");  
	}

    // set the node's status back online. 
    public void repair(String[] tokens)
    {
    	int v = Integer.parseInt(tokens[1]);

    	if(v >= V)
    	{
    		System.out.println("Invalid vertex number!");
    		return;
    	}

    	if(tokens.length<2)
    	{
    		System.out.println("Insufficient parameters for command");
    		return;
    	}

    	if(isOnline(v))
    	{
	    	System.out.println("Node is already online!");
	    	return;
    	}

   		online.set(v, true);
   		V+=1;
   		System.out.println("Node " + v + " is up!");
    }

    /***
     There are 3 things that needed to be reported when this command is called
     (1) Display current active network.
     	a. call edges()
     	b. print each edge elegantly
     (2) Show status of the network (connected or not)
     	a. Run a dfs search. 
     	b. if search.count() < G.V() not connected 
     	c. else connected
     (3) Show the connected components
     	a. continuously run dfs on each vertex.
     	b. print those with the unique ids and shit
     (4) Make them look better?
    */
    public void report()
    {
    	// Step 1: display current active edges
    	System.out.println("All Current Active Edges: ");
    	ArrayList<DirectedEdge> list = (ArrayList<DirectedEdge>)this.edges();
    	
    	int num = 0;
    	for(DirectedEdge de : list)
    	{
    		num+=1;
    		System.out.println("Edge " + num +": " +de);
    	}

    	// Step 2: Inform Connectivity
    	System.out.print("The system is ");
    	DepthSearch search = new DepthSearch(this, 0);
    	if(search.count() != this.V())
    	{
    		System.out.print("not ");
    	}
    	System.out.println("connected.\n");

    	// Step 3: Display all connected components
    	ConnectedComponent cc = new ConnectedComponent(this);
    	int numComp = cc.count(); // cc.count() returns the number of connected components
    	System.out.printf("There is/are %d components\n\n",numComp);

    	@SuppressWarnings("unchecked")
    	ArrayList<Integer>[] components = (ArrayList<Integer>[]) new ArrayList[numComp]; // components is an ArrayList of integers.    
    																					// Each element corresponds to a connected component. 
    																					// The lists hold each vertex in that component
    	for(int i = 0; i < numComp; i++) 
    	{
    		components[i] = new ArrayList<Integer>(); // initializing each element of components
    	}

    	for(int v = 0; v < this.V(); v++)
    	{
    		components[cc.id(v)].add(v); // add each vertex to its respective component
    	}

    	for(int i = 0; i < numComp; i++)
    	{
    		System.out.printf("Component %d: ", i+1); // printing shit
    		for(int v: components[i])
    		{
    			if(isOnline(v)) // only want the vertices that are online
    				System.out.print(v +" ");
    		}
    		System.out.println();
    	}
    }
    // MEthod that processes the Change command
    public void edit_edge(String[] tokens)
    {
    	if(tokens.length != 4)
    	{
    		System.out.println("Please input the appropriate amount of variables (C i j x)");
    		return;
    	}
    	int v = Integer.parseInt(tokens[1]);
    	int w = Integer.parseInt(tokens[2]);

    	if(v >= V || w >= V)
    	{
    		System.out.println("Invalid vertex number!");
    		return;
    	}

    	double weight = Double.parseDouble(tokens[3]);
    	if(weight <= 0) // if x <= 0 the user would like to delete an edge
    	{
    		removeEdge(v,w);
    	} else{
    		DirectedEdge[] result = findEdges(v,w,weight); // Otherwise we will add a new edge or edit an already existing one
			if(result[0] == null) // null value means the edge was not found and we must add a new edge
			{
					addEdge(v,w,weight);
					System.out.println("Edge added!");
			} 
    	}
    }

    // Path command
    public void path(String[] tokens)
    {
    	if(tokens.length != 4)
    	{
    		System.out.println("Incorrect # of tokens for command P!");
    		return;
    	}

    	int source = Integer.parseInt(tokens[1]);
    	int destination = Integer.parseInt(tokens[2]);
    	int weight = Integer.parseInt(tokens[3]);

    	if(source >= V || destination >= V)
    	{
    		System.out.println("Invalid vertex");
    		return;
    	}

    	if(!isOnline(source) || !isOnline(destination))
    	{
    		System.out.println("Node is currently down and paths cannot be found!\n");
    		return;
    	}

    	// Create the path finding object
    	PathFinder p = new PathFinder(this,source,destination,weight);

    	ArrayList<ArrayList<DirectedEdge>> paths = (ArrayList<ArrayList<DirectedEdge>>) p.paths();

    	if(paths.isEmpty()) // formatting
    	{
    		System.out.println("No paths exist!");
    	}

    	int i = 0;
    	for(ArrayList<DirectedEdge> path : paths)
    	{
    		i+=1;
    		System.out.println("Path " + i+":");
    		for(DirectedEdge e : path)	
    		{
    			System.out.println(e);
    		}
    	}
    }

    // Path command
    public void unique_path(String[] tokens)
    {
    	if(tokens.length != 4)
    	{
    		System.out.println("Incorrect # of tokens for command P!");
    		return;
    	}

    	int source = Integer.parseInt(tokens[1]);
    	int destination = Integer.parseInt(tokens[2]);
    	int weight = Integer.parseInt(tokens[3]);

    	if(source >= V || destination >= V)
    	{
    		System.out.println("Invalid vertex");
    		return;
    	}

    	if(!isOnline(source) || !isOnline(destination))
    	{
    		System.out.println("Node is currently down and paths cannot be found!\n");
    		return;
    	}

    	// Create the path finding object
    	UniquePathFinder p = new UniquePathFinder(this,source,destination,weight);

    	ArrayList<ArrayList<DirectedEdge>> paths = (ArrayList<ArrayList<DirectedEdge>>) p.paths();

    	if(paths.isEmpty()) // formatting
    	{
    		System.out.println("No paths exist!");
    	}

    	int i = 0;
    	for(ArrayList<DirectedEdge> path : paths)
    	{
    		i+=1;
    		System.out.println("Path " + i+":");
    		for(DirectedEdge e : path)	
    		{
    			System.out.println(e);
    		}
    	}
    }
    // The Minimum Spanning Tree command. Accounts for multiple spanning trees
    public void mst(String[] tokens)
    {
    	ConnectedComponent cc = new ConnectedComponent(this);
    	int numComp = cc.count();
    	@SuppressWarnings("unchecked")
    	int[] components = new int[numComp];

    	for(int v = 0; v < this.V(); v++)
    	{
    		components[cc.id(v)] = v; // add each vertex to its respective component
    	}

    	for(int i = 0; i < numComp; i++) // this accounts for multiple connected components
    	{
    		MST m = new MST(this, components[i]); // make query object.
    		System.out.printf("\nMST for Component %d\n",i+1);
	    	Queue<DirectedEdge> edges = (Queue<DirectedEdge>) m.edges();
    		System.out.println("The MST's edges: ");
    		
    		int compIndex = 0;
    		for(DirectedEdge e : edges)
    		{
    			compIndex+=1;
    			System.out.println("Edge " + compIndex + ": " + e);
    		}
    		System.out.println("It had a weight of " + m.weight());
    	}
    }

    private void new_edges(String[] tokens)
    {
    	if(tokens.length!=3)
    	{
    		System.out.println("Incorrect number of parameters!");
    		return;
    	}
    	int source = V - 1;
    	int destination = Integer.parseInt(tokens[1]);
    	int weight = Integer.parseInt(tokens[2]);
    	if(destination >= V)
    	{
    		System.out.println("Invalid destination vertex #. Edge could not be added");
    	}

    	if(!isOnline(destination)) 
    	{
    		System.out.println("The destination node is currently shut down. Please try later!");
    		return;
    	}
    	
    	addEdge(source,destination,weight);
    	System.out.println("New edge added!");
    }

    public void add_vertex()
    {
    	V += 1;
    	adj.add(new LinkedList<DirectedEdge>());
    	online.add(true);
    	int shit = V - 1;
    	System.out.println("New vertex " + shit + " added!");
    	String command;
    	do{
    		System.out.println("We now must add edges for the new vertex. To add use the format: A  destinationVertex#  weight. ");
    		System.out.println("To quit input 'Q'");
    		In sc = new In();
    		command = sc.readLine();
    		String[] tokens = command.split(" ");
    		switch(tokens[0]){
    			case "A":
    				new_edges(tokens);
    				break;
    			case "Q":
    				System.out.println("Returning back to main page!");
    				break;
    			default:
    				System.out.println("Invalid command!");
    		}
    	}while(!command.equals("Q"));
    }

    // Method for the shortest path command
    public void find_path(String[] tokens)
    {
    	//error handling
    	if(tokens.length < 3) 
    	{
    		System.out.println("Insufficient parameters for command!");
    		return;
    	}

	   	int source = Integer.parseInt(tokens[1]);
    	int destination = Integer.parseInt(tokens[2]);

		if(source >= V || destination >= V)
    	{
    		System.out.println("Invalid vertex number!");
    		return;
    	}
    	
    	if(!isOnline(source) || !isOnline(destination))
    	{
    		System.out.println("Node " + source+ " is currently down and a path cannot be found!");
    		return;
    	}

    	DijkstraSP dj = new DijkstraSP(this, source); // make a query

	   	if(!dj.hasPathTo(destination)) 
	   	{
	   		System.out.println("Error: No path found between " + source + " and " + destination + ".");
	   		return;
	   	}
	   	// get the shortest path
	    Stack<DirectedEdge> path = (Stack<DirectedEdge>) dj.pathTo(destination);
	    System.out.println("Path from " + source + " to " + destination + ":");
	    int i = 0;
	    while(!path.isEmpty())
	    {
	    	i+=1;
	    	System.out.println("Edge " + i +": " + path.pop());
	    }
    }

    // method to quit 
    public void quit()
    {
    	System.out.println("\nQuitting...");
		try
		{
			System.out.println("...");
			Thread.sleep(500);
			System.out.println("...");
			Thread.sleep(500);
			System.out.println("...");
			Thread.sleep(500);
		}catch(InterruptedException ex)
		{
			Thread.currentThread().interrupt();
		}
		System.out.println("Have a good day!");
    }

	public static void main(String[] args)
	{
		In in = new In();
		NetworkGraph G;
		String sourceFile;
		if(args.length == 0)
		{
			System.out.println("No file found! Please input the desired file: ");
			String file = in.readLine();
			sourceFile = file;
			G = new NetworkGraph(new In(file));		
		}else{
			sourceFile = args[0];
			G = new NetworkGraph(new In(args[0]));		
		}
		String command;

		System.out.println("\nINPUT FILE: " + sourceFile);
		System.out.println("--------------------------");
		do{
			System.out.println("Enter a Command (Q to quit): ");
			command = in.readLine();
			System.out.println("------------------------");
			String[] tokens = command.split(" ");
			switch(tokens[0])
			{
				case "A":
					G.add_vertex();
				case "C":
					G.edit_edge(tokens);
					break;
				case "D":
					G.shut_down(tokens);
					break;
				case "M":
					G.mst(tokens);
					break;
				case "P":
					G.path(tokens);
					break;
				case "Q":
					G.quit();
					break;
				case "R":
					G.report();
					break;
				case "S":
					G.find_path(tokens);
					break;
				case "U":
					G.repair(tokens);
					break;
				case "Z":
					G.unique_path(tokens);
					break;
				default:
					System.out.println("Unknown Command! Try again.\n");
			}
		} while(!command.equals("Q"));
	}
}