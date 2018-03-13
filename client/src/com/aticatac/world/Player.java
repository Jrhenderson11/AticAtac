package com.aticatac.world;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import com.aticatac.utils.Controller;
import com.aticatac.world.items.Gun;

@SuppressWarnings("serial")
public class Player implements Serializable{
	
	/**
	 * The maximum level of paint a player can have
	 */
	public static final double MAX_PAINTLEVEL = 100.0d;
	/**
	 * The rate at which paint regenerates proportional to the amount of territory control.
	 */
	public static final double PAINT_REGEN_RATE = 0.4d;
	/**
	 * Default size (width and height) of the player
	 */
	public static final int PLAYER_SIZE = 8;
	/**
	 * The type of Controller of the player, AI or Real person
	 */
    public Controller controller;
    /**
     * The unique string identifier of this player
     */
    protected String identifier;
    /**
     * The integer that identifies the color of this players paint
     */
    protected int colour;
    /**
     * The current level of paint for this player.
     */
    protected double paintLevel;
    /**
     * The position of this player on the display
     */
    protected Point position;
    /**
     * The direction the player is looking stored as radians, 0 is looking to the right. increases clockwise.
     */
    protected Double lookDirection;
    /**
     * The Gun this player is using, can be null
     */
    protected Gun gun;
    /**
     * The number of points this player has
     */
    protected int points;
    
    protected boolean hasGun;

    
    // -----------
    // Constructor
    // -----------
    
    
    /**
     * Constructor for a Player
     * @param controller The type of Controller of the Player, AI or Real person
     * @param identifier The unique identifier of the player
     * @param colour The number that identifies the colour of this players paint
     */
    public Player(Controller controller, String identifier, int colour) {
    	this.controller = controller;
    	this.setIdentifier(identifier);
    	this.colour = colour;
    	this.position = new Point(10, 10);
    	this.lookDirection = 0.0;
    	this.paintLevel = Player.MAX_PAINTLEVEL;
    	this.points = 0;
    	this.hasGun = false;
    }
    
    
    // -------
    // Methods
    // -------
    
    
    /**
     * Returns the Controller type for this player
     * @return 
     */
    public Controller getController() {
    	return controller;
    }
    
    /**
     * Updates the player
     */
    public void update() {
    	if (gun != null) {
    		gun.update(); //updates gun, used for gun cooldowns
    	}
    }
    
    /**
     * Resets all round based fields back to the start values
     */
    public void reset() {
    	this.paintLevel = MAX_PAINTLEVEL;
    	this.gun = null;
    }
    
    /**
     * Regenerates some paint based of the players level of map control
     * @param tilePercentage The percentage of map control this player has
     */
    public void regenPaint(int tilePercentage) {
    	changePaintLevel(tilePercentage * PAINT_REGEN_RATE); //regen paint
    }

    /**
     * Returns the colour identifier of this player
     * @return The colour identifier as an int
     */
	public int getColour() {
		return colour;
	}

	/**
	 * Sets this players colour to the given identifier
	 * @param colour The colour identifier to set it to
	 */
	public void setColour(int colour) {
		this.colour = colour;
	}

	/**
	 * Get the current display position of the player
	 * @return The display position of the player
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * Sets the Players position to the given display position
	 * @param position The display position of the player
	 */
	public void setPosition(Point position) {
		this.position = position;
	}
	
	/**
	 * Gets the rectangle that defines the collision boundary of the player
	 * @return Returns a Rectangle defining the players collision boundaries
	 */
	public Rectangle getRect() {
		return new Rectangle(position.x-(PLAYER_SIZE/2), position.y-(PLAYER_SIZE/2), PLAYER_SIZE, PLAYER_SIZE);
	}

	/**
	 * Moves the player by the given dX, dY values
	 * @param dX The change in x coordinate, can be negative
	 * @param dY The change in y coordinate, can be negative
	 */
	public void move(int dX, int dY) {
		this.position.x += dX;
		this.position.y += dY;
	}

	/**
	 * Gets the unique identifier of the player
	 * @return The unique identifier of the player
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the unique identifier to the given value 
	 * @param identifier The unique identifier to set it to
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Gets the current paint level
	 * @return Returns the current level of paint, from 0 to MAX_PAINTLEVEL
	 */
	public double getPaintLevel() {
		return paintLevel;
	}

	/**
	 * Change the paint level by the given value, will not change if the value goes out of bounds
	 * @param levelChange The amount the change the paint by, can be negative
	 * @return Returns False if the the paint level was not changed due to it being out of bounds
	 */
	public boolean changePaintLevel(double levelChange) {
		return setPaintLevel(paintLevel + levelChange);
	}
	
	/**
	 * Decreases the paintLevel by the given amount, stopping at 0/ 
	 * @param amount The amount of paint to decrease the level by.
	 */
	public void decreasePaintLevel(double amount) {
		if (!changePaintLevel(-amount)) {
			setPaintLevel(0);
		}
	}
	
	/**
	 * Sets the paint level to the given value
	 * @param paintLevel The level of paint to set it to
	 * @return Returns True if the paint level was changed, False is negative or < MAX_PAINTLEVEL 
	 */
	public boolean setPaintLevel(double paintLevel) {
		if (paintLevel > 0 && paintLevel < MAX_PAINTLEVEL) {
			this.paintLevel = paintLevel;
			return true;
		} else return false;
	}

	/**
	 * Gets the direction the player is currently looking
	 * @return The direction the player is currently looking
	 */
	public Double getLookDirection() {
		return lookDirection;
	}

	/**
	 * Sets the look direction to the given value
	 * @param lookDirection The direction the player is looking
	 */
	public void setLookDirection(Double lookDirection) {
		this.lookDirection = lookDirection;
	}

	/**
	 * Gets the gun the player is holding
	 * @return Returns the Gun the player is holding, can be null
	 */
	public Gun getGun() {
		return gun;
	}

	/**
	 * Sets the players gun to the given value
	 * @param gun The gun to give the player, can be null
	 */
	public void setGun(Gun gun) {
		this.gun = gun;
		this.hasGun = true;
	}
	
	/**
	 * Get the amount of points this player has
	 * @return The amount of points
	 */
	public int getPoints() {
		return points;
	}
	
	/**
	 * Awards the player 1 point
	 */
	public void awardPoint() {
		this.points += 1;
	}
	
	/**
	 * Resets the number of points to 0
	 */
	public void clearPoints() {
		this.points = 0;
	}
}
