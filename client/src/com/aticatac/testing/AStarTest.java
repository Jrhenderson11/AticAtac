package com.aticatac.testing;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.Point;
import java.util.Arrays;

import com.aticatac.world.ai.AStar;

public class AStarTest extends AITest {
	private static AStar aStar;

	@Test
	public void testGetPath1() {
		start = new Point(1, 1);
		end = new Point(2, 4);
		aStar = new AStar(start, end, level, colour);
		/*
		 * Given our set up with co-ords (1,2) and (1,3) being occupied by a different
		 * colour (so having a different cost), the A Star should give this path
		 */
		assertEquals(Arrays.asList(new Point[] { new Point(2, 2), new Point(2, 3), new Point(2, 4) }), aStar.getPath());
	}

	@Test
	public void testGetPath2() {
		start = new Point(4, 3);
		end = new Point(6, 2);
		aStar = new AStar(start, end, level, colour);
		/*
		 * The A Star should determine that although co-ords (4,2) and (5,1) are
		 * occupied by a different colour (and have a higher cost), the overall cost is
		 * still shorter than going the other way around the wall
		 */
		assertEquals(Arrays.asList(new Point[] { new Point(4, 2), new Point(5, 1), new Point(6, 2) }),
				aStar.getPath());
	}

}
