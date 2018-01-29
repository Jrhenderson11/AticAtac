package com.aticatac.keypress;

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

public class KeyInput extends Application{

    /**
     * @param args the command line arguments
     */
    int stageX = 800, stageY = 600;
    int offset = 10;
    boolean moveUp, moveDown, moveRight, moveLeft, run;
    int x, y;
    int speed = 1;

    
    public static void main(String[] args) {
        launch(args);
    }
    
    public void start (Stage stage){
       stage.setTitle("Testing");
        
        Group root = new Group();
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        
        Canvas canvas = new Canvas (stageX, stageY);
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
                
                if (input == KeyCode.W){
                    moveUp = true;
                }else if (input == KeyCode.A){
                    moveLeft = true;
                }else if (input == KeyCode.S){
                    moveDown = true;
                }else if (input == KeyCode.D){
                    moveRight = true;
                }else if (input == KeyCode.SHIFT){
                    run = true;
                }
                

            }
        });
        
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode input = event.getCode();
                
                if (input == KeyCode.W){
                    moveUp = false;
                }else if (input == KeyCode.A){
                    moveLeft = false;
                }else if (input == KeyCode.S){
                    moveDown = false;
                }else if (input == KeyCode.D){
                    moveRight = false;
                }else if (input == KeyCode.SHIFT){
                    run = false;
                    speed = 1;
                }
            }
        });
       
        //Image ufo = new Image("aticatac/assets/keytest/e_f1.png");
        //Image space = new Image("aticatac/assets/keytest/farback.gif");
        Image ufo = new Image("com/aticatac/keypress/keytest/e_f1.png");
        Image space = new Image("com/aticatac/keypress/keytest/farback.gif");
        
        x = 140;
        y = 180;
        gc.drawImage(space, 0, 0);
        gc.drawImage(ufo, x, y );

        
        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                gc.drawImage(space, 0, 0);
                gc.fillText("Use WASD keys to move, SHIFT to run.", 100, 150);
                gc.drawImage(ufo,x,y);

                if (moveUp && checkPos(1)){
                    y-= speed;
                }
                if (moveDown && checkPos(2)){
                    y += speed;
                }
                if (moveLeft && checkPos(3)){
                    x -= speed;
                }
                
                if (moveRight && checkPos(4)){
                    x += speed;
                }
                
                if (run){
                    speed  = 3;
                }
   
            }
            

        };
        timer.start();
        
        stage.show();
    }
    public boolean checkPos(int coord){
        int offsetY = stageY - 10;
        int offsetX = stageX - 10;
        
        int up = y - speed;
        int down = y + speed;
        int left = x - speed;
        int right = x + speed;
        
        int calcOffset = 0 + offset;
        
        if (coord == 1  && calcOffset <= up && up <= offsetY){
            return true;
        }else if (coord == 2 && calcOffset <= down && down <= offsetY){
            return true;
        }else if (coord == 3 && calcOffset <= left && left <= offsetX){
            return true;
        }else if (coord == 4 && calcOffset <= right && right <= offsetX){
            return true;
        }else
            return false;
    }

    
}

