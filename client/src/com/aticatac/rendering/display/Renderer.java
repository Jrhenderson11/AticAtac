package com.aticatac.rendering.display;

import com.aticatac.utils.SystemSettings;
import com.aticatac.world.Level;
import com.aticatac.world.Player;
import com.aticatac.world.World;
import com.aticatac.world.items.Bullet;
import com.aticatac.world.items.GunBox;
import com.aticatac.world.items.ShootGunBox;
import com.aticatac.world.items.SplatGunBox;
import com.aticatac.world.items.SprayGunBox;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer {
	
	private final static Color NORMAL_WALL_COLOR = Color.LIGHTGRAY;
	private Color wallColor;
	/**
	 * The world to render
	 */
	private World world;
	
	
	// ------------
	// Constructors
	// ------------
	
	
	/**
	 * Constructs a Renderer with the given rectangle as the display rectangle
	 */
	public Renderer() {
		this.world = null;
		this.wallColor = NORMAL_WALL_COLOR;
	}
	
	
	// -------
	// Methods
	// -------


	/**
	 * Render map
	 * @param g The GraphicsContext, probably from Canvas.getGraphicsContext2D()
	 */
	public void render(GraphicsContext g) {
		//fill the background with white
		g.setFill(Color.BLACK);
		g.fillRect(0, 0, SystemSettings.getScreenWidth(), SystemSettings.getScreenHeight());
		
		//render the level
		//renderMapBW(g);
		renderTerritory(g);
		renderMapNeon(g, wallColor);
		renderGunBoxes(g);
		renderBullets(g);
		
		//render players
		renderPlayers(g);
	}
	
	/**
	 * Draws the map in black and white
	 * @param g The GraphicsContext to draw to.
	 */
	public void renderMapBW(GraphicsContext g) {
		//render the level
		g.setFill(Color.BLACK);
		Level map = world.getLevel();
		double tileWidth = SystemSettings.getNativeWidth() / map.getWidth();
		double tileHeight = SystemSettings.getNativeHeight() / map.getHeight();
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y <map.getHeight(); y++) {
				if (map.getCoords(x, y) == 1) {                //draw if tile is a wall
					g.fillRect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
				}
			}
		}
	}

	/**
	 * Renders the map with a neon style wire frame for the walls
	 * @param g The GraphicsContext to draw to.
	 * @param color The color of the neon frame
	 */
	public void renderMapNeon(GraphicsContext g, Color color) {
		//settings
		double opacity = 0.5;
		double glowRadius = scaleX(2);
		double glowArc = scaleX(3);
		double barWidth = scaleX(1);
		
		//setup color
		Color opaqueColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
		g.setFill(opaqueColor);
		
		//load map
		Level map = world.getLevel();
		int[][] grid = map.getGrid();
		double tileWidth = scaleX(SystemSettings.getNativeWidth() / map.getWidth());
		double tileHeight = scaleY(SystemSettings.getNativeHeight() / map.getHeight());
		
		// if (x, y) is the top left corner of a quad square
		// 
		// | a | b |
		// ---------
		// | c | d |
		//
		// a = (x, y), b = (x+1, y), c = (x, y+1), d = (x+1, y+1)
		//
		for (int x = 0; x < grid.length - 1; x++) {
			for (int y = 0; y < grid[0].length - 1; y++) {
				//north bar (a ^ b)
				if ((grid[x][y] == 1) ^ (grid[x+1][y] == 1)) {
					double drawX = ((x + 1) * tileWidth);
					double drawY = (y * tileHeight);
					//draw neon glow
					g.setFill(opaqueColor);
					// - glowRadius
					g.fillRoundRect(drawX, drawY, glowRadius * 2, tileHeight + (glowRadius * 2), glowArc, glowArc);
					//draw neon bars
					g.setFill(color);
					g.fillRect(drawX, drawY, barWidth * 2, tileHeight);
				}
				//east bar (b ^ d)
				if ((grid[x+1][y] == 1) ^ (grid[x+1][y+1] == 1)) {
					double drawX = ((x + 1) * tileWidth);
					double drawY = ((y + 1) * tileHeight);
					//draw neon glow
					g.setFill(opaqueColor);
					// - glowRadius
					g.fillRoundRect(drawX, drawY - glowRadius, tileWidth + (glowRadius * 2), glowRadius * 2, glowArc, glowArc);
					//draw neon bars
					g.setFill(color);
					g.fillRect(drawX, drawY, tileWidth, barWidth * 2);
				}
				//south bar (c ^ d)
				if ((grid[x][y+1] == 1) ^ (grid[x+1][y+1] == 1)) {
					double drawX = ((x + 1) * tileWidth);
					double drawY = ((y + 1) * tileHeight);
					//draw neon glow
					g.setFill(opaqueColor);
					//both???
					g.fillRoundRect(drawX, drawY, glowRadius * 2, tileHeight + (glowRadius * 2), glowArc, glowArc);
					//draw neon bars
					g.setFill(color);
					g.fillRect(drawX, drawY, barWidth * 2, tileHeight);
				}
				//west bar (a ^ c)
				if ((grid[x][y] == 1) ^ (grid[x][y+1] == 1)) {
					double drawX = (x * tileWidth);
					double drawY = ((y + 1) * tileHeight);
					//draw neon glow
					g.setFill(opaqueColor);
					//both
					g.fillRoundRect(drawX, drawY, tileWidth + (glowRadius * 2), glowRadius * 2, glowArc, glowArc);
					//draw neon bars
					g.setFill(color);
					g.fillRect(drawX, drawY, tileWidth, barWidth * 2);
				}
			}
		}
	}
	
	/**
	 * Renders the players as a small circle with a line pointing in the lookDirection
	 * @param gc The GraphicsContext to draw to.
	 */
	public void renderPlayers(GraphicsContext gc) {
		double playerSize = scaleX(Player.PLAYER_SIZE);
		double opacity = 0.5;
		double l = scaleX(10); //length of direction pointer
				
		for (Player player: world.getPlayers()) {
			
			Color color = getColourByVal(player.getColour());
			Color opaqueColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
			double px = scaleX(player.getPosition().x); //player position
			double py = scaleY(player.getPosition().y);
			
			gc.setStroke(color);
			gc.setFill(color);
			gc.setLineWidth(scaleX(1.0));
			gc.fillOval(px - (playerSize/4), py - (playerSize/4), playerSize, playerSize);
			gc.strokeLine(px+(playerSize/4), py+(playerSize/4), px+(playerSize/4) + (l * Math.sin(player.getLookDirection())), py+(playerSize/4) - (l * Math.cos(player.getLookDirection())));
			gc.setStroke(opaqueColor);
			gc.setLineWidth(scaleX(3));
			gc.strokeOval(px - (playerSize/4), py - (playerSize/4), playerSize, playerSize);
		}
	}
	
	/**
	 * Draws the territories players have claimed to the map.
	 * @param gc The GraphicsContext to draw to.
	 */
	public void renderTerritory(GraphicsContext gc) {
		int[][] grid = world.getLevel().getGrid();
		double tileWidth = scaleX(SystemSettings.getNativeWidth() / world.getLevel().getWidth());
		double tileHeight = scaleY(SystemSettings.getNativeHeight() / world.getLevel().getHeight());
		double opacity = 1;
		double brightness = 0.4;
		
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				for (Player player: world.getPlayers()) {
					if (grid[x][y] == player.getColour()) {
						Color color = getColourByVal(player.getColour());
						Color opaqueColor = new Color(color.getRed() * brightness, color.getGreen()* brightness, color.getBlue()* brightness, opacity);
						gc.setFill(opaqueColor);
						gc.fillRect(x * tileWidth, y * tileHeight, Math.ceil(tileWidth), Math.ceil(tileHeight) + tileHeight);
					}
				}

				if (grid[x][y] == 1) {
					Color color = wallColor;
					Color opaqueColor = new Color(color.getRed() * brightness, color.getGreen()* brightness, color.getBlue()* brightness, opacity);
					gc.setFill(opaqueColor);
					gc.fillRect(x * tileWidth, y * tileHeight, Math.ceil(tileWidth), Math.ceil(tileHeight) + tileHeight/10);
				}
			}
		}
	}
	
	/**
	 * Render the bullets in the world.
	 * @param g The GraphicsContext to draw to.
	 */
	private void renderBullets(GraphicsContext g) {
		for (Bullet bullet: world.getBullets()) {
			//g.setFill(world.getPlayerColour(bullet.getShooter()));
			g.setFill(getColourByVal(bullet.getShooter()));
			g.fillOval(scaleX(bullet.getRect().x), scaleY(bullet.getRect().y), scaleX(bullet.getRect().width), scaleY(bullet.getRect().height));
		}
	}
	
	/**
	 * Renders the GunBoxes in the World
	 * @param g The GraphicsContext to draw to.
	 */
	private void renderGunBoxes(GraphicsContext g) {
		//settings
		Color boxOutline = Color.RED;
		Color boxDetail = Color.BLACK;
		Color boxType = Color.WHITE;
		double detailWidth = scaleX(2);
		double detailMargin = scaleX(2);
		double shootSize = scaleX(3);
		double sprayLength = scaleX(10);
		double splatSize = scaleX(7);
		double boxArc = scaleX(3);
		//Render each gunbox
		for (GunBox gunbox: world.getGunBoxes()) {
			double xpos = scaleX(gunbox.getRect().getX());
			double ypos = scaleY(gunbox.getRect().getY());
			double width = scaleX(gunbox.getRect().getWidth());
			double height = scaleY(gunbox.getRect().getHeight());
			double tmpx;
			double tmpy;
			//draw generic box
			g.setFill(boxOutline);
			g.fillRoundRect(xpos, ypos, width, height, boxArc, boxArc); //rectangle box
			//draw detail
			g.setFill(boxDetail);
			tmpx = xpos + detailMargin; //get xpos for left side bar
			g.fillRect(tmpx, ypos, detailWidth, height);
			tmpx = xpos + width - detailMargin - detailWidth; //get ypos for right side bar
			g.fillRect(tmpx, ypos, detailWidth, height);
			//draw details specific to item
			g.setFill(boxType);
			if (gunbox.getClass().equals(ShootGunBox.class)) {
				tmpx = xpos + ((width - shootSize) / 2);
				tmpy = ypos + ((height - shootSize) / 2); 
				g.fillRect(tmpx, tmpy, shootSize, shootSize);
			} else if (gunbox.getClass().equals(SplatGunBox.class)) {
				tmpx = xpos + ((width - splatSize) / 2);
				tmpy = ypos + ((height - splatSize) / 2);
				g.fillOval(tmpx, tmpy, splatSize, splatSize);
			} else if (gunbox.getClass().equals(SprayGunBox.class)) {
				tmpx = xpos + ((width - sprayLength) / 2);
				tmpy = ypos + ((height - shootSize) / 2);
				g.fillRect(tmpx, tmpy, sprayLength, shootSize);
			}
		}
	}

	/**
	 * Returns the World instance this renderer draws
	 * @return The World instance
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Sets the World instance this renderer draws
	 * @param world The World instance to set the renderer to draw.s
	 */
	public void setWorld(World world) {
		this.world = world;
	}
	
	/**
	 * Scales the given 'x' value from its relative native position to screen width.
	 * @param x The 'x' value to scale
	 * @return A scaled 'x' value if scaling is on, or returns an unchanged 'x' if not
	 */
	private double scaleX(double x) {
		return SystemSettings.getScaledX(x);
	}
	
	/**
	 * Scales the given 'y' value from its relative native position to screen height.
	 * @param y The 'y' value to scale
	 * @return A scaled 'y' value if scaling is on, or returns an unchanged 'y' if not
	 */
	private double scaleY(double y) {
		return SystemSettings.getScaledY(y);
	}
	/**
	 * Translates between integers and colours
	 * @param val the val to translate
	 * @return The Color the value corresponds to.
	 */
	public static Color getColourByVal(int val) {
		switch (val) {
		case 2:
			return Color.BLUE;
		case 3:
			return Color.YELLOW;
		case 4:
			return Color.GREEN;
		case 5:
			return Color.RED;
		case 6:
			return Color.PURPLE;
		case 7:
			return Color.MAGENTA;
		}
		return Color.PINK;
	}
	
}