package com.aticatac.rendering.tests;

import java.io.File;

import com.aticatac.rendering.components.DisplayComponent;
import com.aticatac.rendering.display.RenderLayer;
import com.aticatac.rendering.display.Renderer;
import com.aticatac.world.Level;
import com.aticatac.world.World;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class Test extends Application {

	public Test() {
		
	}

	@Override
	public void start(Stage stage) throws Exception {
		DisplayComponent c1 = new DisplayComponent("assets/test/tile1.png"); //a component to display
		
		Renderer renderer = new Renderer(1900, 1000); //create a new renderer with a 900x600 display
	
		RenderLayer layer1 = new RenderLayer("default");
		
		Level map = new Level(50, 50);
		map.loadMap("assets/maps/map.txt");
		
		World world = new World(map);
		
		renderer.setWorld(world);
		
		layer1.add(c1); //add the component to this layer
		
		renderer.addLayer(layer1); //add this layer to the renderer
		
		
		Group root = new Group();      //javafx stuff
		Scene scene = new Scene(root); 
		stage.setScene(scene); 
		Canvas canvas = new Canvas(1900, 1000); //create a canvas to allow drawing images
		
		//add canvas to scene
		root.getChildren().add(canvas);
		
		//to render the components in the renderer, pass it the canvas GraphicsContext object
		renderer.render(canvas.getGraphicsContext2D());
		
		stage.show(); //display the window
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
