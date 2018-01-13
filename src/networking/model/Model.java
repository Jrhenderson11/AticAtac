package networking.model;

import java.io.Serializable;

public class Model implements Serializable{

	private int x;
	private int y;
	
	public Model(int newX, int newY) {
		this.x = newX;
		this.y = newY;
	}
	
	public void updateCoords(int newX, int newY) {
		this.x = newX;
		this.y = newY;
	}
	
	public void addToCoords(int addX, int addY) {
		this.x += addX;
		this.y += addY;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
}
