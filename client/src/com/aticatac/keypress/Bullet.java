package com.aticatac.keypress;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bullet {

	private boolean isShot;
	private double x;	
	private double y;
	private Rectangle rect;
	private Color colour;
	private Group group;
	Image bullet;
	public Bullet(double bulX, double bulY, Color col, Group scene){
		x = bulX;
		y = bulY;
		colour = col;
		bullet = new Image("com/aticatac/keypress/keytest/bullet.jpg");
		group = scene;
		isShot = false;
	}
	
	public Image getImg(){
		
		return bullet;
	}
	
	public void setColour(Color newColour){
		colour = newColour;
	}
	
	public double getX(){
		return x;
	}
	
	public void  setX(double newX){
		x = newX;
	}
	
	public double getY(){
		return y;
	}
	
	public void setY(double newY){
		y = newY;
	}
	
	public boolean getStatus(){
		return isShot;
	}
	
	public void setStatus(boolean newStatus){
		isShot = newStatus;
	}
}
