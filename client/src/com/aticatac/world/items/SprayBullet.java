package com.aticatac.world.items;

import java.awt.Point;
import java.awt.Rectangle;

import com.aticatac.world.World;

public class SprayBullet extends Bullet {

	public SprayBullet(double direction, int moveSpeed, Rectangle rect, int shooter) {
		super(direction, moveSpeed, rect, shooter);
	}
	
	@Override
	public void hit(World world, Point location) {
		//a single paint splat in the given location
		world.getLevel().makeSpray(location.x, location.y, super.getDirection(), super.getShooter());
		super.kill(world);
	}

}
