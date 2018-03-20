package com.aticatac.testing.items;

import org.junit.Test;

import com.aticatac.world.Level;
import com.aticatac.world.World;
import com.aticatac.world.items.ShootBullet;

import static org.junit.Assert.*;

import java.awt.Point;

public class BulletTest {
	
	/**
	 * Test the kill() function that removes the bullet from the world
	 */
	@Test
	public void testKill() {
		ShootBullet bullet = new ShootBullet(0, new Point(0,0), new Point(0,0), 2);
		World world = new World(new Level(100, 100));
		assertTrue(world.getBullets().isEmpty());
		world.addBullet(bullet);
		assertFalse(world.getBullets().isEmpty());
		bullet.kill(world);
		assertTrue(world.getBullets().isEmpty());
	}
	
}
