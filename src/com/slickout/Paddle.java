package com.slickout;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Paddle extends CollidableAnimationObject implements MouseListener {
	public static enum PAD_STATE {
		NORMAL, STICKY
	};

	private PAD_STATE currentState;

	public Paddle(String name, Animation animation, Vector2f position, Shape collisionShape, int collisionType) {
		super(name, animation, position, collisionShape, collisionType);
		currentState = PAD_STATE.STICKY;
	}

	@Override
	public void setInput(Input input) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	@Override
	public void inputEnded() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputStarted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(int change) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if(currentState == PAD_STATE.STICKY){
			currentState = PAD_STATE.NORMAL;
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if (newx > 10 && newx < 690)
			position.x = newx;
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		// TODO Auto-generated method stub

	}
	
	public PAD_STATE getState(){
		return currentState;
	}
	
	public void setState(PAD_STATE newState){
		currentState = newState;
	}

}
