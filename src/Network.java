import java.util.ArrayList;
import java.util.Collections;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Network {

	private ArrayList<Node> nodes = new ArrayList<Node>();
	
	Node startNode;
	Node endNode;
	Path path;
	
	Color startNodeColor = Color.GREEN;
	Color endNodeColor = Color.RED;
	
	static final int MAXNODEPLACEATTEMPTS = 20;
	
	public Network(int numNodes, int connectionsPerNode, double nodeRadius, 
			double minDistance,	double width, double height) {
		
		for (int i = 0; i < numNodes; i++) {
			
			Point2D newNodeCenter = getValidCoordinates(width, height, 
					minDistance, nodeRadius, 0);
			
			getNodes().add(new Node(i, 
					newNodeCenter.getX(), 
					newNodeCenter.getY(),
					nodeRadius));
			
		}
		
		connectNodes(connectionsPerNode);
		
		
	}
	
	public ArrayList<Node> getNodes() {
		return nodes;
	}
	
	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}
	
	public Node getStartNode() {
		return startNode;
	}
	
	public Node getEndNode() {
		return endNode;
	}
	
	public void setEndNode(Node endNode) {
		
		if (this.endNode != null) {
			Node oldEndNode = this.endNode;
			oldEndNode.setColor(Color.BLACK);
		}
		
		this.endNode = endNode;
		endNode.setColor(getEndNodeColor());
	}
	
	public void setStartNode(Node startNode) {
		
		if (this.startNode != null) {
			Node oldStartNode = this.startNode;
			oldStartNode.setColor(Color.BLACK);
		}
		
		this.startNode = startNode;
		startNode.setColor(getStartNodeColor());
	}
	
	public Color getStartNodeColor() {
		return startNodeColor;
	}
	
	public void setStartNodeColor(Color startNodeColor) {
		this.startNodeColor = startNodeColor;
	}
	
	public Color getEndNodeColor() {
		return endNodeColor;
	}
	
	public void setEndNodeColor(Color endNodeColor) {
		this.endNodeColor = endNodeColor;
	}
	
	public Point2D getValidCoordinates(double width, double height,
			double minDistance, double nodeRadius, int attemptsMade) {
		
		Point2D validCenter = new Point2D(
				Math.random() * (width - 20 - nodeRadius) + nodeRadius, 
				Math.random() * (height - 20 - nodeRadius) + nodeRadius);
		
		if (attemptsMade < MAXNODEPLACEATTEMPTS) {
			
			for (Node node : getNodes()) {
				
				if (Math.hypot(node.getCenterX() - validCenter.getX(), 
						node.getCenterY() - validCenter.getY()) <= 
						node.getRadius() + nodeRadius + minDistance) {
					
					validCenter = getValidCoordinates(width, height, 
							minDistance, nodeRadius, attemptsMade+1);
					
				}
				
			}
			
		}
		
		return validCenter;
		
	}
	
	/**
	 * Connects each node to its closest <code>numConnections</code> number of 
	 * neighbors. If <code>numConnections</code> is -1, each node will be
	 * connected to each other node
	 * @param maxConnectionsNum
	 */
	public void connectNodes(int numConnectionsPerNode) {
		
		if (numConnectionsPerNode == -1 || 
				numConnectionsPerNode >= getNodes().size() - 1) {
			
			// Connect all nodes to all other nodes
			for (Node node1 : getNodes()) {
				
				for (Node node2 : getNodes()) {
					
					if (node1 != node2) {
						
						/*
						if (!node1.isConnectedTo(node2)) {
							
							node1.addConnectionTo(node2);
							
						}

						if (!node2.isConnectedTo(node1)) {

							node2.addConnectionTo(node1);

						}*/
						
						node1.addConnectionTo(node2);
						
					}
					
				}
				
			}
			
			
		} else {
			
			// Connect each node to its closest numConnectionsPerNode nodes
			
			for (Node node1 : getNodes()) {
				
				for (Node otherNode : getClosestNeighbors(node1, getNodes(), 
						numConnectionsPerNode)) {
					
					node1.addConnectionTo(otherNode);
					
				}
				
			}
			
		}
		
		
	}

	public void clearColors() {
		
		for (Node node : getNodes()) {

			if (!(node.equals(startNode) || node.equals(endNode))) {
				
				node.setColor(Color.BLACK);

			}
			
			for (Connection connection : node.getConnections()) {

				connection.setColor(Color.BLACK);

			}
			
		}
		
	}
	/**
	 * Colors the Nodes and Connections between the beginning Node of the Path 
	 * and end of the Path
	 * @param path the Path to fill, containing a sequence of ordered nodes
	 * beginning with the start Node and terminating with the end Node
	 */
	public void colorPath(Path path, Color nodeColor, Color connectionColor) {
		
		clearColors();
		
		if (path.getSequence().size() > 1) {
			path.getSequence().get(0).getConnectionTo(path.getSequence().get(1)).setColor(Color.GREEN);

			Node currentNode;
			Connection backConnection1;
			Connection backConnection2;

			for (int i = 1; i < path.getSequence().size(); i++) {

				currentNode = path.getSequence().get(i);
				backConnection1 = path.getSequence().get(i - 1).getConnectionTo(currentNode);
				backConnection2 = currentNode.getConnectionTo(path.getSequence().get(i - 1));

				backConnection1.setColor(connectionColor);
				backConnection2.setColor(connectionColor);

				// Color all but the start and end nodes
				if (i != path.getSequence().size() - 1) {
					currentNode.setColor(nodeColor);
				}

			}
		} 		
		
	}
	
	public ArrayList<Node> getClosestNeighbors(Node centerNode, 
			ArrayList<Node> allNodes, int numNeighbors) {
		
		ArrayList<Double> distances = new ArrayList<Double>();
		
		for (Node otherNode : allNodes) {
			
			if (otherNode != centerNode) {
				
				distances.add(getEuclideanDistanceBetween(centerNode, otherNode));
				
			}
			
		}
		
		Collections.sort(distances);
		
		ArrayList<Double> acceptableDistances = new ArrayList<Double>();
		ArrayList<Node> neighbors = new ArrayList<Node>();
		
		for (int i = 0; i < numNeighbors; i++) {
			
			acceptableDistances.add(distances.get(i));
			
		}
		
		for (Node otherNode : allNodes) {
			
			if (acceptableDistances.contains(
					getEuclideanDistanceBetween(centerNode, otherNode))) {
				
				// this node must be one of the closest neighbors
				neighbors.add(otherNode);
				
			}
			
		}
		
		
		return neighbors;
		
		
	}
	
	private double getEuclideanDistanceBetween(Node node1, Node node2) {
		
		return Math.hypot(node1.getCenterX() - node2.getCenterX(), 
				node1.getCenterY() - node2.getCenterY());
		
	}
	
	public void draw(GraphicsContext gc) {
		
		for (Node node : getNodes()) {
			
			node.drawConnections(gc);
			
		}
		
		for (Node node : getNodes()) {
			
			node.draw(gc);
			
		}
		
		if (path != null) {
			
			colorPath(path, Color.GREEN, Color.YELLOW);
			
		}
		
	}
	
	/**
	 * Checks if the coordinate pair given by <code>clickPoint</code> falls 
	 * inside of a node, and selects that node as the start node if it does
	 * @param clickPoint the coordinate pair of the click location
	 */
	public void selectStartNode(Point2D clickPoint) {
		
		for (Node node : getNodes()) {
			
			// Check if click location falls inside a node
			double distanceToNodeCenter = Math.hypot(node.getCenterX() - clickPoint.getX(), 
					node.getCenterY() - clickPoint.getY());
			
			if (distanceToNodeCenter <= node.getRadius()) {
				
				setStartNode(node);
				
			}
			
		}
		
		
		
	}
	
	/**
	 * Checks if the coordinate pair given by <code>clickPoint</code> falls 
	 * inside of a node, and selects that node as the end node if it does
	 * @param clickPoint the coordinate pair of the click location
	 */
	public void selectEndNode(Point2D clickPoint) {
		
		for (Node node : getNodes()) {

			// Check if click location falls inside a node
			double distanceToNodeCenter = Math.hypot(node.getCenterX() - clickPoint.getX(),
					node.getCenterY() - clickPoint.getY());

			if (distanceToNodeCenter <= node.getRadius()) {

				setEndNode(node);
				break;

			}

		}
		
	}

	@Override
	public String toString() {
		
		String str = "";
		str += "Network Summary\n";
		str += getNodes().size() + " nodes\n";
		for (Node node: getNodes()) {
			
			str += "\n";
			str +="[---Node: " + node.getNumber() + "---]\n";
			str +=node.getConnections().size() + " connections\n";
			str +="Coordinates: (" + node.getCenterX() + ", " + 
					node.getCenterY() + ")\n";
			str += "\n";
			
		}
		return str;
	}
}
