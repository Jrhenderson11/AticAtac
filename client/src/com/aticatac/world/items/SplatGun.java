package com.aticatac.world.items;

import java.awt.Point;

import com.aticatac.world.Player;
import com.aticatac.world.World;

public class SplatGun extends Gun {

	/**
	 * The delay between shots
	 */
	public static final int COOLDOWNTIME = 30;
	
	
	// ------------
	// Constructors
	// ------------
	
	
	/**
	 * Creates a gun of 'Splat' type.
	 * @param user The user of the gun
	 */
	public SplatGun(Player user) {
		super(user, COOLDOWNTIME);
	}

	
	// -------
	// Methods
	// -------
	
	
	/**
	 * Creates a new bullet, travelling in the given direction at the given target coordinate, within the given world
	 * @param direction The direction in radians, taken from the Player direction
	 * @param target The target coordinate the bullet was fired at.
	 * @param world The world to add the bullet to.
	 */
	@Override
	public boolean fire(double direction, Point target, World world) {
		if (ready()) {
			resetCooldown();
			SplatBullet bullet = new SplatBullet(direction, getUser().getPosition(), getUser().getColour());
			bullet.move(); //move the bullet once so the bullet doesn't hit the player firing
			return world.addBullet(bullet);
		} else return false;
	}

}