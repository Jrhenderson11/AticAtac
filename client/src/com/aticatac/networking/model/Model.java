package com.aticatac.networking.model;

import java.io.Serializable;

import com.aticatac.world.Level;

public class Model implements Serializable{

	private int x;
	private int y;
	private Level map;
	
	
	public Model(int newX, int newY) {
		this.x = newX;
		this.y = newY;
	}
	
	public void updateCoords(int newX, int newY) {
		this.x = newX;
		this.y = newY;
	}
	
	public void updates(boolean moveUp, boolean moveDown, boolean moveLeft, boolean moveRight, boolean run, int speed) {
		//MOVE TO SRV???
		if (moveUp /*&& checkPos(1)*/) {
			y -= speed;
			if(map.getCoords(x, y)==1) {
				y = y-2;
			}
		}
		if (moveDown /*&& checkPos(2)*/) {
			y += speed;
			
			if(map.getCoords(x, y)==1) {
				y = y+2;
			}
		}
		if (moveLeft /*&& checkPos(3)*/) {
			x -= speed;
			if(map.getCoords(x, y)==1) {
				x = x+2;
			}
		}
		if (moveRight /*&& checkPos(4)*/) {
			x += speed;
			if(map.getCoords(x, y)==1) {
				x = x-2;
			}
		}
		if (run) {
			speed = 3;
		}
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	

	
}
