package com.aticatac.world;

import java.awt.Point;
import java.util.ArrayList;

import com.aticatac.utils.Controller;
import com.aticatac.world.ai.AStar;

public class AIPlayer extends Player {

	public AIPlayer(Controller controller, Level level, int identifier, String colour) {
		super(controller, level, identifier, colour);
	}

	@Override
	public char getAction() {
		 	if(this.inRange()){
				this.stop();
				this.shoot();
		 	}else{
				if(!this.moving()){
					Point Point = this.closestFreePoint();
		 			this.makeMovement('p', this.pathToFreePoint(Point));
				}
			}
		// Have some decision making here
		return 0;
	}

	private boolean moving() {
		// TODO Auto-generated method stub
		return false;
	}

	private void stop() {
		// TODO Auto-generated method stub
		
	}

	private boolean inRange() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void makeMovement(char control) {
		// Need to be able to follow a path, might need a control enum just to contain
		// the different controls
	}
	
	private void makeMovement(char c, ArrayList<Point> pathToFreePoint) {
		// TODO Auto-generated method stub
		
	}

	public Point closestFreePoint() {
		boolean foundClosest = false;
		Point t = null;
		int i = 0;
		//if this.level.getCoords(x,y) == 1 then it is a wall, should this be taken into consideration
		// is there a way to do this more efficently??
		while (!foundClosest) {
			for (int j = -i; j < i; j++) {
				for (int k = -i; k < i; k++) {
					try {
					if(this.level.getCoords(x + j,y + k) == 0) {
						foundClosest = true;
						t = new Point(x+j, y+k);
						break;
					}}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
			i++;
		}
		return t;
	}

	// get the path that the ai player will take to reach the free Point
	public ArrayList<Point> pathToFreePoint(Point endPoint) {
		return (new AStar(this.getCurrentPoint(), endPoint, this.level)).getPath();
	}

}
