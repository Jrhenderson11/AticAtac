package com.aticatac.world.items;

import java.awt.Point;
import java.awt.Rectangle;

import com.aticatac.world.World;

/**
 * Bullet class for the 'Shoot' type bullets
 * @author dave
 *
 */
public class ShootBullet extends Bullet {

	public ShootBullet(double direction, int moveSpeed, Rectangle rect, int shooter) {
		super(direction, moveSpeed, rect, shooter);
	}
	
	@Override
	public void hit(World world, Point location) {
		//a single paint splat in the given location
		world.getLevel().updateCoords(location.x, location.y, super.getShooter());
		super.kill(world);
	}

}
