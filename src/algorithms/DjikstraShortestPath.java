package algorithms;

import java.util.ArrayList;

import network.Network;
import network.components.Node;
import network.components.Path;

public class DjikstraShortestPath extends Algorithm {

	/**
	 * Gets the shortest Path from the start node to the end node
	 * @param startNode the node to begin the path from
	 * @param endNode the node to end the path at
	 * @param allNodes all nodes in the network containing the start node and
	 * end node
	 * @return the shortest path from the start node to the end node
	 */
	public Path getShortestPath(Network network) {
		
		for (Node node : network.getNodes()) {
			
			node.setPredecessor(null);
			
		}
		
		performAlgorithm(network);
		
		Path path = getPathToNodeViaPredecessors(network.getStartNode(), network.getEndNode());
		
		if (path.getSequence().size() <= 1) {
			
			System.out.println("Path not found");
			
		}
		
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
	private void performAlgorithm(Network network) {

		/*
		 * 
		 * Guide at
		 * https://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html is
		 * helpful
		 * 
		 */

		AlgorithmState currentState = new AlgorithmState(network);
		
		while (!currentState.isPathFound()) {
			
			currentState = performSingleStep(currentState);
			
		}
		
	}

	private static int getUnvisitedNodeNumberWithLowestDistance(double[] distances, 
			ArrayList<Node> visitedNodes) {
		
		double minDist = Double.MAX_VALUE;
		int minDistNum = -1; // sentinel

		ArrayList<Integer> visitedNodeNumbers = new ArrayList<Integer>();

		for (Node node : visitedNodes) {

			visitedNodeNumbers.add(node.getNumber());

		}

		for (int i = 0; i < distances.length; i++) {
			
			if (distances[i] != Algorithm.UNDEFINED_DISTANCE && distances[i] < minDist && 
					!visitedNodeNumbers.contains(i)) {

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

			if (!visitedNodes.contains(neighbor) && !(neighbor.isWall())) {

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

	@Override
	public AlgorithmState performSingleStep(AlgorithmState state) {
		
		boolean endNodeNeighborsVisited = true;
		for (Node node : state.getEndNode().getConnectedNodes()) {
			
			if (!node.isWall() && !state.getVisitedNodes().contains(node)) {
				
				endNodeNeighborsVisited = false;
				
			}
			
		}

		if (endNodeNeighborsVisited) {
			
			state.setPathFound(true);
			state.setPath(getPathToNodeViaPredecessors(state.getStartNode(), state.getEndNode()));
			return state;
			
		}
		
		// Get the closest unvisited node
		int smallestDistNodeNum = getUnvisitedNodeNumberWithLowestDistance(
				state.getShortestDistances(), state.getVisitedNodes());

		if (smallestDistNodeNum != -1) {
			
			Node evaluationNode = state.getNetwork().getNodes().get(smallestDistNodeNum);
			
			state.getUnvisitedNodes().remove(evaluationNode);
			
			// Search the nodes connected to the evaluationNode, 
			// recalculating their distances(which are to be stored in
			// shortestDistances) and added to unvisitedNodes
			evaluateNeighbors(evaluationNode, state.getShortestDistances(),  
					state.getVisitedNodes(), state.getUnvisitedNodes());
			
			state.getVisitedNodes().add(evaluationNode);

		} else {
			
			// if smallestDistNodeNum is -1, stop searching
			state.setPathFound(true);
			state.setPath(getPathToNodeViaPredecessors(
					state.getStartNode(), state.getEndNode()));	

		}
		
		state.setIteration(state.getIteration() + 1);
		return state;
		
	}

}
