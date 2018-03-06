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
import com.aticatac.utils.GameState;
import com.aticatac.utils.GameTimer;
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

@SuppressWarnings("serial")
public class World implements Serializable {
	
	/**
	 * Delay between paint regeneration ticks.
	 */
	private static final int REGEN_DELAY = 100;
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
	/**
	 * Some start locations for Players
	 */
	private Point[] startLocs;
	/**
	 * An array of locations for gunbox spawns
	 */
	private Point[] gunBoxLocs;
	/**
	 * The current 'tick' of the paint regen timer, increased every update.
	 */
	private int regenTimer;
	/**
	 * The current second of the round time.
	 */
	private int roundTime;
	/**
	 * The current state of the game.
	 */
	private GameState gameState;
	/**
	 * The timer for the round system
	 */
	private GameTimer gameTimer;
	/**
	 * The last winner of the game, initially null
	 */
	private Player winner;
	/**
	 * Number of times the GunBoxes have been respawned, used to avoid respawning multiple times
	 */
	private int boxRespawns;

	
	// -----------
	// Constructor
	// -----------
	
	
	/**
	 * Creates a World with the given Level
	 * @param level The Level object.
	 */
	public World(Level level) {
		this.level = level;
		this.players = new LinkedList<Player>();
		this.bullets = new LinkedList<Bullet>();
		this.gunboxes = new LinkedList<GunBox>();
		this.regenTimer = 0;
		this.roundTime = 0;
		this.startLocs = generatePlayerSpawnPoints();
		this.gameState = GameState.READY;
		this.gameTimer = new GameTimer(this);
		this.setWinner(null);
		this.boxRespawns = 0;
	}

	
	// -------
	// Methods
	// -------
	
	
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
			}
			Point p = displayPositionToCoords(player.getPosition());
  	        if(level.getGrid()[p.x][p.y] == 0) {
  	        	level.updateCoords(p.x, p.y, player.getColour());
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
		// respawn gunboxes at 20 and 40 seconds in the round
		if ((getRoundTime() == 20 && boxRespawns == 0)|| (getRoundTime() == 40) && boxRespawns == 1) {
			respawnGunBoxes();
			boxRespawns += 1;
		}
		// check for round over
		if (getRoundTime() == GameTimer.ROUND_DURATION && gameState != GameState.OVER) {
			System.out.println("world game over");
			setGameState(GameState.OVER);
			//reset players, calculate winner and award point
			int maxControl = 0;
			Player winner = null;
			for (Player player: players) {
				player.reset();
				int control = level.getPercentTiles(player.getColour());
				if (control > maxControl) {
					maxControl = control;
					winner = player;
				}
			}
			winner.awardPoint();
			setWinner(winner);
			//notify winner, change to a visual display at some point
			System.out.println("winner of the round is: " + winner.getIdentifier());
			gameTimer.startEndRoundDelay();
		}
	}
	
	/**
	 * Resets the World for a new round
	 */
	public void newRound() {
		System.out.println("world new round");
		setGameState(GameState.READY);
		// clear items
		bullets.clear();
		gunboxes.clear();
		// generate new map
		level.randomiseMap();
		// generate player spawns
		this.startLocs = generatePlayerSpawnPoints();
		int i = 0;
		for (Player player: players) {
			player.setPosition(startLocs[i++]);
		}
		// generate gunbox spawns
		this.gunBoxLocs = generateBoxSpawnPoints(3);
		respawnGunBoxes();
		gameTimer.startCountdownTimer();
		//reset box respawn counter
		boxRespawns = 0;
	}
	
	/**
	 * Set the gamestate to playing and starts the game.
	 */
	public void startGame() {
		// reset timer
		setRoundTime(0);
		System.out.println("world start game");
		setGameState(GameState.PLAYING);
		gameTimer.startRoundTimer();
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

		player.setLookDirection(dir);

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
	 * Respawns random GunBoxes in each of the gunBoxLocs
	 */
	public void respawnGunBoxes() {
		gunboxes.clear();
		Random rand = new Random();
		for (Point point: gunBoxLocs) {
			int r = rand.nextInt(3); //random box type
			if (r == 0) {
				spawnShootGunBox(point);
			} else if (r == 1) {
				spawnSplatGunBox(point);
			} else if (r == 2) {
				spawnSprayGunBox(point);
			}
		}
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
			int x, y;
			do {
				x = rand.nextInt(level.getWidth());
				y = rand.nextInt(level.getHeight());
			} while (level.getCoords(x, y) != 0); //generate points, keep randomising until point is not on a wall tile.
			points[i] = coordsToDisplayPosition(new Point(x, y));
		}
		return points;
	}
	
	/**
	 * Generates 4 spawn points for Players
	 * Point[0] will be a spawn point in the top left quadrant
	 * Point[1] will be a spawn point in the top right quadrant
	 * Point[2] will be a spawn point in the bottom left quadrant
	 * Point[3] will be a spawn point in the borrom right quadrant
	 * @return Returns an array of 4 spawn points
	 */
	public Point[] generatePlayerSpawnPoints() {
		int x, y;
		Point[] points = new Point[4];
		Random rand = new Random();
		//top left
		do {
			x = rand.nextInt(level.getWidth()/2);
			y = rand.nextInt(level.getHeight()/2);
		} while (level.getCoords(x, y) != 0);
		points[0] = coordsToDisplayPosition(new Point(x, y));
		//top right
		do {
			x = (level.getWidth()/2) + rand.nextInt(level.getWidth()/2);
			y = rand.nextInt(level.getHeight()/2);
		} while (level.getCoords(x, y) != 0);
		points[1] = coordsToDisplayPosition(new Point(x, y));
		//bottom left
		do {
			x = rand.nextInt(level.getWidth()/2);
			y = (level.getHeight()/2) + rand.nextInt(level.getHeight()/2);
		} while (level.getCoords(x, y) != 0);
		points[2] = coordsToDisplayPosition(new Point(x, y));
		//bottom right
		do {
			x = (level.getWidth()/2) + rand.nextInt(level.getWidth()/2);
			y = (level.getHeight()/2) + rand.nextInt(level.getHeight()/2);
		} while (level.getCoords(x, y) != 0);
		points[3] = coordsToDisplayPosition(new Point(x, y));
		return points;
	}
	
	/**
	 * Gets the level this World is based on
	 * @return The Level object
	 */
	public Level getLevel() {
		return level;
	}
	
	/**
	 * Returns the number of Players in the World
	 * @return Returns the number of players.
	 */
	public int getNumPlayers() {
		return players.size();
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
		if (players.size() < 4) {
			System.out.println("adding player " + player.getIdentifier());
			player.setPosition(this.startLocs[players.size()]);
			this.players.add(player);
			return true;
		} else return false;
	}

	public boolean removePlayer(String id) {
		for (int i=0;i< this.players.size();i++) {
			Player currentPlayer = (Player) ((LinkedList<Player>) players).get(i); 
			if (currentPlayer.getIdentifier().equals(id)) {
				this.players.remove(currentPlayer);
				return true;
			}
		}
		return false;
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

	/**
	 * Returns the current second of the round time
	 * @return The current second as an integer.
	 */
	public int getRoundTime() {
		return roundTime;
	}

	/**
	 * Sets the current round time
	 * @param roundTime
	 * @return
	 */
	public boolean setRoundTime(int roundTime) {
		if (roundTime <= GameTimer.ROUND_DURATION && roundTime > 0) {
			this.roundTime = roundTime;
			return true;
		} else return false;
	}
	
	/**
	 * Changes the current round time by the given value
	 * @param change The number of seconds to change the time by
	 * @return True if the round time has been changed
	 */
	public boolean changeRoundTime(int change) {
		return setRoundTime(roundTime + change);
	}


	public GameState getGameState() {
		return gameState;
	}


	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}


	public Player getWinner() {
		return winner;
	}


	public void setWinner(Player winner) {
		this.winner = winner;
	}
}