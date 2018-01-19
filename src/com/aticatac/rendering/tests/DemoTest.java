package com.aticatac.rendering.tests;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;

import javax.swing.JFrame;

import com.aticatac.rendering.components.AnimatedComponent;
import com.aticatac.rendering.components.SpriteSheet;
import com.aticatac.rendering.components.StaticComponent;
import com.aticatac.rendering.display.DisplayPanel;
import com.aticatac.rendering.display.Scene;

public class DemoTest {

	public DemoTest() {
		
	}

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
		
	}
}
