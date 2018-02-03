package com.aticatac.networking.main;

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

	UDPClient client;
	
	private void initialiseConnection() {
		
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) {
		stage.setTitle("Testing");

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
		//move to srv and replace with msg
		Level level = new Level(1200, 1200);
		//move to srv and replace with msg
		level.loadMap("D:/Documents/College/Term 2/Team project/aticatac/assets/maps/map2.txt");
		int[][] map = level.getGrid();
		
		Model model;
		AnimationTimer timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				model = client.getModel();
				//send inputs (moveUp, moveDown, moveLeft, moveRight, run, speed)
				int x = model.getX();
				int y = model.getY();
				//get model
				gc.drawImage(space, 0, 0, 1200, 1200);
				gc.fillText("Use WASD keys to move, SHIFT to run.", 100, 150);
				gc.drawImage(ufo, x, y);

				//MOVE TO SRV???
				if (moveUp /*&& checkPos(1)*/) {
					y -= speed;
					if(map[x][y]==1) {
						y = y-2;
					}
				
				}
				if (moveDown /*&& checkPos(2)*/) {
					y += speed;
					
					if(map[x][y]==1) {
						y = y+2;
					}
				}
				if (moveLeft /*&& checkPos(3)*/) {
					x -= speed;
					if(map[x][y]==1) {
						x = x+2;
					}
				}

				if (moveRight /*&& checkPos(4)*/) {
					x += speed;
					if(map[x][y]==1) {
						x = x-2;
					}
				}

				if (run) {
					speed = 3;
				}

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
