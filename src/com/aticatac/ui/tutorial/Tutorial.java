package com.aticatac.ui.tutorial;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

import com.aticatac.rendering.display.Renderer;
import com.aticatac.utils.Controller;
import com.aticatac.utils.SystemSettings;
import com.aticatac.world.Level;
import com.aticatac.world.Player;
import com.aticatac.world.World;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class Tutorial extends Scene {
	
	public Tutorial (Group root) {
        super(root);
        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Renderer renderer = new Renderer(width, height);
        
        Level level = new Level(50, 50);
        level.loadMap("assets/maps/map.txt");
        
        
        World world = new World(level);
        Player player = new Player(Controller.REAL, 2, Color.GREEN);
        world.addPlayer(player);
        
        renderer.setWorld(world);
        
        //add key event listeners
  		ArrayList<KeyCode> input = new ArrayList<KeyCode>();
  		
  		setOnKeyPressed(new EventHandler<KeyEvent>() {
  			public void handle(KeyEvent e) {
  				KeyCode code = e.getCode();
  				if (!input.contains(code)) {
  					input.add(code);
  				}
  			}
  	    });
  	 
  		setOnKeyReleased(new EventHandler<KeyEvent>() {
  	        public void handle(KeyEvent e) {
  	            KeyCode code = e.getCode();
  	            input.remove(code);
  	        }
  	    });
  		
  		//sets up an AnimationTimer to update the display
  		new AnimationTimer() {
  	        public void handle(long currentNanoTime) {
  	        	if (input.contains(KeyCode.A)) {
  	        		player.move(-2, 0);
  	        	}
  	        	if (input.contains(KeyCode.D)) {
  	        		player.move(2, 0);
  	        	}
  	        	if (input.contains(KeyCode.W)) {
  	        		player.move(0, -2);
  	        	}
  	        	if (input.contains(KeyCode.S)) {
  	        		player.move(0, 2);
  	        	}
  	        	renderer.render(gc);
  	        }
  	    }.start();   
	}
}
