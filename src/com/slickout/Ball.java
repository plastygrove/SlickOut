package com.slickout;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class Ball extends CollidableImageObject {
	protected float ballSpeed;
	protected Vector2f direction;

	public Ball(String name, Image image, Vector2f position, float ballSpeed, Vector2f initialDirection, Shape collisionShape, int collisionType) {
		super(name, image, position, collisionShape, collisionType);
		this.ballSpeed = ballSpeed;
		this.direction = initialDirection.copy();
	}

	public void setDirection(Vector2f direction) {
		this.direction = direction.copy();
	}

	public Vector2f getDirection() {
		return direction;
	}

	public void setSpeed(float speed) {
		ballSpeed = speed;
	}

	public float getSpeed() {
		return ballSpeed;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		position.x += direction.x * delta * ballSpeed;
		position.y -= direction.y * delta * ballSpeed;
	}

}
