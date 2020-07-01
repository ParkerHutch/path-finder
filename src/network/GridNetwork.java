package network;

import network.components.GridNode;
import network.components.Node;

public class GridNetwork extends Network {

	private double nodeApothem;
	
	public GridNetwork(double nodeApothem, double leftX, double topY, double width, 
			double height) {
		
		super(leftX, topY, width, height);
		
		this.nodeApothem = nodeApothem;
		
		placeNodes();
		
	}
	
	public double getNodeApothem() {
		return nodeApothem;
	}

	public void setNodeApothem(double nodeApothem) {
		this.nodeApothem = nodeApothem;
	}

	@Override
	void placeNodes() {
		
		double nodeWidth = getNodeApothem() * 2;
		double nodeHeight = getNodeApothem() * 2;
		int numColumns = (int) (getWidth() / nodeWidth);
		int numRows = (int) (getHeight() / nodeHeight);

		Node[][] nodes = new Node [numColumns][numRows];
		
		int numNodesPlaced = 0;
		
		for (int i = 0; i < nodes.length; i++) {
			
			for (int j = 0; j < nodes[i].length; j++) {
				
				nodes[i][j] = new GridNode(numNodesPlaced, 
						(i + 1) * nodeWidth, 
						(j + 1) * nodeHeight, 
						getNodeApothem());
				
				getNodes().add(nodes[i][j]);
				
				numNodesPlaced++;
				
			}
			
		}
		
		setNodeOffsets();
		
		connectNodes(nodes);
		
	}

	protected void connectNodes(Node [][] nodes) {
		
		for (int i = 0; i < nodes.length; i++) {

			for (int j = 0; j < nodes[i].length; j++) {
				
				Node currentNode = nodes[i][j];
				
				if (i > 0) {
					
					// Above connection
					currentNode.addConnectionTo(nodes[i - 1][j]);
					
				}
				
				if (i < nodes.length - 1) {
					
					// Below connection
					currentNode.addConnectionTo(nodes[i + 1][j]);
					
				}
				
				if (j > 0) {
					
					// Left connection
					currentNode.addConnectionTo(nodes[i][j - 1]);
					
				}
				
				if (j < nodes[i].length - 1) {
					// Right connection
					currentNode.addConnectionTo(nodes[i][j + 1]);
				}
				
				// Diagonal connections
				if (i > 0) {
					
					if (j > 0) {
						
						currentNode.addConnectionTo(nodes[i-1][j-1]);
						
						
					}
					
					if (j < nodes[i].length - 1) {
						
						currentNode.addConnectionTo(nodes[i-1][j+1]);
						
					}
					
				}
				
				if (i < nodes.length - 1) {
					
					if (j > 0) {
						
						currentNode.addConnectionTo(nodes[i+1][j-1]);
						
					}
					
					if (j < nodes[i].length - 1) {
						
						currentNode.addConnectionTo(nodes[i+1][j+1]);
						
					}
				}
				
				
			}

		}
		
		// Special cases
		/*
		Node topLeftNode = nodes[0][0];
		topLeftNode.addConnectionTo(nodes[1][0]);
		topLeftNode.addConnectionTo(nodes[0][1]);
		Node topRightNode = nodes[0][nodes[0].length - 1];
		topRightNode.addConnectionTo(nodes[1][nodes[0].length - 1]);
		topRightNode.addConnectionTo(nodes[0][nodes[0].length - 2]);
		Node bottomLeftNode = nodes[nodes.length - 1][0];
		bottomLeftNode.addConnectionTo(nodes[nodes.length - 2][0]);
		bottomLeftNode.addConnectionTo(nodes[nodes.length - 1][1]);
		Node bottomRightNode = nodes[nodes.length - 1][nodes[0].length - 1];
		bottomRightNode.addConnectionTo(nodes[nodes.length - 2][nodes[0].length - 1]);
		bottomRightNode.addConnectionTo(nodes[nodes.length - 1][nodes[0].length - 2]);
		
		
		*/
		
		
		
		/*
		for (int i = 0; i < getNodes().size(); i++) {
			
			Node currentNode = getNodes().get(i);
			
			// Check for left node
			if (i > 0 && getNodes().get(i - 1).getCenterX() < currentNode.getCenterX()) {
				
				// found a node on the left of the currentNode
				currentNode.addConnectionTo(getNodes().get(i - 1));
				
			}
			
			// Check for right node
			if (i < getNodes().size() - 1 && getNodes().get(i + 1).getCenterX() > currentNode.getCenterX()) {
				
				// found a node on the right of the currentnode
				currentNode.addConnectionTo(getNodes().get(i + 1));
				
			}
			
		}*/
		
	}

}
