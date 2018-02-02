package com.aticatac.world;

public class World {
	private Player one;
	private Player two;
	private Level level;

	int[][] map;

	public World(Level level) {
		this.level = level;
		map = getLevel().getGrid();
	}

	public Level getLevel() {
		return level;
	}

	public void movementHandler(int x, int y) {

		 
	}
}
