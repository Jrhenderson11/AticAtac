package com.aticatac.keypress;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Gun{
	private int x;
	private int y;
	private int speed;
	private Scene scene;
	private double targetX;
	private double targetY;
	private boolean aimRunning;

	public Gun(Scene pScene){
		scene = pScene;
		speed = 1;
		aimRunning = false;
	}

	public void aim(){
		
		scene.setOnMousePressed(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				EventType<MouseEvent> e =  MouseEvent.MOUSE_CLICKED;
				aimRunning = true;
				System.out.println("X: " + event.getX() + "\nY:" + event.getY());
				targetX =  event.getX();
				targetY = event.getY();
			}
		});
		
	}
	
	public double[] calcDirection(double pX, double pY){
		double xDiff, yDiff, angle;
		xDiff = pX - targetX;
		yDiff = targetY - pY;
		
		angle = Math.atan2(xDiff, yDiff);
		
		double velocityX = speed*Math.cos(angle);
		double velocityY = speed*Math.sin(angle);
		
		double[] velocity = {velocityX, velocityY};

		return velocity;
		
	}
	
	public boolean isAimRunning(){
		return aimRunning;
	}
	


	
	
	
}
