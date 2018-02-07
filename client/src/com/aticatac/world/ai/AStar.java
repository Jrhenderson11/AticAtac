package com.aticatac.world.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import com.aticatac.world.Level;

public class AStar {
	private static final double SLOW_COST = 1.5;

	private Point startPoint;
	private Point finishPoint;
	private Level level;
	private Map<Point, Double> cost;
	private Map<Point, Point> parent;
	private int colour;

	public AStar(Point startPoint, Point finishPoint, Level level, int colour) {
		this.startPoint = startPoint;
		this.finishPoint = finishPoint;
		this.level = level;
		this.colour = colour;
		this.cost = new HashMap<>();
		this.parent = new HashMap<>();
	}

	public int getH(Point point, Point finishPoint) {
		return Math.abs(point.x - finishPoint.x) + Math.abs(point.y - finishPoint.y);
	}

	public ArrayList<Point> getPath() {
		ArrayList<Point> path = new ArrayList<>();
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

			for (Point t : this.removeInvalid(getNeighbours(current))) {
				if (!visited.contains(t)) {
					if (this.level.getCoords(t.x, t.y) == this.colour) {
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
		path.add(startPoint);
		Collections.reverse(path);

		return path;
	}

	private ArrayList<Point> removeInvalid(ArrayList<Point> neighbours) {
		ArrayList<Point> validNeighbours = new ArrayList<>();
		int wall;
		for (Point p : neighbours) {
			wall = this.level.getCoords(p.x, p.y);
			// Remove those which are off the grid or collide with a wall
			if (wall != 1 && wall != -1) {
				validNeighbours.add(p);
			}
		}
		return validNeighbours;
	}

	private ArrayList<Point> getNeighbours(Point p) {
		ArrayList<Point> neighbours = new ArrayList<>();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i != 0 && j != 0) {
					neighbours.add(new Point(p.x + i, p.y + j));
				}
			}
		}
		return neighbours;
	}

}
