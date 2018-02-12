package com.aticatac.world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.aticatac.utils.Controller;
import com.aticatac.world.ai.AStar;
import com.aticatac.world.ai.utils.Translations;
import com.aticatac.world.items.Gun;
import com.aticatac.world.items.ShootGun;
import com.aticatac.world.items.SplatGun;
import com.aticatac.world.items.SprayGun;

import javafx.scene.paint.Color;
import javafx.util.Pair;

public class AIPlayer extends Player {

	private final int PERCENTAGE_TO_MOVE = 85;
	private final int RANGE_TO_SHOOT = 100;

	private Level level;
	private World world;

	private LinkedList<Point> currentPath;
	private Random r;
	private ArrayList<Pair<Integer, Integer>> translations = Translations.TRANSLATIONS;

	public AIPlayer(Controller controller, World world, String identifier, int colour) {
		super(controller, identifier, colour);
		this.world = world;
		this.level = world.getLevel();
		this.r = new Random();
	}

	public void makeDecision() {
		int[][] reducedMap = level.getReducedMap(colour);
		boolean foundTarget = false;
		Player[] otherPlayers = world.getPlayers().toArray(new Player[world.getNumPlayers()]);

		if (!currentPath.isEmpty()) {
			for (Player player : otherPlayers) {
				if (!player.equals(this) && level.hasLOS(position, player.getPosition())
						&& inRange(player.getPosition())) {
					// Spray or spit gun
					foundTarget = true;
					Point target = player.getPosition();
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
		// Hard coded for now, need to get from the gun
		int range = 200; // g.getRange();
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
		int splatCoverage = 5; /* g.getSplatCoverage(); */
		// Hard coded in, need to get from gun
		// Radius = 5
		int x = 0;
		int coord;

		// if this is odd then it is easy, even not so much
		for (int i = -splatCoverage; i < splatCoverage + 1; i++) {
			for (int j = -splatCoverage; j < splatCoverage + 1; j++) {
				coord = level.getCoords(p.x + i, p.y + j);
				if (coord != colour && coord != 1 && coord != -1) {
					x++;
				}
			}
		}
		return x;
	}

	public boolean inRange(Point player) {
		if (Math.sqrt((Math.pow(player.getX() - position.getX(), 2))
				+ Math.pow(player.getY() - position.getY(), 2)) <= RANGE_TO_SHOOT) {
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
		LinkedList<Point> toOpen = new LinkedList<>();
		ArrayList<Point> visited = new ArrayList<>();

		boolean foundClosest = false;

		Point current = position;
		Point translated;
		Pair<Integer, Integer> translation;

		while (!foundClosest) {
			visited.add(current);
			for (int i = 0; i < 8; i++) {
				translation = translations.get(i);
				// Key is x translation, Value is y translation
				translated = new Point(current.x + translation.getKey(), current.y + translation.getValue());
				if (level.getCoords(translated.x, translated.y) == 0) {
					foundClosest = true;
					return translated;
				} else if (!visited.contains(translated) && !toOpen.contains(translated)
						&& level.getCoords(translated.x, translated.y) != 1) {
					toOpen.add(translated);
				}
			}
			current = toOpen.poll();
		}
		return null;
	}

	// get the path that the ai player will take to reach the free Point
	public void pathToFreePoint(Point endPoint) {
		currentPath = (new AStar(getPosition(), endPoint, level, colour)).getPath();
	}
}
