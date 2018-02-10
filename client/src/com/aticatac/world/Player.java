package com.aticatac.world;

import java.awt.Point;

import com.aticatac.utils.Controller;

import javafx.scene.paint.Color;

public class Player {
	
	protected Gun gun;
	
	protected Controller controller;
    protected int identifier;
    protected Color colour;
    protected Point position;
    protected Double lookDirection; //stored as radians, 0 is looking to the right. increases clockwise.
    
    public Player(Controller controller, int identifier, Color colour) {
    	this.controller = controller;
    	this.setIdentifier(identifier);
    	this.colour = colour;
    	this.position = new Point(10, 10);
    	this.lookDirection = 0.0;
    }
        
    public void makeMovement(char control) {
    	// Send a request to renderer to move
    }
    
    
    public char getAction() {
    	// need to get the event and then do the necessary action
    	return 0;
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

	public Double getLookDirection() {
		return lookDirection;
	}

	public void setLookDirection(Double lookDirection) {
		this.lookDirection = lookDirection;
	}
}
