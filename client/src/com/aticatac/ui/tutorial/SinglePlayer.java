package com.aticatac.ui.tutorial;

import java.awt.Point;
import java.util.ArrayList;

import com.aticatac.rendering.display.Renderer;
import com.aticatac.ui.overlay.Overlay;
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
import javafx.scene.input.MouseEvent;

public class SinglePlayer extends Scene {

	public SinglePlayer(Group root) {
		super(root);
		
		//display
		Renderer renderer = new Renderer(SystemSettings.getNativeWidth(), SystemSettings.getNativeHeight());
		Canvas canvas = new Canvas(SystemSettings.getNativeWidth(), SystemSettings.getNativeHeight());
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Overlay overlay = new Overlay();
        
        //world
        World world = new World(new Level(100, 100));
        world.newRound();
        renderer.setWorld(world);
        
        Player player = new Player(Controller.REAL, "player", 2);
        Player ai1 = new Player(Controller.REAL, "ai 1", 3);
        Player ai2 = new Player(Controller.REAL, "ai 2", 4);
        Player ai3 = new Player(Controller.REAL, "ai 3", 5);
        
        world.addPlayer(player);
        world.addPlayer(ai1);
        world.addPlayer(ai2);
        world.addPlayer(ai3);
        
        //add key event listeners
  		ArrayList<KeyCode> input = new ArrayList<KeyCode>();
  		
  		//on key down, keycode is added to input array
  		setOnKeyPressed(new EventHandler<KeyEvent>() {
  			public void handle(KeyEvent e) {
  				KeyCode code = e.getCode();
  				if (!input.contains(code)) {
  					input.add(code);
  				}
  			}
  	    });
  		
  		//on key release, keycode is removed.
  		setOnKeyReleased(new EventHandler<KeyEvent>() {
  	        public void handle(KeyEvent e) {
  	            KeyCode code = e.getCode();
  	            input.remove(code);
  	        }
  	    });
  		
  	//updates player looking direction based on mouse pointer when mouse moves.
  		setOnMouseMoved(new EventHandler<MouseEvent>() {
  	        @Override
  	        public void handle(MouseEvent me) {
  	        	Point p = player.getPosition();
  	        	double dy = me.getY() - p.y; //y axis goes down
  	        	double dx = me.getX() - p.x;
  	        	double r = 0.0;
  	        	
  	        	//upper right angles
  	        	if (dx > 0 && dy < 0) {
  	        		r = Math.abs(Math.atan(dx / dy));
  	        	}
  	        		
  	        	//lower right angles
  	        	if (dx > 0 && dy > 0) {
  	        		r = (0.5 * Math.PI) + Math.abs(Math.atan(dy / dx));
  	        	}
  	        	
  	        	//lower left angles
  	        	if (dx < 0 && dy > 0) {
  	        		r = Math.PI + Math.abs(Math.atan(dx / dy));
  	        	}
  	        	
  	        	//upper left angles
  	        	if (dx < 0 && dy < 0) {
  	        		r = (1.5 * Math.PI) + Math.abs(Math.atan(dy / dx));
  	        	}
  	        	
  	        	player.setLookDirection(r);
  	        }
  	    });
  		
  		//handle shooting with mouse
  		setOnMouseClicked(new EventHandler<MouseEvent>() {
  	        @Override
  	        public void handle(MouseEvent me) {
  	        	if (player.getGun() != null) {
  	        		//m.playShoot();
  	        		player.getGun().fire(player.getLookDirection(), new Point((int) me.getX(), (int) me.getY()), world);
  	        	}
  	        }
  		});
  		
  		new AnimationTimer() {
  	        public void handle(long currentNanoTime) {
  	        	//update world
  	        	world.update();
  	        	world.handleInput(input, player.getLookDirection(), player.getIdentifier());
  	        	
  	        	//draw scene
  	        	renderer.render(gc);
  	        	
  	        	//draw overlay
  	        	overlay.drawOverlay(gc, world, player.getIdentifier());
  	        }
  	    }.start();   
	}

}
