package com.aticatac.world;

import com.aticatac.utils.Controller;

public class AIPlayer extends Player {
	
	public AIPlayer(Controller controller, int identifier, String colour) {
		super(controller, identifier, colour);
	}
	
	@Override
	public char getAction() {
		//Have some decision making here
		return 0;
	}
	
	
}
