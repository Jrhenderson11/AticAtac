package com.aticatac.world;

import java.awt.Point;

import com.aticatac.utils.Controller;
import com.aticatac.world.items.Gun;

import javafx.scene.paint.Color;

public class Player {
    
    private Controller controller;
    private int identifier;
    private Color colour;
    private int paintLevel;
    private Point position;
    private Double lookDirection; //stored as radians, 0 is looking to the right. increases clockwise.
    private Gun gun;
    
    public Player(Controller controller, int identifier, Color colour) {
    	this.controller = controller;
    	this.setIdentifier(identifier);
    	this.colour = colour;
    	this.position = new Point(10, 10);
    	this.lookDirection = 0.0;
    }
    
    public void update() {
    	//regenerate paint and other things
    	if (gun != null) {
    		gun.update(); //updates gun, used for gun cooldowns
    	}
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

	public Gun getGun() {
		return gun;
	}

	public void setGun(Gun gun) {
		this.gun = gun;
	}
}
