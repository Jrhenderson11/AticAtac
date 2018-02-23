package com.aticatac.world;


import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import com.aticatac.lobby.ClientInfo;
import com.aticatac.lobby.Lobby;
import com.aticatac.lobby.Lobby.ai;
import com.aticatac.utils.Controller;
import com.aticatac.utils.SystemSettings;
import com.aticatac.world.items.Bullet;
import com.aticatac.world.items.GunBox;
import com.aticatac.world.items.ShootGun;
import com.aticatac.world.items.ShootGunBox;
import com.aticatac.world.items.SplatGun;
import com.aticatac.world.items.SplatGunBox;
import com.aticatac.world.items.SprayGun;
import com.aticatac.world.items.SprayGunBox;

import javafx.scene.input.KeyCode;

public class World implements Serializable {
	
	/**
	 * Delay between paint regeneration ticks
	 */
	private static final int REGEN_DELAY = 60;
	/**
	 * The Collection of Players in the world, should be up to 4.
	 */
	private Collection<Player> players;
	/**
	 * The Collection of Bullts that are currently travelling in the world.
	 */
	private Collection<Bullet> bullets;
	/**
	 * The Collection of GunBoxes in the world that have not been opened yet.
	 */
	private Collection<GunBox> gunboxes;
	/**
	 * The Level of this world that defines the walls and floors of the map.
	 */
	private Level level;
	private final Point[] startLocs = {new Point(50, 50), new Point(50, 100), new Point(100, 50), new Point(100, 100)};
	/**
	 * The current 'tick' of the paint regen timer, increased every update.
	 */
	private int regenTimer;

	int[][] map;

	public World(Level level) {
		this.level = level;
		map = getLevel().getGrid();
		this.players = new LinkedList<Player>();
		this.bullets = new LinkedList<Bullet>();
		this.gunboxes = new LinkedList<GunBox>();
		this.regenTimer = 0;
	}

	/**
	 * Sets up world for a given lobby
	 * @param lobby The Lobby object to initialise the world with
	 */
	public void init(Lobby lobby) {
		
		for (ClientInfo client: lobby.getAll()) {
			Player newPlayer = new Player(Controller.REAL, client.getID(), client.getColour());
			this.addPlayer(newPlayer);
		}
		
		for(ai a: lobby.getBots()) {
			AIPlayer aiPlayer = new AIPlayer(Controller.AI, this, a.name, a.colour);
			this.addPlayer(aiPlayer);
		}
		
	}
	
	/**
	 * Gets the level this World is based on
	 * @return The Level object
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * Shoots the gun of the given Player at the target location
	 * @param targetX The target X position to shoot towards
	 * @param targetY The target Y position to shoot towards
	 * @param id The unique ID of the Player shooting the gun
	 */
	public void shoot(int targetX, int targetY, String id) {
		
		Player player = this.getPlayerById(id);

		if (player.getGun() != null) {
      		player.getGun().fire(player.getLookDirection(), this.displayPositionToCoords(new Point(targetX, targetY)), this);
      	}
	}
	
	/**
	 * Returns the number of Players in the World
	 * @return Returns the number of players.
	 */
	public int getNumPlayers() {
		return players.size();
	}

	/**
	 * Updates the world, calls the update method for all Bullets, Player and GunBoxes
	 */
	public void update() {
		// update bullets
		for (int i=0; i<bullets.size(); i++) { //iterating this way prevents ConcurrentModificationExceptions
			((Bullet) bullets.toArray()[i]).update(this);
		}
		// update players
		for (Player player : players) {
			if(player.controller == Controller.AI) {
				((AIPlayer) player).update();
				Point p = displayPositionToCoords(player.getPosition());
  	        	if(level.getGrid()[p.x][p.y] != 1) {
  	        		level.updateCoords(p.x, p.y, player.getColour());
  	        	}
			}
			player.update();
			if (regenTimer == REGEN_DELAY) { //used for a delay between each regeneraction call
				player.regenPaint(level.getPercentTiles(player.getColour()));
				regenTimer = 0;
			}
			regenTimer++;
		}
		// update gunboxes
		for (int i=0; i<gunboxes.size(); i++) {
			((GunBox) gunboxes.toArray()[i]).update(this);
		}
	}

	/**
	 * Handles the inputs for a given Player
	 * @param input The ArrayList of KeyCode's pressed by the player this update.
	 * @param dir The direction the Player is facing
	 * @param id The unique identifier of this Player
	 */
	public void handleInput(ArrayList<KeyCode> input, double dir, String id) {
		Player player = this.getPlayerById(id);
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
		
		if (level.getGrid()[p.x][p.y] != 1) {
			level.updateCoords(p.x, p.y, player.getColour());
		}

		player.setLookDirection(dir);

	}
	
	/**
	 * Returns the Collection of Bullets currently travelling
	 * @return A Collection of Bullets
	 */
	public Collection<Bullet> getBullets() {
		return bullets;
	}

	/**
	 * Adds a Bullet to the World
	 * @param collidable The Bullet to add to the World
	 * @return Returns True if the bullet is added to the world
	 */
	public boolean addBullet(Bullet collidable) {
		return bullets.add(collidable);
	}

	/**
	 * Removes a Bullet from the World
	 * @param bullet The Bullet to remove
	 * @return Returns True if the Bullet is removed
	 */
	public boolean removeBullet(Bullet bullet) {
		return bullets.remove(bullet);
	}
	
	/**
	 * Returns a Collection of GunBoxes in the world
	 * @return A Collection of GunBoxes
	 */
	public Collection<GunBox> getGunBoxes() {
		return gunboxes;
	}

	/**
	 * Adds a GunBox to the world
	 * @param gunbox The GunBox to add
	 * @return Returns True if the GunBox was added
	 */
	public boolean addGunBox(GunBox gunbox) {
		return gunboxes.add(gunbox);
	}
	
	/**
	 * Removes the GunBox from the world
	 * @param gunbox The GunBox to remove
	 * @return True if the GunBox was removed
	 */
	public boolean removeGunBox(GunBox gunbox) {
		return gunboxes.remove(gunbox);
	}

	/**
	 * Spawns a ShootGun box in the given position
	 * @param position The Position to spawn the GunBox at
	 */
	public void spawnShootGunBox(Point position) {
		addGunBox(new ShootGunBox(position));
	}
	
	/**
	 * Spawns a SlatGun box in the given position
	 * @param position The Position to spawn the GunBox at
	 */
	public void spawnSplatGunBox(Point position) {
		addGunBox(new SplatGunBox(position));
	}
	
	/**
	 * Spawns a SprayGun box in the given position
	 * @param position The Position to spawn the GunBox at
	 */
	public void spawnSprayGunBox(Point position) {
		addGunBox(new SprayGunBox(position));
	}
	
	/**
	 * Randomly generates a set amount of spawn positions, used for GunBox spawns
	 * @param amount The amount of points to generate
	 * @return An array of Points
	 */
	public Point[] generateBoxSpawnPoints(int amount) {
		Point[] points = new Point[amount];
		Random rand = new Random();
		for (int i=0; i<amount; i++) {
			int x,y;
			do {
				x = rand.nextInt(level.getWidth());
				y = rand.nextInt(level.getHeight());
			} while (level.getCoords(x, y) != 0); //generate points, keep randomising until point is not on a wall tile.
			points[i] = coordsToDisplayPosition(new Point(x, y));
		}
		return points;
	}
	
	/**
	 * Get the Players in this World
	 * @return A Collection of Players
	 */
	public Collection<Player> getPlayers() {
		return players;
	}

	/**
	 * Adds a player to the world
	 * @param player to add
	 * @returns whether it was succesful
	 */
	public boolean addPlayer(Player player) {
		System.out.println("adding player " + player.getIdentifier());
		player.setPosition(this.startLocs[players.size()]);
		this.players.add(player);
		return true;
	}
	
	/**
	 * Get the color int of the given player
	 * @param playerIdentifier The unique player identifier
	 * @return An int representing the color of the Player
	 */
	public int getPlayerColour(String playerIdentifier) {
		for (Player player : players) {
			if (player.getIdentifier() == playerIdentifier) {
				return player.getColour();
			}
		}
		return 0;
	}
	
	/**
	 * Returns the Player instance given the players id
	 * @param id The unique identifier 
	 * @return The Player instance with the given identifier
	 */
	public Player getPlayerById(String id) {
		for (Player p: this.getPlayers()) {
			if (p.getIdentifier().equals(id)) {
				return p;
			} 
		}
		//System.out.println("cant find " + id);
		return null;
	}

	/**
	 * Gets the corresponding map coordinate from the given position within the
	 * specified size of the display.
	 * @param displayPosition The position on the display
	 * @param displaySize The dimensions of the display
	 * @return The Coordinates on the Level this corresponds to
	 */
	public Point displayPositionToCoords(Point displayPosition, Dimension displaySize) {
		int tileWidth = displaySize.width / level.getWidth();
		int tileHeight = displaySize.height / level.getHeight();
		return new Point((displayPosition.x / tileWidth), (displayPosition.y / tileHeight));
	}

	/**
	 * As above but with the game's default dislpay dimension.
	 * @param displayPosition The position on the display
	 * @return The Coordinates on the Level this corresponds to
	 */
	public Point displayPositionToCoords(Point displayPosition) {
		return displayPositionToCoords(displayPosition,
				new Dimension(SystemSettings.getNativeWidth(), SystemSettings.getNativeHeight()));
	}

	/**
	 * Gets the position of center of the given coordinates
	 * @param coords The coordinates of the tile to get the display position of
	 * @param displaySize The dimensions of the display
	 * @return The displayPosition of the center of the given coordinate tile
	 */
	public Point coordsToDisplayPosition(Point coords, Dimension displaySize) {
		int tileWidth = displaySize.width / level.getWidth();
		int tileHeight = displaySize.height / level.getHeight();
		return new Point((coords.x * tileWidth) + (tileWidth / 2), (coords.y * tileHeight) + (tileHeight / 2));
	}

	/**
	 * As above but with the game's default display dimension
	 * @param coords The coordinates of the tile to get the display position of
	 * @return The displayPosition of the center of the given coordinate tile
	 */
	public Point coordsToDisplayPosition(Point coords) {
		return coordsToDisplayPosition(coords,
				new Dimension(SystemSettings.getNativeWidth(), SystemSettings.getNativeHeight()));
	}

	/**
	 * Adds a player to the world without assigning it a position
	 * @param player to add
	 * @returns whether it was succesful
	 */
	public boolean addPlayerWithoutPosition(Player player) {
		System.out.println("adding player " + player.getIdentifier());
		this.players.add(player);
		return true;
	}
}