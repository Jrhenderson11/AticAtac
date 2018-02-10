package com.aticatac.world.items;

import java.awt.Point;
import java.awt.Rectangle;

import com.aticatac.world.World;

public class SplatBullet extends Bullet {

	public SplatBullet(double direction, int moveSpeed, Rectangle rect, int shooter) {
		super(direction, moveSpeed, rect, shooter);
	}
	
	@Override
	public void hit(World world, Point location) {
		//creates a 'splat' at the given location
		world.getLevel().makeSplat(location.x, location.y, super.getShooter());
		super.kill(world);
	}

}
