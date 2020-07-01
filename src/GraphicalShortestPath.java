import javafx.scene.*;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import network.GridNetwork;
import network.Network;
import network.WebNetwork;
import network.components.Path;
import algorithms.Algorithm;
import algorithms.DjikstraShortestPath;

// Start Date: 1/23/2020
public class GraphicalShortestPath extends Application {

	/*
	 * TODO
	 * - Draggable start/end nodes
	 * 		- MouseHandler class?
	 * - Menu screen with buttons to generate Grid/Web Networks & option for # of nodes
	 * - Animated search (make a new class/package in algorithms)
	 * - Maybe make Network extend the Entity class (which should store x/y offsets?)
	 * - Better colors
	 * - Setting for not allowing diagonal connections
	 */
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

	Network network;
	
	
	public static void main(String[] args) {
		launch(args);
	}

	public void init() throws Exception {
		
		network = new GridNetwork(15, 100, 100, WIDTH - 200, HEIGHT - 200);

		//network = new WebNetwork(70, 2, 20, 10, WIDTH, HEIGHT);
		
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

				}
				
				if (arg0.getButton() == MouseButton.SECONDARY) {

					getNetwork().selectEndNode(clickPoint);

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
				
				if (code.equals("W")) {
					
					// Generate new WebNetwork
					setNetwork(new WebNetwork(30, 2, 20, 10, 100, 100, WIDTH - 200, HEIGHT - 200));
					
					
				} else if (code.equals("G")) {
					
					// Generate new GridNetwork
					setNetwork(new GridNetwork(20, 0, 0, WIDTH, HEIGHT));
					
				} else if (code.equals("F")) {
					
					// Connect start and end nodes and set the path in the Network
					if (network.getStartNode() != null && network.getEndNode() != null) {
						
						Algorithm pathFinder = new DjikstraShortestPath();
						Path path = pathFinder.getShortestPath(
								network.getStartNode(), network.getEndNode(), 
								network.getNodes());
						/*
						Path path = DjikstraShortestPath.getShortestPath(
								network.getStartNode(), network.getEndNode(), 
								network.getNodes());
								*/
						
						network.setPath(path);
						
					}
				}
				
			} 
			
		}


	}

}