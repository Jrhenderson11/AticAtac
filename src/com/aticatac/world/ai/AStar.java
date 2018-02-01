package com.aticatac.world.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import com.aticatac.utils.Tile;
import com.aticatac.world.Level;

public class AStar {
	private Tile startTile;
	private Tile finishTile;
	private Level level;
	private Map<Tile, Integer> cost;
	private Map<Tile, Tile> parent;

	public AStar(Tile startTile, Tile finishTile, Level level) {
		this.startTile = startTile;
		this.finishTile = finishTile;
		this.level = level;
		this.cost = new HashMap<>();
		this.parent = new HashMap<>();
	}

	public ArrayList<Tile> getPath() {
		ArrayList<Tile> path = new ArrayList<>();
		cost.put(startTile, 0);

		Queue<Tile> opened = new PriorityQueue<>(11, new Comparator<Tile>() {
			@Override
			public int compare(Tile t1, Tile t2) {
				if ((cost.get(t1) + t1.getH(finishTile)) < (cost.get(t2) + t2.getH(finishTile))) {
					return -1;
				} else if ((cost.get(t1) + t1.getH(finishTile)) > (cost.get(t2) + t2.getH(finishTile))) {
					return 1;
				}
				return 0;
			}
		});
		Tile current = null;
		ArrayList<Tile> visited = new ArrayList<Tile>();
		opened.add(startTile);

		while (!opened.isEmpty()) {
			current = opened.poll();
			visited.add(current);

			if (current.equals(finishTile)) {
				break;
			}

			for (Tile t : this.removeInvalid(getNeighbours(current))) {
				if (!visited.contains(t)) {
					cost.put(t, cost.get(current) + 1);
					parent.put(t, current);
					opened.add(t);
				}
			}
		}

		assert (current.equals(finishTile));
		Tile parentTile = null;
		
		while (!(parentTile = parent.get(current)).equals(startTile)) {
			path.add(current);
			current = parentTile;
		}
		path.add(current);
		path.add(startTile);
		Collections.reverse(path);

		return path;
	}

	private ArrayList<Tile> removeInvalid(ArrayList<Tile> neighbours) {
		ArrayList<Tile> validNeighbours = new ArrayList<>();
		int wall;
		for (Tile t : neighbours) {
			wall = this.level.getCoords(t.X, t.Y);
			// Remove those which are off the grid or collide with a wall
			if (wall != 1 && wall != -1) {
				validNeighbours.add(t);
			}
		}
		return validNeighbours;
	}

	private ArrayList<Tile> getNeighbours(Tile t) {
		ArrayList<Tile> neighbours = new ArrayList<>();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i != 0 && j != 0) {
					neighbours.add(new Tile(t.X + i, t.Y + j));
				}
			}
		}
		return neighbours;
	}

}
