package com.aticatac.rendering.components;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import com.aticatac.rendering.interfaces.Renderable;

import javafx.scene.image.Image;

public class DisplayComponent implements Renderable {

	
	// ------
	// Fields
	// ------
	
	
	/**
	 * The image of this component
	 */
	private Image image;
	/**
	 * The Rectangle that defines this components collision boundaries
	 */
	private Rectangle collisionRect;
	/**
	 * The horizontal offset of the collision rectangle from the left side of the image.
	 */
	private int rectXOffset;
	/**
	 * The vertical offset of the collision rectangle from the top of the image
	 */
	private int rectYOffset;
	
	
	// ------------
	// Constructors
	// ------------
	
	
	/**
	 * The full constructor for a StaticComponent
	 * @param image The image for this component
	 * @param collisionRect The rectangle that defines the collision boundaries of this component
	 * @param rectXOffset The horizontal offset of the collision rectangle from the left side of the image.
	 * @param rectYOffset The vertical offset of the collision rectangle from the top of the image
	 */
	public DisplayComponent(Image image, Rectangle collisionRect, int rectXOffset, int rectYOffset) {
		this.image = image;
		this.collisionRect = collisionRect;
		this.rectXOffset = rectXOffset;
		this.rectYOffset = rectYOffset;
	}
	public DisplayComponent(Image image, Rectangle collisionRect) {
		this(image, collisionRect, 0, 0);
	}
	
	/**
	 * Constructor that takes collision rect dimensions from the image dimensions
	 * @param image The image of this component
	 * @param rectXOffset The horizontal offset of the collision rectangle from the left side of the image.
	 * @param rectYOffset The vertical offset of the collision rectangle from the top of the image
	 */
	public DisplayComponent(Image image, int rectXOffset, int rectYOffset) {
		this(image, new Rectangle((int) image.getWidth(), (int) image.getHeight()), rectXOffset, rectYOffset);
	}
	public DisplayComponent(Image image) {
		this(image, new Rectangle((int) image.getWidth(), (int) image.getHeight()));
	}
	
	/**
	 * Constructor that takes a file path for the image
	 * @param filePath The path to the image file to use
	 * @param collisionRect The rectangle that defines the collision boundaries of this component
	 * @param rectXOffset The horizontal offset of the collision rectangle from the left side of the image.
	 * @param rectYOffset The vertical offset of the collision rectangle from the top of the image
	 * @throws IOException When the file cannot be found or read.
	 */
	public DisplayComponent(String filePath, Rectangle collisionRect, int rectXOffset, int rectYOffset) throws IOException {
		 this(new Image(new File(filePath).toURI().toString()), collisionRect, rectXOffset, rectYOffset);
	}
	public DisplayComponent(String filePath, Rectangle collisionRect) throws IOException {
		this(filePath, collisionRect, 0, 0);
	}
	
	/**
	 * Constructor that takes collision rect dimensions from the image dimensions
	 * @param filePath The path to the image file to use
	 * @param rectXOffset The horizontal offset of the collision rectangle from the left side of the image.
	 * @param rectYOffset The vertical offset of the collision rectangle from the top of the image
	 * @throws IOException When the file cannot be found or read.
	 */
	public DisplayComponent(String filePath, int rectXOffset, int rectYOffset) throws IOException {
		this.image = new Image(new File(filePath).toURI().toString());
		this.collisionRect = new Rectangle((int) image.getWidth(), (int) image.getHeight());
		this.rectXOffset = rectXOffset;
		this.rectYOffset = rectYOffset;
	}
	public DisplayComponent(String filePath) throws IOException {
		this(filePath, 0, 0);
	}
	
	
	// -------
	// Methods
	// -------
	
	
	public void translate(int x, int y) {
		this.collisionRect.x += x;
		this.collisionRect.y += y;
	}
	/**
	 * Set the position of the collision rectangle
	 * @param p The position to set the collision rectangle to.
	 */
	public void setPosition(Point p) {
		this.collisionRect.setLocation(p);
	}
	
	/**
	 * Set the position of the collision rectangle
	 * @param x The x coordinate to set the position to
	 * @param y The y coordinate to set the position to
	 */
	public void setPosition(int x, int y) {
		this.collisionRect.setLocation(x, y);
	}
	
	/**
	 * Get the top left position of the collision rect
	 * @return The top left position of this component
	 */
	public Point getPosition() {
		return collisionRect.getLocation();
	}
	
	/**
	 * Returns the x position of the collision rect
	 * @return The x position of the component
	 */
	public int getXPosition() {
		return getPosition().x;
	}
	
	/**
	 * Returns the y position of the collision rect
	 * @return The y position of the component
	 */
	public int getYPosition() {
		return getPosition().y;
	}
	
	/**
	 * Returns the image of this component.
	 */
	@Override
	public Image getImage() {
		return image;
	}
	
	/**
	 * Returns the rectangle the encapsulates the image.
	 */
	@Override
	public Rectangle getImageRect() {
		return new Rectangle((collisionRect.x - rectXOffset), (collisionRect.y - rectYOffset), (int) image.getWidth(), (int) image.getHeight()); 
	}
	
	/**
	 * Returns the rectangle that defines the collision boundaries
	 */
	public Rectangle getCollisionRect() {
		return collisionRect;
	}

}