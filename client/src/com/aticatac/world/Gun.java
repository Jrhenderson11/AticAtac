package com.aticatac.world;

import java.awt.Point;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

public class Gun{
	private static final int MINIMUM_PAINT = 10;
	
	private int paintLevel;
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
		paintLevel = 100;
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
				shoot(new Point((int) targetX,(int) targetY));
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
	
	public void decreasePaintLevel() {
		paintLevel -= 10;
	}
	
	public void increasePaintLevel() {
		paintLevel += 5;
	}
	
	public void shoot(Point target) {
		if(paintLevel >= MINIMUM_PAINT) {
			// TODO: code to shoot
		}
	}
}
