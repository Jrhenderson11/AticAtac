package com.aticatac.world;

import java.awt.Point;

import com.aticatac.keypress.Gun;
import com.aticatac.utils.Controller;

public class Player {
	
	protected Gun splat;
	protected Gun spit;
	protected Gun spray;
	
    private Controller controller;
    private int identifier;
    protected int colour;
    protected int x;
    protected int y;
    
    public Player(Controller controller, int identifier, int colour) {
    	this.controller = controller;
    	this.identifier = identifier;
    	this.colour = colour;
    	
    	this.spit = new Gun(null);
    	//this.spit = new SpitGun();
    	this.splat = new Gun(null);
    	//this.splat = new SplatGun();
    	this.spray = new Gun(null);
    	//this.spray = new SprayGun();
    }
        
    public void doAction(char control) {
    	assert(control == 'm');
    	// this method should only be called to move
    	// Send a request to renderer to move
    }
    
    public void doAction(char control, Gun g) {
    	assert(control == 's');
    	if(g instanceof /*Spit*/Gun) {
    		
    		
    		this.spit.decreasePaintLevel();
    	}else if (g instanceof /*Splat*/Gun) {
    		
    	}else if (g instanceof /*Spray*/Gun) {
    		
    	}
    }
    
    public char getAction() {
    	// need to get the event and then do the necessary action
    	return 0;
    }
    
    public Point getCurrentPoint() {
    	return new Point(x, y);
    }
}