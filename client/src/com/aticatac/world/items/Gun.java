package com.aticatac.world.items;

import java.awt.Point;
import java.io.Serializable;

import com.aticatac.world.Player;
import com.aticatac.world.World;

public abstract class Gun implements Serializable{

	
	/**
	 * The duration of the 'cooldown' between shots
	 */
	protected final int cooldownTime;
	/**
	 * The current tick of the cooldown
	 */
	private int currentCooldown;
	/**
	 * The Player using the gun
	 */
	private Player user;
	
	
	// -----------
	// Constructor
	// -----------
	
	
	/**
	 * @param user The Player using the gun
	 * @param range The maximum range of the gun
	 * @param cooldownTime The cooldown between shots
	 */
	public Gun(Player user, int cooldownTime) {
		this.cooldownTime = cooldownTime;
		this.currentCooldown = 0;
		this.user = user;
	}
	
	
	// -------
	// Methods
	// -------
	
	/**
	 * Fires a bullet.
	 * 
	 * Implementations should
	 * * check ready() before firing
	 * * add itself to the given world
	 * 
	 * @param direction The direction the bullet is fired
	 * @param target The target tile coordinate (where the player clicked when firing)
	 * @param world The world to add the bullet to.
	 * @return True if the gun was not in cooldown and fired a bullet.
	 */
	public abstract boolean fire(double direction, Point target, World world);
	
	/**
	 * Checks whether the gun is in cooldown or not
	 * @return True if the gun is not in cooldown and ready to fire.
	 */
	public boolean ready() {
		return currentCooldown <= 0;
	}
	
	/**
	 * Called by World.update(), ticks the cooldown timer
	 */
	public void update() {
		currentCooldown--;
	}
	
	/**
	 * Sets the gun into cooldown, meaning it cannot fire until the duration is up
	 */
	public void resetCooldown() {
		currentCooldown = cooldownTime;
	}


	// -------------------
	// Getters and Setters
	// -------------------
	
	
	public Player getUser() {
		return user;
	}


	public void setUser(Player user) {
		this.user = user;
	}
	
}
