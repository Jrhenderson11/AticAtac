package com.aticatac.world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import com.aticatac.utils.Controller;
import com.aticatac.world.ai.AStar;
import com.aticatac.world.ai.utils.Translations;
import com.aticatac.world.items.GunBox;
import com.aticatac.world.items.SplatBullet;
import com.aticatac.world.items.SplatGun;
import javafx.util.Pair;

public class AIPlayer extends Player {

	// private final int PERCENTAGE_TO_MOVE = 85;
	private final int MAXIMUM_DIST_GUNBOXES = 100;
	private final int RANGE_TO_SHOOT = 100;
	private int i;

	private Random r;

	private Level level;
	private World world;
	private Point gridPosition;

	private LinkedList<Point> currentPath;
	private LinkedList<Point> intermediatePath;
	private ArrayList<Pair<Integer, Integer>> translations = Translations.TRANSLATIONS_GRID;
	private ArrayList<GunBox> gunBoxes;
	private boolean hasGun;

	public AIPlayer(Controller controller, World world, String identifier, int colour) {
		super(controller, identifier, colour);
		this.world = world;
		this.level = world.getLevel();
		this.currentPath = new LinkedList<>();
		this.intermediatePath = new LinkedList<>();
		this.i = 0;
		this.hasGun = false;
		this.r = new Random();
		this.gunBoxes = new ArrayList<>();
	}

	@Override
	public void update() {
		if (i++ == 10) {
			// Updates too fast
			// This speed is good for moving perhaps but not for shooting
			makeDecision();
			i = 0;
		}
	}

	/**
	 * A method to get the next action from the AI player, chooses between moving to
	 * find an uncovered square or shooting a gun at a player or in a general
	 * direction
	 */
	public void makeDecision() {
		// int[][] reducedMap = level.getReducedMap(colour);
		boolean foundTarget = false;
		Player[] otherPlayers = world.getPlayers().toArray(new Player[world.getNumPlayers()]);
		if (currentPath.isEmpty()) {
			if (hasGun && gun.enoughPaint(paintLevel)) {
				System.out.println("I should shoot, shouldn't I?");
				// shouldn't throw a null pointer exception
				for (Player player : otherPlayers) {
					if (!player.equals(this) && level.hasLOS(position, player.getPosition())
							&& inRange(player.getPosition())) {
						System.out.println("Found you");
						foundTarget = true;
						Point target = world.displayPositionToCoords(player.getPosition());
						double angle = calculateLookDirection(target);
						setLookDirection(angle);
						// Decide between spray and spit weapon depending on paint levels of each??
						foundTarget = true;
						getGun().fire(lookDirection, target, world);
						break;
					}
				}
				if (!foundTarget && getGun() instanceof SplatGun) {
					Point target = getQuadrant(SplatBullet.RANGE);
					setLookDirection(calculateLookDirection(target));
					getGun().fire(lookDirection, target, world);
				}
			} else if (!intermediatePath.isEmpty()) {
				System.out.println("intermediate path");
				makeNextMove();
			} else {
				boolean gbInRange = false;
				gunBoxes = new ArrayList<>(world.getGunBoxes());
				for (GunBox gb : gunBoxes) {
					if (gb.getState() == 0
							&& calculateDistance(position, gb.getRect().getLocation()) <= MAXIMUM_DIST_GUNBOXES) {
						pathToFreePoint(gb.getRect().getLocation());
						makeNextMove();
						gbInRange = true;
						break;
					}
				}
				System.out.println(gbInRange);
				if (!gbInRange) {
					System.out.println("no gb in range");
					Point point = closestFreePoint();
					if (point != null) {
						pathToFreePoint(point);
						makeNextMove();
					}
				}
			}
		} else {
			makeNextMove();
		}
	}

	/**
	 * Method to calculate the angle in radians between the player and the target
	 * 
	 * @param target
	 *            A Point anywhere on the current map
	 * @return The angle in radians between the player and the target
	 */
	public double calculateLookDirection(Point target) {
		Point p = world.displayPositionToCoords(position);
		double angle;
		if (target.getX() == p.getX()) {
			// To avoid division by 0 when finding tan^-1
			angle = Math.PI / 2;
		} else {
			// tan^-1(opp/adj)
			angle = Math.atan(Math.abs(target.getY() - p.getY()) / Math.abs(target.getX() - p.getX()));
		}

		if (target.getY() <= p.getY() && target.getX() < p.getX()) {
			// If in quadrant IV
			return (3 * Math.PI) / 2 + angle;
		} else if (target.getY() > p.getY() && target.getX() <= p.getX()) {
			// If in quadrant III or if pi/2 in pos y direction
			return (3 * Math.PI) / 2 - angle;
		} else if (target.getY() > p.getY() && target.getX() > p.getX()) {
			// If in quadrant II
			return (Math.PI / 2 + angle);
		}
		// Otherwise it is in quadrant I or pi/2 in neg y direction
		return Math.PI / 2 - angle;
	}

	/**
	 * Method to get the number of tiles in the reduced map currently covered by the
	 * player
	 * 
	 * @param reducedMap
	 *            A reduced map where 1 indicates a tile is owned by the player and
	 *            0 means it is not
	 * @return The percentage owned by the player
	 */
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

	/**
	 * A method to get the direction which has the greatest gain of tiles if a splat
	 * gun is used here
	 * 
	 * @return The point to represent the direction with the greatest gain of tiles
	 */
	public Point getQuadrant(double rng) {
		int range = (int) rng;
		Point[] points = new Point[] { new Point(position.x, position.y + range),
				new Point(position.x + range, position.y), new Point(position.x, position.y - range),
				new Point(position.x - range, position.y) };
		ArrayList<Point> options = new ArrayList<>();
		Point edges = world.coordsToDisplayPosition(new Point(level.getWidth(), level.getHeight()));

		if (position.y + range < edges.y && level.hasLOS(position, new Point(position.x, edges.y))) {
			options.add(points[0]);
		}
		if (position.x + range < edges.getX() && level.hasLOS(position, new Point(position.x, edges.y))) {
			options.add(points[1]);
		}
		if (position.y - range > 0 && level.hasLOS(position, new Point(position.x, 0))) {
			options.add(points[2]);
		}
		if (position.x - range > 0 && level.hasLOS(position, new Point(0, position.y))) {
			options.add(points[3]);
		}

		Point bestPoint = null;
		if (!options.isEmpty()) {
			int bestCover = -1;
			int currentCover;
			for (Point p : options) {
				currentCover = getCoverage(p);
				if (currentCover >= bestCover) {
					bestCover = currentCover;
					bestPoint = p;
				}
			}
		} else {
			// If options is empty, all of the directions are blocked, so for now just
			// choose a direction at random
			return points[r.nextInt(4)];
		}
		return world.displayPositionToCoords(bestPoint);
	}

	/**
	 * Return the number of tiles which will be gained by using the splat gun aimed
	 * at this point
	 * 
	 * @param p
	 *            The point which represent the centre of the splat
	 * @return The number of tiles which are not walls, out of bounds or already
	 *         owned by the player
	 */
	public int getCoverage(Point p) {
		int splatCoverage = 8; /* gun.getSplatCoverage(); */
		// Hard coded in, need to get from gun
		// Radius = 5
		int x = 0;
		int coord;

		// if this is odd then it is easy, even not so much
		for (int i = -splatCoverage; i < splatCoverage + 1; i++) {
			for (int j = -splatCoverage; j < splatCoverage + 1; j++) {
				/*
				 * System.out.println((p.x + i) + "\t" + (p.y + j)); Point t =
				 * world.displayPositionToCoords(new Point(p.x + i, p.y + j));
				 * System.out.println(t.x + "\t" + t.y);
				 */
				coord = level.getCoords(p.x + i, p.y + j);
				if (coord != colour && coord != 1 && coord != -1) {
					x++;
				}
			}
		}
		// System.out.println(x);
		return x;
	}

	/**
	 * A method to check whether the target is in shooting range of the player
	 * 
	 * @param target
	 *            The point to check against
	 * @return True - if the target is in range False - if the target is not in
	 *         range
	 */
	public boolean inRange(Point target) {
		// Should use an enum to be able to access the bullets for the gun without knowing explicitly the type of gun
		if (calculateDistance(target, position) <= RANGE_TO_SHOOT) {
			// If the point lies inside the circle created by the range
			return true;
		}
		return false;
	}

	public double calculateDistance(Point p1, Point p2) {
		return Math.sqrt((Math.pow(p1.getX() - p2.getX(), 2)) + Math.pow(p1.getY() - p2.getY(), 2));
	}

	/**
	 * Method to get the next movement from a path that has been generated
	 */
	public void makeNextMove() {
		// Need to make this go more smoothly
		if (intermediatePath.isEmpty()) {
			gunBoxes = new ArrayList<>(world.getGunBoxes());
			for (GunBox gb : gunBoxes) {
				if (gb.getRect().getLocation().equals(position)) {
					hasGun = true;
					break;
				}
			}
			if (!hasGun) {
				gridPosition.setLocation(world.displayPositionToCoords(position));
				Point next = currentPath.poll();
				intermediatePath = gridToDisplay(position, next);
				if (!intermediatePath.isEmpty()) {
					Point intermediate = intermediatePath.poll();
					move(intermediate.x - position.x, intermediate.y - position.y);
					gridPosition.setLocation(world.displayPositionToCoords(position));
				}
			}
		} else {
			Point intermediate = intermediatePath.poll();
			move(intermediate.x - position.x, intermediate.y - position.y);
			gridPosition.setLocation(world.displayPositionToCoords(position));
		}
	}

	public LinkedList<Point> gridToDisplay(Point currentGrid, Point nextGrid) {
		LinkedList<Point> newPath = new LinkedList<>();
		Point current = currentGrid;
		Point next = world.coordsToDisplayPosition(nextGrid);
		while (!world.displayPositionToCoords(current).equals(world.displayPositionToCoords(next))
				&& (Math.abs(current.x - next.x) != 1 || Math.abs(current.y - next.y) != 1)) {
			if (current.x < next.x && current.y < next.y) {
				current = new Point(current.x + 2, current.y + 2);
			} else if (current.x < next.x && current.y > next.y) {
				current = new Point(current.x + 2, current.y - 2);
			} else if (current.x < next.x && current.y == next.y) {
				current = new Point(current.x + 2, current.y);
			} else if (current.x > next.x && current.y < next.y) {
				current = new Point(current.x - 2, current.y + 2);
			} else if (current.x > next.x && current.y > next.y) {
				current = new Point(current.x - 2, current.y - 2);
			} else if (current.x > next.x && current.y == next.y) {
				current = new Point(current.x - 2, current.y);
			} else if (current.x == next.x && current.y < next.y) {
				current = new Point(current.x, current.y + 2);
			} else if (current.x == next.x && current.y > next.y) {
				current = new Point(current.x, current.y - 2);
			}
			newPath.add(current);
		}

		if (Math.abs(current.x - next.x) == 1) {
			while (current.y != next.y && Math.abs(current.y - next.y) != 1) {
				if (current.y < next.y) {
					current = new Point(current.x, current.y + 2);
				} else {
					current = new Point(current.x, current.y - 2);
				}
				newPath.add(current);
			}
		} else if (Math.abs(current.y - next.y) == 1) {
			while (current.x != next.x && Math.abs(current.x - next.x) != 1) {
				if (current.x < next.x) {
					current = new Point(current.x + 2, current.y);
				} else {
					current = new Point(current.x - 2, current.y);
				}
				newPath.add(current);
			}
		}
		return newPath;

	}

	/**
	 * Method to get the closest point which is unclaimed by any player
	 * 
	 * @return A point which is unclaimed by any player, is not a wall and is not
	 *         out of bounds
	 */
	public Point closestFreePoint() {
		// Need to fix for when all possible tiles are covered or the player cannot escape
		LinkedList<Point> toOpen = new LinkedList<>();
		ArrayList<Point> visited = new ArrayList<>();
		Collections.shuffle(translations);

		boolean foundClosest = false;

		Point current = gridPosition;
		Point translated;
		Pair<Integer, Integer> translation;

		while (!foundClosest) {
			visited.add(current);
			for (int i = 0; i < 8; i++) {
				translation = translations.get(i);
				// Key is x translation, Value is y translation
				translated = new Point(current.x + translation.getKey(), current.y + (int) translation.getValue());
				// Condition here to check whether the point is accessible
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

	/**
	 * A method to get the path to a given end point, starting from the player's
	 * current position
	 * 
	 * @param endPoint
	 *            The point to find a path to
	 */
	public void pathToFreePoint(Point endPoint) {
		currentPath = (new AStar(gridPosition, endPoint, level, colour)).getPath();
	}

	/*
	 * Sets the Players position to the given display position
	 * 
	 * @param position The display position of the player
	 */
	@Override
	public void setPosition(Point position) {
		this.position = position;
		this.gridPosition = world.displayPositionToCoords(position);
	}

}
