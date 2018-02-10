package com.aticatac.world;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Collection;
import java.util.LinkedList;

import com.aticatac.utils.SystemSettings;
import com.aticatac.world.items.Collidable;

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

	//calls the update method for all Collideables
	//this is currently used for moving the bullets.
	public void update() {
		for (Collidable collidable: collidables) {
			collidable.update(this); //passes 
		}
	}
	
	public Collection<Collidable> getCollidables() {
		return collidables;
	}

	public boolean addCollidable(Collidable collidable) {
		return collidables.add(collidable);
	}
	
	public boolean removeCollidable(Collidable collideable) {
		return collidables.remove(collideable);
	}
	
	public Collection<Player> getPlayers() {
		return players;
	}
	
	public boolean addPlayer(Player player) {
		return players.add(player);
	}
	
	/**
	 * Gets the corresponding map coordinate from the given position within the specified size of the display.
	 */
	public Point displayPositionToCoords(Point displayPosition, Dimension displaySize) {
		int tileWidth = displaySize.width / level.getWidth();
		int tileHeight = displaySize.height / level.getHeight();
		return new Point((displayPosition.x / tileWidth), (displayPosition.y / tileHeight));
	}

	/**
	 * As above but with the game's default dislpay dimension.
	 */
	public Point displayPositionToCoords(Point displayPosition) {
		return displayPositionToCoords(displayPosition, new Dimension(SystemSettings.getNativeWidth(), SystemSettings.getNativeHeight()));
	}
	
	/**
	 * Gets the position of center of the given coordinates 
	 */
	public Point coordsToDisplayPosition(Point coords, Dimension displaySize) {
		int tileWidth = displaySize.width / level.getWidth();
		int tileHeight = displaySize.height / level.getHeight();
		return new Point((coords.x * tileWidth) + (tileWidth / 2), (coords.y * tileHeight) + (tileHeight / 2));
	}
	
	/**
	 * As above but with the game's default display dimension
	 */
	public Point coordsToDisplayPosition(Point coords) {
		return coordsToDisplayPosition(coords, new Dimension(SystemSettings.getNativeWidth(), SystemSettings.getNativeHeight()));
	}
}