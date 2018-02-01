package com.aticatac.world.ai;

import java.util.ArrayList;

import com.aticatac.utils.Tile;
import com.aticatac.world.Level;

public class TestAStar {

	public TestAStar() {
		
	}
	
	
	public static void main(String[] args) {
		Level level = new Level(50,50);
		level.loadMap("assets/maps/map.txt");
		AStar aStar = new AStar(new Tile(1,1), new Tile(2,4), level);
		ArrayList<Tile> path = aStar.getPath();
		for(Tile t: path) {
			System.out.println(t.X + "\t" + t.Y);
		}
	}
}
