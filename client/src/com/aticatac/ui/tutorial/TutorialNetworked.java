package com.aticatac.ui.tutorial;

import java.awt.Point;
import java.util.ArrayList;

import com.aticatac.networking.client.UDPClient;
import com.aticatac.networking.globals.Globals;
import com.aticatac.rendering.display.Renderer;
import com.aticatac.utils.Controller;
import com.aticatac.utils.SystemSettings;
import com.aticatac.world.Level;
import com.aticatac.world.Player;
import com.aticatac.world.World;
import com.aticatac.world.items.ShootGun;
import com.aticatac.world.items.SplatGun;
import com.aticatac.world.items.SprayGun;

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

public class TutorialNetworked extends Scene {
	
	private int displayWidth;
	private int displayHeight;
	private Level level;
	private Renderer renderer;
	private UDPClient client;
	private World world;
	
	public TutorialNetworked (Group root, UDPClient newClient) {
        super(root);
        this.client = newClient;
        client.joinLobby(1, "password");
        client.startGame();
        //init display stuff
        this.displayWidth = SystemSettings.getNativeWidth();
        this.displayHeight = SystemSettings.getNativeHeight();
        this.renderer = new Renderer(displayWidth, displayHeight);
        
        Canvas canvas = new Canvas(displayWidth, displayHeight);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        /* ================ */
        System.out.println("waiting for network");
        while (client.getStatus() != Globals.IN_GAME) {
        	System.out.println(client.getStatus());
        	System.out.println(client.getStatus() == Globals.IN_GAME);
        }
        // 	MOVE TO SRV
        World world = client.getModel();
        /* ================ */
        
        renderer.setWorld(world);
        /* ================ */
        //	MOVE TO SRV
        Player player = new Player(Controller.REAL, 2, 2);
        player.setPosition(new Point(50, 50));
        //world.addPlayer(player);
    	Double dir = new Double(0);
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
  	        	
  	        	player.setLookDirection(r);
  	        	
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
  	        	client.sendData("input:" + input.toString() + ":" + (int) (player.getLookDirection() * 1000));
  	        	World world = client.getModel();
  	        	renderer.setWorld(world);
  	        	player.setPosition(((Player) world.getPlayers().toArray()[0]).getPosition());
  	        	//draw scene
  	        	renderer.render(gc);
  	        }
  	    }.start();   
	}
}