package com.aticatac.world;

import java.awt.Point;
import java.io.Serializable;

import com.aticatac.utils.Controller;
import com.aticatac.world.items.Gun;

public class Player implements Serializable{
	

	public static final double MAX_PAINTLEVEL = 100.0d;
	public static final double PAINT_REGEN_RATE = 0.8d;
    public Controller controller;
    protected String identifier;
    protected int colour;
    protected double paintLevel;
    protected Point position;
    protected Double lookDirection; //stored as radians, 0 is looking to the right. increases clockwise.
    protected Gun gun;

    public Player(Controller controller, String identifier, int colour) {
    	this.controller = controller;
    	this.setIdentifier(identifier);
    	this.colour = colour;
    	this.position = new Point(10, 10);
    	this.lookDirection = 0.0;
    	this.paintLevel = Player.MAX_PAINTLEVEL;
    }
    
    public Controller getController() {
    	return controller;
    }
    
    public void update() {
    	//regenerate paint and other things
    	if (gun != null) {
    		gun.update(); //updates gun, used for gun cooldowns
    	}
    }
    
    public void regenPaint(int tilePercentage) {
    	changePaintLevel(tilePercentage * PAINT_REGEN_RATE); //regen paint
    }

	public int getColour() {
		return colour;
	}

	public void setColour(int colour) {
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

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public double getPaintLevel() {
		return paintLevel;
	}

	public void changePaintLevel(double levelChange) {
		setPaintLevel(paintLevel + levelChange);
	}
	
	public boolean setPaintLevel(double paintLevel) {
		if (paintLevel > 0 && paintLevel < MAX_PAINTLEVEL) {
			this.paintLevel = paintLevel;
			return true;
		} else return false;
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
