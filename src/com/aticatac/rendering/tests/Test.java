package com.aticatac.rendering.tests;


import com.aticatac.rendering.components.DisplayComponent;
import com.aticatac.rendering.display.GameWindow;
import com.aticatac.rendering.display.RenderLayer;
import com.aticatac.world.Level;
import com.aticatac.world.World;

import javafx.application.Application;
import javafx.stage.Stage;

public class Test extends Application {

	public Test() {
		
	}

	@Override
	public void start(Stage stage) throws Exception {
		DisplayComponent c1 = new DisplayComponent("assets/test/tile1.png"); //a component to display
		RenderLayer layer1 = new RenderLayer("default");
		
		Level map = new Level(50, 50);
		map.loadMap("assets/maps/map.txt");
		World world = new World(map);
		
		GameWindow window = new GameWindow(1920, 1080);
		window.getRenderer().setWorld(world);
		
		layer1.add(c1); //add the component to this layer
		window.getRenderer().addLayer(layer1); //add this layer to the renderer
		
		
		window.update();
		window.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
