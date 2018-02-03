package com.aticatac.world;

import com.aticatac.utils.Controller;

import javafx.scene.paint.Color;

public class AIPlayer extends Player {
	
	public AIPlayer(Controller controller, int identifier, Color colour) {
		super(controller, identifier, colour);
	}
	
	@Override
	public char getAction() {
		//Have some decision making here
		return 0;
	}
	
	
}
