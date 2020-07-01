import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

abstract class Entity {

	private Color color = Color.BLACK;
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	abstract void draw(GraphicsContext gc);
	
}
