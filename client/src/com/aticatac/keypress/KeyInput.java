package com.aticatac.keypress;

<<<<<<< HEAD
=======
import java.util.ArrayList;

>>>>>>> 60b9b1fcbdd59d3d3a94ef6a7c79173cffa3d597
import com.aticatac.world.Level;
import com.aticatac.world.World;

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
<<<<<<< HEAD
=======
import javafx.scene.shape.Rectangle;
>>>>>>> 60b9b1fcbdd59d3d3a94ef6a7c79173cffa3d597
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class KeyInput extends Application {

	/**
	 * @param args
	 *            the command line arguments
	 */
	int stageX = 1200, stageY = 1200;
	int offset = 10;
	boolean moveUp, moveDown, moveRight, moveLeft, run;
<<<<<<< HEAD
	int x, y;
	int speed = 1;

=======
	int x = 0, y = 0;
	int speed = 1;
	private boolean runBullet;
	private int MAX_BULLET = 5;
	private Rectangle[] bullets = new Rectangle[MAX_BULLET];
	private double[] bulletX = new double[MAX_BULLET];
	private double[] bulletY = new double[MAX_BULLET];
	private boolean[] bulletActivity = new boolean[MAX_BULLET];
	private int currentBullet;
	private int direction = 3;
>>>>>>> 60b9b1fcbdd59d3d3a94ef6a7c79173cffa3d597
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
<<<<<<< HEAD

=======
		
		for (int i = 0; i < MAX_BULLET; i++){
			bullets[i] = new Rectangle(x,y,10,5);
			bullets[i].setFill(Color.RED);
			bullets[i].setVisible(false);
		
			bulletX[i] = 0;
			bulletY[i] = 0;
			bulletActivity[i] = false;
			root.getChildren().add(bullets[i]);
		}
		currentBullet = 0;
		
>>>>>>> 60b9b1fcbdd59d3d3a94ef6a7c79173cffa3d597
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
<<<<<<< HEAD
=======
				}else if(input == KeyCode.SPACE){
					if (bullets.length != 0){
						bullets[currentBullet].setVisible(true);
						bulletX[currentBullet] = x;
						bulletY[currentBullet] = y;
						runBullet = true;
						bulletActivity[currentBullet] = true;
					}
					currentBullet++;
					
					if (currentBullet >= MAX_BULLET){
						currentBullet = 0;
					}
>>>>>>> 60b9b1fcbdd59d3d3a94ef6a7c79173cffa3d597
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
<<<<<<< HEAD
				}
			}
		});

=======
				}else if (input == KeyCode.Q){
					direction *= -1;
				}
			}
		});
>>>>>>> 60b9b1fcbdd59d3d3a94ef6a7c79173cffa3d597
		// Image ufo = new Image("aticatac/assets/keytest/e_f1.png");
		// Image space = new Image("aticatac/assets/keytest/farback.gif");
		Image ufo = new Image("com/aticatac/keypress/keytest/e_f1.png");
		Image space = new Image("com/aticatac/keypress/keytest/farback.gif");

		x = 5;
		y = 5;
<<<<<<< HEAD
=======
		
		
		
>>>>>>> 60b9b1fcbdd59d3d3a94ef6a7c79173cffa3d597
		gc.drawImage(space, 0, 0);
		gc.drawImage(ufo, x, y);

		Level level = new Level(1200, 1200);
		level.loadMap("D:/Documents/College/Term 2/Team project/aticatac/assets/maps/map2.txt");
		int[][] map = level.getGrid();
		
<<<<<<< HEAD
=======
		Gun gun = new Gun(scene);
		gun.aim();
		
		Rectangle r = new Rectangle(500,400,40,20);
		r.setFill(Color.AZURE);
		root.getChildren().add(r);
		
		Rectangle bullet = new Rectangle(x,y, 10,5);
		bullet.setFill(Color.AQUA);
		Bullet bulletC = new Bullet(x,y, Color.AQUA, root);
		// bullet = bulletC.getRect();
		root.getChildren().add(bullet);
		
		
		
>>>>>>> 60b9b1fcbdd59d3d3a94ef6a7c79173cffa3d597
		AnimationTimer timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
<<<<<<< HEAD
				gc.drawImage(space, 0, 0, 1200, 1200);
				gc.fillText("Use WASD keys to move, SHIFT to run.", 100, 150);
				gc.drawImage(ufo, x, y);

=======
				//bullet.setRotate(gun.calcDirection(500, 500));
				gc.drawImage(space, 0, 0, 1200, 1200);
				gc.fillText("Use WASD keys to move\nSHIFT to run\nSPACE"
						+ " to shoot\nQ to change shooting direction"
						+ "\nShoot the rectangle to make it disappear.", 100, 150);
				gc.drawImage(ufo, x, y);
				
				double[] v = gun.calcDirection(x, y);
				//System.out.println(x+v[0]);
				double bulX = bulletC.getX();
				double bulY = bulletC.getY();
				if (gun.isAimRunning()){
					bullet.setTranslateX(bulX += v[0]);
					bulletC.setX(bulX + v[0]);
				}
				
				if(gun.isAimRunning()){
					bullet.setTranslateY(bulY += v[1]);
					bulletC.setY(bulY + v[1]);
				}
				//bullet.setTranslateY(y += v[1]);
>>>>>>> 60b9b1fcbdd59d3d3a94ef6a7c79173cffa3d597
				
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
<<<<<<< HEAD
=======
				
					for (int i = 0; i < MAX_BULLET; i++){
						if (bulletActivity[i]){
							bulletX[i] += direction;
							double xval = bulletX[i];
							bullets[i].setTranslateX(xval);
							bullets[i].setTranslateY(bulletY[i]);
						}
						
						//If bullet intersects with rectangle, the don't show the rectangle
						if (bullets[i].getBoundsInParent().intersects(r.getBoundsInParent())){
							r.setVisible(false);
							bullets[i].setVisible(false);
							bulletActivity[i] = false;
						}
					}
					
>>>>>>> 60b9b1fcbdd59d3d3a94ef6a7c79173cffa3d597

			}

		};
		timer.start();

<<<<<<< HEAD
		stage.show();
	}

	public boolean checkPos(int coord) {
=======
		
		stage.show();
	}

	public boolean checkPos(double coord) {
>>>>>>> 60b9b1fcbdd59d3d3a94ef6a7c79173cffa3d597
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

<<<<<<< HEAD
=======

>>>>>>> 60b9b1fcbdd59d3d3a94ef6a7c79173cffa3d597
}
