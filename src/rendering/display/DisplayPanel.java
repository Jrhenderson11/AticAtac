package rendering.display;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import javax.swing.JPanel;

import rendering.interfaces.Renderable;

@SuppressWarnings("serial")
public class DisplayPanel extends JPanel implements Runnable {
	
	
	// ------
	// Fields
	// ------
	
	
	/**
	 * A rectangle encapsulating the area of the coordinate space where renderable components in this space will be displayed.
	 */
	private Rectangle displayRect;
	/**
	 * List of the components that will be renderered if they are within the display rect
	 */
	private LinkedList<Renderable> components;
	/**
	 * Delay between each redrawing of the panel
	 */
	private int frameDelay;
	/**
	 * While condition for thread loop
	 */
	private boolean running;
	
	
	// ------------
	// Constructors
	// ------------
	
	
	/**
	 * Constructs a new DisplayPanel with the given rectangle as the display rectangle
	 * @param displayRect The rectangle that describes the display's position and dimensions
	 * @param frameRate The frame rate for refreshing the screen
	 */
	public DisplayPanel(Rectangle displayRect, int frameRate) {
		this.displayRect = displayRect;
		this.components = new LinkedList<Renderable>();
		this.frameDelay = 1000 / frameRate;
		this.running = true;
		this.setPreferredSize(displayRect.getSize());
	}
	
	/**
	 * Constructs a new DisplayPanel with a display rectangle of given width and height at the given coordinate (left, top)
	 * @param left The x coordinate of the left side of the display rectangle
	 * @param top The y coordinate of the top side of the display rectangle
	 * @param width The width of the display rectangle
	 * @param height The height of the display rectangle
	 * @param frameRate The frame rate for refreshing the screen
	 */
	public DisplayPanel(int left, int top, int width, int height, int frameRate) {
		this(new Rectangle(left, top, width, height), frameRate);
	}
	
	/**
	 * Creates a DisplayPanel with the given dimensions
	 * @param screenSize The dimensions of the panel
	 * @param frameRate The frame rate for refreshing the screen
	 */
	public DisplayPanel(Dimension screenSize, int frameRate) {
		this(new Rectangle(screenSize), frameRate);
	}
	
	/**
	 * Creates a DisplayPanel with the given dimensions
	 * @param width The width of the panel
	 * @param height The height of the panel
	 * @param frameRate The frame rate for refreshing the screen
	 */
	public DisplayPanel(int width, int height, int frameRate) {
		this(0, 0, width, height, frameRate);
	}
	
	
	// -------
	// Methods
	// -------
	
	
	/**
	 * Adds a component to the end of the components list
	 * Adding a component means it will be drawn to the screen if in view.
	 * @param component The component to add
	 * @return Returns true if the list is changed as a result of the add.
	 */
	public boolean addComponent(Renderable component) {
		return components.add(component);
	}
	
	/**
	 * Removes the given component from the list of components
	 * @param component The component to remove
	 * @return True if the component is in the list and has been removed.
	 */
	public boolean removeComponent(Renderable component) {
		return components.remove(component);
	}
	
	/**
	 * Redraws the panel.
	 */
	public void draw() {
		this.repaint();
	}
	
	/**
	 * For every component in the components list
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Renderable component: components) {
			Rectangle rect = component.getImageRect();
			if (displayRect.intersects(rect)) {
				g.drawImage(component.getImage(), (rect.x - displayRect.x), (rect.y - displayRect.y), this);
			}
		}
	}
	
	/**
	 * Set the running thread loop flag to the given value
	 * @param running True to keep the thread running, False to stop the loop and kill the thread.
	 */
	private synchronized void setRunning(boolean running) {
		synchronized (this) {
			this.running = running;
		}
	}
	
	/**
	 * Returns whether the thread is running or not.
	 * @return True if the thread is running.
	 */
	public synchronized boolean running() {
		synchronized (this) {
			return this.running;
		}
	}
	
	/**
	 * Starts this instance in a new thread, to update at the given framerate
	 */
	public void start() {
		setRunning(true);
		new Thread(this).start();
	}
	
	/**
	 * Changes the thread loop running flag to false to kill the thread
	 */
	public void stop() {
		setRunning(false);
	}
	
	/**
	 * Thread to redraw the panel at the instances frame rate
	 */
	@Override
	public void run() {
		while(running()) {
			draw();
			System.out.println("tick2");
			try {
				Thread.sleep(frameDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}