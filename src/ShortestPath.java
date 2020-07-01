import java.util.*;

public class ShortestPath {

	// TODO Make methods not static 
	
	 ArrayList<Node> unvisitedNodes = new ArrayList<Node>();
	 ArrayList<Node> visitedNodes = new ArrayList<Node>();
	 double [] shortestDistances; // store distances to nodes by number
	
	public ShortestPath(ArrayList<Node> allNodes, Node startNode, Node endNode) {
		
		getShortestPaths(startNode, endNode, allNodes);
		
	}
	/*
	public  void main(String[] args) {
		
		Network network = new Network(5, 2, 0, 4, 50, 50);
		
		ArrayList<Node> nodes = network.getNodes();
		
		System.out.println(nodes);
		
		getShortestPaths(nodes.get(0), nodes.get(4), nodes);
		
		System.out.println("Distances to nodes:");
		for (int i = 0; i < shortestDistances.length; i++) {
			
			System.out.println("Node " + i + ": " + shortestDistances[i]);

		}
		
		for (Node node : nodes) {
			
			if (node.getPredecessor() != null) {
				
				System.out.println(node + " predecessor: " + node.getPredecessor());
				
			}
			
		}
		
		Node finalNode = nodes.get(nodes.size() - 1);
		
		Path pathToStart = new Path(finalNode);
		
		while (finalNode.getPredecessor() != null) {
			
			pathToStart.addNode(finalNode.getPredecessor());
			finalNode = finalNode.getPredecessor();
			
		}
		
		System.out.println("manual: " + pathToStart);
		
		System.out.println("function path: " + getPathToNodeViaPredecessors(
				nodes.get(0), nodes.get(nodes.size() - 1)));

	}*/
	
	public void getShortestPaths(Node startNode, Node endNode, ArrayList<Node> allNodes) {
		
		/*
		 * 
		 * Guide at https://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
		 * is helpful
		 * 
		 */
		shortestDistances = new double[allNodes.size()]; // store shortest distances to nodes by number
		
		for (int i = 0; i < shortestDistances.length; i++) {
			
			shortestDistances[i] = Double.MAX_VALUE; // TODO can i remove this and use initial null vals?
			
		}
		
		unvisitedNodes.add(startNode);
		shortestDistances[startNode.getNumber()] = 0;
		
		Path currentPath = new Path(startNode);
		
		while (!unvisitedNodes.isEmpty()) {
			
			int smallestDistNodeNum = getUnvisitedNodeNumberWithLowestDistance(shortestDistances);
				
			if (smallestDistNodeNum != -1) {
				
				Node evaluationNode = allNodes.get(smallestDistNodeNum);
				/*
				 * Node evaluationNode = allNodes.get(
				 * getUnvisitedNodeNumberWithLowestDistance(shortestDistances));
				 */

				currentPath.addNode(evaluationNode);

				unvisitedNodes.remove(evaluationNode);
				visitedNodes.add(evaluationNode);

				//System.out.println("Visited: " + visitedNodes);
				evaluateNeighbors(evaluationNode, currentPath);
				
			} else {
				
				break;
				
			}
			
		}
		
	}

	public  int getUnvisitedNodeNumberWithLowestDistance(double [] distances) {
		
		double minDist = Double.MAX_VALUE;//distances[0];
		int minDistNum = -1; // sentinel
		
		ArrayList<Integer> visitedNodeNumbers = new ArrayList<Integer>();
		
		for (Node node : visitedNodes) {
			
			visitedNodeNumbers.add(node.getNumber());
			
		}
		
		for (int i = 0; i < distances.length; i++) {
			
			if (distances[i] < minDist && !visitedNodeNumbers.contains(i)) {
				
				minDist = distances[i];
				minDistNum = i;
				
			}
			
		}
		
		return minDistNum;
		
	}
	
	// TODO change name
	private  void evaluateNeighbors(Node evaluationNode, Path precedingPath) {
		
		for (Node neighbor : evaluationNode.getConnectedNodes()) {
			
			if (!visitedNodes.contains(neighbor)) {
				
				double distanceToNeighbor = evaluationNode.getConnectionTo(neighbor).getLength();
				
				double newDistance = shortestDistances[evaluationNode.getNumber()] + distanceToNeighbor;
				
				if (shortestDistances[neighbor.getNumber()] > newDistance) {
					
					shortestDistances[neighbor.getNumber()] = newDistance;
					neighbor.setPredecessor(evaluationNode);
					unvisitedNodes.add(neighbor); 
					// TODO add to precedingpath here
					
				}
				
			}
			
		}
		
	}

	public  Path getPathToNodeViaPredecessors(Node baseNode, Node endNode) {
		
		Path path = new Path(endNode);
		Node tempNode = endNode;
		while (tempNode.getPredecessor() != null) {
			path.addNode(tempNode.getPredecessor());
			
			if (tempNode.getPredecessor() == baseNode) {
				
				break;
				
			}
			
			tempNode = tempNode.getPredecessor();
			
			
		}
		
		path.reverseSequence();
		return path;
		
	}
	
}
