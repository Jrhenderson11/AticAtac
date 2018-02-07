package com.aticatac.rendering.tests;


import java.util.ArrayList;

import com.aticatac.rendering.components.DisplayComponent;
import com.aticatac.rendering.display.GameWindow;
import com.aticatac.rendering.display.RenderLayer;
import com.aticatac.world.Level;
import com.aticatac.world.World;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Test extends Application {

	public Test() {
		
	}

	@Override
	public void start(Stage stage) throws Exception {
		DisplayComponent c1 = new DisplayComponent("assets/test/tile1.png"); //a component to display
		RenderLayer layer1 = new RenderLayer("default");
		
		Level map = new Level(50, 50);
		map.loadMap("assets/maps/map.txt");
		World world = new World(map);
		
		GameWindow window = new GameWindow(1920, 1080);
		window.getRenderer().setWorld(world);
		
		layer1.add(c1); //add the component to this layer
		window.getRenderer().addLayer(layer1); //add this layer to the renderer
	
		//add key event listeners
		ArrayList<String> input = new ArrayList<String>();
		
		window.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				String code = e.getCode().toString();
				if (!input.contains(code)) {
					input.add(code);
				}
			}
	    });
	 
		window.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
	        public void handle(KeyEvent e) {
	            String code = e.getCode().toString();
	            input.remove(code);
	        }
	    });
		
		//sets up an AnimationTimer to update the display
		new AnimationTimer() {
	        public void handle(long currentNanoTime) {
	        	if (input.contains("LEFT")) {
	        		c1.translate(-2, 0);
	        	}
	        	if (input.contains("RIGHT")) {
	        		c1.translate(2, 0);
	        	}
	        	if (input.contains("UP")) {
	        		c1.translate(0, -2);
	        	}
	        	if (input.contains("DOWN")) {
	        		c1.translate(0, 2);
	        	}
	        	window.update();
	        }
	    }.start();
		
		window.show();
		
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
