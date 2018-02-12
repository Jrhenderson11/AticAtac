package com.aticatac.world.items;

import java.awt.Rectangle;

import com.aticatac.world.World;

public interface Collidable {
	
	/**
	 * Return the rectangle that defines the collision boundaries of the Collidable object
	 * @return The Rectangle object
	 */
	public Rectangle getRect();
	/**
	 * Updates the item working within the given world.
	 * Implementing classes should use the world object to check for collisions and affect the world accordingly
	 * @param world The world the Collidable object interacts in
	 */
	public void update(World world);
	
}
