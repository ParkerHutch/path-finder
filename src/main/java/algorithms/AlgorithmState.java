package algorithms;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import network.Network;
import network.components.Node;
import network.components.Path;

public class AlgorithmState {
	
	private Network network;
	
	private ArrayList<Node> visitedNodes;
	private ArrayList<Node> unvisitedNodes;
	private Node startNode;
	private Node endNode;
	
	boolean pathFound = false; // TODO Rename to searchComplete, since this could be true in the case
	// of no path to the end node existing?
	Path path;
	
	// shortestDistances contains the shortest distance to each node, where
	// each node's number is its index in the array
	private double [] shortestDistances;
	
	private int iteration = 0;
	
	
	/**
	 * Creates an AlgorithmState with a Network containing start and end nodes.
	 * The lists for visited and unvisited nodes are initialized to empty 
	 * lists (this would be useful for the initial state of an algorithm).
	 * @param network the Network containing the start and end nodes
	 */
	public AlgorithmState(Network network) {
		this.network = network;
		this.startNode = network.getStartNode();
		this.endNode = network.getEndNode();
		this.visitedNodes = new ArrayList<Node>();
		this.unvisitedNodes = new ArrayList<Node>();

		this.shortestDistances = getUndefinedDistancesArray(network.getNodes().size());
		this.shortestDistances[network.getStartNode().getNumber()] = 0;
		this.unvisitedNodes.add(network.getStartNode());
		this.path = null;
		
	}
	
	
	/**
	 * Creates an AlgorithmState with a Network containing start and end nodes,
	 * as well as defined lists for nodes that have already been visited or are
	 * still unvisited. This would be useful for defining the state of an 
	 * algorithm after it has worked through the first iteration.
	 * @param network the Network containing the start and end nodes
	 * @param startNode one of the nodes to be connected by a Path
	 * @param endNode one of the nodes to be connected by a Path
	 * @param visitedNodes the nodes already explored by the Algorithm at this 
	 * step
	 * @param unvisitedNodes the nodes that have not been explored by the 
	 * Algorithm yet
	 * @param iteration the current iteration of the Algorithm
	 */
	public AlgorithmState(Network network, Node startNode, Node endNode, 
			ArrayList<Node> visitedNodes, ArrayList<Node> unvisitedNodes, int iteration) {
		this.network = network;
		this.visitedNodes = visitedNodes;
		this.unvisitedNodes = unvisitedNodes;
		this.startNode = startNode;
		this.endNode = endNode;
		
		if (shortestDistances == null) {
			
			// shortestDistances contains the shortest distance to each node, where
			// each node's number is its index in the array
			this.shortestDistances = getUndefinedDistancesArray(network.getNodes().size());
			this.shortestDistances[network.getStartNode().getNumber()] = 0;
			
		}
	}

	public Network getNetwork() {
		return network;
	}
	public void setNetwork(Network network) {
		this.network = network;
	}
	public ArrayList<Node> getVisitedNodes() {
		return visitedNodes;
	}
	public void setVisitedNodes(ArrayList<Node> visitedNodes) {
		this.visitedNodes = visitedNodes;
	}
	public ArrayList<Node> getUnvisitedNodes() {
		return unvisitedNodes;
	}
	public void setUnvisitedNodes(ArrayList<Node> unvisitedNodes) {
		this.unvisitedNodes = unvisitedNodes;
	}
	public Node getStartNode() {
		return startNode;
	}
	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}
	public Node getEndNode() {
		return endNode;
	}
	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}

	public double[] getShortestDistances() {
		return shortestDistances;
	}

	public void setShortestDistances(double[] shortestDistances) {
		this.shortestDistances = shortestDistances;
	}


	public boolean isPathFound() {
		return pathFound;
	}

	public void setPathFound(boolean pathFound) {
		this.pathFound = pathFound;
	}

	public Path getPath() {
		return path;
	}


	public void setPath(Path path) {
		this.path = path;
	}


	public int getIteration() {
		return iteration;
	}


	public void setIteration(int iteration) {
		this.iteration = iteration;
	}
	
	public double [] getUndefinedDistancesArray(int size) {
		
		double[] arr = new double[size];

		for (int i = 0; i < arr.length; i++) {

			arr[i] = Algorithm.UNDEFINED_DISTANCE;

		}
		
		return arr;
		
	}
	
	public void colorVisitedNodes(Color visitedNodeColor) {
		
		for (Node node : getVisitedNodes()) {
			if (getNetwork().getStartNode() != null && getNetwork().getEndNode() != null
					&& node != getNetwork().getStartNode() && node != getNetwork().getEndNode()
					&& !node.isWall()) {

				node.setInnerColor(visitedNodeColor);
				
			}
		}
		
	}
}
