package com.aticatac.world;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Level {

	private int[][] grid;

	private int width;

	private int height;

	public Level(int newWidth, int newHeight) {
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

	public int[][] getReducedMap(int playercolour) {
		//returns a map: 1 indicates this square is playercolour, 0 means it is not
		int[][] redMap = new int[10][10];

		int chunkwidth = width/10;
		int chunkheight = height/10;
		
		for (int y=0; y< 10; y++) {
			for (int x=0; x< 10; x++) {
				//get max for 10th sq 
				
				int[] colours = new int[10];
				for (int colour: colours) {
					colour = 0;
				}
				
				for (int y2=0; y2< chunkheight; y2++) {
					for (int x2=0; x2< chunkwidth; x2++) {
				//		 colours[grid[x*chunkwidth + x2][y*chunkwidth + y2]] +=1; 
						if (grid[x*chunkwidth + x2][y*chunkwidth + y2] == playercolour) {
							colours[1]++;
						} else {
							colours[0]++;
						}
					}
				}
				if (colours[1]>colours[0]) {
					redMap[x][y] = 1;
				} else {
					redMap[x][y] = 0;
				}
				/*
				//calc max and set in red
				int max = 0;
				for (int colour:colours) {
					if (colour > max) {
						
					}
				}
				*/
				
			}
		}
		
		
		return redMap;
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
	
	//updates coords with input restrictions and no overwriting walls
	public boolean safeUpdateCoords(int x, int y, int val) {
		if (x < width && y < height && x >= 0 && y >= 0) {
			if (grid[x][y] != 1) {
				this.grid[x][y] = val;
				return true;
			}
		} 
		return false;
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

	public void makeSplat(int posX, int posY, int colour) {
		//placeholder: make circle radius 5
		this.makeCircle(posX, posY, 5, colour, 1);
	}
	
	public void makeSpray(int posX, int posY, double direction, int colour) {
		//placeholder: make spray of length 6, with the center at the given position
		int length = 8;
		
		//paint center point
		safeUpdateCoords(posX, posY, colour);
		
		//increase outwards from either side center point
		for (int i = 0; i < length/2; i++) {
			int x1 = (int) (posX + (i * Math.sin(direction))); //one direction
			int y1 = (int) (posY - (i * Math.cos(direction)));
			int x2 = (int) (posX + (i * Math.sin(direction + Math.PI))); //the opposite direction
			int y2 = (int) (posY - (i * Math.cos(direction + Math.PI))); 
			safeUpdateCoords(x1, y1, colour);
			safeUpdateCoords(x2, y2, colour);
		}
	}

	public void makeCircle(int posX, int posY, int radius, int fillVal, int blockVal) {
		int width = grid.length;
		int height = grid[0].length;

		for (int x = 0; x <= radius; x++) {
			for (int y = 0; y < Math.sqrt((radius * radius) - (x * x)) + 1; y++) {
				if (posY + y < (height - 1)) {
					if (posX + x < (width - 1) && grid[posX + x][posY + y]!=blockVal) {
						grid[posX + x][posY + y] = fillVal;
					}
					if ((posX - x > 0) && grid[posX - x][posY + y]!=blockVal) {
						grid[posX - x][posY + y] = fillVal;
					}
				}
				if ((posY - y > 0) && grid[posX + x][posY - y]!=blockVal) {
					if (posX + x < (width - 1)) {
						grid[posX + x][posY - y] = fillVal;
					}
					if ((posX - x > 0) && grid[posX - x][posY - y]!=blockVal) {
						grid[posX - x][posY - y] = fillVal;
					}
				}
			}
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
