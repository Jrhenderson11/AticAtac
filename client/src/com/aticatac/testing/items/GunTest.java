package com.aticatac.testing.items;

import com.aticatac.utils.Controller;
import com.aticatac.world.Player;
import com.aticatac.world.items.ShootGun;
import org.junit.Test;
import static org.junit.Assert.*;

public class GunTest {
	
	/**
	 * Tests if the enoughPaint() function that checks if the player has enough paint to fire the weapon.
	 */
	@Test
	public void testEnoughPaint() {
		Player player = new Player(Controller.REAL, "test", 2, null);
		ShootGun gun = new ShootGun(player); 
		//underlying implementation is in Gun, type of gun used here is irrelevant
		player.setPaintLevel(0);
		assertFalse(gun.enoughPaint()); //no paint, should be false
		player.setPaintLevel(100);      
		assertTrue(gun.enoughPaint());  //max paint, should be true
	}
	
	/**
	 * Tests if the ready() function that checks if the gun is not in cooldown.
	 */
	@Test
	public void testReady() {
		Player player = new Player(Controller.REAL, "test", 2, null);
		ShootGun gun = new ShootGun(player);
		//underlying implementation is in Gun, type of gun used here is irrelevant
		assertTrue(gun.ready());
		gun.resetCooldown();
		assertFalse(gun.ready());
	}
}
