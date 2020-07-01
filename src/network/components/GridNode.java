package network.components;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GridNode extends Node {
	
	private double apothem = 10;
	
    /**
     * Creates a numbered Node centered at (centerX, centerY) with a defined 
     * radius.
     * @param number the Node's number, which should be unique in its Network,
	 * preferably its index in the Network's ArrayList of Nodes.
     * @param centerX the center x-coordinate of the Node
     * @param centerY the center y-coordinate of the Node
     * @param radius the radius of the Node
     */
    public GridNode(int number, double centerX, double centerY, double apothem) {
    	
    	super(number, centerX, centerY);
    	
    	setInnerColor(Color.WHITE);
    	setOuterColor(Color.BLACK);
    	
    	this.apothem = apothem;
    	
    }

	public double getApothem() {
		return apothem;
	}

	public void setApothem(double apothem) {
		this.apothem = apothem;
	}

	@Override
	public boolean boundariesContainPoint(Point2D point) {
		
		return point.getX() < getCenterX() + getApothem() && 
				point.getX() > getCenterX() - getApothem() && 
				point.getY() < getCenterY() + getApothem() && 
				point.getY() > getCenterY() - getApothem();
				
	}

	/**
	 * TODO Redo javadoc
	 * Adds a connection between this node and otherNode to this Node's list 
	 * of connections, as well as the other Node's list of connections, if the
	 * connection isn't already there.
	 * @param otherNode the Node to add a connection to
	 */
	@Override
	public void addConnectionTo(Node otherNode) {
		
		if (!isConnectedTo(otherNode)) {

			getConnections().add(new Connection(this, otherNode));
			
			if (!otherNode.isConnectedTo(this)) {
				
				otherNode.addConnectionTo(this);
				
			}
		
		} 

	}
	
	/**
	 * Draws the Node's number near its center
	 * @param gc the GraphicsContext for the Canvas to draw the number on
	 */
	public void drawNumber(GraphicsContext gc) {
		
		gc.setFill(Color.BLACK);
		gc.setFont(new Font(24));
		gc.fillText(""+getNumber(), getCenterX() - 12, 
				getCenterY() + getApothem() - 12);
		
	}
	
	@Override
	public void draw(GraphicsContext gc) {

		gc.setFill(getInnerColor());
		gc.fillRect(getCenterX() - getApothem(), getCenterY() - getApothem(), 
				getApothem() * 2, getApothem() * 2);
		gc.setLineWidth(5);
		gc.setStroke(getOuterColor());
		gc.strokeRect(getCenterX() - getApothem(), getCenterY() - getApothem(), 
				getApothem() * 2, getApothem() * 2);
				
		
		//drawNumber(gc);
	}
    
    
    
}
