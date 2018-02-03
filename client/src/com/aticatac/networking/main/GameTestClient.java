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
	int offset = 10;
	boolean moveUp, moveDown, moveRight, moveLeft, run;
	int x, y;
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
		client.sendData("init");
		client.sendData("join");
		System.out.println("Client started and joined");
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) {
		stage.setTitle("Network test");

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

		x = 5;
		y = 5;
		gc.drawImage(space, 0, 0);
		gc.drawImage(ufo, x, y);
		
		

		AnimationTimer timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				model = client.getModel();
				//send inputs (moveUp, moveDown, moveLeft, moveRight, run, speed)
				client.sendData("input:"+moveUp + ":"+moveDown + ":"+moveLeft + ":"+moveRight + ":" + run + ":"+speed);
				int x = model.getX();
				int y = model.getY();
				//get model
				gc.drawImage(space, 0, 0, 1200, 1200);
				gc.fillText("Use WASD keys to move, SHIFT to run.", 100, 150);
				gc.drawImage(ufo, x, y);

				

			}

		};
		timer.start();

		stage.show();
	}

	
	//move to srv
	public boolean checkPos(int coord) {
		int offsetY = stageY - 10;
		int offsetX = stageX - 10;

		int up = y - speed;
		int down = y + speed;
		int left = x - speed;
		int right = x + speed;

		int calcOffset = 0 + offset;

		if (coord == 1 && calcOffset <= up && up <= offsetY) {
			return true;
		} else if (coord == 2 && calcOffset <= down && down <= offsetY) {
			return true;
		} else if (coord == 3 && calcOffset <= left && left <= offsetX) {
			return true;
		} else if (coord == 4 && calcOffset <= right && right <= offsetX) {
			return true;
		} else
			return false;
	}

}
