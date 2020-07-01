package network;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import network.components.Connection;
import network.components.GridNode;
import network.components.Node;
import network.components.Path;

public abstract class Network {

	private double leftX = 0;
	private double topY = 0;
	private double width;
	private double height;
	
	private ArrayList<Node> nodes = new ArrayList<Node>();
	
	private Node startNode;
	private Node endNode;
	private Path path;
	
	private Color startNodeColor = Color.BLUE;
	private Color endNodeColor = Color.RED;
	private Color pathNodeColor = Color.GREEN;
	private Color pathConnectionColor = Color.YELLOW;
	
	static final int MAXNODEPLACEATTEMPTS = 20;
	
	public Network(double leftX, double topY, double width, double height) {
		
		this.leftX = leftX;
		this.topY = topY;
		this.width = width;
		this.height = height;
		
	}
	
	public double getLeftX() {
		return leftX;
	}

	public void setLeftX(double leftX) {
		this.leftX = leftX;
	}

	public double getTopY() {
		return topY;
	}

	public void setTopY(double topY) {
		this.topY = topY;
	}

	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
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
		
		this.endNode = endNode;
		endNode.setOuterColor(getEndNodeColor());
	}
	
	public void setStartNode(Node startNode) {

		this.startNode = startNode;
		startNode.setOuterColor(getStartNodeColor());
		
	}
	
	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
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
	
	public Color getPathNodeColor() {
		return pathNodeColor;
	}

	public void setPathNodeColor(Color pathNodeColor) {
		this.pathNodeColor = pathNodeColor;
	}

	public Color getPathConnectionColor() {
		return pathConnectionColor;
	}

	public void setPathConnectionColor(Color pathConnectionColor) {
		this.pathConnectionColor = pathConnectionColor;
	}

	/**
	 * Places nodes in the Network, connects them to each other, and stores 
	 * them in the Network's <code>nodes</code> list. The nature of the 
	 * placement and connection of the nodes is determined by the class 
	 * extending this Network class. 
	 * @param numNodes the number of nodes to place
	 * @param connectionsPerNode the number of connections each Node should 
	 * have
	 * @param minDistance the minimum distance between the outer edges of each
	 * node
	 * @param width the width of the Network
	 * @param height the height of the Network
	 */
	abstract void placeNodes();
	
	/**
	 * Sets the color of all Nodes that are not start or end nodes to the
	 * default Node color, and all the connections in the Network to the
	 * default Connection color.
	 */
	public void clearColors() {
		
		for (Node node : getNodes()) {

			if (!(node.equals(startNode) || node.equals(endNode))) {
				
				node.setOuterColor(Color.BLACK);
				if (node.getClass() == GridNode.class) {
					
					node.setInnerColor(Color.WHITE);
					
				}

			}
			
			for (Connection connection : node.getConnections()) {

				connection.setInnerColor(Color.BLACK);

			}
			
		}
		
	}
	
	/**
	 * Colors the Nodes and Connections between the beginning Node of the Path 
	 * and end of the Path
	 * @param path the Path to fill, containing a sequence of ordered nodes
	 * beginning with the start Node and terminating with the end Node
	 */
	private void colorPath(Path path) {
		
		clearColors();
		
		if (path.getSequence().size() > 1) {
			
			Node currentNode;
			Connection backConnection1;
			Connection backConnection2;

			for (int i = 1; i < path.getSequence().size(); i++) {

				currentNode = path.getSequence().get(i);
				backConnection1 = path.getSequence().get(i - 1).getConnectionTo(currentNode);
				backConnection2 = currentNode.getConnectionTo(path.getSequence().get(i - 1));

				backConnection1.setInnerColor(getPathConnectionColor());
				backConnection2.setInnerColor(getPathConnectionColor());

				// Color all but the start and end nodes
				if (i != path.getSequence().size() - 1) {
					currentNode.setOuterColor(getPathNodeColor());
					if (currentNode.getClass() == GridNode.class) {
						
						currentNode.setInnerColor(getPathNodeColor());
						
					}
				}

			}
		} 		
		
	}
	
	/**
	 * Calculates the straight-line distance between node1 and node2
	 * @param node1 one of the Nodes in the distance calculation
	 * @param node2 another Node to calculate distance with
	 * @return the geometric distance between node1 and node2
	 */
	protected double getEuclideanDistanceBetween(Node node1, Node node2) {
		
		return Math.hypot(node1.getCenterX() - node2.getCenterX(), 
				node1.getCenterY() - node2.getCenterY());
		
	}
	
	/**
	 * Draws the Network on the screen. Nodes are drawn on top of Connections,
	 * and the Path from the Network's start node to end node, if it exists,
	 * is drawn over all the other objects. 
	 * @param gc the GraphicsContext of the Canvas to draw the Network on
	 */
	public void draw(GraphicsContext gc) {
		
		for (Node node : getNodes()) {

			node.drawConnections(gc);
			
		}
		
		for (Node node : getNodes()) {
			
			node.draw(gc);
			
		}
		
		
		
		if (path != null) {
			
			colorPath(path);
			
			for (Node node : path.getSequence()) {
				
				node.draw(gc);
				
			}
			
		}
		
		if (getStartNode() != null) {
			getStartNode().draw(gc);
		}
		if (getEndNode() != null) {
			getEndNode().draw(gc);
		}
		
	}
	
	/**
	 * Checks if the coordinate pair given by <code>clickPoint</code> falls 
	 * inside of a node, and selects that node as the start node if it does
	 * @param clickPoint the coordinate pair of the click location
	 */
	public void selectStartNode(Point2D clickPoint) {
		
		for (Node node : getNodes()) {
			
			if (node.boundariesContainPoint(clickPoint) && node != getEndNode()) {
				
				setPath(null);
				setStartNode(node);
				clearColors();
				break;
				
			}
			
		}
		
	}
	
	/**
	 * Checks if the coordinate pair given by <code>clickPoint</code> falls inside
	 * of a node, and selects that node as the end node if it does
	 * 
	 * @param clickPoint the coordinate pair of the click location
	 */
	public void selectEndNode(Point2D clickPoint) {

		for (Node node : getNodes()) {

			if (node.boundariesContainPoint(clickPoint) && node != getStartNode()) {
				
				setPath(null);
				setEndNode(node);
				clearColors();
				break;

			}

		}

	}
	
	// Should be called before Nodes are connected
	public void setNodeOffsets() {
		
		for (Node node: getNodes()) {
			
			node.setOffsets(getLeftX(), getTopY());
			node.setCenterX(node.getCenterX() + getLeftX());
			node.setCenterY(node.getCenterY() + getTopY());
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
