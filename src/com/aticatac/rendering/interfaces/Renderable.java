package com.aticatac.rendering.interfaces;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public interface Renderable {
	
	/**
	 * Gets the BufferedImage
	 * @return The BufferedImage instance
	 */
	public BufferedImage getImage();
	/**
	 * Gets the Rectangle that encapsulates the image.
	 * Defined by the top, left coordinate of the collision rect (including the offset), and the width and height of the image.
	 * Whether the image is rendered or not depends on the intersection of this rect with the DisplayPanel's displayRect.
	 * @return A Rectangle that encapsulates the image
	 */
	public Rectangle getImageRect();
	
}