package com.aticatac.world;

import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import com.aticatac.utils.Controller;
import com.aticatac.utils.SystemSettings;
import com.aticatac.world.items.Bullet;
import com.aticatac.world.items.Collidable;
import com.aticatac.world.items.ShootGun;
import com.aticatac.world.items.SplatGun;
import com.aticatac.world.items.SprayGun;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class World implements Serializable {
	private Collection<Player> players;
	private Collection<Bullet> bullets;
	private Level level;
	private final Point[] startLocs = {new Point(50, 50), new Point(100, 100)};

	int[][] map;

	public World(Level level) {
		this.level = level;
		map = getLevel().getGrid();
		this.players = new LinkedList<Player>();
		this.bullets = new LinkedList<Bullet>();
	}

	// sets up world
	public void init(Player player) {
		this.addPlayer(player);
	}
	
	public Level getLevel() {
		return level;
	}

	// calls the update method for all Collideables
	// this is currently used for moving the bullets.
	public void update() {
		// update bullets
		for (int i=0; i<bullets.size(); i++) {
			((Bullet) bullets.toArray()[i]).update(this);
		}
		// update players
		for (Player player : players) {
			player.update();
		}
	}

	public void handleInput(ArrayList<KeyCode> input, double dir, String id) {
		Player player = this.getPlayerById(id);
		//System.out.println(this.players.size());
		//System.out.println("got player " + player + " by id " + id);
		if (player==null) {
			return;
		}
		// left
		if (input.contains(KeyCode.A)) {
	
			player.move(-2, 0);
			Point p = this.displayPositionToCoords(player.getPosition());
			if (level.getGrid()[p.x][p.y] == 1) {
				player.move(2, 0);
			}
		}
		// System.out.println(input.size());
		// right
		if (input.contains(KeyCode.D)) {
		
			player.move(2, 0);
			Point p = this.displayPositionToCoords(player.getPosition());
			if (level.getGrid()[p.x][p.y] == 1) {
				player.move(-2, 0);
			}
		}
		// System.out.println(input.size());
		// up
		if (input.contains(KeyCode.W)) {
		
			player.move(0, -2);
			Point p = this.displayPositionToCoords(player.getPosition());
			if (level.getGrid()[p.x][p.y] == 1) {
				player.move(0, 2);
			}
		}
		// System.out.println(input.size());
		// down
		if (input.contains(KeyCode.S)) {
		
			player.move(0, 2);
			Point p = this.displayPositionToCoords(player.getPosition());
			if (level.getGrid()[p.x][p.y] == 1) {
				player.move(0, -2);
			}
		}
		// System.out.println(input.size());

		// Gun spawn in, using for testing, remove in game
		// Shoot gun
		if (input.contains(KeyCode.I)) {
			player.setGun(new ShootGun(player));
			// input.remove(KeyCode.I);
		}
		// Splat gun
		if (input.contains(KeyCode.O)) {
			player.setGun(new SplatGun(player));
			// input.remove(KeyCode.O);

		}
		// Spray gun
		if (input.contains(KeyCode.P)) {
			player.setGun(new SprayGun(player));
			// input.remove(KeyCode.P);
		}

		Point p = this.displayPositionToCoords(player.getPosition());
		if (level.getGrid()[p.x][p.y] == 0) {
			level.updateCoords(p.x, p.y, player.getColour());
		}

		player.setLookDirection(dir);

	}

	public void shoot(int targetX, int targetY) {
		// TODO: replace with specific player

		Player player = (Player) players.toArray()[0];

		if (player.getGun() != null) {
      		player.getGun().fire(player.getLookDirection(), this.displayPositionToCoords(new Point(targetX, targetY)), this);
      	}
	}
	
	public Collection<Bullet> getBullets() {
		return bullets;
	}

	public boolean addBullet(Bullet collidable) {
		return bullets.add(collidable);
	}

	public boolean removeBullet(Bullet bullet) {
		return bullets.remove(bullet);
	}

	public Collection<Player> getPlayers() {
		return players;
	}

	public boolean addPlayer(Player player) {
		System.out.println("adding player " + player.getIdentifier());
		player.setPosition(this.startLocs[players.size()]);
		this.players.add(player);
		return true;
	}

	public int getPlayerColour(String playerIdentifier) {
		for (Player player : players) {
			if (player.getIdentifier() == playerIdentifier) {
				return player.getColour();
			}
		}
		return 0;
	}

	public Player getPlayerById(String id) {
		for (Player p: this.getPlayers()) {
			if (p.getIdentifier()==id) {
				return p;
			} 
		}
		return null;
	}

	/**
	 * Gets the corresponding map coordinate from the given position within the
	 * specified size of the display.
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
		return displayPositionToCoords(displayPosition,
				new Dimension(SystemSettings.getNativeWidth(), SystemSettings.getNativeHeight()));
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
		return coordsToDisplayPosition(coords,
				new Dimension(SystemSettings.getNativeWidth(), SystemSettings.getNativeHeight()));
	}
}