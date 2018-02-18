package com.aticatac.rendering.display;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.LinkedList;

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
	
	
	// ------
	// Fields
	// ------
	
	
	/**
	 * A rectangle encapsulating the area of the coordinate space where renderable components in this space will be displayed.
	 */
	private Rectangle displayRect;
	/**
	 * A list of the layers to render
	 */
	private LinkedList<RenderLayer> layers;
	/**
	 * The world to render
	 */
	private World world;
	
	
	// ------------
	// Constructors
	// ------------
	
	
	/**
	 * Constructs a new DisplayPanel with the given rectangle as the display rectangle
	 * @param displayRect The rectangle that describes the display's position and dimensions
	 */
	public Renderer(Rectangle displayRect) {
		this.displayRect = displayRect;
		this.layers = new LinkedList<RenderLayer>();
	}
		
	/**
	 * Constructs a new DisplayPanel with a display rectangle of given width and height at the given coordinate (left, top)
	 * @param left The x coordinate of the left side of the display rectangle
	 * @param top The y coordinate of the top side of the display rectangle
	 * @param width The width of the display rectangle
	 * @param height The height of the display rectangle
	 */
	public Renderer(int left, int top, int width, int height) {
		this(new Rectangle(left, top, width, height));
	}
	
	/**
	 * Creates a DisplayPanel with the given dimensions
	 * @param screenSize The dimensions of the panel
	 */
	public Renderer(Dimension screenSize) {
		this(new Rectangle(screenSize));
	}
	
	/**
	 * Creates a DisplayPanel with the given dimensions
	 * @param width The width of the panel
	 * @param height The height of the panel
	 */
	public Renderer(int width, int height) {
		this(0, 0, width, height);
	}
	
	
	// -------
	// Methods
	// -------

	  
	/**
	 * Render map
	 * Render each layer in the layer list in order
	 * @param g The GraphicsContext, probably from Canvas.getGraphicsContext2D()
	 */
	public void render(GraphicsContext g) {
		//fill the background with white
		g.setFill(Color.BLACK);
		g.fillRect(0, 0, displayRect.width, displayRect.height);
		
		//render the level
		//renderMapBW(g);
		renderTerritory(g);
		renderMapNeon(g, Color.RED);
		renderGunBoxes(g);
		renderBullets(g);
		
		//render any other components in the layers
		for (RenderLayer layer: layers) {
			layer.render(g, displayRect);
		}
		
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
		int tileWidth = displayRect.width / map.getWidth();
		int tileHeight = displayRect.height / map.getHeight();
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
		int glowRadius = 2;
		int glowArc = 3;
		int barWidth = 1;
		
		//setup color
		Color opaqueColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
		g.setFill(opaqueColor);
		
		//load map
		Level map = world.getLevel();
		int[][] grid = map.getGrid();
		int tileWidth = displayRect.width / map.getWidth();
		int tileHeight = displayRect.height / map.getHeight();
		
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
					int drawX = ((x + 1) * tileWidth);
					int drawY = (y * tileHeight);
					//draw neon glow
					g.setFill(opaqueColor);
					g.fillRoundRect(drawX, drawY - glowRadius, glowRadius * 2, tileHeight + (glowRadius * 2), glowArc, glowArc);
					//draw neon bars
					g.setFill(color);
					g.fillRect(drawX, drawY, barWidth * 2, tileHeight);
				}
				//east bar (b ^ d)
				if ((grid[x+1][y] == 1) ^ (grid[x+1][y+1] == 1)) {
					int drawX = ((x + 1) * tileWidth);
					int drawY = ((y + 1) * tileHeight);
					//draw neon glow
					g.setFill(opaqueColor);
					g.fillRoundRect(drawX - glowRadius, drawY - glowRadius, tileWidth + (glowRadius * 2), glowRadius * 2, glowArc, glowArc);
					//draw neon bars
					g.setFill(color);
					g.fillRect(drawX, drawY, tileWidth, barWidth * 2);
				}
				//south bar (c ^ d)
				if ((grid[x][y+1] == 1) ^ (grid[x+1][y+1] == 1)) {
					int drawX = ((x + 1) * tileWidth);
					int drawY = ((y + 1) * tileHeight);
					//draw neon glow
					g.setFill(opaqueColor);
					g.fillRoundRect(drawX - glowRadius, drawY - glowRadius, glowRadius * 2, tileHeight + (glowRadius * 2), glowArc, glowArc);
					//draw neon bars
					g.setFill(color);
					g.fillRect(drawX, drawY, barWidth * 2, tileHeight);
				}
				//west bar (a ^ c)
				if ((grid[x][y] == 1) ^ (grid[x][y+1] == 1)) {
					int drawX = (x * tileWidth);
					int drawY = ((y + 1) * tileHeight);
					//draw neon glow
					g.setFill(opaqueColor);
					g.fillRoundRect(drawX - glowRadius, drawY - glowRadius, tileWidth + (glowRadius * 2), glowRadius * 2, glowArc, glowArc);
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
		int playerSize = 8;
		double opacity = 0.5;
		int l = 10; //length of direction pointer
				
		for (Player player: world.getPlayers()) {
			
			Color color;
			
			if (player.getColour()==2) {
				color = Color.BLUE;
			} else {
				color =Color.YELLOW;
			}

			Color opaqueColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
			int px = player.getPosition().x; //player position
			int py = player.getPosition().y;
			
			gc.setStroke(color);
			gc.setLineWidth(1.0);
			gc.strokeOval(px - (playerSize/2), py - (playerSize/2), playerSize, playerSize);
			gc.strokeLine(px, py, px + (l * Math.sin(player.getLookDirection())), py - (l * Math.cos(player.getLookDirection())));
			gc.setStroke(opaqueColor);
			gc.setLineWidth(3);
			gc.strokeOval(player.getPosition().x - (playerSize/2), player.getPosition().y - (playerSize/2), playerSize, playerSize);
		}
	}
	
	/**
	 * Draws the territories players have claimed to the map.
	 * @param gc The GraphicsContext to draw to.
	 */
	public void renderTerritory(GraphicsContext gc) {
		int[][] grid = world.getLevel().getGrid();
		int tileWidth = displayRect.width / world.getLevel().getWidth();
		int tileHeight = displayRect.height / world.getLevel().getHeight();
		double opacity = 0.5;
		double brightness = 0.7;
		
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				for (Player player: world.getPlayers()) {
					if (grid[x][y] == player.getColour()) {
						Color color;
						if (player.getColour()==2) {
							color = Color.BLUE;
						} else {
							color =Color.YELLOW;
						}
						Color opaqueColor = new Color(color.getRed() * brightness, color.getGreen()* brightness, color.getBlue()* brightness, opacity);
						gc.setFill(opaqueColor);
						gc.fillRect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
					}
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
			g.setFill(Color.BLUE);
			g.fillOval(bullet.getRect().x, bullet.getRect().y, bullet.getRect().width, bullet.getRect().height);
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
		int detailWidth = 2;
		int detailMargin = 2;
		int shootSize = 3;
		int sprayLength = 14;
		int splatSize = 10;
		//Render each gunbox
		for (GunBox gunbox: world.getGunBoxes()) {
			double xpos = gunbox.getRect().getX();
			double ypos = gunbox.getRect().getY();
			double width = gunbox.getRect().getWidth();
			double height = gunbox.getRect().getHeight();
			double tmpx;
			double tmpy;
			//draw generic box
			g.setFill(boxOutline);
			g.fillRect(xpos, ypos, width, height); //rectangle box
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
	 * Adds a layer to render
	 * @param layer The RenderLayer to add
	 * @return True if the layer was added
	 */
	public boolean addLayer(RenderLayer layer) {
		return layers.add(layer);
	}
	
	/**
	 * Removes a layer from the rendering.
	 * @param name The name of the layer to remove
	 * @return Returns false if the name is not found
	 */
	public boolean removeLayer(String name) {
		RenderLayer layer = getLayer(name);
		if (layer != null) {
			layer.hide();
			return true;
		} else return false;
	}
	
	/**
	 * Get a layer by its name
	 * @param name The name of the layer
	 * @return Returns the layer, or null if the name is not found/
	 */
	public RenderLayer getLayer(String name) {
		for (RenderLayer layer: layers) {
			if (layer.getName().equals(name)) {
				return layer;
			}
		}
		return null;
	}
	
	/**
	 * Hides the layer from being rendered
	 * @param name
	 * @return
	 */
	public boolean hideLayer(String name) {
		RenderLayer layer = getLayer(name);
		if (layer != null) {
			layer.hide();
			return true;
		} else return false;
	}
	
	/**
	 * Set the layer to be drawn
	 * @param name The name of the layer
	 * @return Returns false if the name was not found
	 */
	public boolean showLayer(String name) {
		RenderLayer layer = getLayer(name);
		if (layer != null) {
			layer.show();
			return true;
		} else return false;
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
	
}