package com.aticatac.map;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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

	public void updateCoords(int x, int y, int val) {
		this.grid[x][y] = val;
	}
	
	private void fillmap(int fillval) {
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				grid[x][y] = fillval;
			}	
		}
	}
	
	public boolean saveMap(String fileName) {
		String[] lines = new String[grid.length];
		int j =0;
		for (int[] nums : grid) {
			String line = Integer.toString(nums[0]);
			
			for (int i = 1; i < nums.length; i++) {
				line += (":" + Integer.toString(nums[i]));
			}
			lines[j++] = line;
		}
		/*
		for (String line:lines) {
			System.out.println("line: " + line);
		}
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
			int x=0;
			for (String num:line.split(":")) {
				this.grid[x++][y] = Integer.parseInt(num);
			}
		}

	
	}
	
}
