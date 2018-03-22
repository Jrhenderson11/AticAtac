package com.aticatac.ui.tutorial;

import java.awt.Point;
import java.util.ArrayList;

import com.aticatac.Main;
import com.aticatac.rendering.display.Renderer;
import com.aticatac.sound.SoundManager;
import com.aticatac.ui.mainmenu.MainMenu;
import com.aticatac.ui.overlay.Overlay;
import com.aticatac.ui.overlay.PauseMenu;
import com.aticatac.ui.utils.UIDrawer;
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
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Tutorial extends Scene {
	
	private int displayWidth;
	private int displayHeight;
	private Level level;
	private Renderer renderer;
	private boolean tips;
	private	ArrayList<Point> visited = new ArrayList<Point>();
	private Overlay overlay;
	
	public Tutorial (Group root, Stage primaryStage, MainMenu mainMenu) {
        super(root);
        
        //init display stuff
        this.displayWidth = SystemSettings.getScreenWidth();
        this.displayHeight = SystemSettings.getScreenHeight();
        this.renderer = new Renderer();
        this.overlay = new Overlay();
        this.tips = true;
        
        Canvas canvas = new Canvas(displayWidth, displayHeight);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        this.level = new Level(100, 100);
        //level.randomiseMap();
        level.loadMap("assets/maps/tutorial.txt");
        World world = new World(level);
        
        renderer.setWorld(world);
        Player player = new Player(Controller.REAL, "player", 2);
        player.setPosition(new Point(25, 25));
        world.addPlayerWithoutPosition(player);
        
        Point[] points = {new Point(100, 200), new Point(275, 200), new Point(400, 200)};
        
        //testing with gun boxes
        world.spawnShootGunBox(points[0]);
        world.spawnSplatGunBox(points[1]);
        world.spawnSprayGunBox(points[2]);
        
        PauseMenu pauseMenu = new PauseMenu(primaryStage, mainMenu);
        
        SoundManager m = new SoundManager(Main.soundEnabled);
        m.playBgBattle();
        
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
  	        		m.playShoot(player);
  	        		player.getGun().fire(player.getLookDirection(), 
  							new Point((int) SystemSettings.getDescaledX(me.getX()), 
  									  (int) SystemSettings.getDescaledY(me.getY())), 
  							world);
  	        	}
  	        	pauseMenu.handleClick();
  	        }
  		});
  		
  		//sets up an AnimationTimer to update the display
  		new AnimationTimer() {
  	        public void handle(long currentNanoTime) {
  	        	//resize canvas to screen height for matching window resizing
  	        	canvas.setWidth(SystemSettings.getScreenWidth());
  	        	canvas.setHeight(SystemSettings.getScreenHeight());
  	        	GraphicsContext gc = canvas.getGraphicsContext2D();
  	        	//handle movement, reverting moves when detecting collision
				// left
				if (input.contains(KeyCode.A) && !input.contains(KeyCode.W) && !input.contains(KeyCode.S) && !input.contains(KeyCode.D)) {

					player.move(-2, 0);
					Point p = world.displayPositionToCoords(player.getPosition());
					if (level.getGrid()[p.x][p.y] == 1) {
						player.move(2, 0);
					}
				}
				// System.out.println(input.size());
				// right
				if (input.contains(KeyCode.D) && !input.contains(KeyCode.W) && !input.contains(KeyCode.A) && !input.contains(KeyCode.S)) {

					player.move(2, 0);
					Point p = world.displayPositionToCoords(player.getPosition());
					if (level.getGrid()[p.x][p.y] == 1) {
						player.move(-2, 0);
					}
				}
				// System.out.println(input.size());
				// up
				if (input.contains(KeyCode.W) && !input.contains(KeyCode.S) && !input.contains(KeyCode.A) && !input.contains(KeyCode.D)) {

					player.move(0, -2);
					Point p = world.displayPositionToCoords(player.getPosition());
					if (level.getGrid()[p.x][p.y] == 1) {
						player.move(0, 2);
					}
				}
				// System.out.println(input.size());
				// down
				if (input.contains(KeyCode.S) && !input.contains(KeyCode.W) && !input.contains(KeyCode.A) && !input.contains(KeyCode.D)) {

					player.move(0, 2);
					Point p = world.displayPositionToCoords(player.getPosition());
					if (level.getGrid()[p.x][p.y] == 1) {
						player.move(0, -2);
					}
				}
				// System.out.println(input.size());
				// down & right
				if (input.contains(KeyCode.S) && input.contains(KeyCode.D) && !input.contains(KeyCode.W) && !input.contains(KeyCode.A)){
					player.move(0, 1);
					Point pS = world.displayPositionToCoords(player.getPosition());
					if (level.getGrid()[pS.x][pS.y] == 1) {
						player.move(0, -1);
					}
					player.move(1, 0);
					Point pD = world.displayPositionToCoords(player.getPosition());
					if (level.getGrid()[pD.x][pD.y] == 1) {
						player.move(-1, 0);
					}
				}
				// System.out.println(input.size());
				// down & left
				if (input.contains(KeyCode.S) && input.contains(KeyCode.A) && !input.contains(KeyCode.W) && !input.contains(KeyCode.D)){
					player.move(0, 1);
					Point pS = world.displayPositionToCoords(player.getPosition());
					if (level.getGrid()[pS.x][pS.y] == 1) {
						player.move(0, -1);
					}
					player.move(-1, 0);
					Point pA = world.displayPositionToCoords(player.getPosition());
					if (level.getGrid()[pA.x][pA.y] == 1) {
						player.move(1, 0);
					}
				}
				// System.out.println(input.size());
				// up & right
				if (input.contains(KeyCode.W) && input.contains(KeyCode.D) && !input.contains(KeyCode.A) && !input.contains(KeyCode.S)){
					player.move(0, -1);
					Point pW = world.displayPositionToCoords(player.getPosition());
					if (level.getGrid()[pW.x][pW.y] == 1) {
						player.move(0, 1);
					}
					player.move(1, 0);
					Point pD = world.displayPositionToCoords(player.getPosition());
					if (level.getGrid()[pD.x][pD.y] == 1) {
						player.move(-1, 0);
					}
				}
				// System.out.println(input.size());
				// up & left
				if (input.contains(KeyCode.W) && input.contains(KeyCode.A) && !input.contains(KeyCode.S) && !input.contains(KeyCode.D)){
					player.move(0, -1);
					Point pW = world.displayPositionToCoords(player.getPosition());
					if (level.getGrid()[pW.x][pW.y] == 1) {
						player.move(0, 1);
					}
					player.move(-1, 0);
					Point pA = world.displayPositionToCoords(player.getPosition());
					if (level.getGrid()[pA.x][pA.y] == 1) {
						player.move(1, 0);
					}
				}
  	        	//Gun spawn in, using for testing, remove in game
  	        	//Shoot gun
  	        	if (input.contains(KeyCode.I)) {
  	        		player.setGun(new ShootGun(player));
  	        		input.remove(KeyCode.I);
  	        	}
  	        	//Splat gun
  	        	if (input.contains(KeyCode.O)) {
  	        		player.setGun(new SplatGun(player));
  	        		input.remove(KeyCode.O);
  	        		
  	        	}
  	        	//Spray gun
  	        	if (input.contains(KeyCode.P)) {
  	        		player.setGun(new SprayGun(player));
  	        		input.remove(KeyCode.P);
  	        	}
  	        	if (input.contains(KeyCode.H)) {
  	        		if (tips)
  	        			tips = false;
  	        		else
  	        			tips = true;
  	        		input.remove(KeyCode.H);
  	        	}
  	        	
  	        	//claim walking territory
  	        	Point p = world.displayPositionToCoords(player.getPosition());
  	        	if (level.getGrid()[p.x][p.y] == 0) {
  	        		level.updateCoords(p.x, p.y, player.getColour());
  	        	}

  	        	/* ================ */

  	        	
  	        	//update world
  	        	world.update();
  	        	

  	        	//draw scene
  	        	renderer.render(gc);
  	        	
  	        	
  	        	//draw overlay
  	        	overlay.drawOverlay(gc, world, player.getIdentifier());
  	        	
  	        	int bigX = (p.x/(world.getLevel().getWidth()/10));
  	        	int bigY = (p.y/(world.getLevel().getHeight()/10));
  	        	String[][] texts = new String[10][10];
  	        	Point[][] pos = new Point[10][10];
  	        	
  	        	for (int i=0;i<10;i++) {
  	        		for (int j=0;j<10;j++) {
  	  	        		pos[i][j] = new Point(400, 400);
  	  	        	}
  	        	}
  	        	
  	        	texts[0][0]  ="Use WASD to move";
  	        	pos[0][0] = new Point((int) SystemSettings.getScaledX(60), (int) SystemSettings.getScaledY(75));
  	        	texts[2][0]  ="you paint the ground when you walk";
  	        	pos[2][0] = new Point((int) SystemSettings.getScaledX(255), (int) SystemSettings.getScaledY(20));
  	        	texts[2][1]  ="you paint the ground when you walk";
  	        	pos[2][1] = new Point((int) SystemSettings.getScaledX(255), (int) SystemSettings.getScaledY(20));
  	        	
  	        	 	        	
  	        	texts[6][0]  ="and walk faster on your paint";
	        	pos[6][0] = new Point((int) SystemSettings.getScaledX(330), (int) SystemSettings.getScaledY(85));
	        	texts[6][1]  ="and walk faster on your paint";
	        	pos[6][1] = new Point((int) SystemSettings.getScaledX(330), (int) SystemSettings.getScaledY(85));
	        	
	        	
	        	texts[8][0]  ="Use mouse to aim";
	        	pos[8][0] = new Point((int) SystemSettings.getScaledX(530), (int) SystemSettings.getScaledY(55));
	        	texts[8][1]  ="Use mouse to aim";
	        	pos[8][1] = new Point((int) SystemSettings.getScaledX(530), (int) SystemSettings.getScaledY(55));
	        	texts[8][4]  ="and left click to shoot";
	        	pos[8][4] = new Point((int) SystemSettings.getScaledX(530), (int) SystemSettings.getScaledY(325));
  	        	
	        	
	        	
	        	
	        	//Gunbox descriptons, left to right
  	        	texts[1][5]  ="     Shoot Cannon\n\n\n\n\n\n\n\n\n\n\n\n\n   Aim at other players";
	        	pos[1][5] = new Point((int) SystemSettings.getScaledX(55), (int) SystemSettings.getScaledY(195));
  	        	
  	        	texts[4][5]  ="Splat Cannon\n\n\n\n\n\n\n\n\n\n\n\n\nCover areas in paint";
	        	pos[4][5] = new Point((int) SystemSettings.getScaledX(250), (int) SystemSettings.getScaledY(195));
	        	
	        	texts[4][6]  ="Splat Cannon\n\n\n\n\n\n\n\n\n\n\n\n\nCover areas in paint";
	        	pos[4][6] = new Point((int) SystemSettings.getScaledX(250), (int) SystemSettings.getScaledY(195));
	        	
  	        	texts[6][5]  ="Spray Cannon\n\n\n\n\n\n\n\n\n\n\n\n\nLong range area paint";
	        	pos[6][5] = new Point((int) SystemSettings.getScaledX(390), (int) SystemSettings.getScaledY(195));
	        	
	        	texts[6][6]  ="Spray Cannon\n\n\n\n\n\n\n\n\n\n\n\n\nLong range area paint";
	        	pos[6][6] = new Point((int) SystemSettings.getScaledX(390), (int) SystemSettings.getScaledY(195));
	        	
	        	
	        	
	        	gc.setFill(Color.WHITE);
  	        	gc.setFont(UIDrawer.TUTORIAL_FONT);
  	        	
  	        	//draw help tips
  	        	if (tips) {
  	        		if (!pointCheck(new Point(bigX, bigY), visited)) {
  	        			visited.add(new Point(bigX, bigY));
  	        		}
  	        		for (Point newP: visited) {
  	  	        		gc.fillText(texts[newP.x][newP.y], pos[newP.x][newP.y].x, pos[newP.x][newP.y].y);  	        			
  	        		}
  	        		

  	        	}
  	        	
  	        	pauseMenu.draw(gc);
  	        	
  	        }
  	    }.start();   
	}
	private boolean pointCheck(Point p, ArrayList<Point> list) {
		for (Point point: list) {
			if (p.x==point.x && p.y==point.y) {
				return true;
			}
		}
		return false;
	}

}
