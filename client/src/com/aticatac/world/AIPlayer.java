package com.aticatac.world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import com.aticatac.utils.Controller;
import com.aticatac.world.ai.AStar;
import com.aticatac.world.ai.utils.Translations;
import com.aticatac.world.items.GunBox;
import com.aticatac.world.items.SplatBullet;
import com.aticatac.world.items.SplatGun;

import javafx.util.Pair;

public class AIPlayer extends Player {

	/**
	 * The maximum distance that an AI player should move to get to the nearest gun
	 * box
	 */
	private final double MAX_DIST_GUNBOXES = 100;

	// Change this and the inRange function so we can actually get the accurate
	// ranges
	private final int RANGE_TO_SHOOT = 100;

	/**
	 * The upper bound of the delay, that the delay variable should reach before
	 * resetting itself
	 */
	private final int DELAY = 5;

	/**
	 * The possible translations that can be applied to a point to move it in any
	 * directions adjacent to it
	 */
	private final ArrayList<Pair<Integer, Integer>> translations = Translations.TRANSLATIONS_GRID;

	/**
	 * A variable used to delay the updates of the AI player so that it is not
	 * updated too frequently when the world updates and emulates the speed of the
	 * regular player(s)
	 */
	private int delay;

	/**
	 * The current level that is being played, which contains the map of the world
	 */
	private Level level;

	/**
	 * The current world that the game is played in, which contains details of all
	 * other real and AI players in the game
	 */
	private World world;

	/**
	 * The position on the map of the level that the player is currently at,
	 * differing from the display position
	 */
	private Point gridPosition;

	/**
	 * The current path that is in process of being followed by the player
	 */
	private LinkedList<Point> currentPath;

	/**
	 * The current intermediate path between two points in the current path
	 */
	private LinkedList<Point> intermediatePath;

	/**
	 * The current gun boxes in the level
	 */
	private ArrayList<GunBox> gunBoxes;

	// -----------
	// Constructor
	// -----------

	/**
	 * Constructor for an AI player
	 * 
	 * @param controller
	 *            The type of Controller of the Player, AI or Real person
	 * @param world
	 *            The current world which the game is being played in
	 * @param identifier
	 *            The unique identifier of the player
	 * @param colour
	 *            The number that identifies the colour of this players paint
	 */
	public AIPlayer(Controller controller, World world, String identifier, int colour) {
		super(controller, identifier, colour);
		this.world = world;
		this.level = world.getLevel();
		this.currentPath = new LinkedList<>();
		this.intermediatePath = new LinkedList<>();
		this.delay = 0;
	}

	// -------
	// Methods
	// -------

	@Override
	public void update() {
		// Different update speeds for movement and shooting?
		if (delay++ == DELAY) {
			// Updates too fast
			// This speed is good for moving perhaps but not for shooting
			makeDecision();
			delay = 0;
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
			if (hasGun && getGun().enoughPaint(getPaintLevel())) {
				for (Player player : otherPlayers) {
					// Fix hasLOS to work a bit better
					if (!player.equals(this) /* && level.hasLOS(position, player.getPosition()) */
							&& inRange(player.getPosition())) {
						foundTarget = true;
						Point target = world.displayPositionToCoords(player.getPosition());
						double angle = calculateLookDirection(target);
						setLookDirection(angle);
						while (getGun().ready()) {
							getGun().fire(lookDirection, target, world);
						}
						// Won't repeatedly shoot at target but follows it around
						break;
					}
				}
				if (!foundTarget && getGun() instanceof SplatGun) {
					Point target = getQuadrant(SplatBullet.RANGE);
					setLookDirection(calculateLookDirection(target));
					getGun().fire(lookDirection, target, world);
				} else if (!foundTarget && !currentPath.isEmpty()) {
					// This shouldn't have an effect on it but it does??
					makeNextMove();
				}
			} else if (!intermediatePath.isEmpty()) {
				makeNextMove();
			} else {
				boolean gbInRange = false;
				gunBoxes = new ArrayList<>(world.getGunBoxes());
				for (GunBox gb : gunBoxes) {
					if (calculateDistance(gb.getRect().getLocation(), position) < MAX_DIST_GUNBOXES) {
						pathToFreePoint(world.displayPositionToCoords(gb.getRect().getLocation()));
						gbInRange = true;
						makeNextMove();
						break;
					}
				}
				if (!gbInRange) {
					// Need to get the reduced Map method to do what we want it to
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
			// System.out.println("Quad 2");
			// If in quadrant IV
			return (3 * Math.PI) / 2 + angle;
		} else if (target.getY() > p.getY() && target.getX() <= p.getX()) {
			// System.out.println("Quad 3");
			// If in quadrant III or if pi/2 in pos y direction
			return (3 * Math.PI) / 2 - angle;
		} else if (target.getY() > p.getY() && target.getX() > p.getX()) {
			// System.out.println("Quad 4");
			// If in quadrant II
			return (Math.PI / 2 + angle);
		}
		// Otherwise it is in quadrant I or pi/2 in neg y direction
		// System.out.println("Quad 1");
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
		Point edges = world.coordsToDisplayPosition(new Point(level.getWidth(), level.getHeight()));
		ArrayList<Point> options = new ArrayList<>();
		if (gridPosition.y + range < level.getHeight()) {
			options.add(new Point(gridPosition.x, gridPosition.y + range));
		}
		if (gridPosition.x + range < level.getWidth()) {
			options.add(new Point(gridPosition.x + range, gridPosition.y));
		}
		if (gridPosition.y - range > 0) {
			options.add(new Point(gridPosition.x, gridPosition.y - range));
		}
		if (gridPosition.x - range > 0) {
			options.add(new Point(gridPosition.x - range, gridPosition.y));
		}

		int bestCover = -1;
		Point bestPoint = null;
		int currentCover;
		int t = 0;
		for (Point p : options) {
			currentCover = getCoverage(p);
			System.out.println(currentCover);
			// ok this is nonsense for the moment, need to change for the future
			if (currentCover >= bestCover /* && (t==0 || r.nextInt()%2 == 1) */) {
				bestCover = currentCover;
				bestPoint = p;
			}
			t++;
		}
		return bestPoint;
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
		if (calculateDistance(target, position) <= RANGE_TO_SHOOT) {
			// If the point lies inside the circle created by the range
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param p1
	 *            A point
	 * @param p2
	 *            A point
	 * @return The distance between the two points entered
	 */
	public double calculateDistance(Point p1, Point p2) {
		return Math.sqrt((Math.pow(p1.getX() - p2.getX(), 2)) + Math.pow(p1.getY() - p2.getY(), 2));
	}

	/**
	 * Method to get the next movement from a path that has been generated
	 */
	public void makeNextMove() {
		// Need to make this go more smoothly
		if (intermediatePath.isEmpty()) {
			gridPosition.setLocation(world.displayPositionToCoords(position));
			Point next = currentPath.poll();
			intermediatePath = gridToDisplay(position, next);
			if (!intermediatePath.isEmpty()) {
				Point intermediate = intermediatePath.poll();
				move(intermediate.x - position.x, intermediate.y - position.y);
				gridPosition.setLocation(world.displayPositionToCoords(position));
			}
		} else {
			Point intermediate = intermediatePath.poll();
			move(intermediate.x - position.x, intermediate.y - position.y);
			gridPosition.setLocation(world.displayPositionToCoords(position));
		}

		// move(world.coordsToDisplayPosition(next).x - position.x,
		// world.coordsToDisplayPosition(next).y - position.y);
		// gridPosition.setLocation(next);
	}

	/**
	 * 
	 * @param currentGrid
	 *            The current grid point on the map that the user is currently at
	 * @param nextGrid
	 *            The next grid point on the map that the user wants to move to
	 * @return An "intermediate" path of display points that join the current grid
	 *         point and the next one
	 */
	public LinkedList<Point> gridToDisplay(Point currentGrid, Point nextGrid) {
		LinkedList<Point> newPath = new LinkedList<>();
		Point current = currentGrid;
		Point next = world.coordsToDisplayPosition(nextGrid);
		while (!world.displayPositionToCoords(current).equals(world.displayPositionToCoords(next))
				&& calculateDistance(current, next) > 1.5) {
			// 1.5 used as a cutoff for when distance is at most a 1,1 translation
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
		if (calculateDistance(current, next) != 0) {
			if (current.x < next.x && current.y < next.y) {
				current = new Point(current.x + 1, current.y + 1);
			} else if (current.x < next.x && current.y > next.y) {
				current = new Point(current.x + 1, current.y - 1);
			} else if (current.x < next.x && current.y == next.y) {
				current = new Point(current.x + 1, current.y);
			} else if (current.x > next.x && current.y < next.y) {
				current = new Point(current.x - 1, current.y + 1);
			} else if (current.x > next.x && current.y > next.y) {
				current = new Point(current.x - 1, current.y - 1);
			} else if (current.x > next.x && current.y == next.y) {
				current = new Point(current.x - 1, current.y);
			} else if (current.x == next.x && current.y < next.y) {
				current = new Point(current.x, current.y + 1);
			} else if (current.x == next.x && current.y > next.y) {
				current = new Point(current.x, current.y - 1);
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
