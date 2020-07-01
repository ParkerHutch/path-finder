package algorithms;

import java.util.ArrayList;

import network.components.Node;
import network.components.Path;

public abstract class Algorithm {

	protected static final int UNDEFINED_DISTANCE = -1;
	
	public abstract Path getShortestPath(Node startNode, Node endNode, 
			ArrayList<Node> allNodes);
	
	public abstract AlgorithmState performSingleStep(AlgorithmState priorState);
	
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
	protected Path getPathToNodeViaPredecessors(Node startNode, Node endNode) {

		Path path = new Path(endNode);
		
		Node tempNode = endNode;
		
		while (tempNode.getPredecessor() != null && tempNode != startNode) {
			
			path.addNode(tempNode.getPredecessor());

			tempNode = tempNode.getPredecessor();

		}
		
		path.reverseSequence();
		
		return path;

	}
	
	
}
