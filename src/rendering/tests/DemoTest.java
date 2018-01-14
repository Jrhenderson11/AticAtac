package rendering.tests;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;

import javax.swing.JFrame;

import rendering.components.AnimatedComponent;
import rendering.components.SpriteSheet;
import rendering.components.StaticComponent;
import rendering.display.DisplayPanel;

public class DemoTest {

	public DemoTest() {
		
	}

	public static void main(String[] args) {
		JFrame window = new JFrame();
		DisplayPanel display = new DisplayPanel(900, 600, 30);
		try {
			StaticComponent s1 = new StaticComponent("assets/test/tile1.png");
			s1.setPosition(50, 50);
			
			AnimatedComponent a1 = new AnimatedComponent(new SpriteSheet("assets/test/spritesheet1.png", new Dimension(100, 100)), 
					new Rectangle(200, 50, 100, 100), 0, 0, 1);
			
			window.setContentPane(display);
			window.setPreferredSize(new Dimension(900, 600));
		
			display.addComponent(s1);
			display.addComponent(a1);
			window.pack();
			window.setVisible(true);
			
			display.start();
			
			while (true) {
				s1.translate(1, 1);
				System.out.println("tick1");
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
