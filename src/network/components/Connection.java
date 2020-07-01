package network.components;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Connection extends Entity {
	
	private Node startNode;
	private Node endNode;
	
	public Connection(Node startNode, Node endNode) {
		this.startNode = startNode;
		this.endNode = endNode;
	}

	public Node getStartNode() {
		return startNode;
	}

	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}

	public Node getEndNode() {
		return endNode;
	}

	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}

	public void draw(GraphicsContext gc) {
		
		gc.setStroke(getInnerColor());
		gc.setLineWidth(5);
		gc.strokeLine(startNode.getCenterX(), startNode.getCenterY(), 
				endNode.getCenterX(), endNode.getCenterY());
		
		//drawLengthLabel(gc);
		
	}
	
	public void drawLengthLabel(GraphicsContext gc) {
		
		gc.setFill(Color.BLACK);
		gc.setFont(new Font(18));
		gc.fillText(""+(int)getLength(), getMidpoint().getX(), getMidpoint().getY());
		
	}
	
	public double getLength() {
		
			return Math.hypot(endNode.getCenterX() - startNode.getCenterX(),
					endNode.getCenterY() - startNode.getCenterY());
		
	}
	
	public Point2D getMidpoint() {
		
		double midX = (getStartNode().getCenterX() + getEndNode().getCenterX()) / 2;
		double midY = (getStartNode().getCenterY() + getEndNode().getCenterY()) / 2;

		return new Point2D(midX, midY);
		
	}
	
	@Override
	public String toString() {
		
		String str = "";
		str += "Conection from Node " + getStartNode().getNumber() + " to " 
				+ getEndNode().getNumber() + "\nLength: " + getLength();  
		return str;
		
	}

}
