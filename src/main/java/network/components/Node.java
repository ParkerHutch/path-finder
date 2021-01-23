package network.components;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Node extends Entity {

	private int number = -1;
	
	private Node predecessor; // only initialized when searching
    
    private double centerX;
    private double centerY;
    
    private boolean wall = false;
	
    private ArrayList<Connection> connections = new ArrayList<Connection>();
    
    /**
     * Creates a numbered Node centered at (centerX, centerY) with a defined 
     * radius.
     * @param number the Node's number, which should be unique in its Network,
	 * preferably its index in the Network's ArrayList of Nodes.
     * @param centerX the center x-coordinate of the Node
     * @param centerY the center y-coordinate of the Node
     * @param radius the radius of the Node
     */
    public Node(int number, double centerX, double centerY) {
    	
    	this.number = number;
    	this.centerX = centerX;
    	this.centerY = centerY;
    	
    }
    
    /**
     * Returns the Node's unique number(index) in its Network
     * @return the Node's number
     */
    public int getNumber() {
    	
		return number;
		
	}
    
	/**
	 * Sets the Node's number, which should be unique in its Network,
	 * preferably its index in the Network's ArrayList of Nodes.
	 * @param number the new number for the Node
	 */
	public void setNumber(int number) {
		
		this.number = number;
		
	}
	
	public boolean isWall() {
		return wall;
	}

	public void setWall(boolean wall) {
		this.wall = wall;
		
		if (wall) {
			setInnerColor(Color.BLACK); // TODO getter?
		} else {
			setInnerColor(Color.GRAY);
		}
		setPredecessor(null); // TODO not sure if this interferes with other things
	}

	/**
	 * Gets the Node that must be traversed immediately prior to this Node
	 * on the way from a start Node to an end Node
	 * @return the Node between this Node and the start Node
	 */
	public Node getPredecessor() {
		return predecessor;
	}

	/**
	 * Sets the Node's predecessor, the Node between this Node and the start
	 * Node which is closest to this node
	 * @param predecessor the Node which must be traversed immediately before
	 * this Node
	 */
	public void setPredecessor(Node predecessor) {
		this.predecessor = predecessor;
	}

	/**
	 * Gets the center x-coordinate of the Node
	 * @return the center x-coordinate
	 */
	public double getCenterX() {
		return centerX;
	}

	/**
	 * Sets the center x-coordinate of the Node
	 * @param centerX the new center x-coordinate
	 */
	public void setCenterX(double centerX) {
		this.centerX = centerX;
	}

	/**
	 * Gets the center y-coordinate of the Node
	 * @return the center y-coordinate
	 */
	public double getCenterY() {
		return centerY;
	}

	/**
	 * Sets the center y-coordinate of the Node
	 * @param centerY the new center y-coordinate
	 */
	public void setCenterY(double centerY) {
		this.centerY = centerY;
	}

	/**
	 * Gets all of the Node's stored connections to other Nodes
	 * @return the Node's connections
	 */
	public ArrayList<Connection> getConnections() {
		return connections;
	}

	/**
	 * Sets the Node's connections to other Nodes
	 * @param connections a list of connections containing this Node as one
	 * endpoint
	 */
	public void setConnections(ArrayList<Connection> connections) {
		this.connections = connections;
	}
	
	/**
	 * Draws all the Connection objects stored in this Node's connections list
	 * @param gc the GraphicsContext for the Canvas to draw the connections on
	 */
	public void drawConnections(GraphicsContext gc) {

		for (Connection connection : getConnections()) {

			connection.draw(gc);

		}
		
	}
	
	/**
	 * Adds a connection between this node and otherNode to this Node's list 
	 * of connections, as well as the other Node's list of connections, if the
	 * connection doesn't already exist there.
	 * @param otherNode the Node to add a connection to
	 */
	public void addConnectionTo(Node otherNode) {
		
		if (!isConnectedTo(otherNode)) {

			getConnections().add(new Connection(this, otherNode));
			
			if (!otherNode.isConnectedTo(this)) {
				
				otherNode.addConnectionTo(this);
				
			}
		
		} 

	}
	
	/**
	 * Adds a connection with a defined length between this node and otherNode 
	 * to this Node's list of connections, as well as the other Node's list of 
	 * connections, if the connection doesn't already exist there.
	 * @param otherNode the Node to add a connection to
	 */
	public void addConnectionTo(Node otherNode, double length) {
		
		if (!isConnectedTo(otherNode)) {

			getConnections().add(new Connection(this, otherNode, length));
			
			if (!otherNode.isConnectedTo(this)) {
				
				otherNode.addConnectionTo(this, length);
				
			}
		
		} 

	}
	
	/**
	 * Checks if the otherNode is one of the endpoints in any of the
	 * connections contained by this Node.
	 * @param otherNode the Node to check for a connection to
	 * @return true if this Node has a connection to otherNode, false otherwise
	 */
	public boolean isConnectedTo(Node otherNode) {
		
		for (Connection connection : getConnections()) {
			
			if (connection.getEndNode() == otherNode) {
				
				return true;
				
			}
			
		}
		
		return false;
		
	}
	
	/**
	 * Gets the Connection object between this Node and otherNode. Before this
	 * method is called, a check should be performed to ensure that a 
	 * connection between the two Nodes exists, because this will return null
	 * if it cannot find such a connection.
	 * @param otherNode the Node to find a connection to
	 * @return the Connection object starting with this Node and ending with 
	 * otherNode if it exists, or null if that Connection object is not found.
	 */
	public Connection getConnectionTo(Node otherNode) {
		
		if (isConnectedTo(otherNode)) {
			
			for (Connection connection : getConnections()) {
				
				if (connection.getEndNode() == otherNode) {
					
					return connection;
					
				}
				
			}
			
		} 
		
		return null;
			
	}
	
	/**
	 * Uses the endpoints of this Node's list of connections to assemble a
	 * list of Nodes that this node is connected to. 
	 * @return all the Nodes that this Node is connected to, according to this
	 * Node's list of connections. 
	 */
	public ArrayList<Node> getConnectedNodes() {
		
		ArrayList<Node> connectedNodes = new ArrayList<Node>();
		for (Connection connection : getConnections()) {
			
			connectedNodes.add(connection.getEndNode());
			
		}
		
		return connectedNodes;
		
	}
	
	/**
	 * Checks if the Node's boundaries contain the two-dimensional point given.
	 * @param point the test point
	 * @return true if the Node's boundaries contain the point, false 
	 * otherwise.
	 */
	public abstract boolean boundariesContainPoint(Point2D point);
	
	@Override
	public abstract void draw(GraphicsContext gc);

	@Override
	public String toString() {
		
		return "Node " + getNumber() + "(" + (int)getCenterX() + ", " 
				+ (int)getCenterY() + ")";
		
	}
    
}
