package com.aticatac.world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Queue;

import com.aticatac.world.Player;
import com.aticatac.keypress.Gun;
import com.aticatac.utils.Controller;
import com.aticatac.world.ai.AStar;

public class AIPlayer extends Player {

	private static final int PERCENTAGE_TO_MOVE = 85;
	private Level level;
	private Queue<Point> currentPath;

	public AIPlayer(Controller controller, Level level, int identifier, int colour) {
		super(controller, identifier, colour);
		this.level = level;
		this.currentPath = null;
	}

	@Override
	public char getAction() {
		int[][] reducedMap = this.level.getReducedMap(this.colour);

		if (getCurrentPercentage(reducedMap) > PERCENTAGE_TO_MOVE) {
			// If the area is mostly covered by the players own paint
			if (this.currentPath.isEmpty()) {
				Point point = this.closestFreePoint();
				pathToFreePoint(point);
				this.doAction('p');
			}
		} else if (this.inRange()) {

			// If AI player is in range of another player
			// Decide between spray and spit weapon depending on ammunition levels of each??
		} else {
			Point direction = getQuadrant(this.splat);
			this.doAction('s', this.splat, direction);
			// splat to this point
		}
		return 0;
	}

	// Splat - Fires a paint explosive a !! certain range that covers an !! area
	// with
	// paint on impact. Medium paint usage
	// Spray - Fires paint that covers the ground and hits any opponents in a
	// straight line.
	// Spit - Fires paint bullets that remove the paint from the ground where
	// it hits an opponent, and takes away some paint ammunition from that player.

	public int getCurrentPercentage(int[][] reducedMap) {
		int count = 0;
		for (int x = 0; x < reducedMap.length; x++) {
			for (int y = 0; y < reducedMap[x].length; y++) {
				if (reducedMap[x][y] == 1) {
					count++;
				}
			}
		}
		return count;
	}

	public Point getQuadrant(Gun g) {
		Point[] options = new Point[4];
		int range = g.getRange();
		options[0] = new Point(this.x, this.y + range);
		options[1] = new Point(this.x + range, this.y);
		options[2] = new Point(this.x, this.y - range);
		options[3] = new Point(this.x - range, this.y);

		int bestCover = 0;
		Point bestPoint = null;
		int currentCover;
		for (Point p : options) {
			currentCover = getCoverage(g, p);
			if (currentCover > bestCover) {
				bestCover = currentCover;
				bestPoint = p;
			}
		}
		return bestPoint;
	}

	public int getCoverage(Gun g, Point p) {
		int splatCoverage = g.getSplatCoverage();
		// 3x3 area would probs be best
		int x = 0;
		int coord;
		// if this is odd then it is easy, even not so much
		for (int i = -(splatCoverage / 2); i < (splatCoverage / 2) + 1; i++) {
			for (int j = -(splatCoverage / 2); j < (splatCoverage / 2) + 1; j++) {
				coord = this.level.getCoords(p.x + i, p.y + j);
				if (coord != this.colour && coord != 1 && coord != -1) {
					x++;
				}
			}
		}
		return x;
	}

	public boolean inRange() {
		return false;
	}

	public void doAction(char c, Gun g, Point direction) {
		assert (c == 's');
		
		// Only should be called with 's'
		// Shoot gun
	}


	public void doAction(char c) {
		assert (c == 'p');
		Point current = currentPath.poll();
		Point next;
		while(!this.currentPath.isEmpty()) {
			next = currentPath.poll();
			move(next.x - current.x, next.y - current.y);
			current = next;
		}
		
		// Only should be called with 'p'

		// Use a class similar to KeyInput to move
	}

	public Point closestFreePoint() {
		boolean foundClosest = false;
		Point t = null;
		int i = 0;
		// is there a way to do this more efficently??
		while (!foundClosest) {
			for (int j = -i; j < i; j++) {
				for (int k = -i; k < i; k++) {
					if (this.level.getCoords(x + j, y + k) == 0) {
						foundClosest = true;
						t = new Point(x + j, y + k);
						break;
					}
				}
			}
			i++;
		}
		return t;
	}

	// get the path that the ai player will take to reach the free Point
	public void pathToFreePoint(Point endPoint) {
		this.currentPath = (new AStar(this.getCurrentPoint(), endPoint, this.level, this.colour)).getPath();
	}

}
