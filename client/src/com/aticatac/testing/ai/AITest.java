package com.aticatac.testing.ai;

import java.awt.Point;

import org.junit.Before;

import com.aticatac.world.Level;

public abstract class AITest {
	protected static Level level;
	protected static Point start;
	protected static Point end;
	protected static int colour;

	/**
	 * Create the environment in which the tests will be run
	 */
	@Before
	public void setUp() {
		level = new Level(25, 25);
		level.makeWalls();

		// Make a few walls in the middle of the grid
		for (int i = 2; i <= 6; i++) {
			level.updateCoords(5, i, 1);
		}
		for (int j = 16; j <= 24; j++) {
			level.updateCoords(j, 9, 1);
		}
		// Populate the grid with some points occupied by different colours
		level.updateCoords(1, 2, 3);
		level.updateCoords(1, 3, 3);
		level.updateCoords(4, 2, 4);
		level.updateCoords(5, 1, 4);
		level.updateCoords(18, 10, 5);
		level.updateCoords(18, 11, 5);
		level.updateCoords(18, 12, 5);
		level.updateCoords(19, 10, 5);
		level.updateCoords(19, 11, 5);
		level.updateCoords(19, 12, 5);
		level.updateCoords(20, 10, 5);
		level.updateCoords(20, 11, 5);
		level.updateCoords(20, 12, 5);
		level.updateCoords(21, 10, 5);
		level.updateCoords(21, 11, 5);
		level.updateCoords(22, 10, 5);
		level.updateCoords(22, 11, 5);
		level.updateCoords(22, 12,  5);

		colour = 2;
	}

}
