package com.aticatac.world;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import com.aticatac.utils.Controller;
import com.aticatac.world.items.Gun;

@SuppressWarnings("serial")
public class Player implements Serializable {
	
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
    protected Point.Double position;
    /**
     * The direction the player is looking stored as radians, 0 is looking to the right. increases clockwise.
     */
    protected Double lookDirection;
    
    protected World world;
    
    public static double SPEED = 1;
    public static double MAXSPEED = 1.5;
    
	protected double xAccel=0, yAccel = 0;
    protected double xVel = 0, yVel = 0;
    protected double friction = 0.4;
    
    
    /**
     * The Gun this player is using, can be null
     */
    protected Gun gun;
    /**
     * The number of points this player has
     */
    protected int points;
    
    /**
     * Whether the player currently has a gun that it can use
     */
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
    public Player(Controller controller, String identifier, int colour, World braveNewWorld) {
    	this.controller = controller;
    	this.setIdentifier(identifier);
    	this.colour = colour;
    	this.position = new Point.Double(10, 10);
    	this.lookDirection = 0.0;
    	this.paintLevel = Player.MAX_PAINTLEVEL;
    	this.points = 0;
    	this.hasGun = false;
    	this.world = braveNewWorld;
    }
    
    // -------
    // Methods
    // -------
    
    /**
     * Returns the Controller type for this player
     * @return The type of player that it is: AI or Real
     */
    public Controller getController() {
    	return controller;
    }
    
    /**
     * Updates the player
     */
    public void update() {
    	
    	//move
    	//get current acceleration + add to velocity
    	System.out.println("\nBEFORE FRICTION:");
    	System.out.println("XACCEL, YACCEL = " + xAccel + ", " + yAccel);
    	System.out.println("XVEL, YVEL = " + xVel + ", " + yVel);
    	System.out.println("fric force x: " + (friction*xVel));
    	System.out.println("fric force y: " + (friction*yVel));
		
    	//apply friction
    	if (this.xVel > 0) {
    		
    		//going right so friction should be applied left
			
    		this.xAccel=Math.min(0, xAccel-Math.abs(friction*xVel));
    		
    	}else if (this.xVel < 0) {
    		this.xAccel=Math.max(0, xAccel+Math.abs(friction*xVel));
    	}
    	
    	if (this.yVel > 0) {
    		this.yAccel=Math.min(0, yAccel-Math.abs(friction*yVel));
    	}else if (this.yVel < 0) {
    		this.yAccel=Math.max(0, yAccel+Math.abs(friction*yVel));
    	}
    	
    	System.out.println("AFTER FRICTION:");
    	System.out.println("XACCEL, YACCEL = " + xAccel + ", " + yAccel);
    	System.out.println("XVEL, YVEL = " + xVel + ", " + yVel);

    	
    	//apply acceleration and cap
		this.xVel = (xVel+xAccel);
		this.yVel = (yVel+yAccel);
		
		this.xVel = Math.min(MAXSPEED, Math.max(-MAXSPEED, this.xVel));
		this.yVel = Math.min(MAXSPEED, Math.max(-MAXSPEED, this.yVel));
   
		
		if (xVel<0.001 && xVel>0) {
			xVel=0;
		}
		if (yVel<0.001 && yVel>0) {
			yVel=0;
		}
		
		
    	System.out.println("AFTER UPDATE: ");
    	System.out.println("XVEL, YVEL = " + xVel + ", " + yVel);
    	
    	//move and check collisions
    	this.move(xVel, yVel);
    	Point p = world.displayPositionToCoords(position);
		if (world.getLevel().getGrid()[p.x][p.y] == 1) {
			System.out.println("COLLIDE");
			this.move(-xVel, -yVel);   	    	    	
		}

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
	public Point.Double getPosition() {
		return position;
	}

	/**
	 * Sets the Players position to the given display position
	 * @param position The display position of the player
	 */
	public void setPosition(Point.Double position) {
		this.position = position;
	}
	
	public void setPosition(Point position) {
		this.position = new Point.Double(position.x, position.y);
	}
	
	/**
	 * Gets the rectangle that defines the collision boundary of the player
	 * @return Returns a Rectangle defining the players collision boundaries
	 */
	public Rectangle getRect() {
		return new Rectangle((int) position.x-(PLAYER_SIZE/2),(int) position.y-(PLAYER_SIZE/2), PLAYER_SIZE, PLAYER_SIZE);
	}

	/**
	 * Moves the player by the given dX, dY values
	 * @param dX The change in x coordinate, can be negative
	 * @param dY The change in y coordinate, can be negative
	 */
	public void move(double dX, double dY) {
		double newX = this.position.x + dX;
		double newY = this.position.y + dY;
		
		//&& newX<this.world.getLevel().getWidth()
		if (newX>0 ) {
			this.position.x = newX;
		} else {
			System.out.println("im being petulant");
			
			//repeat after me: I'm an idiot because the player is definitely not off the edge of the screen
			System.out.println("I'm an idiot because the player is definitely not off the edge of the screen");
			System.out.println("width: " + this.world.getLevel().getWidth());
			System.out.println("newX: " + newX);
		
		}
		
		//&& newY<this.world.getLevel().getHeight()
		if (newY>0 ) {
			this.position.y = newY;
		} else {
			System.out.println("im being petulant vertically");
		}
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
	 * @return Returns True if the paint level was changed, 
	 * False if the paint level is negative or less than MAX_PAINTLEVEL 
	 */
	public boolean setPaintLevel(double paintLevel) {
		if (paintLevel >= 0 && paintLevel <= MAX_PAINTLEVEL) {
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

    public void setxAccel(double xAccel) {
		this.xAccel = xAccel;
	}

	public void setyAccel(double yAccel) {
		this.yAccel = yAccel;
	}
}