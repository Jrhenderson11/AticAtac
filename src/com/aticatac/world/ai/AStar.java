package com.aticatac.world.ai;

import java.util.ArrayList;
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
	private Map<Tile, Integer> function;
	private Map<Tile, Tile> parent;
	
	
	public AStar(Tile startTile, Tile finishTile, Level level) {
		this.startTile = startTile;
		this.finishTile = finishTile;
		this.level = level;
		this.cost = new HashMap<>();
		this.function = new HashMap<>();
		this.parent = new HashMap<>();
	}

	
	public int getCost(Tile tile) {
		return this.cost.get(tile);
	}
	
	
	public ArrayList<Tile> getPath() {
		ArrayList<Tile> path = new ArrayList<>();
		
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
		
		while(!opened.isEmpty()) {
			current = opened.poll();
			function.put(current, current.getH(finishTile) + cost.get(current));
			visited.add(current);
			
			if(current.equals(finishTile)) {
				break;
			}
						
			for (Tile t : this.removeInvalid(getNeighbours(current))) {
				if (!visited.contains(t)) {
					cost.put(t, cost.get(current)+1);
					parent.put(t, current);
					opened.add(t);
				}
			}
		}
		
		assert(current.equals(finishTile));
		Tile parentTile = null;
		while(!(parentTile = parent.get(current)).equals(startTile)) {
			path.add(current);
			current = parentTile;
		}
		
		
		return path;
	}

	private ArrayList<Tile> removeInvalid(ArrayList<Tile> neighbours) {
		ArrayList<Tile> validNeighbours = new ArrayList<>();
		for(Tile t: neighbours) {
			if(this.level.getCoords(t.X, t.Y)!=1) {
				validNeighbours.add(t);
			}
		}
		return validNeighbours;
	}
	
	private ArrayList<Tile> getNeighbours(Tile t) {
		ArrayList<Tile> neighbours = new ArrayList<>();
		neighbours.add(new Tile(t.X, t.Y));
		// Do this for each neighbour combination
		return neighbours;
	}

}
