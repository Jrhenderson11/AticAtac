package com.aticatac.ui.tutorial;

import java.awt.Point;
import java.util.ArrayList;

import com.aticatac.rendering.display.Renderer;
import com.aticatac.ui.mainmenu.MainMenu;
import com.aticatac.ui.overlay.Overlay;
import com.aticatac.ui.overlay.PauseMenu;
import com.aticatac.ui.utils.UIDrawer;
import com.aticatac.utils.Controller;
import com.aticatac.utils.GameState;
import com.aticatac.utils.SystemSettings;
import com.aticatac.world.AIPlayer;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class SinglePlayer extends Scene {
	
	public World world;

	/**
	 * Creates a single player game
	 * @param root something
	 * @param primaryStage The Stage this is a child of
	 * @param mainMenu The MainMenu this was launched from, used for returning in pause menu
	 */
	public SinglePlayer(Group root, Stage primaryStage, MainMenu mainMenu) {
		super(root);
		Renderer renderer = new Renderer();
		Canvas canvas = new Canvas(SystemSettings.getScreenWidth(), SystemSettings.getScreenHeight());
        root.getChildren().add(canvas);
        Overlay overlay = new Overlay();
        
        //world
        this.world = new World(new Level(100, 100));
        world.newRound();
        renderer.setWorld(world);
        
        Player player = new Player(Controller.REAL, "player", 2);
        AIPlayer ai1 = new AIPlayer(Controller.AI, world, "ai 1", 3);
        AIPlayer ai2 = new AIPlayer(Controller.AI, world, "ai 2", 4);
        AIPlayer ai3 = new AIPlayer(Controller.AI, world, "ai 3", 5);
        
        world.addPlayer(player);
        world.addPlayer(ai1);
        world.addPlayer(ai2);
        world.addPlayer(ai3);
        
        PauseMenu pauseMenu = new PauseMenu(primaryStage, mainMenu);
        
        //add key event listeners
  		ArrayList<KeyCode> input = new ArrayList<KeyCode>();
  		
  		//on key down, keycode is added to input array
  		setOnKeyPressed(new EventHandler<KeyEvent>() {
  			public void handle(KeyEvent e) {
  				KeyCode code = e.getCode();
  				if (!input.contains(code)) {
  					input.add(code);
  				}
  				if (code == KeyCode.J) {
  					for (Player player: world.getPlayers()) {
  						System.out.println(player.getIdentifier() + " controls: " + world.getLevel().getPercentTiles(player.getColour()) + "%");
  					}
  				}
  				if (code == KeyCode.ESCAPE) {
  					pauseMenu.togglePaused();
  					input.remove(code);
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
  	        	pauseMenu.handleHover(new Point((int) me.getX(), (int) me.getY()));
  	        	
  	        	Point p = player.getPosition();
  	        	double dy = (int) SystemSettings.getDescaledY(me.getY()) - p.y; //y axis goes down
  	        	double dx = (int) SystemSettings.getDescaledX(me.getX()) - p.x;
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
  	        		player.getGun().fire(player.getLookDirection(), 
  	        							new Point((int) SystemSettings.getDescaledX(me.getX()), 
  	        									  (int) SystemSettings.getDescaledY(me.getY())), 
  	        							world);
  	        	}
  	        	pauseMenu.handleClick();
  	        }
  		});
  		
  		
  		
  		new AnimationTimer() {
  	        public void handle(long currentNanoTime) {
  	        	canvas.setWidth(SystemSettings.getScreenWidth());
  	        	canvas.setHeight(SystemSettings.getScreenHeight());
  	        	GraphicsContext gc = canvas.getGraphicsContext2D();
  	        	//update world
  	        	if (world.getGameState() == GameState.PLAYING) {
  	        		world.update();
  	        		world.handleInput(input, player.getLookDirection(), player.getIdentifier());
  	        	}
  	        	
  	        	//draw scene
  	        	renderer.render(gc);
  	        	
  	        	//draw overlay
  	        	overlay.drawOverlay(gc, world, player.getIdentifier());
  	        	
  	        	//check for round over
  	        	if (world.getGameState() == GameState.OVER) {
  	        		if (world.getWinner() != null) {
  	        			Color color = new Color(0, 0, 0, 0.7f);
  	        			gc.setFill(color);
  	        			gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
  	        			gc.setTextAlign(TextAlignment.CENTER);
  	        			gc.setFill(Color.WHITE);
  	        			gc.setFont(UIDrawer.OVERLAY_FONT_SMALL);
  	        			gc.fillText("Winner is: " + world.getWinner().getIdentifier(), SystemSettings.getScreenWidth()/2, SystemSettings.getScreenHeight()/2);
  	        		}
  	        	}
  	        	
  	        	//check for ready message
  	        	if (world.getGameState() == GameState.READY) {
        			Color color = new Color(0, 0, 0, 0.7f);
        			gc.setFill(color);
        			gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        			gc.setTextAlign(TextAlignment.CENTER);
        			gc.setFill(Color.WHITE);
        			gc.setFont(UIDrawer.OVERLAY_FONT_SMALL);
        			gc.fillText("Ready: " + world.getRoundTime(), SystemSettings.getScreenWidth()/2, SystemSettings.getScreenHeight()/2);
  	        	}
  	        	pauseMenu.draw(gc);
  	        }
  	    }.start();   
	}

}
