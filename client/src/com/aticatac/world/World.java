package com.aticatac.world;

import java.util.Collection;
import java.util.LinkedList;

public class World {
	private Collection<Player> players;
	private Collection<Collidable> collidables;
	private Level level;

	int[][] map;

	public World(Level level) {
		this.level = level;
		map = getLevel().getGrid();
		this.players = new LinkedList<Player>();
		this.collidables = new LinkedList<Collidable>();
	}

	public Level getLevel() {
		return level;
	}

	public void movementHandler(int x, int y) {

		 
	}
	//calls the update method for all Collideables
	//this is currently used for moving the bullets.
	public void update() {
		for (Collidable collidable: collidables) {
			collidable.update();
		}
	}
	
	public Collection<Player> getPlayers() {
		return players;
	}
	
	public boolean addPlayer(Player player) {
		return players.add(player);
	}
}
