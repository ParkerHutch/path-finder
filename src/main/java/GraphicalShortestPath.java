import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;

import network.GridNetwork;
import network.Network;
import network.WebNetwork;
import network.components.Node;
import network.components.Path;

import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.io.File;

import algorithms.Algorithm;
import algorithms.AlgorithmState;
import algorithms.DjikstraShortestPath;

// Start Date: 1/23/2020
public class GraphicalShortestPath extends Application {

	/*
	 * TODO
	 * - Menu screen with buttons to generate Grid/Web Networks & option for # of nodes
	 * - Maybe make Network extend the Entity class (which should store x/y offsets?)
	 * - Better colors
	 * - Setting for not allowing diagonal connections
	 * - Should network really store start and end nodes?
	 * - Sounds while algorithm is searching
	 */

	// Window dimensions
	static final int WIDTH = 1000;
	static final int HEIGHT = 680;

	Network network;
	
	AlgorithmState currentState;
	
	Algorithm pathFinder = new DjikstraShortestPath();
	// TODO Remove beep if I can't figure out
	// AudioClip beep = new AudioClip(Paths.get("beep1.mp3").toUri().toString());
	AudioClip beep = new AudioClip(new File("src/main/resources/beep1.mp3").toURI().toString());
	public static void main(String[] args) {
		launch(args);
	}

	public void init() throws Exception {

		network = new GridNetwork(Color.GRAY, Color.BLACK, 15, 100, 100, 
				WIDTH - 200, HEIGHT - 200, true);
		
	}

	public void start(Stage gameStage) {
		
		Stage theStage = gameStage;
		theStage.setTitle("Dijkstra's Algorithm");

		Group root = new Group();

		Scene primaryScene = new Scene(root, WIDTH, HEIGHT);

		KeyboardHandler keyboardHandler = new KeyboardHandler();
		primaryScene.setOnKeyPressed(keyboardHandler);
		primaryScene.setOnKeyReleased(keyboardHandler);
		
		MouseHandler mouseHandler = new MouseHandler(); 
		addMouseHandling(mouseHandler, primaryScene);

		theStage.setWidth(WIDTH + 50); // adding these 2 lines fixes drawing errors
		theStage.setHeight(HEIGHT + 50);

		Canvas canvas = new Canvas(theStage.getWidth(), theStage.getHeight());

		root.getChildren().add(canvas);

		GraphicsContext gc = canvas.getGraphicsContext2D();

		AnimationTimer animator = new AnimationTimer() {
			
			Instant start;// = Instant.now();
			Instant now;
			
			@Override
			public void start() {
				
				start = Instant.now();
				
				super.start();

			}
			
			@Override
			public void handle(long arg0) {
				
				now = Instant.now();
				
				long timeElapsed = Duration.between(start, now).toSeconds();

				if (timeElapsed > 0 && network.hasStartAndEndNodes()
						&& currentState != null && !mouseHandler.mouseBeingDragged) {
					
					currentState = pathFinder.performSteps(currentState, 5);
					
					timeElapsed = 0; // TODO is this needed?
					
				}
				
				//System.out.println("here");
				//beep.play();
				
				gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // clear the screen
				
				gc.setFill(Color.LIGHTGREEN);
				gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
				network.draw(gc);
				
				if (currentState != null && !mouseHandler.mouseBeingDragged) {
					
					currentState.colorVisitedNodes(Color.LIGHTCORAL);
					
					if (currentState.isPathFound()) {
						
						network.setPath(currentState.getPath());
						network.draw(gc);
						
					} 
				} 
				
			}
		};
		animator.start();

		theStage.setScene(primaryScene); // stuff won't show up without this
		theStage.show();
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
	public void addMouseHandling(MouseHandler mouseHandler, Scene scene) {
		//MouseHandler mouseHandler = new MouseHandler();
		//mouseHandler = new MouseHandler();
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

		boolean mouseBeingDragged = false;
		boolean dragBeganOnStartNode = false;
		boolean dragBeganOnEndNode = false;
		boolean dragBeganOnNormalNode = false;
		
		public void handle(MouseEvent arg0) {
			
			Point2D eventPoint = new Point2D(arg0.getX(), arg0.getY());

			if (arg0.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
				
				for (Node node : network.getNodes()) {

					if (node.boundariesContainPoint(eventPoint)) {

						network.setPath(null);
						currentState = null;
						
						if (getNetwork().getStartNode() == null) {

							network.setStartNode(node);

						} else if (getNetwork().getEndNode() == null && 
								!node.equals(network.getStartNode())) {

							network.setEndNode(node);

						} else if (network.hasStartAndEndNodes() &&
								node != network.getStartNode() && 
								node != network.getEndNode() && !mouseBeingDragged){
							node.setWall(!node.isWall());
							
						}
						
					}
					
					
				}
				
			} else if (arg0.getEventType().equals(MouseEvent.MOUSE_DRAGGED) && network.hasStartAndEndNodes()) {

				network.setPath(null);
				network.clearColors();
				
				if (!mouseBeingDragged) {
					
					// The drag must have just been initiated 
					// (since mouseBeingDragged will be set to true after this)
					if (network.getStartNode() != null && 
							network.getStartNode().boundariesContainPoint(
									eventPoint)) {
						dragBeganOnStartNode = true;
						dragBeganOnEndNode = false;
						dragBeganOnNormalNode = false;
						
					}
					
					if (network.getEndNode() != null && 
							network.getEndNode().boundariesContainPoint(
									eventPoint)) {
						
						dragBeganOnEndNode = true;
						dragBeganOnStartNode = false;
						dragBeganOnNormalNode = false;
						
					}
					
					if (network.hasStartAndEndNodes() && 
							!(network.getStartNode().boundariesContainPoint(eventPoint) ||
									network.getEndNode().boundariesContainPoint(eventPoint))) {
						
						dragBeganOnNormalNode = true;
						dragBeganOnStartNode = false;
						dragBeganOnEndNode = false;
						
					}
					
				}
				
				if (dragBeganOnStartNode || dragBeganOnEndNode || dragBeganOnNormalNode) {

					for (Node node : network.getNodes()) {

						if (node.boundariesContainPoint(eventPoint)) {

							if (!node.isWall()) {
								
								if (dragBeganOnStartNode && !node.equals(network.getEndNode())) {

									network.setStartNode(node);
									currentState = null;
									network.setPath(null);

								} else if (dragBeganOnEndNode && !node.equals(network.getStartNode())) {

									network.setEndNode(node);
									currentState = null;
									network.setPath(null);

								} else if (dragBeganOnNormalNode && 
										!(node.equals(network.getStartNode()) || 
												node.equals(network.getEndNode()))) {
									
									node.setWall(true);
									
								}
							}

						}

					}

//					for (Node node : network.getNodes()) {
//						
//						if (node.boundariesContainPoint(eventPoint) && !node.isWall()) {
//							
//							if (dragBeganOnStartNode && !node.equals(network.getEndNode())) {
//								
//								network.setStartNode(node);
//								currentState = null;
//								network.setPath(null);
//							
//							} else {
//								
//								network.setEndNode(node);
//								currentState = null;
//								network.setPath(null);
//								
//							}
//							
//						}
//						
//					}
					
				}
				/*else if (dragBeganOnNormalNode) {
					
					for (Node node : network.getNodes()) {
						
						if (node.boundariesContainPoint(eventPoint) && 
								!(node.equals(network.getStartNode()) || 
								node.equals(network.getEndNode()))) {
							
							node.setWall(true);
							
						}
						
					}
					
					
				}*/
				
				mouseBeingDragged = true;
				
			} else if (arg0.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
				
				mouseBeingDragged = false;
				eventPoint = null;
				
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
					
					network.clearColors();
					
					if (network.hasStartAndEndNodes()) {
						
						if (currentState == null) {

							currentState = new AlgorithmState(network);

						} else {
							
							if (currentState.isPathFound()) {
								
								currentState = new AlgorithmState(network);
								network.setPath(null);
								
							} else {
								
								currentState = pathFinder.performSingleStep(currentState);
							}

						}
					}
					
				}
				if (code.equals("W")) {
					
					// Generate new WebNetwork
					setNetwork(new WebNetwork(60, 2, 20, 10, 
							Color.WHITE, Color.BLACK, 
							50, 50, WIDTH - 100, HEIGHT - 100));
					currentState = null;
					
				} else if (code.equals("G")) {
					
					// Generate new GridNetwork
					setNetwork(new GridNetwork(Color.GRAY, Color.BLACK, 15, 50, 50, 
							WIDTH - 100, HEIGHT - 100, true));
					currentState = null;
					
				} else if (code.equals("F")) {
					
					// Connect start and end nodes and set the path in the Network
					if (network.getStartNode() != null && network.getEndNode() != null) {
						
						Path path = pathFinder.getShortestPath(network);
						
						network.setPath(path);
						
					}
					
				} else if (code.equals("P")) {
					
					beep.play();
					
				}
				
			} 
			
		}


	}

}