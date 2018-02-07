package com.aticatac.rendering.interfaces;

import java.awt.Rectangle;

import javafx.scene.image.Image;

public interface Renderable {
	
	/**
	 * Gets the Image
	 * @return The Image instance
	 */
	public Image getImage();
	/**
	 * Gets the Rectangle that encapsulates the image.
	 * Defined by the top, left coordinate of the collision rect (including the offset), and the width and height of the image.
	 * Whether the image is rendered or not depends on the intersection of this rect with the DisplayPanel's displayRect.
	 * @return A Rectangle that encapsulates the image
	 */
	public Rectangle getImageRect();
	
}