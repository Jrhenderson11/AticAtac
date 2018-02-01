package com.aticatac.world;

import java.util.LinkedList;

import com.aticatac.utils.Controller;
import com.aticatac.utils.Tile;

public class Player {
	protected final static int MINIMUM_PAINT_FOR_SHOOT = 5;
	protected final static int PAINT_DECREASE = 5;
	protected final static int BASE_PAINT_INCREASE = 5;

	protected Controller controller;
	protected LinkedList<Tile> playerTiles;
	protected int identifier;
	protected String colour;
	protected int paintLevel;
	protected int x;
	protected int y;
	protected Level level;

	public Player(Controller controller, Level level, int identifier, String colour) {
		this.controller = controller;
		this.level = level;
		this.identifier = identifier;
		this.colour = colour;
	}

	public void makeMovement(char control) {
		// Send a request to renderer to move
		// Add tile to playerTiles
	}

	public void shoot() {
		if (this.paintLevel >= MINIMUM_PAINT_FOR_SHOOT) {
			// Send a request to renderer to shoot
			this.decreasePaintLevel();
		}
	}

	public char getAction() {
		// need to get the event and then do the necessary action
		return 0;
	}
	
	public Tile getCurrentTile() {
		return new Tile(this.x, this.y);
	}

	public void increasePaintLevel(int multiplier) {
		this.paintLevel = multiplier * BASE_PAINT_INCREASE;
		// increase paint depending on multiplier from possession in world
	}

	public void decreasePaintLevel() {
		this.paintLevel = this.paintLevel - PAINT_DECREASE;
		// make a sensible decrease to paint level
	}
}
