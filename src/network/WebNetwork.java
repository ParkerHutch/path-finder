package network;

import java.util.ArrayList;
import java.util.Collections;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import network.components.Node;
import network.components.WebNode;

public class WebNetwork extends Network {
	
	static final int MAXNODEPLACEATTEMPTS = 20;
	
	private double nodeRadius = 20;
	
	private double minDistanceBetweenNodes = 10;
	
	private int connectionsPerNode;
	
	private int numNodes;
	
	public WebNetwork(int numNodes, int connectionsPerNode, double nodeRadius, 
			double minDistance,	Color defaultInnerColor, Color defaultOuterColor, 
			double leftX, double topY, double width, double height) {
		
		super(defaultInnerColor, defaultOuterColor, leftX, topY, width, height);
		
		this.numNodes = numNodes;
		this.connectionsPerNode = connectionsPerNode;
		this.nodeRadius = nodeRadius;
		this.minDistanceBetweenNodes = minDistance;
		
		placeNodes();
		
		setNodeOffsets();
		
		connectNodes();
		
		clearColors();
		
	}
	
	public int getNumNodes() {
		return numNodes;
	}

	public void setNumNodes(int numNodes) {
		this.numNodes = numNodes;
	}

	public double getMinDistanceBetweenNodes() {
		return minDistanceBetweenNodes;
	}

	public void setMinDistanceBetweenNodes(double minDistanceBetweenNodes) {
		this.minDistanceBetweenNodes = minDistanceBetweenNodes;
	}

	public int getConnectionsPerNode() {
		return connectionsPerNode;
	}

	public void setConnectionsPerNode(int connectionsPerNode) {
		this.connectionsPerNode = connectionsPerNode;
	}

	@Override
	void placeNodes() {
		
		for (int i = 0; i < getNumNodes(); i++) {

			Point2D newNodeCenter = getValidCoordinates(getWidth(), getHeight(), 
					getMinDistanceBetweenNodes(), 0);

			getNodes().add(new WebNode(i, newNodeCenter.getX(), 
					newNodeCenter.getY(), getNodeRadius()));

		}
		
	}
	
	/**
	 * Connects each node to its closest <code>numConnections</code> number of 
	 * neighbors. If <code>numConnections</code> is -1, each node will be
	 * connected to each other node
	 * @param maxConnectionsNum
	 */
	protected void connectNodes() {
		
		if (getConnectionsPerNode() == -1 || 
				getConnectionsPerNode() >= getNodes().size() - 1) {
			
			// Connect all nodes to all other nodes
			for (Node node1 : getNodes()) {
				
				for (Node node2 : getNodes()) {
					
					if (node1 != node2) {
						
						node1.addConnectionTo(node2);
						
					}
					
				}
				
			}
			
			
		} else {
			
			// Connect each node to its closest numConnectionsPerNode nodes
			
			for (Node node1 : getNodes()) {
				
				for (Node otherNode : getClosestNeighbors(node1, getNodes(), 
						getConnectionsPerNode())) {
					
					node1.addConnectionTo(otherNode);
					
				}
				
			}
			
		}
		
		
	}
	
	/**
	 * Find the closest neighbors of the centerNode, up to numNeighbors 
	 * neighbors.
	 * @param centerNode the Node to find the closest neighbors of
	 * @param allNodes all the nodes in the Network with centerNode
	 * @param numNeighbors the number of closest neighbors to find
	 * @return the centerNode's <code>numNeighbors</code> closest neighbors
	 */
	private ArrayList<Node> getClosestNeighbors(Node centerNode, 
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
		
		// TODO I should be able to do this better - use a temporary shortestDistances
		// array, where the indexes are node numbers, and the values are distances
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
	
	
	private Point2D getValidCoordinates(double width, double height, 
			double minDistance, int attemptsMade) {
		
		Point2D validCenter = new Point2D(
				Math.random() * (width - 20 - getNodeRadius()) + getNodeRadius(), 
				Math.random() * (height - 20 - getNodeRadius()) + getNodeRadius());

		if (attemptsMade < MAXNODEPLACEATTEMPTS) {

			for (Node node : getNodes()) {

				if (Math.hypot(node.getCenterX() - validCenter.getX(),
						node.getCenterY() - validCenter.getY()) <= 
						2 * nodeRadius + minDistance) {

					validCenter = getValidCoordinates(width, height, 
							minDistance, attemptsMade + 1);

				}

			}

		}

		return validCenter;
		
	}


	public double getNodeRadius() {
		return nodeRadius;
	}
	

	public void setNodeRadius(double nodeRadius) {
		this.nodeRadius = nodeRadius;
	}
}
