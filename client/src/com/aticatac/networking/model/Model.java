package com.aticatac.networking.model;

import java.io.Serializable;

import com.aticatac.world.Level;

public class Model implements Serializable{

	
	private int x;
	private int y;

	int offset = 10;

	private Level map;
	
	public Model(int newX, int newY) {
		this.x = newX;
		this.y = newY;
	}
	
	public void init() {
		this.map = new Level(100, 100);
		//move to srv and replace with msg
		//this.map.loadMap("client/assets/maps/map2.txt");
	}
	
	public void updateCoords(int newX, int newY) {
		this.x = newX;
		this.y = newY;
	}
	
	public void update(boolean moveUp, boolean moveDown, boolean moveLeft, boolean moveRight, boolean run, int speed) {
		if (moveUp /*&& checkPos(1)*/) {
			y -= speed;
			/*if(map.getCoords(x, y)==1) {
				y = y-2;
			}*/
		}
		if (moveDown /*&& checkPos(2)*/) {
			y += speed;
			/*
			if(map.getCoords(x, y)==1) {
				y = y+2;
			}*/
		}
		if (moveLeft /*&& checkPos(3)*/) {
			x -= speed;
			/*if(map.getCoords(x, y)==1) {
				x = x+2;
			}*/
		}
		if (moveRight /*&& checkPos(4)*/) {
			x += speed;
			/*if(map.getCoords(x, y)==1) {
				x = x-2;
			}*/
		}
		if (run) {
			speed = 3;
		}
	}
	
	//move to srv
	private boolean checkPos(int coord, int speed) {
		int offsetY = 90;//map.getHeight() - 10;
		int offsetX = 90;//map.getWidth() - 10;

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

	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
}
