package com.aticatac.rendering.display;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.LinkedList;

import com.aticatac.world.Level;
import com.aticatac.world.World;

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
		g.setFill(Color.WHITE);
		g.fill();
		
		//render the level
		renderMap(g);	
		
		//render any other components in the layers
		for (RenderLayer layer: layers) {
			layer.render(g, displayRect);
		}
	}
	
	public void renderMap(GraphicsContext g) {
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

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
}