package com.aticatac.rendering.display;

import java.awt.Dimension;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class GameWindow extends Stage {
	
	private Group root;
	private Scene scene;
	private Canvas canvas;
	private Renderer renderer;
	
	/**
	 * Creates a game window with the given dimensions
	 * @param dimension The dimension of the window
	 */
	public GameWindow(Dimension dimension) {
		this.root = new Group();      //javafx stuff
		this.scene = new Scene(root); 
		this.canvas = new Canvas(dimension.width, dimension.height);
		this.renderer = new Renderer(dimension);
		setScene(scene);
		setWidth(dimension.getWidth());
		setHeight(dimension.getHeight());
		setResizable(false);
		root.getChildren().add(canvas);
	}
	/**
	 * Constructor using width/height instead of Dimension
	 * @param width
	 * @param height
	 */
	public GameWindow(int width, int height) {
		this(new Dimension(width, height));
	}
	
	/**
	 * Returns the renderer for this window
	 * @return The Renderer object
	 */
	public Renderer getRenderer() {
		return renderer;
	}
	
	/**
	 * Redraws the screen
	 */
	public void update() {
		renderer.render(canvas.getGraphicsContext2D());
	}
}	
