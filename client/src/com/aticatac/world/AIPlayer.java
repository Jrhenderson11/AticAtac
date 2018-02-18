package com.aticatac.world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.plaf.synth.SynthSpinnerUI;

import com.aticatac.utils.Controller;
import com.aticatac.world.items.Gun;
import com.aticatac.world.ai.AStar;
import com.aticatac.world.ai.utils.Translations;
import com.aticatac.world.items.ShootGun;
import com.aticatac.world.items.SplatGun;
import com.aticatac.world.items.SprayGun;

import javafx.scene.paint.Color;
import javafx.util.Pair;

public class AIPlayer extends Player {

	private final int PERCENTAGE_TO_MOVE = 85;
	private final int RANGE_TO_SHOOT = 100;
	private int i;

	private Level level;
	private World world;
	private Point gridPosition;

	private LinkedList<Point> currentPath;
	private Random r;
	private ArrayList<Pair<Integer, Integer>> translations = Translations.TRANSLATIONS;

	public AIPlayer(Controller controller, World world, String identifier, int colour) {
		super(controller, identifier, colour);
		this.world = world;
		this.level = world.getLevel();
		this.r = new Random();
		this.currentPath = new LinkedList<>();
		this.i = 0;
		this.gridPosition = new Point(7, 11);
	}

	@Override
	public void update() {
		if (i++ == 200) {
			// Updates too fast
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
		int[][] reducedMap = level.getReducedMap(colour);
		boolean foundTarget = false;
		Player[] otherPlayers = world.getPlayers().toArray(new Player[world.getNumPlayers()]);

		if (currentPath.isEmpty()) {
			for (Player player : otherPlayers) {
				// Fix hasLOS to work a bit better
				if (!player.equals(this) /* && level.hasLOS(position, player.getPosition()) */
						&& inRange(player.getPosition())) {
					// Spray or spit gun
					foundTarget = true;
					Point target = world.displayPositionToCoords(player.getPosition());
					double angle = calculateLookDirection(target);
					setLookDirection(angle);
					// Decide between spray and spit weapon depending on paint levels of each??
					foundTarget = true;
					// For now this is random which one it chooses but in the future
					// we can have a way to decide which one will do more damage
					if (r.nextInt() % 2 == 1) {
						System.out.println("spray");
						setGun(new SprayGun(this));
					} else {
						System.out.println("shoot");
						setGun(new ShootGun(this));
					}
					gun.fire(lookDirection, target, world);
					break;
				}
			}
			// Need to get the reduced Map method to do what we want it to
			if (!foundTarget /* && getCurrentPercentage(reducedMap) > PERCENTAGE_TO_MOVE */) {
				System.out.println("calculate path");
				Point point = closestFreePoint();
				pathToFreePoint(point);
				makeNextMove();
			} /*
				 * else if (!foundTarget) { System.out.println("splat"); setGun(new
				 * SplatGun(this)); // Work on getting this to make better choices for the splat
				 * gun Point target = getQuadrant();
				 * setLookDirection(calculateLookDirection(world.displayPositionToCoords(target)
				 * )); gun.fire(lookDirection, target, world); }
				 */
		} else {
			System.out.println("move");
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
			System.out.println("Quad 2");
			// If in quadrant IV
			return  (3 * Math.PI)/2 + angle;
		} else if (target.getY() > p.getY() && target.getX() <= p.getX()) {
			System.out.println("Quad 3");
			// If in quadrant III or if pi/2 in pos y direction
			return (3 * Math.PI)/2 - angle;
		} else if (target.getY() > p.getY() && target.getX() > p.getX()) {
			System.out.println("Quad 4");
			// If in quadrant II
			return (Math.PI - angle);
		}
		// Otherwise it is in quadrant I or pi/2 in neg y direction
		System.out.println("Quad 1");
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
	public Point getQuadrant() {
		ArrayList<Point> options = new ArrayList<>();
		// Hard coded for now, need to get from the gun
		int range = 20; // gun.getRange();
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
		if (Math.sqrt((Math.pow(target.getX() - position.getX(), 2))
				+ Math.pow(target.getY() - position.getY(), 2)) <= RANGE_TO_SHOOT) {
			// If the point lies inside the circle created by the range
			return true;
		}
		return false;
	}

	/**
	 * Method to get the next movement from a path that has been generated
	 */
	public void makeNextMove() {
		// Need to make this go more smoothly
		Point next = currentPath.poll();
		System.out.println(world.coordsToDisplayPosition(next) + "\t" + position);
		move(world.coordsToDisplayPosition(next).x - position.x, world.coordsToDisplayPosition(next).y - position.y);
		gridPosition.setLocation(next);
	}
	
	public ArrayList<Point> gridToDisplay(Point currentGrid, Point nextGrid){
		ArrayList<Point> newPath = new ArrayList<>();
		Point current = currentGrid;
		Point next;
		while(current!=nextGrid) {
			
		}
		
		return null;
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
}
