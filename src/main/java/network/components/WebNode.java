package network.components;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class WebNode extends Node {
	
    private double radius = 20;
    
    /**
     * Creates a numbered Node centered at (centerX, centerY) with a defined 
     * radius.
     * @param number the Node's number, which should be unique in its Network,
	 * preferably its index in the Network's ArrayList of Nodes.
     * @param centerX the center x-coordinate of the Node
     * @param centerY the center y-coordinate of the Node
     * @param radius the radius of the Node
     */
    public WebNode(int number, double centerX, double centerY, double radius) {
    	
    	super(number, centerX, centerY);
    	this.radius = radius;
    	
    }
    
	/**
	 * Gets the radius of the Node
	 * @return the Node's radius
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Sets the radius of the Node
	 * @param radius the new radius
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}
    
	
	// TODO Check that this works properly 
	@Override
	public boolean boundariesContainPoint(Point2D point) {
		
		double distanceToNodeCenter = Math.hypot(getCenterX() - point.getX(), 
				getCenterY() - point.getY()); // TODO remove variable
		
		
		return distanceToNodeCenter <= getRadius();
	}
	
	/**
	 * Draws the Node on a Canvas
	 * @param gc the GraphicsContext for the Canvas to draw the Node on
	 */
	public void draw(GraphicsContext gc) {
		
		gc.setFill(getInnerColor());
		gc.fillOval(getCenterX() - getRadius(), getCenterY() - getRadius(), 
				getRadius() * 2, getRadius() * 2);
		gc.setStroke(getOuterColor());
		gc.strokeOval(getCenterX() - getRadius(), getCenterY() - getRadius(), 
				getRadius() * 2, getRadius() * 2);
		
		//drawNumber(gc);
	}
	
	/**
	 * Draws the Node's number near its center
	 * @param gc the GraphicsContext for the Canvas to draw the number on
	 */
	public void drawNumber(GraphicsContext gc) {
		
		gc.setFill(Color.YELLOW);
		gc.setFont(new Font(24));
		gc.fillText(""+getNumber(), getCenterX() - 6, 
				getCenterY() + getRadius() - 12);
		
	}
	
}
