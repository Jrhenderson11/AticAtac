package com.aticatac.ui.overlay;

import com.aticatac.rendering.display.Renderer;
import com.aticatac.ui.utils.UIDrawer;
import com.aticatac.utils.SystemSettings;
import com.aticatac.world.Player;
import com.aticatac.world.World;
import com.aticatac.world.items.Gun;
import com.aticatac.world.items.ShootGun;
import com.aticatac.world.items.SplatGun;
import com.aticatac.world.items.SprayGun;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class Overlay {
	
	/**
	 * The percentage of the native height the overlay uses (along the bottom of the screen)
	 */
	private static int overlayHeight = 80;
	
	private double getHeight() {
		return SystemSettings.getScaledY(overlayHeight);
	}
	
	/**
	 * Draws the overlay for the game
	 * @param gc The GraphicsContext to draw to
	 * @param world The world to draw information about
	 * @param id The id of the player to render for.
	 */
	public void drawOverlay(GraphicsContext gc, World world, String id) {
		drawFraming(gc);
		drawTerritoryPercents(gc, world, id);
		drawWeapon(gc, world, id);
		drawPaintLevel(gc, world, id);
		drawRoundInfo(gc, world, id);
	}
	
	private void drawFraming(GraphicsContext gc) {
		double glowRadius = SystemSettings.getScaledX(3);
		double glowArc = SystemSettings.getScaledX(6);
		double boxWidth = SystemSettings.getScaledX(2);
		double opacity = 0.5;
		//background
		gc.setFill(Color.BLACK);
		gc.fillRect(0, SystemSettings.getScreenHeight() - getHeight(), SystemSettings.getScreenWidth(), getHeight());
		//border bars
		Color color = Color.WHITE;
		Color opaqueColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
		//top
		gc.setFill(color);
		gc.fillRect(0, (SystemSettings.getScreenHeight() - getHeight()) - (boxWidth/2), SystemSettings.getScreenWidth(), boxWidth); //box
		gc.setFill(opaqueColor);
		gc.fillRect(0, (SystemSettings.getScreenHeight() - getHeight()) - (glowRadius/2), SystemSettings.getScreenWidth(), glowRadius); //glow
		//left
		gc.setFill(color);
		gc.fillRect((SystemSettings.getScreenWidth()/3) - (boxWidth/2), (SystemSettings.getScreenHeight() - getHeight()), boxWidth, getHeight()); //box
		gc.setFill(opaqueColor);
		gc.fillRoundRect((SystemSettings.getScreenWidth()/3) - (glowRadius/2), (SystemSettings.getScreenHeight() - getHeight()), glowRadius, getHeight(), glowArc, glowArc); //glow
		//right
		//left
		gc.setFill(color);
		gc.fillRect((2*SystemSettings.getScreenWidth()/3) - (boxWidth/2), (SystemSettings.getScreenHeight() - getHeight()), boxWidth, getHeight()); //box
		gc.setFill(opaqueColor);
		gc.fillRoundRect((2*SystemSettings.getScreenWidth()/3) - (glowRadius/2), (SystemSettings.getScreenHeight() - getHeight()), glowRadius, getHeight(), glowArc, glowArc); //glow

	}
		
	/**
	 * Draws the opponents map percentage values.
	 * @param gc The GraphicsContext to draw to
	 * @param world The world to draw information about
	 * @param id The id of the player to render for.
	 */
	private void drawTerritoryPercents(GraphicsContext gc, World world, String id) {
		
		double sidePadding = 20;
		int i = 1;
		
		for (Player player: world.getPlayers()) {
			if (player.getIdentifier() != id) {
				gc.setTextAlign(TextAlignment.LEFT);
				gc.setFont(UIDrawer.OVERLAY_FONT_SMALL);
				gc.setFill(Renderer.getColourByVal(player.getColour()));
				gc.fillText(player.getIdentifier() + " control: " + world.getLevel().getPercentTiles(player.getColour()) + "%",  //e.g. player1: 45%
						sidePadding, 
						(SystemSettings.getScreenHeight() - getHeight()) + (i++ * (getHeight() / 5)));
			} else {
				double topPadding = 20;
				double padding = 96;
				gc.setTextAlign(TextAlignment.CENTER);
				gc.setFont(UIDrawer.OVERLAY_FONT_SMALL);
				gc.setFill(Renderer.getColourByVal(player.getColour()));
				gc.fillText(player.getIdentifier() + " control: " + world.getLevel().getPercentTiles(player.getColour()) + "%",  //e.g. player1: 45%
						(2*SystemSettings.getScreenWidth()/3) + padding, 
						(SystemSettings.getScreenHeight() - getHeight()) + topPadding);
				
			}
		}
	}

	private void drawPaintLevel(GraphicsContext gc, World world, String id) {
		double textWidth = 60;
		double sidePadding = 20;
		double barHeight = getHeight() / 6;
		double barLength = (world.getPlayerById(id).getPaintLevel() / Player.MAX_PAINTLEVEL) * ((SystemSettings.getScreenWidth()/3) - (2 * sidePadding) - textWidth);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFont(UIDrawer.OVERLAY_FONT_SMALL);
		gc.setFill(Renderer.getColourByVal(world.getPlayerById(id).getColour()));
		gc.setStroke(Color.WHITE);
		gc.fillText("Paint: ", (2*SystemSettings.getScreenWidth()/3) + (2*sidePadding), (SystemSettings.getScreenHeight() - (getHeight()/1.9)));
		gc.fillRect((2*(SystemSettings.getScreenWidth()/3)) + sidePadding + textWidth, (SystemSettings.getScreenHeight() - (getHeight()/1.9) - (barHeight)), barLength, barHeight);
		gc.strokeRect((2*(SystemSettings.getScreenWidth()/3)) + sidePadding + textWidth, (SystemSettings.getScreenHeight() - (getHeight()/1.9) - (barHeight)), barLength, barHeight);
//		String text = "paint";
//		
//		int width = SystemSettings.getScreenWidth();
//	    int height = SystemSettings.getScreenHeight();
//		
//		//draw bar on right side
//		
//		int paintPercent = (int) ((world.getPlayerById(id).getPaintLevel()*100) / Player.MAX_PAINTLEVEL);
//		
//		int rwidth = 50;
//		int rheight = 300;
//		
//		int acHeight = (paintPercent * rheight) / 100;
//		
//		int x = width - 100;
//		int y = height - 150;
//		
//		gc.setFill(Renderer.getColourByVal(world.getPlayerById(id).getColour()));
//		gc.setStroke(Color.WHITE);
//        gc.setLineWidth(2);
//
//		gc.fillRect(x, y-acHeight-25, rwidth, acHeight);
//		gc.strokeRect(x, y-acHeight-25, rwidth, acHeight);
//		
//		gc.setTextAlign(TextAlignment.CENTER);
//        gc.setFill(Color.WHITE);
//
//        gc.setFont(UIDrawer.OVERLAY_FONT_SMALL);
//        
//        //draw word paint as well
//        gc.fillText(text, x, y);
	}
	
	private void drawWeapon(GraphicsContext gc, World world, String id) {
		double sidePadding = 10;
		double bottomPadding = SystemSettings.getScaledY(28);
		String weapName = "None";
		Gun w = world.getPlayerById(id).getGun();
		if (w instanceof ShootGun) {
			weapName = "Shoot Gun";
		} else if (w instanceof SplatGun) {
			weapName = "Splat Gun";
		} else if (w instanceof SprayGun) {
			weapName = "Spray Gun";
		}
		gc.setTextAlign(TextAlignment.LEFT);
		gc.setFont(UIDrawer.OVERLAY_FONT_SMALL);
		gc.setFill(Renderer.getColourByVal(world.getPlayerById(id).getColour()));
        gc.fillText("Weapon: " + weapName, (2*(SystemSettings.getScreenWidth()/3)) + sidePadding, (SystemSettings.getScreenHeight() - (bottomPadding)));
        //gc.strokeText(weapName, x, y);
       
	} 

	private void drawRoundInfo(GraphicsContext gc, World world, String id) {
		double padding = 20;
		//draw timer
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFill(Color.WHITE);
      	gc.setFont(UIDrawer.TITLE_FONT);
      	gc.fillText(""+world.getRoundTime(), (SystemSettings.getScreenWidth()/2) + (SystemSettings.getScreenWidth()/9),(SystemSettings.getScreenHeight() - (getHeight() / 2)));
      	//draw round count
      	gc.setTextAlign(TextAlignment.LEFT);
      	gc.setFill(Color.WHITE);
      	gc.setFont(UIDrawer.OVERLAY_FONT_SMALL);
      	gc.fillText("Round: "+world.getRound(), (SystemSettings.getScreenWidth()/3) + padding, (SystemSettings.getScreenHeight() - getHeight()) + padding);
      	//draw win count
      	gc.fillText("Wins: "+world.getPlayerById(id).getPoints(), (SystemSettings.getScreenWidth()/3) + padding, (SystemSettings.getScreenHeight() - (2 * padding)));
	}
}