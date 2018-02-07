package com.aticatac.world;

import java.awt.Point;
import java.util.ArrayList;

import com.aticatac.utils.Controller;
import com.aticatac.world.ai.AStar;

public class AIPlayer extends Player {

	private static final int PERCENTAGE_TO_MOVE = 85;
	private boolean moving;

	public AIPlayer(Controller controller, Level level, int identifier, int colour) {
		super(controller, level, identifier, colour);
		moving = false;
	}

	@Override
	public char getAction() {
		int[][] reducedMap = this.level.getReducedMap(this.colour);

		if (getCurrentPercentage(reducedMap) > PERCENTAGE_TO_MOVE) {
			// If the area is mostly covered by the players own paint
			if (!this.moving) {
				Point Point = this.closestFreePoint();
				this.makeMovement('p', this.pathToFreePoint(Point));
			}
		} else if (this.inRange()) {
			// If AI player is in range of another player
			// Decide between spray and spit weapon depending on ammunition levels of each??
			this.stop();
			this.shoot();
		} else {
			// choose a direction and use the splat weapon
		}
		return 0;
	}

	// Splat - Fires a paint explosive a certain range that covers an area with
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

	public void stop() {
		// TODO Auto-generated method stub
		this.moving = false;
	}

	public boolean inRange() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void makeMovement(char control) {
		// Need to be able to follow a path, might need a control enum just to contain
		// the different controls
	}

	public void makeMovement(char c, ArrayList<Point> pathToFreePoint) {
		assert (c == 'p');
		this.moving = true;
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
					try {
						if (this.level.getCoords(x + j, y + k) == 0) {
							foundClosest = true;
							t = new Point(x + j, y + k);
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
	public ArrayList<Point> pathToFreePoint(Point endPoint) {
		return (new AStar(this.getCurrentPoint(), endPoint, this.level, this.colour)).getPath();
	}

}
