package com.aticatac.rendering.components;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.aticatac.rendering.interfaces.Renderable;

public class AnimatedComponent implements Renderable {
	
	
	// ------
	// Fields
	// ------
	
	
	/**
	 * The SpriteSheet object representing the frames in this animation
	 */
	private SpriteSheet spriteSheet;
	/**
	 * The rectangle that defines the collision boundaries of this component
	 */
	private Rectangle collisionRect;
	/**
	 * The horizontal offset of the collision rectangle from the left side of the image
	 */
	private int rectXOffset;
	/**
	 * The vertical offset of the collision rectangle from the top side of the image
	 */
	private int rectYOffset;
	/**
	 * The number of ticks the frame appears for, the duration of the ticks is handled by the DisplayPanel
	 */
	private int frameDuration;
	/**
	 * The current tick value of the frame duration
	 */
	private int frameTicker;
	/**
	 * The index of the current frame of the spritesheet
	 */
	private int currentFrame;
	
	
	// ------------
	// Constructors
	// ------------
	
	
	/**
	 * The main constructor for AnimatedComponent
	 * @param spriteSheet The SpriteSheet object that this AnimatedComponent uses.
	 * @param collisionRect The collision boundary rectangle for this component
	 * @param rectXOffset The horizontal offset of the collision rectangle from the left side of the image
	 * @param rectYOffset The vertical offset of the collision rectangle from the top side of the image
	 * @param frameDuration The duration of ticks each frame lasts for before changing to next frame.
	 */
	public AnimatedComponent(SpriteSheet spriteSheet, Rectangle collisionRect, int rectXOffset, int rectYOffset, int frameDuration) {
		this.spriteSheet = spriteSheet;
		this.collisionRect = collisionRect;
		this.rectXOffset = rectXOffset;
		this.rectYOffset = rectYOffset;
		this.frameDuration = frameDuration;
		this.frameTicker = 0;
		this.currentFrame = 0;
	}
	
	/**
	 * Returns a BufferedImage from the SpriteSheet
	 * Each call of getImage will increase the frameTicker by one, until the frame duration is reached.
	 * Then the next frame of the SpriteSheet will be returned.
	 */
	@Override
	public BufferedImage getImage() {
		if (frameTicker >= frameDuration) {
			frameTicker = 0;
			if (currentFrame >= (spriteSheet.getNumFrames() - 1)) {
				currentFrame = 0;
			} else {
				currentFrame++;
			}
		} else {
			frameTicker++;
		}
		return spriteSheet.getImage(currentFrame);
	}
	
	
	// -------
	// Methods
	// -------
	
	
	/**
	 * Returns the rectangle that encapsulates the image.
	 * This is calculated from the top, left coordinate
	 */
	@Override
	public Rectangle getImageRect() {
		return new Rectangle((collisionRect.x - rectXOffset), (collisionRect.y - rectYOffset), 
				spriteSheet.getSpriteSize().width, spriteSheet.getSpriteSize().height);
	}
	
	/**
	 * Return the Rectangle that defines the collision boundary for the component
	 */
	public Rectangle getCollisionRect() {
		return collisionRect;
	}

}
