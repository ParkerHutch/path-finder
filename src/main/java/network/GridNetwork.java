package network;

import javafx.scene.paint.Color;
import network.components.GridNode;
import network.components.Node;

public class GridNetwork extends Network {

	private double nodeApothem;
	
	private boolean diagonalConnectionsAllowed = true;
	
	private static final double STRAIGHT_CONNECTION_LENGTH = 1;
	private static final double DIAGONAL_CONNECTION_LENGTH = Math.sqrt(2);
	
	public GridNetwork(Color defaultInnerColor, Color defaultOuterColor, 
			double nodeApothem, double leftX, double topY, double width, 
			double height, boolean diagonalConnectionsAllowed) {
		
		super(defaultInnerColor, defaultOuterColor, leftX, topY, width, height);

		this.nodeApothem = nodeApothem;
		this.diagonalConnectionsAllowed = diagonalConnectionsAllowed;
		
		placeNodes();
		
		clearColors();
		
	}
	
	public double getNodeApothem() {
		return nodeApothem;
	}

	public void setNodeApothem(double nodeApothem) {
		this.nodeApothem = nodeApothem;
	}

	public boolean diagonalConnectionsAllowed() {
		return diagonalConnectionsAllowed;
	}

	public void setDiagonalConnectionsAllowed(boolean diagonalConnectionsAllowed) {
		this.diagonalConnectionsAllowed = diagonalConnectionsAllowed;
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
					currentNode.addConnectionTo(nodes[i - 1][j], STRAIGHT_CONNECTION_LENGTH);
					
				}
				
				if (i < nodes.length - 1) {
					
					// Below connection
					currentNode.addConnectionTo(nodes[i + 1][j], STRAIGHT_CONNECTION_LENGTH);
					
				}
				
				if (j > 0) {
					
					// Left connection
					currentNode.addConnectionTo(nodes[i][j - 1], STRAIGHT_CONNECTION_LENGTH);
					
				}
				
				if (j < nodes[i].length - 1) {
					// Right connection
					currentNode.addConnectionTo(nodes[i][j + 1], STRAIGHT_CONNECTION_LENGTH);
				}
				
				// Diagonal connections
				if (diagonalConnectionsAllowed()) {
					
					if (i > 0) {

						if (j > 0) {

							currentNode.addConnectionTo(nodes[i - 1][j - 1], DIAGONAL_CONNECTION_LENGTH);

						}

						if (j < nodes[i].length - 1) {

							currentNode.addConnectionTo(nodes[i - 1][j + 1], DIAGONAL_CONNECTION_LENGTH);

						}

					}

					if (i < nodes.length - 1) {

						if (j > 0) {

							currentNode.addConnectionTo(nodes[i + 1][j - 1], DIAGONAL_CONNECTION_LENGTH);

						}

						if (j < nodes[i].length - 1) {

							currentNode.addConnectionTo(nodes[i + 1][j + 1], DIAGONAL_CONNECTION_LENGTH);

						}
					}

				}
			}

		}
		
	}

}
