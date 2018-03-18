package com.aticatac.testing.ai;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.Point;
import java.util.Arrays;

import com.aticatac.world.Level;
import com.aticatac.world.ai.AStar;

public class AStarTest {
	private static Level level;
	private static AStar aStar;
	protected static Point start;
	protected static Point end;

	/**
	 * Test that the optimum path is generated between points (1,1) and (2,4),
	 * taking into account the cost of other tiles
	 */
	@Test
	public void testGetPath1() {
		start = new Point(1, 1);
		end = new Point(2, 4);
		aStar = new AStar(start, end, level, 2);
		assertEquals(Arrays.asList(new Point[] { new Point(2, 2), new Point(2, 3), new Point(2, 4) }), aStar.getPath());
	}

	/**
	 * Test that the optimum path is generated between points (4,3) and (6,2),
	 * taking into account the cost of other tiles
	 */
	@Test
	public void testGetPath2() {
		start = new Point(4, 3);
		end = new Point(6, 2);
		aStar = new AStar(start, end, level, 2);
		assertEquals(Arrays.asList(new Point[] { new Point(4, 2), new Point(5, 1), new Point(6, 2) }), aStar.getPath());
	}

}
