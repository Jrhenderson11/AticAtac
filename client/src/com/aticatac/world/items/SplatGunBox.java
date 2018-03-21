package com.aticatac.world.items;

import java.awt.Point;
import java.io.Serializable;

import com.aticatac.world.Player;

public class SplatGunBox extends GunBox implements Serializable {
	
	/**
	 * Creates a box that drops a SplatGun to the player when opened 
	 * @param position The position of the top left of the collision rectangle
	 */
	public SplatGunBox(Point position) {
		super(position);
	}

	/**
	 * Opens the box and gives the weapon to the given player
	 * @param player The Player that opened the box
	 */
	@Override
	public void openBox(Player player) {
		Gun gun = new SplatGun(player);
		player.setGun(gun);
		super.setState(OPENED);
	}

}
