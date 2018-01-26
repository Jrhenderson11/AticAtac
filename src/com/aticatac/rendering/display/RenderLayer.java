package com.aticatac.rendering.display;

import java.awt.Rectangle;
import java.util.LinkedList;

import com.aticatac.rendering.interfaces.Renderable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

@SuppressWarnings("serial")
public class RenderLayer extends LinkedList<Renderable> {
	
	
	// ------
	// Fields
	// ------
	
	
	/**
	 * Name of this layer
	 */
	private String name;
	
	/**
	 * Whether the layer is being rendered or not.
	 */
	private boolean visible;
	
	
	// ------------
	// Constructors
	// ------------
	
	
	/**
	 * The default constructor.
	 * @param name The name of this layer
	 * @param componentsThe Renderable components in this layer
	 */
	public RenderLayer(String name, LinkedList<Renderable> components) {
		super(components);
		this.name = name;
		this.visible = true;
	}
	/**
	 * Constructor for an empty layer
	 */
	
	public RenderLayer(String name) {
		this(name, new LinkedList<Renderable>());
	}
	
	
	// -------
	// Methods
	// -------
	
	
	/**
	 * Render components based on their relative position of their imageRect to the displayRect
	 * @param g The GraphicsContext to draw to
	 * @param displayRect The rect that describes the location of the display within the 2d space
	 */
	public void render(GraphicsContext g, Rectangle displayRect) {
		if (isVisible()) {
			for (Renderable component: this) {
				Rectangle rect = component.getImageRect();
				if (displayRect.intersects(rect)) {
					g.drawImage((Image) component.getImage(), (rect.x - displayRect.x), (rect.y - displayRect.y));
				}
			}
		}
	}
	
	/**
	 * Render components based on their imageRect, where the top left of the screen is 0,0. 
	 * @param g The GraphicsContext to draw to
	 */
	public void render(GraphicsContext g) {
		if (isVisible()) {
			for (Renderable component: this) {
				Rectangle rect = component.getImageRect();
				g.drawImage((Image) component.getImage(), rect.x, rect.y);
			}
		}
	}
	
	/**
	 * Get the name of this layer
	 * @return The name of this layer
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Whether the layer will be drawn when render() is called
	 * @return Returns true if this layer will be drawn. 
	 */
	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * Sets the layer to be drawn when render() is called.
	 */
	public void show() {
		this.visible = true;
	}
	
	/**
	 * Sets the layer to not be drawn when render() is called.
	 */
	public void hide() {
		this.visible = false;
	}
	
}