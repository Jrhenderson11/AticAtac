package com.aticatac.networking.main;

import java.io.ObjectOutputStream;

import org.apache.commons.lang3.SerializationUtils;

import com.aticatac.world.Level;

public class SerialisationTest {
	public static void main(String[] args) {
		System.out.println("starting");
		Level level = new Level(50, 50);
		level.randomiseMap();
//		level.setGrid(new int[][] { { 1, 3 }, { 1, 2 } });
		byte[] buffer = SerializationUtils.serialize(level);
		System.out.println(Integer.BYTES);
		System.out.println("buffer: " + buffer.length);
		System.out.println("=================");
		
		/*
		 * for (byte b: buffer) { System.out.println(b); }
		 */
		Level level2 = SerializationUtils.deserialize(buffer);
		System.out.println("FINISHED");
		/*for (int y = 0; y < level.getHeight(); y++) {
			for (int x = 0; x < level.getWidth(); x++) {
				System.out.println(level.getGrid()[x][y]);
			}
		}*/
	}
}
