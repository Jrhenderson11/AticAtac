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
		
		for(int i = 0; i<level.getHeight(); i++) {
			System.out.print(i + "\t");
			for(int j=0; j<level.getHeight(); j++) {
				System.out.print(level.getCoords(i, j));
			}
			System.out.println("\n");
		}
		
		AStar aStar = new AStar(new Point(1,1), new Point(2,4), level, 2);
		ArrayList<Point> path = aStar.getPath();
		for(Point p: path) {
			System.out.println(p.x + "\t" + p.y);
		}
	}
}
