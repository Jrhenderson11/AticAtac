package com.aticatac.world;

import java.awt.Point;

import com.aticatac.utils.Controller;

import javafx.scene.paint.Color;

public class Player {
	private final static int MINIMUM_PAINT_FOR_SHOOT = 5;
	private final static int PAINT_DECREASE = 5;
	private final static int BASE_PAINT_INCREASE = 5;
	
    private Controller controller;
    private int identifier;
    private Color colour;
    private int paintLevel;
    private Point position;
    
    public Player(Controller controller, int identifier, Color colour) {
    	this.controller = controller;
    	this.setIdentifier(identifier);
    	this.colour = colour;
    	this.position = new Point(10, 10);
    }
        
    public void makeMovement(char control) {
    	// Send a request to renderer to move
    }
    
    public void shoot() {
    	if(this.paintLevel >= MINIMUM_PAINT_FOR_SHOOT) {
    		// Send a request to renderer to shoot
        	this.decreasePaintLevel();
    	}
    }
    
    public char getAction() {
    	// need to get the event and then do the necessary action
    	return 0;
    }
    
    public void increasePaintLevel(int multiplier) {
    	this.paintLevel = multiplier * BASE_PAINT_INCREASE;
    	// increase paint depending on multiplier from possession in world
    }
    
    public void decreasePaintLevel() {
    	this.paintLevel = this.paintLevel - PAINT_DECREASE;
    	// make a sensible decrease to paint level
    }

	public Color getColour() {
		return colour;
	}

	public void setColour(Color colour) {
		this.colour = colour;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}
	
	public void move(int dX, int dY) {
		this.position.x += dX;
		this.position.y += dY;
	}

	public int getIdentifier() {
		return identifier;
	}

	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}
}
