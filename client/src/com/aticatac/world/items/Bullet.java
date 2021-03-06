package com.aticatac.world.items;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import com.aticatac.world.Player;
import com.aticatac.world.World;

@SuppressWarnings("serial")
public abstract class Bullet implements Collidable, Serializable{
	
	/**
	 * The distance the bullet will travel
	 */
	protected final int range;
	/**
	 * Direction the bullet was fired in. Stored in radians, where 'north' (up) is 0 rads, increasing in value clockwise.
	 */
	protected double direction;
	/**
	 * The grid position of the world the bullet is shot at.
	 */
	protected Point target;
	/**
	 * Distance the bullet travels evey update.
	 */
	protected int moveSpeed;
	/**
	 * Rectangle defining the collision boundaries.
	 */
	protected Rectangle rect;
	/**
	 * The player that fired the bullet.
	 */
	protected int shooter;
	/**
	 * How far the bullet has travelled.
	 */
	protected int travel;
	
	
	// -----------
	// Constructor
	// -----------
	
	
	/**
	 * Generic constructor for Bullets
	 * @param range The distance the bullet will travel
	 * @param direction The direction in rads, with 'north' being 0 increasing clockwise to 2 pi
	 * @param target The target map grid the bullet is shot at
	 * @param moveSpeed The distance the bullet travels each tick
	 * @param rect The collision rectangle for this bullet
	 * @param shooter The unique int id of the Player who fired the gun, used for coloring
	 */
	public Bullet(int range, double direction, Point target, int moveSpeed, Rectangle rect, int shooter) {
		this.range = range;
		this.direction = direction;
		this.target = target;
		this.moveSpeed = moveSpeed;
		this.shooter = shooter;
		this.rect = rect;
		this.travel = 0;
	}
	
	
	// -------
	// Methods
	// -------
	
	
	/**
	 * Moves the bullet in the initialised direction, distance depends on moveSpeed
	 */
	public void move() {
		int dx = (int) (moveSpeed * Math.sin(direction));
		int dy = (int) (moveSpeed * Math.cos(direction));
		translate(dx, -dy); //y axis goes down so -dy
		travel += moveSpeed;
	}
	
	/**
	 * Translates the rect by the given values
	 * @param dx The change in x
	 * @param dy The change in y
	 */
	public void translate(int dx, int dy) {
		rect.x += dx;
		rect.y += dy;
	}
	
	/**
	 * Updates the bullet within world, will call 'kill' if it collides with any other Collideables or hits a wall.
	 * @param world The world object this bullet exists within, used for collision
	 */
	@Override
	public void update(World world) {
		//store position before movement
		Point before = world.displayPositionToCoords(rect.getLocation());
	
		move();
		
		Point after = world.displayPositionToCoords(rect.getLocation());
		
		//check for range limit
		if (travel > range) {
			hit(world, after);
		}
		
		//check for wall collision after movement
		if (world.getLevel().getCoords(after.x, after.y) == 1) {
			hit(world, before); //paint splat on floor just before the wall it hits
		}
		
		//check for collisions with players
		for (Player player: world.getPlayers()) {
			if (player.getColour() != shooter && player.getRect().intersects(getRect())) {
				player.decreasePaintLevel(20);
				hit(world, world.displayPositionToCoords(player.getPosition()));
			}
		}
		
		//check for collision with collideables. Currently only other Bullets.
		for (int i=0; i<world.getBullets().size(); i++) {
			Collidable collidable = (Collidable) world.getBullets().toArray()[i];
			if (collidable != this && collidable.getRect().intersects(this.getRect())) {
				hit(world, after); //splat where the bullet hits
			}
		}
	}

	/**
	 * Called when a bullet hits a wall or another Collideable.
	 * Removes this object from the world and destroys the object.
	 * More specific bullet classes can override this to create paint splats when hitting things
	 * @param world The world that contains this object to delete from
	 * @param location The location of the object in the world when it is killed
	 */
	public void hit(World world, Point location) {
		kill(world);
	}
	
	/**
	 * Deletes the object from the world and destroys the object
	 * @param world The world that contains this object to delete from
	 */
	public void kill(World world) {
		world.removeBullet(this); 
		//garbage collector should delete this object as it will now be unreachable
	}
	
	
	// -------------------
	// Getters and Setters
	// -------------------
	
	
	/**
	 * Returns the Rectangle defining the collision boundaries of this object
	 * @return The Rectangle defining collision boundaries
	 */ 
	@Override
	public Rectangle getRect() {
		return rect;
	}

	/**
	 * Get the direction the bullet is travelling in.
	 * Stored in radians, with 0 being north or up, increasing to 2PI clockwise
	 * @return The direction in radians as a double
	 */
	public double getDirection() {
		return direction;
	}
	
	public Point getTarget() {
		return target;
	}


	public void setTarget(Point target) {
		this.target = target;
	}


	/**
	 * Get the number of pixels the bullet moves every tick 
	 * @return The move speed
	 */
	public int getMoveSpeed() {
		return moveSpeed;
	}

	/**
	 * Set the number of pixels the bullet moves every tick
	 * @param moveSpeed The value to set the move speed to
	 */
	public void setMoveSpeed(int moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	/**
	 * Get the unique player identifier of the Player that fired this bullet
	 * @return The colour of the player who shot the bullet
	 */
	public int getShooter() {
		return shooter;
	}
	
}