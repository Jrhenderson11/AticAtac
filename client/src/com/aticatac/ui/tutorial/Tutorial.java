package com.aticatac.ui.tutorial;

import java.awt.MouseInfo;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Tutorial extends Scene {
	
	private int displayWidth;
	private int displayHeight;
	private Level level;
	private Renderer renderer;
	
	public Tutorial (Group root) {
        super(root);
        
        //init display stuff
        this.displayWidth = SystemSettings.getNativeWidth();
        this.displayHeight = SystemSettings.getNativeHeight();
        this.renderer = new Renderer(displayWidth, displayHeight);
        
        Canvas canvas = new Canvas(displayWidth, displayHeight);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        //init world
        this.level = new Level(50, 50);
        level.loadMap("client/assets/maps/map.txt");
        World world = new World(level);
        
        renderer.setWorld(world);
        
        //init player
        Player player = new Player(Controller.REAL, 2, Color.YELLOW);
        player.setPosition(new Point(50, 50));
        world.addPlayer(player);
        
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
  	        	double r = Math.atan(dy / dx);
  	        	
  	        	//r = 0 means player looking to right.
  	        	//r increases clockwise to 1.5pi, then drops down to -0.5pi.
  	        	//may fix later but shouldn't affect trig calculations
  	        	
  	        	//allows looking to left
  	        	if (dx < 0)
  	        		r += Math.PI;
  	        	
  	        	player.setLookDirection(r);
  	        	//System.out.println("dx: " + dx + " | dy: " + dy + " | a: " + player.getLookDirection());
  	        }
  	    });
  		
  		//sets up an AnimationTimer to update the display
  		new AnimationTimer() {
  	        public void handle(long currentNanoTime) {
  	        	//handle movement, reverting moves when detecting collision
  	        	//left
  	        	if (input.contains(KeyCode.A)) {
  	        		player.move(-2, 0);
  	        		Point p = getMapCoords(player.getPosition());
  	        		if (level.getGrid()[p.x][p.y] == 1) {   //if the grid coordinate of player is on a wall tile (1) in the level grid.
  	        			player.move(2, 0);
  	        		}
  	        	}
  	        	//right
  	        	if (input.contains(KeyCode.D)) {
  	        		player.move(2, 0);
  	        		Point p = getMapCoords(player.getPosition());
  	        		if (level.getGrid()[p.x][p.y] == 1) {
  	        			player.move(-2, 0);
  	        		}
  	        	}
  	        	//up
  	        	if (input.contains(KeyCode.W)) {
  	        		player.move(0, -2);
  	        		Point p = getMapCoords(player.getPosition());
  	        		if (level.getGrid()[p.x][p.y] == 1) {
  	        			player.move(0, 2);
  	        		}
  	        	}
  	        	//down
  	        	if (input.contains(KeyCode.S)) {
  	        		player.move(0, 2);
  	        		Point p = getMapCoords(player.getPosition());
  	        		if (level.getGrid()[p.x][p.y] == 1) {
  	        			player.move(0, -2);
  	        		}
  	        	}
  	        	
  	        	//claim walking territory
  	        	Point p = getMapCoords(player.getPosition());
  	        	if (level.getGrid()[p.x][p.y] == 0) {
  	        		level.updateCoords(p.x, p.y, player.getIdentifier());
  	        	}
  	        	
  	        	//draw scene
  	        	renderer.render(gc);
  	        }
  	    }.start();   
	}
	
	/**
	 * Get the map coordinates from the on screen display position
	 * @param displayPosition
	 * @return
	 */
	private Point getMapCoords(Point displayPosition) {
		int tileWidth = displayWidth / level.getWidth();
		int tileHeight = displayHeight / level.getHeight();
		return new Point((displayPosition.x / tileWidth), (displayPosition.y / tileHeight));
	}
}
