package com.aticatac.ui.tutorial;

import java.awt.Point;
import java.util.ArrayList;

import com.aticatac.rendering.display.Renderer;
import com.aticatac.ui.overlay.Overlay;
import com.aticatac.utils.Controller;
import com.aticatac.utils.SystemSettings;
import com.aticatac.world.AIPlayer;
import com.aticatac.world.Level;
import com.aticatac.world.Player;
import com.aticatac.world.World;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class AiDemo extends Scene {
	
	private int displayWidth;
	private int displayHeight;
	private Level level;
	private Renderer renderer;
	private boolean tips;
	
	private double dir;
	
	public AiDemo (Group root) {
        super(root);
        
        Image image = new Image("file:assets/sprites/crosshair.png");
        this.setCursor(new ImageCursor(image));
        
        //init display stuff
        this.displayWidth = SystemSettings.getNativeWidth();
        this.displayHeight = SystemSettings.getNativeHeight();
        this.renderer = new Renderer(displayWidth, displayHeight);
        this.tips = true;
        
        Canvas canvas = new Canvas(displayWidth, displayHeight);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        this.level = new Level(50, 50);
        //level.randomiseMap();
        level.loadMap("assets/maps/map.txt");
        World world = new World(level);
        
        renderer.setWorld(world);
        Player player = new Player(Controller.REAL, "player", 2);
        player.setPosition(new Point(50, 50));
        world.addPlayer(player);
        AIPlayer aiPlayer = new AIPlayer(Controller.AI, world, "aiPlayer", 3);
        world.addPlayerWithoutPosition(aiPlayer);
        
        /* ================ */
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
  	        	
  	        	dir = r;
  	        }
  	    });
  		
  		//handle shooting with mouse
  		setOnMouseClicked(new EventHandler<MouseEvent>() {
  	        @Override
  	        public void handle(MouseEvent me) {
  	        	if (player.getGun() != null) {
  	        		player.getGun().fire(player.getLookDirection(), world.displayPositionToCoords(new Point((int) me.getX(), (int) me.getY())), world);
  	        	}
  	        }
  		});
  		
  		//sets up an AnimationTimer to update the display
  		new AnimationTimer() {
  	        public void handle(long currentNanoTime) {
  	        	
  	        	if (input.contains(KeyCode.H)) {
  	        		if (tips)
  	        			tips = false;
  	        		else
  	        			tips = true;
  	        		input.remove(KeyCode.H);
  	        	}
  	        	
  	        	//claim walking territory
  	        	
  	        	
  	        
  	        	
  	        	world.handleInput(input, dir, player.getIdentifier());
  	        	//update world
  	        	world.update();
  	        	

  	        	//draw scene
  	        	renderer.render(gc);
  	        	Overlay overlay = new Overlay();
  	        	overlay.drawOverlay(gc, world, player.getIdentifier());
  	        	//draw help tips
  	        }
  	    }.start();   
	}
}
