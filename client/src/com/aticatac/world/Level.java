package com.aticatac.world;

import com.aticatac.world.utils.LevelGen;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Level implements Serializable {

	/**
	 * A two-dimensional array that represents the map
	 */
	private int[][] grid;

	/**
	 * The width of the grid
	 */
	private int width;

	/**
	 * The height of the grid
	 */
	private int height;

	// -----------
	// Constructor
	// -----------

	/**
	 * Constructor for a Level given its width and height
	 * 
	 * @param newWidth
	 *            The width of the level
	 * @param newHeight
	 *            The height of the level
	 */
	public Level(int newWidth, int newHeight) {
		this.width = newWidth;
		this.height = newHeight;
		this.grid = new int[width][height];
		this.fillmap(0);
	}

	/**
	 * Constructor for a Level when the grid is already known
	 * 
	 * @param grid
	 *            The two-dimensional array that represents the grid
	 */
	public Level(int[][] grid) {
		this.grid = grid;
		this.width = grid.length;
		this.height = grid[0].length;
	}

	// -------
	// Methods
	// -------

	/**
	 * Get the current grid of this level
	 * 
	 * @return The two-dimensional array that represents the current grid of this
	 *         level
	 */
	public int[][] getGrid() {
		return this.grid;
	}

	/**
	 * Set the current grid of this level
	 * 
	 * @param map
	 *            A two-dimensional array that represents the grid
	 */
	public void setGrid(int[][] map) {
		this.grid = map;
	}

	/**
	 * Get the value associated with a co-ordinate
	 * 
	 * @param x
	 *            The x value of the co-ordinate
	 * @param y
	 *            The y value of the co-ordinate
	 * @return The value in the grid at the position of the co-ordinate
	 */
	public int getCoords(int x, int y) {
		try {
			return grid[x][y];
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Get the width of the current level's grid
	 * 
	 * @return The width of the grid
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Get the height of the current level's grid
	 * 
	 * @return The height of the grid
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Counts the number of tiles associated to a colour
	 * 
	 * @param val
	 *            The number associated to the colour
	 * @return The number of tiles that contain this number as their value
	 */
	public int getNumTiles(int val) {
		int count = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (grid[x][y] == val) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Calculates the percentage of tiles (excluding walls) that a colour has
	 * 
	 * @param val
	 *            The number associated to the colour
	 * @return The percentage of tiles that this number has, to the nearest percent
	 */
	public int getPercentTiles(int val) {
		return (int) ((long) (this.getNumTiles(val) * 100) / (long) ((this.width * this.height) - this.getNumTiles(1)));
	}

	/**
	 * Change a co-ordinate to have a new value
	 * 
	 * @param x
	 *            The x value of the co-ordinate
	 * @param y
	 *            The y value of the co-ordinate
	 * @param val
	 *            The int corresponding to the colour to change the co-ordinate to
	 * @return True if the co-ordinate was changed successfully, False if not
	 */
	public boolean updateCoords(int x, int y, int val) {
		if (x < width && y < height && x >= 0 && y >= 0) {
			if (grid[x][y] != 1) {
				this.grid[x][y] = val;
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns whether a point has the direct line of sight (no walls in between)
	 * with another point
	 * 
	 * @param player
	 *            The position of the player
	 * @param target
	 *            The position of the target
	 * @return Whether there is a direct line of sight
	 */
	public boolean hasLOS(Point player, Point target) {
		int dx = target.x - player.x;
		int dy = target.y - player.y;

		int stepx = 1;
		int stepy = 1;

		if (dx < 0) {
			stepx = -1;
		}
		if (dy < 0) {
			stepy = -1;
		}

		if (dx == 0) {
			for (int y = 0; Math.abs(y - dy) > 0; y += stepy) {
				if (grid[player.x][player.y + y] == 1) {
					return false;
				}
			}
		} else if (dy == 0) {
			for (int x = 0; Math.abs(x - dx) > 0; x += stepx) {
				if (grid[player.x + x][player.y] == 1) {
					return false;
				}
			}
		}

		for (int x = 0; Math.abs(x - dx) > 0; x += stepx) {
			for (int y = 0; Math.abs(y - dy) > 0; y += stepy) {

				if (grid[player.x + x][player.y + y] == 1) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Fill all cells in the map with a given value
	 * 
	 * @param fillval
	 *            The value to change all the cells to
	 */
	private void fillmap(int fillval) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				grid[x][y] = fillval;
			}
		}
	}

	/**
	 * Changes all the cells on the edges of the map to -1 to create a 'wall'
	 */
	public void makeWalls() {

		for (int x = 0; x < width; x++) {
			grid[x][0] = 1;
			grid[x][height - 1] = 1;
		}
		for (int y = 0; y < height; y++) {
			grid[0][y] = 1;
			grid[width - 1][y] = 1;
		}
	}

	/**
	 * Change the cells to reflect that a splat has been made at a position
	 * 
	 * @param posX
	 *            The x value of the co-ordinate at the centre of the splat
	 * @param posY
	 *            The y value of the co-ordinate at the centre of the splat
	 * @param colour
	 *            The int that represents the colour to fill the cells with
	 */
	public void makeSplat(int posX, int posY, int colour) {
		this.makeCircle(posX, posY, 5, colour, 1);
	}

	/**
	 * Change the cells to reflect that a shot has been made at a position
	 * 
	 * @param posX
	 *            The x value of the co-ordinate at the centre of the shot
	 * @param posY
	 *            The y value of the co-ordinate at the centre of the shot
	 * @param colour
	 *            The int that represents the colour to fill the cells with
	 */
	public void makeShoot(int posX, int posY, int colour) {
		this.makeCircle(posX, posY, 1, colour, 1);
	}

	/**
	 * Change the cells to reflect that a spray has been made at a position
	 * 
	 * @param posX
	 *            The x value of the co-ordinate at the centre of the spray
	 * @param posY
	 *            The y value of the co-ordinate at the centre of the spray
	 * @param direction
	 *            The angle of the direction that the spray has been made, in
	 *            radians
	 * @param colour
	 *            The int that represents the colour to fill the cells with
	 */
	public void makeSpray(int posX, int posY, double direction, int colour) {
		int length = 10;
		boolean posSpray = true;
		boolean negSpray = true;
		// When a wall is hit, these will become false to stop the spray going through
		// walls

		// paint center point
		updateCoords(posX, posY, colour);

		// Increase outwards from either side centre point
		for (int i = 0; i < length / 2; i++) {
			int x1 = (int) (posX + (i * Math.sin(direction))); // In one direction
			int y1 = (int) (posY - (i * Math.cos(direction)));
			int x2 = (int) (posX + (i * Math.sin(direction + Math.PI))); // In the opposite direction
			int y2 = (int) (posY - (i * Math.cos(direction + Math.PI)));
			if (posSpray && !updateCoords(x1, y1, colour)) {
				// Spray in positive direction has hit a wall
				posSpray = false;
			}
			if (negSpray && !updateCoords(x2, y2, colour)) {
				// Spray in negative direction has hit wall
				negSpray = false;
			}
		}
	}

	/**
	 * 
	 * @param posX
	 *            The x value of the co-ordinate at the centre of the circle
	 * @param posY
	 *            The x value of the co-ordinate at the centre of the circle
	 * @param radius
	 *            The radius of the circle
	 * @param fillVal
	 *            The int that represents the colour to fill the cells with
	 * @param blockVal
	 *            The value that should not be filled in on the map
	 */
	public void makeCircle(int posX, int posY, int radius, int fillVal, int blockVal) {
		if (posX > width - 1 || posX < 0 || posY > height - 1 || posY < 0) {
			return;
		}
		for (int x = 0; x <= radius; x++) {
			for (int y = 0; y < Math.sqrt((radius * radius) - (x * x)) + 1; y++) {
				if (posY + y < (height - 1)) {
					if (posX + x < (width - 1) && grid[posX + x][posY + y] != blockVal
							&& this.hasLOS(new Point(posX, posY), new Point(posX + x, posY + y))) {
						grid[posX + x][posY + y] = fillVal;
					}
					if ((posX - x > 0) && grid[posX - x][posY + y] != blockVal
							&& this.hasLOS(new Point(posX, posY), new Point(posX - x, posY + y))) {
						grid[posX - x][posY + y] = fillVal;
					}
				}
				if ((posY - y > 0)) {
					if ((posX + x < (width - 1)) && grid[posX + x][posY - y] != blockVal
							&& this.hasLOS(new Point(posX, posY), new Point(posX + x, posY - y))) {
						grid[posX + x][posY - y] = fillVal;
					}
					if ((posX - x > 0) && grid[posX - x][posY - y] != blockVal
							&& this.hasLOS(new Point(posX, posY), new Point(posX - x, posY - y))) {
						grid[posX - x][posY - y] = fillVal;
					}
				}
			}
		}

	}

	/**
	 * Save the current map into a file
	 * 
	 * @param fileName
	 *            The file name of the file to write to
	 * @return True if the map has been saved successfully, False if it has not
	 */
	public boolean saveMap(String fileName) {
		String[] lines = new String[grid.length];
		int j = 0;
		for (int[] nums : grid) {
			String line = Integer.toString(nums[0]);

			for (int i = 1; i < nums.length; i++) {
				line += ("," + Integer.toString(nums[i]));
			}
			lines[j++] = line;
		}
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(fileName));
			try {
				for (String l : lines) {
					output.write(l);
					output.newLine();
				}
				output.close();
				return true;
			} finally {
				output.close();
			}
		} catch (FileNotFoundException ex) {
			System.err.print("ERROR: file " + fileName + " not found");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return false;

	}

	/**
	 * Load and construct a map from a file
	 * 
	 * @param fileName
	 *            The file name of the file containing the map
	 */
	public void loadMap(String fileName) {
		ArrayList<String> lines = new ArrayList<String>();
		// read all Lines from all files in fileName Array

		try {
			BufferedReader input = new BufferedReader(new FileReader(fileName));
			try {
				String line = null;
				while ((line = input.readLine()) != null) {
					lines.add(line);
				}
			} finally {
				input.close();
			}

		} catch (FileNotFoundException ex) {
			System.err.print("ERROR: file " + fileName + " not found");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		for (int y = 0; y < lines.size(); y++) {
			String line = lines.get(y);
			int x = 0;
			for (String num : line.split(",")) {
				this.grid[x++][y] = Integer.parseInt(num);
			}
		}

	}

	/**
	 * Create a random map that is not connected
	 */
	public void randomiseMap() {
		this.setGrid(LevelGen.get(this.width, this.height));
	}

	/**
	 * Serialise the level object so that it may be sent via an output stream
	 * 
	 * @param out
	 *            The output stream to write to
	 * @throws IOException
	 *             May occur due to using an output stream
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		ArrayList<Integer> intList = new ArrayList<Integer>();
		int lastval = -1;
		int num = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (this.grid[x][y] != lastval) {
					if (num != 0) {
						intList.add(num);
						intList.add(lastval);
						num = 0;
					}

					lastval = this.grid[x][y];
				}
				if ((x == width - 1 && y == height - 1)) {
					intList.add(num + 1);
					intList.add(lastval);
				}
				num++;
			}
		}

		byte[] arr = new byte[(intList.size() + 1) * 4];
		int x = 4;
		byte[] num2 = ByteBuffer.allocate(4).putInt((intList.size() * 4)).array();
		for (int j = 0; j < 4; j++) {
			arr[j] = num2[j];
		}
		for (int i = 0; i < intList.size(); i++) {
			num2 = ByteBuffer.allocate(4).putInt(intList.get(i)).array();
			for (int j = 0; j < 4; j++) {
				arr[x++] = num2[j];
			}
		}
		out.write(arr);
	}

	/**
	 * Read in a level object and reconstruct it
	 * 
	 * @param in
	 *            The input stream to read from
	 * @throws IOException
	 *             May occur due to reading from an input stream
	 * @throws ClassNotFoundException
	 *             If the object being received is not a Level object
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {

		byte[] byteArray = new byte[in.readInt()];

		for (int i = 0; i < byteArray.length; i++) {
			byteArray[i] = in.readByte();
		}
		IntBuffer intBuf = ByteBuffer.wrap(byteArray).order(ByteOrder.BIG_ENDIAN).asIntBuffer();

		int[] intArray = new int[intBuf.remaining()];

		for (int j = 0; j < intArray.length; j++) {
			intArray[j] = intBuf.get(j);
		}

		int sum = 0;
		for (int x2 = 0; x2 < intArray.length; x2 += 2) {
			sum += intArray[x2];
		}
		int width = (int) Math.sqrt(sum);

		int x = 0;
		int y = 0;
		int[][] grid = new int[width][width];
		for (int i = 0; i < intArray.length; i += 2) {
			int val = intArray[i + 1];
			int num = intArray[i];
			for (int count = 0; count < num; count++) {
				grid[x][y] = val;

				if (++x == width) {
					x = 0;
					y++;
				}
			}
		}
		this.grid = grid;
		this.width = width;
		this.height = width;
	}
}
