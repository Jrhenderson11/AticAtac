package com.aticatac.testing.items;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Point;

import com.aticatac.utils.Controller;
import com.aticatac.world.Level;
import com.aticatac.world.Player;
import com.aticatac.world.World;
import com.aticatac.world.items.ShootGun;
import com.aticatac.world.items.ShootGunBox;
import com.aticatac.world.items.SplatGun;
import com.aticatac.world.items.SplatGunBox;
import com.aticatac.world.items.SprayGun;
import com.aticatac.world.items.SprayGunBox;


public class GunBoxTest {
	
	/**
	 * Tests the openBox() method
	 */
	@Test
	public void testOpenBox() {
		Player player = new Player(Controller.REAL, "test", 2, null);
		ShootGunBox shootGunBox = new ShootGunBox(new Point(0,0));
		SprayGunBox sprayGunBox = new SprayGunBox(new Point(0,0));
		SplatGunBox splatGunBox = new SplatGunBox(new Point(0,0));
		shootGunBox.openBox(player);
		assertTrue(player.getGun().getClass().equals(ShootGun.class));
		sprayGunBox.openBox(player);
		assertTrue(player.getGun().getClass().equals(SprayGun.class));
		splatGunBox.openBox(player);
		assertTrue(player.getGun().getClass().equals(SplatGun.class));
	}
	
	/**
	 * Test kill() method
	 */
	@Test
	public void testKill() {
		World world = new World(new Level(100, 100));
		ShootGunBox gunBox = new ShootGunBox(new Point(0,0));
		//specific child class doesnt mattter as underlying implementation is in GunBox
		assertTrue(world.getGunBoxes().isEmpty());
		world.addGunBox(gunBox);
		assertFalse(world.getGunBoxes().isEmpty());
		gunBox.kill(world);
		assertTrue(world.getGunBoxes().isEmpty());
	}
	
}
