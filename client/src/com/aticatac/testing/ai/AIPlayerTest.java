package com.aticatac.testing.ai;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Arrays;

import org.junit.Test;

import com.aticatac.testing.TestLevelToUse;
import com.aticatac.utils.Controller;
import com.aticatac.world.AIPlayer;
import com.aticatac.world.Level;
import com.aticatac.world.World;
import com.aticatac.world.items.SplatBullet;

public class AIPlayerTest {
	
	private Level level;
	private AIPlayer player;
	private World world;
	private String identifier;

	/**
	 * Create the environment in which the tests will be run, a visual
	 * representation of this map can be found in the appendix of the Test Report
	 */
	public void setUp() {
		level = TestLevelToUse.getTestLevel();
		world = new World(level);
		identifier = "test-ai";
		player = new AIPlayer(Controller.AI, world, identifier, 2);
	}

	/**
	 * Test that the closest free point is found by the AI player. When setting up
	 * the level, we have blocked in the player at position (20,10), so the closest
	 * free point is (21,12).
	 */
	@Test
	public void testClosestFreePoint() {
		player.setPosition(world.coordsToDisplayPosition(new Point(20, 10)));
		assertEquals(new Point(21, 12), player.closestFreePoint());
	}

	/**
	 * Test the calculation of the angle between (1,3) and (2,8)
	 */
	@Test
	public void testCalculateLD1() {
		player.setPosition(world.coordsToDisplayPosition(new Point(1, 3)));
		Point target = new Point(2, 8);
		// tan-1(5) in quadrant 2, means gives an angle of pi/2 + tan-1(5)
		assertEquals(2.944197, player.calculateLookDirection(target), 0.1);
	}

	/**
	 * Test the calculation of the angle between (4,2) and (4,7), avoiding a
	 * division by zero error
	 */
	@Test
	public void testCalculateLD2() {
		player.setPosition(world.coordsToDisplayPosition(new Point(4, 2)));
		Point target = new Point(4, 7);
		// When both co-ords x is the same, angle is pi/2, in quadrant 3 gives angles of
		// (3 pi)/2 - pi/2 = pi
		assertEquals(Math.PI, player.calculateLookDirection(target), 0);
	}

	/**
	 * Test whether the function returns the point which has the greatest gain. When
	 * the grid position is (5,18), the function should check around (12,18) and
	 * (5,7).
	 */
	@Test
	public void testGetQuadrant() {
		player.setPosition(world.coordsToDisplayPosition(new Point(5, 18)));
		// The function should determine that more tiles can be converted around (5,7)
		assertEquals(new Point(5, 7), world.displayPositionToCoords(player.getQuadrant(SplatBullet.RANGE)));
	}

	/**
	 * Test the creation of a path of display points between two grid points (2,2)
	 * and (1,3)
	 */
	@Test
	public void testGridToDisplay() {
		Point current = new Point(2, 2);
		Point next = new Point(1, 3);
		/*
		 * In the testing model we have created, a grid point of (2,2) corresponds to a
		 * display position of (70,47) and first display position we travel to that
		 * corresponds to a grid point of (1,3) is (54,63)
		 */
		assertEquals(
				Arrays.asList(new Point[] { new Point(68, 49), new Point(66, 51), new Point(64, 53), new Point(62, 55),
						new Point(60, 57), new Point(58, 59), new Point(56, 61), new Point(54, 63) }),
				player.gridToDisplay(world.coordsToDisplayPosition(current), next));
	}

	/**
	 * Test that a player with position (3,3) (which corresponds to (98,66) in
	 * display position) is in range of a target with position (4,5) (which
	 * corresponds to (126,104)
	 */
	@Test
	public void testInRange1() {
		player.setPosition(world.coordsToDisplayPosition(new Point(3, 3)));
		Point target = world.coordsToDisplayPosition(new Point(4, 5));
		assertTrue(player.inRange(target));
	}

	/**
	 * Test that a player with position (1,1) (which corresponds to (42,28) in
	 * display position) is not in range of a target with position (8,8) (which
	 * corresponds to (238,161)
	 */
	@Test
	public void testInRange2() {
		player.setPosition(world.coordsToDisplayPosition(new Point(1, 1)));
		Point target = world.coordsToDisplayPosition(new Point(8, 8));
		assertFalse(player.inRange(target));
	}

}
