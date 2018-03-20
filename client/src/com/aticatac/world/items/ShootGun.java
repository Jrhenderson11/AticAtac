package com.aticatac.world.items;

import java.awt.Point;

import com.aticatac.world.Player;
import com.aticatac.world.World;

@SuppressWarnings("serial")
public class ShootGun extends Gun {
	
	/**
	 * Cost to the users paint level when fired.
	 */
	public static final int PAINTCOST = 4;
	/**
	 * The delay between shots
	 */
	public static final int COOLDOWNTIME = 5;
	
	
	// ------------
	// Constructors
	// ------------
	
	
	/**
	 * Creates a gun of 'Shoot' type.
	 * @param user The user of the gun
	 */
	public ShootGun(Player user) {
		super(user, COOLDOWNTIME, PAINTCOST);
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
		if (ready() && enoughPaint()) {
			resetCooldown();
			ShootBullet bullet = new ShootBullet(direction, target, getUser().getPosition(), getUser().getColour());
			getUser().changePaintLevel(-PAINTCOST);
			return world.addBullet(bullet);
		} else return false;
	}
	
	public int getType(){
		return 1;
	}

}