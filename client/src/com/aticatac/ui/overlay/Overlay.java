package com.aticatac.ui.overlay;

import java.util.LinkedList;

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
	
	public void drawOverlay(GraphicsContext gc, World world, String id) {
		drawPercents(gc, world, id);
		drawWeapon(gc, world, id);
		drawPaintLevel(gc, world, id);
	}

	private void drawPercents(GraphicsContext gc, World world, String id) {
		
		int width = SystemSettings.getNativeWidth();
	    int height = SystemSettings.getNativeHeight();
		
	    //TODO: make adaptable to screen size
		int x = 200;
		int y = 50;
				
		LinkedList<Player> players = (LinkedList<Player>) world.getPlayers();
		
		for (int i=0; i< world.getNumPlayers(); i++) {
			Player player = players.get(i);
			int percent = world.getLevel().getPercentTiles(player.getColour());
			
	        gc.setTextAlign(TextAlignment.CENTER);
	        gc.setFill(Renderer.getColourByVal(player.getColour()));
	        gc.setStroke(Color.WHITE);
	        gc.setLineWidth(1);
	        gc.setFont(UIDrawer.OVERLAY_FONT);
	        
	        String text = Integer.toString(percent) + "%";
	        gc.fillText(text, x, y);
	        if (player.getIdentifier().equals(id)) {
	        	gc.strokeText(text, x, y);
	        }
	        x+=100;
		}
	}

	private void drawPaintLevel(GraphicsContext gc, World world, String id) {
		String text = "paint";
		
		int width = SystemSettings.getNativeWidth();
	    int height = SystemSettings.getNativeHeight();
		
		//draw bar on right side
		
		int paintPercent = (int) ((world.getPlayerById(id).getPaintLevel()*100) / Player.MAX_PAINTLEVEL);
		
		int rwidth = 50;
		int rheight = 300;
		
		int acHeight = (paintPercent * rheight) / 100;
		
		int x = width - 100;
		int y = height - 150;
		
		gc.setFill(Renderer.getColourByVal(world.getPlayerById(id).getColour()));
		gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);

		gc.fillRect(x, y-acHeight-25, rwidth, acHeight);
		gc.strokeRect(x, y-acHeight-25, rwidth, acHeight);
		
		gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.WHITE);

        gc.setFont(UIDrawer.OVERLAY_FONT_SMALL);
        
        //draw word paint as well
        gc.fillText(text, x, y);
	}
	
	private void drawWeapon(GraphicsContext gc, World world, String id) {

		String weapName = "No current weapon";
		Gun w = world.getPlayerById(id).getGun();
		if (w instanceof ShootGun) {
			weapName = "Shoot Gun";
		} else if (w instanceof SplatGun) {
			weapName = "Splat Gun";
		} else if (w instanceof SprayGun) {
			weapName = "Spray Gun";
		}
		int width = SystemSettings.getNativeWidth();
	    int height = SystemSettings.getNativeHeight();
		int x = width - 150;
		int y = height - 100;
		//draw in lower corner
		
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.setFont(UIDrawer.OVERLAY_FONT_SMALL);
        
        gc.fillText(weapName, x, y);
        //gc.strokeText(weapName, x, y);
       
	} 

	private void drawTimer(GraphicsContext gc, World world) {
		
	}
}