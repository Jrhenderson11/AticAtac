package com.aticatac.world.items;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import com.aticatac.world.Player;
import com.aticatac.world.World;

public abstract class GunBox implements Serializable {
	
	/**
	 * Default dimensions of the gun box rectangle
	 */
	public static final Dimension DEFAULT_DIMENSION = new Dimension(20, 20);
	/**
	 * The constant for the CLOSED state
	 */
	public static final int CLOSED = 0;
	/**
	 * The constant for the OPENED state
	 */
	public static final int OPENED = 1;
	/**
	 * The rectangle defining the collision boundaries of the box
	 */
	private Rectangle rect;
	/**
	 * The state of the box
	 */
	private int state;
	
	
	// -----------
	// Constructor
	// -----------
	
	
	/**
	 * The constructor for an abstract GunBox
	 * @param position The position of the top left of the collision rectangle of the GunBox
	 */
	public GunBox(Point position) {
		this.setRect(new Rectangle(position, DEFAULT_DIMENSION));
		this.setState(CLOSED);
	}

	
	// -------
	// Methods
	// -------
	
	
	/**
	 * Opens the box, giving the weapon inside to the given Player
	 * @param player The Player that opens the box.
	 */
	public abstract void openBox(Player player);
	
	/**
	 * Updates this box within the world
	 * If a player intersects with the box's rect, open the box for that player and remove box from world.
	 * @param world The World to update the box within.
	 */
	public void update(World world) {
		for (Player player: world.getPlayers()) {
			if (getRect().contains(player.getPosition())) {
				openBox(player);
				kill(world);
			}
		}
	}
	
	/**
	 * Removes this box from the given world
	 * @param world The World to remove this box from.
	 */
	private void kill(World world) {
		world.removeGunBox(this);
	}
	
	
	// -------------------
	// Getters and Setters
	// ------------------- 
	
	
	/**
	 * Get the rectangle defining the collision boundaries of the box
	 * @return Returns a Rectangle object
	 */
	public Rectangle getRect() {
		return rect;
	}

	/**
	 * Set the collision rectangle to the given Rectangle.
	 * @param rect The rectangle to set this box to use.
	 */
	protected void setRect(Rectangle rect) {
		this.rect = rect;
	}

	/**
	 * Gets the current state of the box.
	 * 	0 (CLOSED) - Box is unopened
	 *  1 (OPENED) - Box is opened and will soon be deleted from world.
	 * @return Returns 0 for closed, 1 for open
	 */
	public int getState() {
		return state;
	}

	/**
	 * Set the state to the given value
	 *  0 (CLOSED) - Box is unopened
	 *  1 (OPENED) - Box is opened and will soon be deleted from world.
	 * @param state
	 */
	protected void setState(int state) {
		this.state = state;
	}
	
	
}