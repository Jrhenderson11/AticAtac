package com.aticatac.testing.world;

import org.junit.Test;

import com.aticatac.world.Level;
import com.aticatac.world.World;

import static org.junit.Assert.*;

import java.awt.Point;

public class SpawnTest {
	
	/**
	 * Test GunBox spawning is only on floor tiles
	 */
	@Test
	public void testGunBoxSpawning() {
		Level level = new Level(100, 100);
		level.randomiseMap();
		World world = new World(level);
		Point[] points = world.generateBoxSpawnPoints(4);
		for (Point point : points) {
			Point p = world.displayPositionToCoords(point);
			assertTrue(world.getLevel().getCoords(p.x, p.y) == 0);
		}
	}
	
	/**
	 * Test spawn points generated are only on floor tiles
	 */
	@Test
	public void testPlayerSpawning() {
		Level level = new Level(100, 100);
		level.randomiseMap();
		World world = new World(level);
		Point[] points = world.generatePlayerSpawnPoints();
		for (Point point : points) {
			Point p = world.displayPositionToCoords(point);
			assertTrue(world.getLevel().getCoords(p.x, p.y) == 0);
		}
	}
	
}