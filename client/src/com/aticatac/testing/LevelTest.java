package com.aticatac.testing;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.aticatac.world.Level;

public class LevelTest {
	private Level level;

	/**
	 * Create level that we will test on
	 */
	@Before
	public void setUp() {
		level = TestLevelToUse.getTestLevel();
	}

	/**
	 * Test that we can access the value of the co-ordinates when a valid
	 * co-ordinate is entered
	 */
	@Test
	public void testGetCoords1() {
		assertEquals(1, level.getCoords(0, 0));
	}

	/**
	 * Test that entering an invalid co-ordinate returns -1 rather than throwing an
	 * exception
	 */
	@Test
	public void testGetCoords2() {
		assertEquals(-1, level.getCoords(-2, 4));
	}

	/**
	 * Test that the correct percentage of tiles is returned for the player who's
	 * colour corresponds to 3
	 */
	@Test
	public void testGetPercentT() {
		// 14 tiles have the value 5 and there are 509 overall tiles (excluding walls),
		// the program should round the 2.75 down to 2
		assertEquals(2, level.getPercentTiles(5));
	}

	/**
	 * Test that we can change the values of cells in a circle of radius 2 and
	 * centre position of (15,23) to the value 6, while not changing the walls
	 * surrounding
	 */
	@Test
	public void testMakeCircle() {
		level.makeCircle(15, 23, 2, 6, 1);
		// (12,20) - (18,20)
		ArrayList<Point> pointsToCheck = new ArrayList<>();
		for (int i = 12; i < 19; i++)
			for (int j = 20; j < 25; j++)
				pointsToCheck.add(new Point(i, j));
		// The ordered associated expected values that you would get by using
		// level.getCoords(x, y)
		int[] associatedExpVals = new int[] { 0, 2, 2, 2, 1, 0, 2, 2, 2, 1, 1, 1, 1, 1, 1, 0, 6, 6, 6, 1, 0, 6, 6, 6, 1,
				0, 0, 0, 6, 1, 0, 0, 0, 0, 1 };
		Point p;
		for (int j = 0; j < pointsToCheck.size(); j++) {
			p = pointsToCheck.get(j);
			assertEquals(associatedExpVals[j], level.getCoords(p.x, p.y));
		}

	}

	/**
	 * Test that it is correctly determined that points (5,1) and (7,4) do not have
	 * a direct line of sight
	 */
	@Test
	public void testHasLOS1() {
		assertFalse(level.hasLOS(new Point(4, 1), new Point(7, 4)));
	}

	/**
	 * Test that it is correctly determined that points (6,10) and (13,7) do have a
	 * direct line of sight
	 */
	@Test
	public void testHasLOS2() {
		assertTrue(level.hasLOS(new Point(6, 10), new Point(13, 7)));
	}
}
