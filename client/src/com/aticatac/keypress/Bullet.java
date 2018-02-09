package com.aticatac.keypress;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bullet {

	private double x;	
	private double y;
	private Rectangle rect;
	private Color colour;
	private Group group;
	public Bullet(double bulX, double bulY, Color col, Group scene){
		x = bulX;
		y = bulY;
		colour = col;
		rect = new Rectangle(x, y, 10, 5);
		group = scene;
	}
	
	public Rectangle getRect(){
		return rect;
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
}
