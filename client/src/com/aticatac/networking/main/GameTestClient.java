package com.aticatac.networking.main;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.aticatac.networking.client.UDPClient;
import com.aticatac.networking.model.Model;
import com.aticatac.world.Level;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GameTestClient extends Application {

	/**
	 * @param args
	 *            the command line arguments
	 */
	int stageX = 1200, stageY = 1200;
	boolean moveUp, moveDown, moveRight, moveLeft, run;
	int speed = 1;
	Model model;
	
	UDPClient client;
	
	private void initialiseConnection() {
		InetAddress srvAddress = null;
		try {
			srvAddress = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			System.out.println("server unreachable on this network");
			System.exit(-1);
		}
		client = new UDPClient("C1", srvAddress);
		Thread th = new Thread(client);
		th.setDaemon(true);
		th.start();
		
		client.sendData("join");
		client.sendData("init");
		System.out.println("Client started and connected");
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) {
		initialiseConnection();
		stage.setTitle("Network test");
		System.out.println("network started");
		Group root = new Group();
		Scene scene = new Scene(root);

		stage.setScene(scene);

		Canvas canvas = new Canvas(stageX, stageY);
		root.getChildren().add(canvas);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		gc.setFill(Color.AQUA);
		gc.setLineWidth(4);
		Font font = Font.font("Calibri", FontWeight.MEDIUM, 20);
		gc.setFont(font);
		System.out.println("adding key handlers");
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode input = event.getCode();

				if (input == KeyCode.W) {
					moveUp = true;
				} else if (input == KeyCode.A) {
					moveLeft = true;
				} else if (input == KeyCode.S) {
					moveDown = true;
				} else if (input == KeyCode.D) {
					moveRight = true;
				} else if (input == KeyCode.SHIFT) {
					run = true;
				} else if (input == KeyCode.ESCAPE) {
					client.halt();
					client.cancel();
				}

			}
		});

		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode input = event.getCode();

				if (input == KeyCode.W) {
					moveUp = false;
				} else if (input == KeyCode.A) {
					moveLeft = false;
				} else if (input == KeyCode.S) {
					moveDown = false;
				} else if (input == KeyCode.D) {
					moveRight = false;
				} else if (input == KeyCode.SHIFT) {
					run = false;
					speed = 1;
				}
			}
		});

		Image ufo = new Image("com/aticatac/keypress/keytest/e_f1.png");
		Image space = new Image("com/aticatac/keypress/keytest/farback.gif");

		gc.drawImage(space, 0, 0);
		gc.drawImage(ufo, 5, 5);

		AnimationTimer timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				//update model
				model = client.getModel();
				if (model==null) {
					//System.out.println("model is null");
					gc.drawImage(space, 0, 0, 1200, 1200);
					gc.fillText("Network down " , 100, 150);
					//send inputs (moveUp, moveDown, moveLeft, moveRight, run, speed)
					client.sendData("input:"+moveUp + ":"+moveDown + ":"+moveLeft + ":"+moveRight + ":" + run + ":"+speed);

				} else {
					//model = new Model(0,0);
					int x = model.getX();
					int y = model.getY();
					//send inputs (moveUp, moveDown, moveLeft, moveRight, run, speed)
					client.sendData("input:"+moveUp + ":"+moveDown + ":"+moveLeft + ":"+moveRight + ":" + run + ":"+speed);
					
					gc.drawImage(space, 0, 0, 1200, 1200);
					gc.fillText("Use WASD keys to move, SHIFT to run.", 100, 150);
					gc.drawImage(ufo, x, y);
				}

			}

		};
		timer.start();
		System.out.println("starting animation timer");
		stage.show();
		//try {
		//	client.halt();
		//client.cancel();
		//} catch (InterruptedException e) {
		//}
		//System.out.println("done");
	}
	

}
