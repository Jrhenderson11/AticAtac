package com.aticatac.map;

public class demoMap {

	public static void main(String[] args) {
		Map map = new Map(50, 50);
		map.randomiseMap();
		map.saveMap("assets/maps/map.txt");
	}

}
