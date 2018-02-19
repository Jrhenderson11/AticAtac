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
		drawPercents(gc, world);
		drawWeapon(gc, world, id);
	}

	private void drawPercents(GraphicsContext gc, World world) {
		
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
		        gc.setStroke(Color.BLACK);
		        gc.setLineWidth(2);
		        gc.setFont(UIDrawer.OVERLAY_FONT);
		        
		        String text = Integer.toString(percent) + "%";
		        gc.fillText(text, x, y);
		        gc.strokeText(text, x, y);
		        //gc.restore();
		        x+=100;
		}
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
}
