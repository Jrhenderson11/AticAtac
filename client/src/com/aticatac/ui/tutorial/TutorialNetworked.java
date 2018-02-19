package com.aticatac.ui.tutorial;

import java.awt.Point;
import java.util.ArrayList;

import com.aticatac.lobby.ClientInfo;
import com.aticatac.networking.client.UDPClient;
import com.aticatac.networking.globals.Globals;
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
import ui.overlay.Overlay;

public class TutorialNetworked extends Scene {

	private int displayWidth;
	private int displayHeight;
	private Level level;
	private Renderer renderer;
	private UDPClient client;
	private World world;
	
	private void skipLobby() {
		client.joinLobby(1, "password");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.startGame();
//		client.setStatus(Globals.IN_GAME);
		System.out.println("waiting for network");
		
		while (client.getStatus() != Globals.IN_GAME) {
			System.out.println("waiting");
		}
		System.out.println("DONE");
		
	}

	public TutorialNetworked(Group root, UDPClient newClient) {
		super(root);
		this.client = newClient;
		// init display stuff
		this.skipLobby();
		System.out.println("skip lobby");
		this.displayWidth = SystemSettings.getNativeWidth();
		this.displayHeight = SystemSettings.getNativeHeight();
		this.renderer = new Renderer(displayWidth, displayHeight);
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

		// on key down, keycode is added to input array
		setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode code = e.getCode();
				if (!input.contains(code)) {
					input.add(code);
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

				} catch (Exception e){
					//shhhh, let's just pretend this never happened
				}
				
				renderer.render(gc);
				
				overlay.drawOverlay(gc, world, myInfo.getID());
			}
		}.start();
	}
}
