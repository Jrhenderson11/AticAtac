package com.aticatac.map;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Map {

	private int[][] grid;

	private int width;

	private int height;

	public Map(int newWidth, int newHeight) {
		this.width = newWidth;
		this.height = newHeight;
		this.grid = new int[width][height];
		this.fillmap(0);
	}

	public int[][] getGrid() {
		return this.grid;
	}

	public int getCoords(int x, int y) {
		try {
			return grid[x][y];
		} catch (Exception e) {
			return -1;
		}
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	// used to determine who has control of map by counting tiles of one colour
	public int getNumTiles(int val) {
		int count=0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (grid[x][y]==val) {
					count++;
				}
			}
		}
		return count;
	}

	public void updateCoords(int x, int y, int val) {
		this.grid[x][y] = val;
	}

	private void fillmap(int fillval) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				grid[x][y] = fillval;
			}
		}
	}

	private void makeWalls() {

		for (int x = 0; x < width; x++) {
			grid[x][0] = 1;
			grid[x][height - 1] = 1;
		}
		for (int y = 0; y < height; y++) {
			grid[0][y] = 1;
			grid[width - 1][y] = 1;
		}
	}

	// filehandling
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
		/*
		 * for (String line:lines) { System.out.println("line: " + line); }
		 */
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(fileName));
			try {
				for (String l : lines) {
					output.write(l);
					output.newLine();
				}
				output.close();
				System.out.println("map written to " + fileName);
				return true;
			} finally {
				output.close();
			}
		} catch (FileNotFoundException ex) {
			System.out.print("ERROR: file " + fileName + " not found");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return false;

	}

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
			System.out.print("ERROR: file " + fileName + " not found");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		String[] linesArr = new String[lines.size()];
		for (int y = 0; y < lines.size(); y++) {
			String line = lines.get(y);
			int x = 0;
			for (String num : line.split(",")) {
				this.grid[x++][y] = Integer.parseInt(num);
			}
		}

	}

	public void randomiseMap() {
		Random random = new Random();
		// background
		this.fillmap(0);

		// make squares

		int numsquares = randomInt(25, 10);

		for (int i = 0; i < numsquares; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			makeSqRoom(x, y, random.nextInt(width / 3), random.nextInt(height / 3), 1, random.nextInt(width / 6), 1);
			// pick shape
			switch (random.nextInt(1)) {
			case 0:
				// rect
				makeRect(x, y, random.nextInt(width / 3), random.nextInt(height / 3), 1);
				break;
			case 1:
				// ellipse
				makeSqRoom(x, y, random.nextInt(width / 3), random.nextInt(height / 3), 1, random.nextInt(width / 6),
						1);
				break;
			}

		}

		this.makeWalls();
	}

	// random methods
	private void makeRect(int posX, int posY, int xLen, int yLen, int tile) {

		for (int y = posY; (y - posY < yLen) && (y < height) && (y > 0); y++) {
			for (int x = posX; (x - posX < xLen) && (x < width) && (x > 0); x++) {
				grid[x][y] = tile;
			}
		}
	}

	private void makeSqRoom(int posX, int posY, int xLen, int yLen, int thickness, int doorwidth, int tile) {
		// make outline
		makeRect(posX, posY, xLen, yLen, tile);
		// carve inner
		makeRect(posX + thickness, posY + thickness, (xLen - 2 * thickness), (yLen - 2 * thickness), 0);

		// make doors (even)
		int centreX = posX + (xLen / 2);
		int centreY = posY + (yLen / 2);

		// carve horizontal and vertical to make rooms
		makeRect(posX, (centreY - doorwidth / 2), xLen, doorwidth, 0);

	}

	private int randomGaussian(double mean, double standardDeviation) {
		Random randomer = new Random();
		double val = randomer.nextGaussian() * standardDeviation + mean;
		return (int) val;
	}

	private int randomPosGaussian(double mean, double standardDeviation) {
		Random randomer = new Random();
		double val = randomer.nextGaussian() * standardDeviation + mean;
		return (int) Math.abs(val);
	}

	private int randomInt(int max, int min) {
		Random rand = new Random();

		return rand.nextInt((max - min) + 1) + min;
	}
}
