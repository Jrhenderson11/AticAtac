package com.aticatac.world.items;

import java.awt.Point;

import com.aticatac.world.Player;

public class ShootGunBox extends GunBox {

	/**
	 * Creates a box that drops a ShootGun to the Player when opened
	 * @param position The top left position of the collision Rectangle
	 */
	public ShootGunBox(Point position) {
		super(position);
	}

	/**
	 * Opens the box and gives a ShootGun to the given Player
	 * @param player The Player who opened the box
	 */
	@Override
	public void openBox(Player player) {
		Gun gun = new ShootGun(player);
		player.setGun(gun);
		super.setState(OPENED);
	}

}
