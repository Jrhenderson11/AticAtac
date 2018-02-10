package com.aticatac.world;

import javafx.scene.paint.Color;
import java.awt.Rectangle;

public class Bullet implements Collidable {
	
	private double direction;
	private int moveSpeed;
	private Rectangle rect;
	private Color colour;
	
	public Bullet(double direction, int moveSpeed, Rectangle rect, Color colour){
		this.colour = colour;
		this.rect = rect;
	}
	
	public void update() {
		int dx = (int) (moveSpeed * Math.sin(direction));
		int dy = (int) (moveSpeed * Math.cos(direction));
		translate(dx, dy);
	}
	
	@Override
	public Rectangle getRect() {
		return rect;
	}
	
	public void translate(int dx, int dy) {
		rect.x += dx;
		rect.y += dy;
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}

	public int getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(int moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public Color getColour() {
		return colour;
	}
}
