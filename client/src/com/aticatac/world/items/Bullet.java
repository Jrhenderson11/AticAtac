package com.aticatac.world.items;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import com.aticatac.world.World;

public abstract class Bullet implements Collidable, Serializable{
	
	/**
	 * The maximum range of the gun
	 */
	protected final int range;
	/**
	 * Direction the bullet was fired in. Stored in radians, where 'north' (up) is 0 rads, increasing in value clockwise.
	 */
	protected double direction;
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
	 * How far the bullet has traveled.
	 */
	protected int travel;
	
	
	// -----------
	// Constructor
	// -----------
	
	
	public Bullet(int range, double direction, int moveSpeed, Rectangle rect, int shooter) {
		this.range = range;
		this.direction = direction;
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
		
		//check for collision with collideables. Currently only other Bullets.
		for (Collidable collidable: world.getBullets()) {
			if (collidable != this && collidable.getRect().intersects(this.getRect())) {
				hit(world, after); //splat where the bullet hits
			}
		}
	}
	
	/**
	 * Called when a bullet hits a wall or another Collideable.
	 * Removes this object from the world and destroys the object.
	 * More specific bullet classes can override this to create paint splats when hitting things.
	 * @param world The world that contains this object, to delete from.
	 */
	public void hit(World world, Point location) {
		kill(world);
	}
	
	/**
	 * Deletes the object from the world and destroys the object
	 * @param world
	 */
	public void kill(World world) {
		world.removeBullet(this); 
		//garbage collector should delete this object as it will now be unreachable
	}
	
	
	// -------------------
	// Getters and Setters
	// -------------------
	
	
	@Override
	public Rectangle getRect() {
		return rect;
	}

	public double getDirection() {
		return direction;
	}

	public int getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(int moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public int getShooter() {
		return shooter;
	}
	
}