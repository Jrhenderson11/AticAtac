package com.aticatac.ui.tutorial;

import java.awt.Point;
import java.util.ArrayList;

import com.aticatac.rendering.display.Renderer;
import com.aticatac.sound.SoundManager;
import com.aticatac.ui.overlay.Overlay;
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

public class Tutorial extends Scene {
	
	private int displayWidth;
	private int displayHeight;
	private Level level;
	private Renderer renderer;
	private boolean tips;
	private	ArrayList<Point> visited = new ArrayList<Point>();
	private Overlay overlay;
	
	public Tutorial (Group root) {
        super(root);
        
        //Image image = new Image("~/Documents/teamproj/aticatac/client/assets/sprites/crosshair.png");
        //Image image = new Image("assets/sprites/crosshair.png");
        
        Image image = new Image("file:assets/sprites/crosshair.png");
        this.setCursor(new ImageCursor(image));
        
        //init display stuff
        this.displayWidth = SystemSettings.getNativeWidth();
        this.displayHeight = SystemSettings.getNativeHeight();
        this.renderer = new Renderer(displayWidth, displayHeight);
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
        
        Point[] points = world.generateBoxSpawnPoints(3);
        for (Point point: points) {
        	System.out.println(point.toString());
        }
        
        //testing with gun boxes
        world.spawnShootGunBox(points[0]);
        world.spawnSplatGunBox(points[1]);
        world.spawnSprayGunBox(points[2]);
        
        SoundManager m = new SoundManager();
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
  	        		int choice = -1;
  	        		
  	        		if (!player.getGun().enoughPaint(player.getPaintLevel())){
  	        			choice = 0;
  	        		}else if (player.getGun().getClass() == ShootGun.class){
  	        			choice = 1;
  	        		}else if (player.getGun().getClass() == SplatGun.class){
  	        			choice = 2;
  	        		}else if (player.getGun().getClass() == SprayGun.class){
  	        			choice = 3;
  	        		}else{
  	        			System.out.println("Can't find gun type.");
  	        		}
  	        		
  	        		
  	        		m.playShoot(choice);
  	        		player.getGun().fire(player.getLookDirection(), new Point((int) me.getX(), (int) me.getY()), world);
  	        	}
  	        }
  		});
  		
  		//sets up an AnimationTimer to update the display
  		new AnimationTimer() {
  	        public void handle(long currentNanoTime) {
  	        	//handle movement, reverting moves when detecting collision
  	        	//left
  	        	if (input.contains(KeyCode.A)) {
  	        		player.move(-2, 0);
  	        		Point p = world.displayPositionToCoords(player.getPosition());
  	        		if (level.getGrid()[p.x][p.y] == 1) {   //if the grid coordinate of player is on a wall tile (1) in the level grid.
  	        			player.move(2, 0);
  	        		}
  	        	}
  	        	//right
  	        	if (input.contains(KeyCode.D)) {
  	        		player.move(2, 0);
  	        		Point p = world.displayPositionToCoords(player.getPosition());
  	        		if (level.getGrid()[p.x][p.y] == 1) {
  	        			player.move(-2, 0);
  	        		}
  	        	}
  	        	//up
  	        	if (input.contains(KeyCode.W)) {
  	        		player.move(0, -2);
  	        		Point p = world.displayPositionToCoords(player.getPosition());
  	        		if (level.getGrid()[p.x][p.y] == 1) {
  	        			player.move(0, 2);
  	        		}
  	        	}
  	        	//down
  	        	if (input.contains(KeyCode.S)) {
  	        		player.move(0, 2);
  	        		Point p = world.displayPositionToCoords(player.getPosition());
  	        		if (level.getGrid()[p.x][p.y] == 1) {
  	        			player.move(0, -2);
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
  	        	pos[0][0] = new Point(10, 75);
  	        	texts[2][0]  ="Use mouse to aim";
  	        	pos[2][0] = new Point(170, 20);
  	        	texts[2][1]  ="Use mouse to aim";
  	        	pos[2][1] = new Point(170, 20);
  	        	
  	        	
  	        	texts[4][2]  ="press O to select splat cannon";
	        	pos[4][2] = new Point(190, 105);
	        	
  	        	
  	        	texts[6][0]  ="and left click to shoot";
	        	pos[6][0] = new Point(330, 20);
	        	texts[6][1]  ="and left click to shoot";
	        	pos[6][1] = new Point(330, 20);
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


