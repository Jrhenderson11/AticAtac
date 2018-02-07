package com.aticatac.ui.tutorial;

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
	
	private int displayWidth;
	private int displayHeight;
	private Level level;
	
	public Tutorial (Group root) {
        super(root);
        
        this.displayWidth = SystemSettings.getNativeWidth();
        this.displayHeight = SystemSettings.getNativeHeight();
        
        this.level = new Level(50, 50);
        level.loadMap("client/assets/maps/map.txt");
        
        Canvas canvas = new Canvas(displayWidth, displayHeight);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Renderer renderer = new Renderer(displayWidth, displayHeight);
        
        World world = new World(level);
        Player player = new Player(Controller.REAL, 2, Color.YELLOW);
        player.setPosition(new Point(50, 50));
        
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
  	        		System.out.println("Updating: " + p.x + ", " + p.y);
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
