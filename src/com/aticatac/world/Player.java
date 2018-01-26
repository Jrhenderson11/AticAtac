package com.aticatac.world;

import com.aticatac.utils.Controller;

public class Player {
	private final static int MINIMUM_PAINT_FOR_SHOOT = 5;
	private final static int PAINT_DECREASE = 5;
	private final static int BASE_PAINT_INCREASE = 5;
	
    private Controller controller;
    private String identifier;
    private String colour;
    private int paintLevel;
    private int x;
    private int y;
    
    public Player(Controller controller, String identifier, String colour) {
    	this.controller = controller;
    	this.identifier = identifier;
    	this.colour = colour;
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
}
