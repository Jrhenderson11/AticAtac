package com.aticatac.world.ai;

import java.awt.Point;
import java.util.ArrayList;
import com.aticatac.world.Level;

public class TestAStar {

	public TestAStar() {
		
	}
	
	
	public static void main(String[] args) {		
		Level level = new Level(50,50);
		level.loadMap("assets/maps/map.txt");
		
		for(int j =0; j<20;j++) {
			System.out.println(level.getCoords(j, 5));
		}
		
		AStar aStar = new AStar(new Point(1,1), new Point(2,4), level);
		ArrayList<Point> path = aStar.getPath();
		for(Point p: path) {
			System.out.println(p.x + "\t" + p.y);
		}
	}
}
