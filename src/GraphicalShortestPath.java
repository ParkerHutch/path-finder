import javafx.scene.*;
import javafx.stage.Stage;
import javafx.application.Application;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.canvas.*;

// Start Date: 1/23/2020
public class GraphicalShortestPath extends Application {

	Scene gameScene;
	Stage theStage;

	// Window dimensions
	int WIDTH = 1000;
	int HEIGHT = 680;

	// Useful global variables
	Group root;
	Canvas canvas;

	AnimationTimer animator;

	KeyboardHandler keyboardHandler;

	GraphicsContext gc;

	// Framerate Measuring
	final long[] frameTimes = new long[100];
	int frameTimeIterator = 0;
	boolean frameTimesArrayFull = false;

	Network network;
	
	
	public static void main(String[] args) {
		launch(args);
	}

	public void init() throws Exception {
		
		network = new Network(70, 2, 20, 10, WIDTH, HEIGHT);
		
	}

	public void start(Stage gameStage) throws Exception {
		theStage = gameStage;
		theStage.setTitle("Dijkstra's Algorithm");

		root = new Group();

		gameScene = new Scene(root, WIDTH, HEIGHT);

		keyboardHandler = new KeyboardHandler();
		gameScene.setOnKeyPressed(keyboardHandler);
		gameScene.setOnKeyReleased(keyboardHandler);
		
		addMouseHandling(gameScene);

		theStage.setWidth(WIDTH + 50); // adding these 2 lines fixes drawing errors
		theStage.setHeight(HEIGHT + 50);

		canvas = new Canvas(theStage.getWidth(), theStage.getHeight());

		root.getChildren().add(canvas);

		gc = canvas.getGraphicsContext2D();

		animator = new AnimationTimer() {
			@Override
			public void handle(long arg0) {

				gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // clear the screen
				
				network.draw(gc);
				

			}
		};
		animator.start();

		theStage.setScene(gameScene); // stuff won't show up without this
		theStage.show();
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	/**
	 * Adds mouse event handling to the given Scene
	 * @param scene the Scene to add mouse event handling to
	 */
	public void addMouseHandling(Scene scene) {
		MouseHandler mouseHandler = new MouseHandler();
		scene.setOnMouseMoved(mouseHandler);
		scene.setOnMouseDragged(mouseHandler);
		scene.setOnMousePressed(mouseHandler);
		scene.setOnMouseClicked(mouseHandler);
		scene.setOnMouseReleased(mouseHandler);
	}
	
	/**
	 * A class for handling mouse input
	 */
	class MouseHandler implements EventHandler<MouseEvent> {

		public void handle(MouseEvent arg0) {
			
			if (arg0.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
				
				Point2D clickPoint = new Point2D(arg0.getX(), arg0.getY());
				
				if (arg0.getButton() == MouseButton.PRIMARY) {
					
					getNetwork().selectStartNode(clickPoint);
					network.path = null;
					network.clearColors();

				}
				if (arg0.getButton() == MouseButton.SECONDARY) {

					getNetwork().selectEndNode(clickPoint);
					network.path = null;
					network.clearColors();

				}
			}
		}
		
	}
	
	/**
	 * A class for handling keyboard input
	 */
	class KeyboardHandler implements EventHandler<KeyEvent> {

		public void handle(KeyEvent arg0) {
			
			if (arg0.getEventType() == KeyEvent.KEY_PRESSED) {
				String code = arg0.getCode().toString();
				
				if (code.equals("SPACE")) {
					
					// Generate new Network
					network = new Network(30, 2, 20, 10, WIDTH, HEIGHT);
					
				} else if (code.equals("F")) {
					
					// Connect start and end nodes
					
					if (network.getStartNode() != null && network.getEndNode() != null) {
						
						// TODO Don't use a class for this if possible
						
						/*
						ShortestPath calculator = new ShortestPath(network.getNodes(), network.getStartNode(),
								network.getEndNode());
						Path path = calculator.getPathToNodeViaPredecessors(
								network.getStartNode(), network.getEndNode());
						*/
						
						Path path = StaticShortestPath.getShortestPath(
								network.getStartNode(), network.getEndNode(), 
								network.getNodes());
						
						if (path.getSequence().size() == 1) {
							
							System.out.println("Path not found");
							
						}
						
						network.path = path;
						
					}
				}
				
			} 
			
		}


	}

}