package com.aticatac.testing;

import com.aticatac.world.Level;

public class TestLevelToUse {
	private static Level level;
	
	public static Level getTestLevel() {
		level = new Level(25, 25);
		level.makeWalls();

		// Make a few walls in the middle of the grid
		for (int i = 2; i <= 6; i++) {
			level.updateCoords(5, i, 1);
		}
		for (int j = 16; j <= 24; j++) {
			level.updateCoords(j, 9, 1);
		}
		for (int k = 17; k < 24; k++) {
			level.updateCoords(14, k, 1);
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
		level.updateCoords(22, 12, 5);

		for (int l = 10; l <= 14; l++) {
			for (int m = 21; m <= 23; m++) {
				level.updateCoords(l, m, 2);
			}
		}
		return level;

	}
	
}
