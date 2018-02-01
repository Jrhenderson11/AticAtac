package com.aticatac.ui.tutorial;

import com.aticatac.rendering.display.Renderer;
import com.aticatac.utils.SystemSettings;
import com.aticatac.world.Level;
import com.aticatac.world.World;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Tutorial extends Scene {
	
	public Tutorial (Group root) {
        super(root);
        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Renderer renderer = new Renderer(width, height);
        
        Level level = new Level(50, 50);
        level.loadMap("assets/maps/map.txt");
        
        World world = new World(level);
        
        renderer.setWorld(world);
        
        new AnimationTimer() {
        	public void handle(long currentNanoTime) {
        		renderer.render(gc);
        	}
        }.start();
    }
	
	
}
