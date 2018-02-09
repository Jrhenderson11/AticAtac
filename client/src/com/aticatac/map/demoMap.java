package com.aticatac.map;

import com.aticatac.world.Level;

public class demoMap {

	public static void main(String[] args) {
		Level map = new Level(1200, 1200);
		map.randomiseMap();
		map.saveMap("assets/maps/map2.txt");
	}
}