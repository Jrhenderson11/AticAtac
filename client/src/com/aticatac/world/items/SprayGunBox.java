package com.aticatac.world.items;

import java.awt.Point;
import java.io.Serializable;

import com.aticatac.world.Player;

public class SprayGunBox extends GunBox {

	/**
	 * Creates a SprayGunBox at the given position
	 * @param position The position of the top left of the box's collision rectangle
	 */
	public SprayGunBox(Point position) {
		super(position);
	}

	/**
	 * Opens the box, giving a SprayGun to the given Player
	 * @param player The Player who opened the box
	 */
	@Override
	public void openBox(Player player) {
		Gun gun = new SprayGun(player);
		player.setGun(gun);
		super.setState(OPENED);
	}

}
