package com.aticatac.world;

import java.util.Collection;
import java.util.LinkedList;

public class World {
	private Collection<Player> players;
	private Level level;

	int[][] map;

	public World(Level level) {
		this.level = level;
		map = getLevel().getGrid();
		this.players = new LinkedList<Player>();
	}

	public Level getLevel() {
		return level;
	}

	public void movementHandler(int x, int y) {

		 
	}
	
	public Collection<Player> getPlayers() {
		return players;
	}
	
	public boolean addPlayer(Player player) {
		return players.add(player);
	}
}
