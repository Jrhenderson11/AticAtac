package com.aticatac.world.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import com.aticatac.world.Level;
import com.aticatac.world.ai.utils.Translations;

import javafx.util.Pair;

public class AStar {
	private static final double SLOW_COST = 1.5;

	private Point startPoint;
	private Point finishPoint;
	private Level level;
	private Map<Point, Double> cost;
	private Map<Point, Point> parent;
	private int identifier;

	private ArrayList<Pair<Integer, Integer>> translations;

	public AStar(Point startPoint, Point finishPoint, Level level, int identifier) {
		this.startPoint = startPoint;
		this.finishPoint = finishPoint;
		this.level = level;
		this.identifier = identifier;
		this.cost = new HashMap<>();
		this.parent = new HashMap<>();
		this.translations = Translations.TRANSLATIONS_GRID;
	}

	public int getH(Point point, Point finishPoint) {
		return Math.abs(point.x - finishPoint.x) + Math.abs(point.y - finishPoint.y);
	}

	public LinkedList<Point> getPath() {
		LinkedList<Point> path = new LinkedList<>();
		cost.put(startPoint, 0.0);

		Queue<Point> opened = new PriorityQueue<>(11, new Comparator<Point>() {
			@Override
			public int compare(Point p1, Point p2) {
				if ((cost.get(p1) + getH(p1, finishPoint)) < (cost.get(p2) + getH(p2, finishPoint))) {
					return -1;
				} else if ((cost.get(p1) + getH(p1, finishPoint)) > (cost.get(p2) + getH(p2, finishPoint))) {
					return 1;
				}
				return 0;
			}
		});
		Point current = null;
		ArrayList<Point> visited = new ArrayList<Point>();
		opened.add(startPoint);

		while (!opened.isEmpty()) {
			current = opened.poll();
			visited.add(current);

			if (current.equals(finishPoint)) {
				break;
			}

			for (Point t : removeInvalid(getNeighbours(current))) {
				if (!visited.contains(t)) {
					if (level.getCoords(t.x, t.y) == identifier) {
						cost.put(t, cost.get(current) + 1);
					} else {
						// If the tile is covered in another player's colour, they will run over it
						// slower
						cost.put(t, cost.get(current) + SLOW_COST);
					}
					parent.put(t, current);
					opened.add(t);
				}
			}
		}

		assert (current.equals(finishPoint));
		Point parentPoint = null;

		while (!(parentPoint = parent.get(current)).equals(startPoint)) {
			path.add(current);
			current = parentPoint;
		}
		path.add(current);
		Collections.reverse(path);

		return path;
	}

	private ArrayList<Point> removeInvalid(ArrayList<Point> neighbours) {
		ArrayList<Point> validNeighbours = new ArrayList<>();
		int wall;
		for (Point p : neighbours) {
			wall = level.getCoords(p.x, p.y);
			// Remove those which are off the grid or collide with a wall
			if (wall != 1 && wall != -1) {
				validNeighbours.add(p);
			}
		}
		return validNeighbours;
	}
	
	private ArrayList<Point> getNeighbours(Point p) {
		ArrayList<Point> neighbours = new ArrayList<>();
		Point current;
		int val;
		Pair<Integer, Integer> translation;
		for (int i = 0; i < 8; i++) {
			translation = translations.get(i);
			// Key is x translation, Value is y translation
			current = new Point(p.x + translation.getKey(), p.y + translation.getValue());
			val = level.getCoords(current.x,current.y);
			if(val != 1 && val != -1) {
				neighbours.add(current);
			}
		}
		return neighbours;
	}

}
