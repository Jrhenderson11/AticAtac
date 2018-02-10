package com.aticatac.world.items;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import com.aticatac.world.World;

public class SprayBullet extends Bullet {
	
	/**
	 * Default movement speed
	 */
	public static final int MOVESPEED = 6;
	/**
	 * Default collision rect dimensions
	 */
	private static final Dimension RECTSIZE = new Dimension(4, 4);

	
	// -----------
	// Constructor
	// -----------
	
	
	/**
	 * Creates a bullet of the 'Spray' type
	 * @param direction The direction in which the bullet is fired
	 * @param startPosition The position from which it was fired
	 * @param shooter The indentifier of the shooter, used for determining the color
	 */
	public SprayBullet(double direction, Point startPosition, int shooter) {
		super(direction, MOVESPEED, new Rectangle(startPosition, RECTSIZE), shooter);
	}

	
	// -------
	// Methods
	// -------
	
	
	/**
	 * Called when the bullet collides with a wall or another collidable
	 * @param world The world in which this bullet exists
	 * @param location The location of the bullet when it collides
	 */
	@Override
	public void hit(World world, Point location) {
		//a single paint splat in the given location
		world.getLevel().makeSpray(location.x, location.y, super.getDirection(), super.getShooter());
		super.kill(world); //removes this bullet from the world
	}

}