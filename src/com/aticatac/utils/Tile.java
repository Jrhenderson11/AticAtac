package com.aticatac.utils;

/* Just a way to represent a tile in a way that is easy to manipulate for AI */

public class Tile {
	public final int X;
	public final int Y;

	public Tile(int x, int y) {
		this.X = x;
		this.Y = y;
	}

	public boolean equals(Tile t) {
		if (this.X == t.X && this.Y == t.Y)
			return true;
		return false;
	}
	
	public int getH(Tile goal) {
		return Math.abs(goal.X - this.X) + Math.abs(goal.Y - this.Y);
	}
	
}
