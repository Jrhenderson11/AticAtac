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

<<<<<<< HEAD
	@Override
	public void start(Stage stage) throws Exception {
		DisplayComponent c1 = new DisplayComponent("assets/test/tile1.png");
=======
	public static void main(String[] args) {
		JFrame window = new JFrame();
		DisplayPanel display = new DisplayPanel(900, 600, 24);
		try {
			StaticComponent s1 = new StaticComponent("assets/test/tile1.png");
			s1.setPosition(50, 50);
			
			AnimatedComponent a1 = new AnimatedComponent(new SpriteSheet("assets/test/spritesheet1.png", new Dimension(100, 100)), 
					new Rectangle(200, 50, 100, 100), 0, 0, 3);
			
			window.setContentPane(display);
			window.setPreferredSize(new Dimension(900, 600));
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
>>>>>>> branch 'rendering' of https://git-teaching.cs.bham.ac.uk/mod-team-proj-2017/aticatac
		
<<<<<<< HEAD
=======
			//Creates a new scene to display
			Scene scene = new Scene();
			scene.addComponent(s1); //adds components to the default layer of the scene
			scene.addComponent(a1);
			
			
			//set the display to the scene
			display.setScene(scene);
			
			window.pack();
			window.setVisible(true);
			
			display.start();
			
			while (true) {
				s1.translate(1, 1);
				Thread.sleep(100);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			display.stop();
		}
>>>>>>> branch 'rendering' of https://git-teaching.cs.bham.ac.uk/mod-team-proj-2017/aticatac
		
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
