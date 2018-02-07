package com.aticatac.world;

import java.awt.Point;

import com.aticatac.utils.Controller;

public class Player {
	private final static int MINIMUM_PAINT_FOR_SHOOT = 5;
	private final static int PAINT_DECREASE = 5;
	private final static int BASE_PAINT_INCREASE = 5;
	
	protected Gun splat;
	protected Gun spit;
	protected Gun spray;
	
	
	//Need to change gun class to contain these levels within them
	protected int splatLevel;
	protected int spitLevel;
	protected int sprayLevel;
	
    private Controller controller;
    private int identifier;
    protected int colour;
    protected int x;
    protected int y;
	protected Level level;
    
    public Player(Controller controller, Level level, int identifier, int colour) {
    	this.controller = controller;
    	this.level = level;
    	this.identifier = identifier;
    	this.colour = colour;
    	
    	this.spit = new SpitGun();
    	this.splat = new SplatGun();
    	this.spray = new SprayGun();
    	
    	this.spitLevel = 100;
    	this.sprayLevel = 100;
    	this.splatLevel = 100;
    }
        
    public void makeMovement(char control) {
    	// Send a request to renderer to move
    }
    
    public void shoot(Gun g) {
    	/* if(g.getPaintLevel() >= MINIMUM_PAINT_FOR_SHOOT) {
    		// Send a request to renderer to shoot
        	this.decreasePaintLevel();
    	} */
    }
    
    public char getAction() {
    	// need to get the event and then do the necessary action
    	return 0;
    }
    
    public void increasePaintLevel(int multiplier, Gun g) {
    	//g.increasePaintLevel();
    	//this.paintLevel = multiplier * BASE_PAINT_INCREASE;
    	// increase paint depending on multiplier from possession in world
    }
    
    public void decreasePaintLevel(Gun g) {
    	//g.decreasePaintLevel();
    	//this.paintLevel = this.paintLevel - PAINT_DECREASE;
    	// make a sensible decrease to paint level
    }
    
    public Point getCurrentPoint() {
    	return new Point(x, y);
    }
}