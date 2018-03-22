package com.aticatac.ui.tutorial;

import java.awt.Point;
import java.util.ArrayList;

import com.aticatac.Main;
import com.aticatac.lobby.ClientInfo;
import com.aticatac.networking.client.UDPClient;
import com.aticatac.rendering.display.Renderer;
import com.aticatac.sound.SoundManager;
import com.aticatac.ui.mainmenu.MainMenu;
import com.aticatac.ui.overlay.Overlay;
import com.aticatac.ui.overlay.PauseMenu;
import com.aticatac.ui.utils.UIDrawer;
import com.aticatac.utils.Controller;
import com.aticatac.utils.GameState;
import com.aticatac.utils.SystemSettings;
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

public class MultiPlayer extends Scene {

	private int displayWidth;
	private int displayHeight;
	private Renderer renderer;
	private UDPClient client;
	private Stage stage;

	public MultiPlayer(Group root, UDPClient newClient, Stage newStage, Scene mainMenu) {
		super(root);
		this.client = newClient;
		this.stage = newStage;

		// network
		System.out.println("starting waiting");
		while (client.getModel() == null)
			System.out.println("wait");
		;
		System.out.println("finished waiting for world");

		// init display stuff
		this.displayWidth = SystemSettings.getScreenWidth();
		this.displayHeight = SystemSettings.getScreenHeight();
		this.renderer = new Renderer();
		Overlay overlay = new Overlay();
		System.out.println("added renderer");
		Canvas canvas = new Canvas(displayWidth, displayHeight);
		root.getChildren().add(canvas);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		Player player = new Player(Controller.REAL, "id", 2);
		player.setPosition(new Point(50, 50));
		System.out.println("added player");
		// add key event listeners
		ArrayList<KeyCode> input = new ArrayList<KeyCode>();
		SoundManager m = new SoundManager(Main.soundEnabled);
		m.playBgBattle();

		PauseMenu pauseMenu = new PauseMenu(stage, mainMenu);

		// on key down, keycode is added to input array
		setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode code = e.getCode();
				if (code == (KeyCode.ESCAPE)) {
					System.out.println("ESCAPE PRESSED");
					pauseMenu.togglePaused();
  				} else {
					if (!input.contains(code)) {
						input.add(code);
					}
				}
			}
		});

		// on key release, keycode is removed.
		setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode code = e.getCode();
				input.remove(code);
			}
		});

		// updates player looking direction based on mouse pointer when mouse moves.
		setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				pauseMenu.handleHover(new Point((int) me.getX(), (int) me.getY()));
				Point p = player.getPosition();
				double dy = me.getY() - p.y; // y axis goes down
				double dx = me.getX() - p.x;
				double r = 0.0;

				// upper right angles
				if (dx > 0 && dy < 0) {
					r = Math.abs(Math.atan(dx / dy));
				}

				// lower right angles
				if (dx > 0 && dy > 0) {
					r = (0.5 * Math.PI) + Math.abs(Math.atan(dy / dx));
				}

				// lower left angles
				if (dx < 0 && dy > 0) {
					r = Math.PI + Math.abs(Math.atan(dx / dy));
				}

				// upper left angles
				if (dx < 0 && dy < 0) {
					r = (1.5 * Math.PI) + Math.abs(Math.atan(dy / dx));
				}

				player.setLookDirection(r);

			}
		});

		// handle shooting with mouse
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
  	        	int result = pauseMenu.handleClick();
				m.playShoot(player);
				client.sendData("click:" + (int) me.getX() + ":" + (int) me.getY());
			}
		});

		// sets up an AnimationTimer to update the display
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				client.sendData("input:" + input.toString() + ":" + (int) (player.getLookDirection() * 1000));

				World world = client.getModel();
				renderer.setWorld(world);

				ClientInfo myInfo = client.myInfo();

				try {
					Player p = (world.getPlayerById(myInfo.getID()));
					player.setPosition(p.getPosition());
				} catch (Exception e) {
					// shhhh, let's just pretend this never happened
				}

				renderer.render(gc);
				pauseMenu.draw(gc);
				overlay.drawOverlay(gc, world, myInfo.getID());
				if (world.getGameState() == GameState.PLAYING) {
  	        		world.update();
  	        		//world.handleInput(input, player.getLookDirection(), player.getIdentifier());
  	        	}
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

			
			}
		}.start();
	}
}
