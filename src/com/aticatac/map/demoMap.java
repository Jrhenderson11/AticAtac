package com.aticatac.map;

import com.aticatac.world.Level;

public class demoMap {

	public static void main(String[] args) {
		Level map = new Level(50, 50);
		map.randomiseMap();
		map.saveMap("assets/maps/map.txt");
	}
}