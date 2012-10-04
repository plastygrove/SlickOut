package com.slickout;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Brick extends CollidableAnimationObject {
	public static final String RED_BRICK = "data/brickanimationred.png";
	public static final String BLUE_BRICK = "data/brickanimationblue.png";
	public static final String GREEN_BRICK = "data/brickanimationgreen.png";
	public static final String YELLOW_BRICK = "data/brickanimationyellow.png";
	
	protected int hitNumber;
	protected Color color;
	public Brick(String name, Animation animation, Vector2f position, int hitNumber, Color color, Shape collisionShape, int collisionType) {
		super(name, animation, position, collisionShape, collisionType);
		this.hitNumber = hitNumber;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void incrementHit(){
		hitNumber++;
	}
	
	public void decreaseHit(){
		hitNumber--;
	}
	
	public int getHitsLeft(){
		return hitNumber;
	}

}
