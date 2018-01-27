package com.aticatac.world;

public class World {
    private Player one;
    private Player two;
    private Level level;
    
    public World(Level level) {
    	this.level = level;
    }
    
    public Level getLevel() {
    	return level;
    }
}
