import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Finds the minimum spanning tree of a given number of edges supplied in 
 * a file using Kruskal's algorithm.
 * @author Torre_000
 *
 */
public class Kruskals {
	private String filename;
	private int sum;
	private DisjSets set;
	private PriorityQueue<Edge> queue;
	private HashMap<String, Integer> vertices; 
	private ArrayList<Edge> minSpanTree;
	/**
	 * Edge of a graph
	 * 
	 */
	public class Edge implements Comparable<Edge>{
		public String v1;
		public String v2;
		public int weight;

		Edge(String v1, String v2, int weight) {
			this.v1 = v1;
			this.v2 = v2;
			this.weight = weight;
		}
		
		@Override
		public int compareTo(Edge e) 
		{
			if(this.weight < e.weight)
				return -1;
			else if(this.weight > e.weight)
				return 1;
			
			return 0;
		}
	}
	
	/**
	 * Creates the priority queue of edges. Gets input from csv file. 
	 * 
	 * @throws FileNotFoundException
	 */
	private void getEdges() throws FileNotFoundException {
		Scanner input = new Scanner(new File(filename));
		String line;
		String[] lineSplit;
		while (input.hasNext()) 
		{
			line = input.nextLine();
			lineSplit = line.split(",");
			String lastCity = lineSplit[1];
			queue.add(new Edge(lineSplit[0], lineSplit[1], Integer.parseInt(lineSplit[2])));
			addVertex(lineSplit[0]);
			addVertex(lineSplit[1]);
			for (int i = 3; i < lineSplit.length; i = i + 2) {
				queue.add(new Edge(lastCity, lineSplit[i], Integer.parseInt(lineSplit[i + 1])));
				lastCity = lineSplit[i];
				addVertex(lastCity);
			}
		}
		input.close();
	}
	
	/**
	 * Each vertex is given a unique key
	 * @param val value of vertex
	 */
	private void addVertex(String val) {
		if(!vertices.containsKey(val))
		{
			vertices.put(val, vertices.size());
		}
	}
	
	/**
	 * Finds minimum spanning tree using Kruskal's algorithm
	 * 
	 */
	private void kruskal() {
		int edgesAccepted = 0;
		Edge e;
		this.set = new DisjSets(queue.size() + 1); // number of vertices = edges + 1
		sum = 0;
		for (int i = 0; i < queue.size() + 1; i++) {
			set.find(i);
		}
		
		while (edgesAccepted < queue.size())
		{
			e = queue.remove();
			if (set.find(vertices.get(e.v1)) != set.find(vertices.get(e.v2)))    // if not same set (not yet connected)
	        {
	             // accept the edge
	             edgesAccepted++;
	             set.union(set.find(vertices.get(e.v1)), set.find(vertices.get(e.v2))); // connect them
	             
	             //add edge to our minimum spanning tree list
	             minSpanTree.add(e);	             
	             sum = sum + e.weight; //increase the total sum
	         }
		}
	}

	/**
	 * Print the minimum spanning tree and its sum.
	 */
	public void print() {
		for (Edge e : minSpanTree) {
			System.out.println(e.v1 + ", " + e.v2 + " " + e.weight);
		}
		System.out.println();
		System.out.println("Sum of all distances:" + sum);
	}
	
	public int getSum() {
		return sum;
	}

	public ArrayList<Edge> getMinSpanTree() {
		return minSpanTree;
	}
	
	Kruskals(String filename) throws FileNotFoundException {
		this.filename = filename;
		this.queue = new PriorityQueue<Edge>();
		this.vertices = new HashMap<String, Integer>();
		this.minSpanTree = new ArrayList<Edge>();
		this.getEdges();
		this.kruskal();
	}

	public static void main(String[] args) {
		try {
			Kruskals kru = new Kruskals("assn9_data.txt");
			kru.print();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

}
