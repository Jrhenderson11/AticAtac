package rendering.components;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	
	
	// ------
	// Fields
	// ------
	
	
	/**
	 * The full image of the sprite sheet
	 */
	private BufferedImage fullSheet;
	
	/**
	 * The array of individual sprite images
	 */
	private BufferedImage[] sprites;

	/**
	 * The size of individual frames within the sprite sheet
	 */
	private Dimension spriteSize;
	
	
	// -----------
	// Constructor
	// -----------
	
	
	/**
	 * Constructs a sprite sheet from the given image file using the given dimensions for the sprites
	 * @param sheetFilePath
	 * @param spriteSize
	 */
	public SpriteSheet(String sheetFilePath, Dimension spriteSize) {
		this.spriteSize = spriteSize;
		//try and read file
		try {
			this.fullSheet = ImageIO.read(new File(sheetFilePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//check if full sprite sheet dimensions are a multiple of the spriteSize dimension, so that images obtained from spritesheet tile properly
		if (verifySpritesheet(new Dimension(fullSheet.getWidth(), fullSheet.getHeight()), spriteSize)) {
			System.err.println("Warning: Given spriteSize does not match sprite sheet \"" + sheetFilePath + "\". The sprite sheet dimensions should be a multiple of the given spriteSize");
		}
		//Fill the sprite sheet array
		this.sprites = new BufferedImage[(fullSheet.getWidth() * fullSheet.getHeight()) / (spriteSize.width * spriteSize.height)];
		int i = 0;
		for (int y = 0, h = fullSheet.getHeight()/spriteSize.height; y < h; y++) {
			for (int x = 0, w = fullSheet.getWidth()/spriteSize.width; x < w; x++) {
				sprites[i++] = fullSheet.getSubimage(x*spriteSize.width, y*spriteSize.height, spriteSize.width, spriteSize.height);
			}
		}
		
	}
	
	
	// -------
	// Methods
	// -------
	
	
	/**
	 * Returns the image from the sprite sheet at the given frame position.
	 * @param frame The frame index from the
	 * @return
	 */
	public BufferedImage getImage(int frame) {
		return sprites[frame];
	}
	
	/**
	 * Returns the number of frames in the spritesheet
	 * @return The number of frames in the spritesheet
	 */
	public int getNumFrames() {
		return sprites.length;
	}
	
	/**
	 * Get the size of individual sprites within the spritesheet
	 * @return The dimension of the sprites
	 */
	public Dimension getSpriteSize() {
		return spriteSize;
	}
	
	/**
	 * Check whether the full sprite sheet is the correct size from the given sprite size dimensions
	 * @param sheetSize The full sprite sheet image
	 * @param spriteSize The size each sprite in the sheet should be
	 * @return True if the full sprite sheet dimensions are a multiple of the given sprite size
	 */
	private static boolean verifySpritesheet(Dimension sheetSize, Dimension spriteSize) {
		return (sheetSize.getWidth() % spriteSize.getWidth() == 0) && (sheetSize.getHeight() % spriteSize.getHeight() == 0);
	}

}