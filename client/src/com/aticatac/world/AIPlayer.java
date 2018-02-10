package com.aticatac.world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import com.aticatac.utils.Controller;
import com.aticatac.world.ai.AStar;

import javafx.scene.paint.Color;

public class AIPlayer extends Player {

	private static final int PERCENTAGE_TO_MOVE = 85;
	private boolean moving;
	private Level level;

	public AIPlayer(Controller controller, Level level, int identifier, Color colour) {
		super(controller, identifier, colour);
		this.level = level;
		moving = false;
	}

	@Override
	public char getAction() {
		int[][] reducedMap = this.level.getReducedMap(this.identifier);

		if (getCurrentPercentage(reducedMap) > PERCENTAGE_TO_MOVE) {
			// If the area is mostly covered by the players own paint
			if (!this.moving) {
				Point Point = this.closestFreePoint();
				this.doAction('p');
			}
		} else if (this.inRange()) {
			// If AI player is in range of another player
			// Decide between spray and spit weapon depending on ammunition levels of each??
			this.stop();
			this.shoot();
		} else {
			Point direction = getQuadrant(null);
			this.doAction('s');
			// splat to this point
		}
		return 0;
	}

	// Splat - Fires a paint explosive a !! certain range that covers an !! area with
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
		int range = 3; /*g.getRange();*/
		options[0] = new Point(position.x, position.y + range);
		options[1] = new Point(position.x + range, position.y);
		options[2] = new Point(position.x, position.y - range);
		options[3] = new Point(position.x - range, position.y);

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
		int splatCoverage = 3; /*g.getSplatCoverage();*/
		// 3x3 area would probs be best
		int x = 0;
		int coord;
		// if this is odd then it is easy, even not so much
		for (int i = -(splatCoverage/2); i < (splatCoverage/2)+1; i++) {
			for (int j = -(splatCoverage/2); j < (splatCoverage/2)+1; j++) {
				coord = this.level.getCoords(p.x + i, p.y + j); 
				if (coord != this.identifier && coord != 1) {
					x++;					
				}
			}
		}
		return x;
	}

	public void stop() {
		this.moving = false;
	}

	public boolean inRange() {
		return false;
	}

	public void doAction(char c) {
		assert (c == 's');
		// Only should be called with 's'
		// Shoot gun
		//this.decreasePaintLevel(g);
	}

	public void doAction(char c, ArrayList<Point> pathToFreePoint) {
		assert (c == 'p');
		// Only should be called with 'p'

		this.moving = true;
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
					try {
						if (this.level.getCoords(position.x + j, position.y + k) == 0) {
							foundClosest = true;
							t = new Point(position.x + j, position.y + k);
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			i++;
		}
		return t;
	}

	// get the path that the ai player will take to reach the free Point
	public LinkedList<Point> pathToFreePoint(Point endPoint) {
		return (new AStar(this.getCurrentPoint(), endPoint, this.level, this.identifier)).getPath();
	}

	private Point getCurrentPoint() {
		// TODO Auto-generated method stub
		return null;
	}

}
