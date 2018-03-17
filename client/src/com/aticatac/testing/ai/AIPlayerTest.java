package com.aticatac.testing.ai;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Arrays;

import org.junit.Test;

import com.aticatac.utils.Controller;
import com.aticatac.world.AIPlayer;
import com.aticatac.world.World;

public class AIPlayerTest extends AITest {
	private AIPlayer player;
	private World world;
	private String identifier;

	@Override
	public void setUp() {
		super.setUp();
		world = new World(level);
		identifier = "test-ai";
		player = new AIPlayer(Controller.AI, world, identifier, colour);
	}

	@Test
	public void testClosestFreePoint() {
		player.setPosition(world.coordsToDisplayPosition(new Point(20, 10)));
		// We have blocked in the player by another player's colour so that (21,12) is
		// the closest free point, regardless of the order the translations are applied
		// in the function
		assertEquals(new Point(21, 12), player.closestFreePoint());
	}

	@Test
	public void testMakeDecision() {
		fail("Not implemented yet");
	}

	@Test
	public void testCalculateLookDecision1() {
		player.setPosition(world.coordsToDisplayPosition(new Point(1, 3)));
		Point target = new Point(2, 8);
		// tan-1(5) in quadrant 2, means gives an angle of pi/2 + tan-1(5)
		assertEquals(2.944197, player.calculateLookDirection(target), 0.1);
	}

	@Test
	public void testCalculateLookDirection2() {
		player.setPosition(world.coordsToDisplayPosition(new Point(4, 2)));
		Point target = new Point(4, 7);
		// when both co-ords x is the same, angle is pi/2, in quadrant 3 gives angles of
		// (3 pi)/2 - pi/2 = pi
		assertEquals(Math.PI, player.calculateLookDirection(target), 0);
	}

	@Test
	public void testGetQuadrant() {
		fail("Not implemented yet");
	}

	@Test
	public void testMakeNextMove() {
		fail("Not implemented yet");
	}

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

	@Test
	public void testInRange1() {
		player.setPosition(world.coordsToDisplayPosition(new Point(3, 3)));
		Point target = world.coordsToDisplayPosition(new Point(4, 5));
		// Player should be well within range of target
		assertTrue(player.inRange(target));
	}

	@Test
	public void testInRange2() {
		player.setPosition(world.coordsToDisplayPosition(new Point(1, 1)));
		Point target = world.coordsToDisplayPosition(new Point(8, 8));
		// Player should not be within range of target
		assertFalse(player.inRange(target));
	}

}
