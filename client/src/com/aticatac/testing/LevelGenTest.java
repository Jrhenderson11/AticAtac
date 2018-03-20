package com.aticatac.testing;

import com.aticatac.world.Level;
import com.aticatac.world.utils.LevelGen;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Before;

public class LevelGenTest {

	/**
	 * Test that the method only returns a map that is connected
	 */
	@Test
	public void testGet() {
		assertTrue(LevelGen.connected(LevelGen.get(100, 100)));
	}

	/**
	 * Test that an empty level map is correctly identified as being connected (i.e.
	 * there is a path between every point on the map which is not a wall and every
	 * other point on the map which is not a wall)
	 */
	@Test
	public void testConnected() {
		Level level = new Level(100, 100);
		assertTrue(LevelGen.connected(level.getGrid()));
	}

	/**
	 * Test that a map where we have boxed in tiles that are not walls with walls is
	 * correctly identified as being not connected (i.e. there is not a path between
	 * every point on the map which is not a wall and every other point on the map
	 * which is not a wall)
	 */
	@Test
	public void testConnected2() {
		Level level = new Level(100, 100);
		level.makeWalls();
		for (int i = 1; i < 7; i++) {
			level.updateCoords(i, 6, 1);
			level.updateCoords(6, i, 1);
		}
		level.updateCoords(6, 6, 1);
		assertFalse(LevelGen.connected(level.getGrid()));
	}
}