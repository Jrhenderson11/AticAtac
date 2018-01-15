package com.aticatac.rendering.tests;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;

import javax.swing.JFrame;

import com.aticatac.rendering.components.AnimatedComponent;
import com.aticatac.rendering.components.SpriteSheet;
import com.aticatac.rendering.components.StaticComponent;
import com.aticatac.rendering.display.DisplayPanel;

public class DemoTest {

	StaticComponent s1;
	
	public DemoTest() {
		
	}


	
	public static void main(String[] args) {
		JFrame window = new JFrame();
		DemoTest demo = new DemoTest();
		DisplayPanel display = new DisplayPanel(900, 600, 30, demo);
	
		try {
			demo.s1 = new StaticComponent("assets/test/tile1.png");
			demo.s1.setPosition(50, 50);
			
			AnimatedComponent a1 = new AnimatedComponent(new SpriteSheet("assets/test/spritesheet1.png", new Dimension(100, 100)), 
					new Rectangle(200, 50, 100, 100), 0, 0, 1);
			
			window.setContentPane(display);
			window.setPreferredSize(new Dimension(900, 600));
		
			display.addComponent(demo.s1);
			display.addComponent(a1);
			window.pack();
			window.setVisible(true);
			display.setFocusable(true);
			display.setFocusTraversalKeysEnabled(false);
			display.addKeyListener(display);
			display.start();
			
			while (true) {
				
				
				
				//s1.translate(1, 1);
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
	public void move(int x, int y) {
		this.s1.translate(x, y);
	}
}
