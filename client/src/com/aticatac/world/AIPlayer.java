package com.aticatac.world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.aticatac.utils.Controller;
import com.aticatac.world.ai.AStar;
import com.aticatac.world.items.Gun;
import com.aticatac.world.items.ShootGun;
import com.aticatac.world.items.SplatGun;
import com.aticatac.world.items.SprayGun;

import javafx.scene.paint.Color;

public class AIPlayer extends Player {

	private final int PERCENTAGE_TO_MOVE = 85;
	private Level level;
	private World world;
	private LinkedList<Point> currentPath;
	private Random r;

	public AIPlayer(Controller controller, World world, int identifier, Color colour) {
		super(controller, identifier, colour);
		this.world = world;
		this.level = world.getLevel();
		this.r = new Random();
	}

	public void makeDecision(Point[] otherPlayers) {
		int[][] reducedMap = level.getReducedMap(identifier);
		boolean foundTarget = false;

		if (!currentPath.isEmpty()) {
			for (Point player : otherPlayers) {
				if (level.hasLOS(position, player) && inRange(player, gun)) {
					// Spray or spit gun
					foundTarget = true;
					Point target = player.getLocation();
					double angle = calculateLookDirection(target);
					setLookDirection(angle);
					// For now this is random which one it chooses but in the future
					// we can have a way to decide which one will do more damage
					if (r.nextInt() % 2 == 1) {
						setGun(new SprayGun(this));
					} else {
						setGun(new ShootGun(this));
					}
					gun.fire(lookDirection, target, world);
					break;
				}
			}
			if (!foundTarget && getCurrentPercentage(reducedMap) > PERCENTAGE_TO_MOVE) {
				Point point = closestFreePoint();
				pathToFreePoint(point);
				makeNextMove();
			} else {
				setGun(new SplatGun(this));
				Point target = getQuadrant(gun);
				setLookDirection(calculateLookDirection(target));
				gun.fire(lookDirection, target, world);
			}
		} else {
			makeNextMove();
		}
	}

	public double calculateLookDirection(Point target) {
		double angle;
		if (target.getX() == position.getX()) {
			// To avoid division by 0 when finding tan^-1
			angle = Math.PI / 2;
		} else {
			// tan^-1(opp/adj)
			angle = Math.atan(Math.abs(target.getY() - position.getY()) / Math.abs(target.getX() - position.getX()));
		}

		if (target.getY() <= position.getY() && target.getX() < position.getX()) {
			// If in quadrant II
			return (Math.PI - angle);
		} else if (target.getY() > position.getY() && target.getX() <= position.getX()) {
			// If in quadrant III or if pi/2 in pos y direction
			return (Math.PI + angle);
		} else if (target.getY() > position.getY() && target.getX() > position.getX()) {
			// If in quadrant IV
			return ((2 * Math.PI) - angle);
		}
		// Otherwise it is in quadrant I or pi/2 in neg y direction
		return angle;
	}

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
		int range = 300; // g.getRange();
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
		int splatCoverage = 3; /* g.getSplatCoverage(); */
		// 3x3 area would probs be best
		int x = 0;
		int coord;

		// if this is odd then it is easy, even not so much
		for (int i = -(splatCoverage / 2); i < (splatCoverage / 2) + 1; i++) {
			for (int j = -(splatCoverage / 2); j < (splatCoverage / 2) + 1; j++) {
				coord = level.getCoords(p.x + i, p.y + j);
				if (coord != identifier && coord != 1 && coord != -1) {
					x++;
				}
			}
		}
		return x;
	}

	public boolean inRange(Point player, Gun gun) {
		// int range = gun.getRange();
		int range = 200;
		if (Math.sqrt((Math.pow(player.getX() - position.getX(), 2))
				+ Math.pow(player.getY() - position.getY(), 2)) <= range) {
			// If the point lies inside the circle created by the range
			return true;
		}
		return false;
	}

	public void makeNextMove() {
		Point next = currentPath.poll();
		move(next.x - position.x, next.y - position.y);
	}

	public Point closestFreePoint() {
		boolean foundClosest = false;
		Point t = null;
		int i = 0;
		// is there a way to do this more efficently??
		while (!foundClosest) {
			for (int j = -i; j < i; j += 2) {
				for (int k = -i; k < i; k += 2) {
					if (level.getCoords(position.x + j, position.y + k) == 0) {
						foundClosest = true;
						t = new Point(position.x + j, position.y + k);
						break;
					}
				}
			}
			i += 2;
		}
		return t;
	}

	// get the path that the ai player will take to reach the free Point
	public void pathToFreePoint(Point endPoint) {
		currentPath = (new AStar(getPosition(), endPoint, level, identifier)).getPath();
	}
}
