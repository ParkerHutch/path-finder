import java.util.ArrayList;

public class StaticShortestPath {

	private static final int UNDEFINED_DISTANCE = -1;
	
	/**
	 * Gets the shortest Path from the start node to the end node
	 * @param startNode the node to begin the path from
	 * @param endNode the node to end the path at
	 * @param allNodes all nodes in the network containing the start node and
	 * end node
	 * @return the shortest path from the start node to the end node
	 */
	public static Path getShortestPath(Node startNode, Node endNode, ArrayList<Node> allNodes) {
		
		for (Node node : allNodes) {
			
			node.setPredecessor(null);
			
		}
		
		assignNodePredecessors(startNode, endNode, allNodes);
		
		Path path = getPathToNodeViaPredecessors(startNode, endNode);
		
		return path;
		
	}
	
	/**
	 * Performs Djikstra's algorithm to find the shortest distance to each
	 * Node from the startNode. When a new shortest distance to a Node is
	 * found, that Node's predecessor is set to the Node that was being
	 * evaluated to get to it.
	 * @param startNode the beginning Node
	 * @param endNode the end Node
	 * @param allNodes all the nodes in the Network containing the start and
	 * end Nodes
	 */
	private static void assignNodePredecessors(Node startNode, Node endNode, ArrayList<Node> allNodes) {

		/*
		 * 
		 * Guide at
		 * https://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html is
		 * helpful
		 * 
		 */
		
		ArrayList<Node> unvisitedNodes = new ArrayList<Node>();
		ArrayList<Node> visitedNodes = new ArrayList<Node>();
		
		// shortestDistances contains the shortest distance to each node, where
		// each node's number is its index in the array
		double[] shortestDistances = new double[allNodes.size()]; 

		// Initialize all distances to undefined initially
		for (int i = 0; i < shortestDistances.length; i++) {

			shortestDistances[i] = UNDEFINED_DISTANCE;

		}

		unvisitedNodes.add(startNode);
		shortestDistances[startNode.getNumber()] = 0;

		while (!unvisitedNodes.isEmpty()) {

			// Get the closest unvisited node
			int smallestDistNodeNum = getUnvisitedNodeNumberWithLowestDistance(
					shortestDistances, visitedNodes);

			if (smallestDistNodeNum != -1) {
				
				Node evaluationNode = allNodes.get(smallestDistNodeNum);
				
				unvisitedNodes.remove(evaluationNode);
				//visitedNodes.add(evaluationNode); TODO Remove if everything is working
				
				// Search the nodes connected to the evaluationNode, 
				// recalculating their distances(which are to be stored in
				// shortestDistances) and added to unvisitedNodes
				evaluateNeighbors(evaluationNode, shortestDistances,  
						visitedNodes, unvisitedNodes);
				
				visitedNodes.add(evaluationNode);

			} else {
				
				// if smallestDistNodeNum is -1, stop searching
				break;

			}

		}

	}

	private static int getUnvisitedNodeNumberWithLowestDistance(double[] distances, 
			ArrayList<Node> visitedNodes) {
		
		double minDist = Double.MAX_VALUE;// distances[0];
		int minDistNum = -1; // sentinel

		ArrayList<Integer> visitedNodeNumbers = new ArrayList<Integer>();

		for (Node node : visitedNodes) {

			visitedNodeNumbers.add(node.getNumber());

		}

		for (int i = 0; i < distances.length; i++) {
			
			if (distances[i] != -1 && distances[i] < minDist && !visitedNodeNumbers.contains(i)) {

				minDist = distances[i];
				minDistNum = i;

			}

		}

		return minDistNum;

	}

	/**
	 * Searches the nodes connected to the evaluationNode if they are unvisited.
	 * Distances to the neighboring nodes through the evaluationNode are 
	 * calculated, and if those distances  are less than the previously held 
	 * shortest distances to those nodes, then those new distances are used as
	 * the shortest distances, the evaluationNode is set as that neighbor's 
	 * predecessor, and that node is added to unvisitedNodes. 
	 * @param evaluationNode the Node whose neighbors will be searched
	 * @param shortestDistances an array containing the current shortest 
	 * distance to each Node, where the number of the Node corresponds to its
	 * index in the array
	 * @param precedingPath the Path from the startNode to the evaluationNode
	 * @param visitedNodes the ArrayList of Nodes that have been previously 
	 * evaluated
	 * @param unvisitedNodes the ArrayList of Nodes that have not yet been 
	 * evaluated
	 */
	private static void evaluateNeighbors(Node evaluationNode, double [] shortestDistances, 
			ArrayList<Node> visitedNodes, ArrayList<Node> unvisitedNodes) {

		for (Node neighbor : evaluationNode.getConnectedNodes()) {

			if (!visitedNodes.contains(neighbor)) {

				double distanceToNeighbor = evaluationNode.getConnectionTo(neighbor).getLength();

				double newDistance = shortestDistances[evaluationNode.getNumber()] + distanceToNeighbor;

				double oldDistance = shortestDistances[neighbor.getNumber()];
				
				if (oldDistance == UNDEFINED_DISTANCE || oldDistance > newDistance) {

					shortestDistances[neighbor.getNumber()] = newDistance;
					neighbor.setPredecessor(evaluationNode);
					unvisitedNodes.add(neighbor);

				}
				
			}

		}

	}

	/**
	 * Constructs a path from the startNode to the endNode by traversing
	 * backwards from endNode, adding each predecessor to the path, then
	 * returning the reversed version of that path so that the path begins
	 * with the startNode and ends with the endNode
	 * @param startNode the origin of the path
	 * @param endNode the end of the path, with predecessors leading to the
	 * startNode
	 * @return a Path object from startNode to endNode
	 */
	private static Path getPathToNodeViaPredecessors(Node startNode, Node endNode) {

		Path path = new Path(endNode);
		
		Node tempNode = endNode;
		
		//while (tempNode.getPredecessor() != null) {
		while (tempNode.getPredecessor() != null && tempNode != startNode) {
			
			path.addNode(tempNode.getPredecessor());

			/* This can be removed if everything is working - I changed the while condition to accomodate
			if (tempNode.getPredecessor() == startNode) {

				break;

			}*/

			tempNode = tempNode.getPredecessor();

		}
		
		path.reverseSequence();
		
		return path;

	}

}
