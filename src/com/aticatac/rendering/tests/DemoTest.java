package com.aticatac.rendering.tests;

import com.aticatac.rendering.components.DisplayComponent;
import com.aticatac.rendering.display.RenderLayer;
import com.aticatac.rendering.display.Renderer;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class DemoTest extends Application {

	public DemoTest() {
		
	}

	@Override
	public void start(Stage stage) throws Exception {
		DisplayComponent c1 = new DisplayComponent("assets/test/tile1.png");
		
		
		Renderer renderer = new Renderer(900, 600);
	
		RenderLayer layer1 = new RenderLayer("default");
		
		layer1.add(c1);
		
		renderer.addLayer(layer1);
		
		Group root = new Group();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		Canvas canvas = new Canvas(900, 600);
		
		root.getChildren().add(canvas);
		
		renderer.render(canvas.getGraphicsContext2D());
		
		stage.show();
		
		renderer.render(canvas.getGraphicsContext2D());
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
