package network.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

abstract class Entity {

	// Offsets are used in drawing, i.e. if a network is offset on the screen
	private double xOffset = 0;
	private double yOffset = 0;
	private Color innerColor = Color.BLACK;
	private Color outerColor = Color.BLACK;
	
	
	public Color getInnerColor() {
		return innerColor;
	}


	public void setInnerColor(Color innerColor) {
		this.innerColor = innerColor;
	}


	public Color getOuterColor() {
		return outerColor;
	}


	public void setOuterColor(Color outerColor) {
		this.outerColor = outerColor;
	}
	
	public double getxOffset() {
		return xOffset;
	}


	public void setxOffset(double xOffset) {
		this.xOffset = xOffset;
	}


	public double getyOffset() {
		return yOffset;
	}


	public void setyOffset(double yOffset) {
		this.yOffset = yOffset;
	}


	public void setOffsets(double xOffset, double yOffset) {
		
		setxOffset(xOffset);
		setyOffset(yOffset);
		
	}

	abstract void draw(GraphicsContext gc);
	
}
