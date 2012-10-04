package com.slickout;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class BumperAndPadBallCollisionHandler implements ICollisionHandler {
	private ILevel levelData;

	public BumperAndPadBallCollisionHandler(ILevel level) {
		levelData = level;
	}

	@Override
	public int getCollider1Type() {
		return 1;
	}

	@Override
	public int getCollider2Type() {
		return 2;
	}

	@Override
	public void performCollision(ICollidableObject collidable1, ICollidableObject collidable2) throws SlickException {
		if (!collidable1.isCollidingWith(collidable2)) {
			return;
		}

		Ball ball = null;
		ICollidableObject object = null;

		if (collidable1 instanceof Ball) {
			ball = (Ball) collidable1;
			object = collidable2;
		} else {
			ball = (Ball) collidable2;
			object = collidable1;
		}

		Vector2f direction = ball.getDirection().copy();
		direction.set(direction.x * -1, direction.y * -1);
		do {
			Vector2f pos = ball.getPosition();
			ball.setPosition(new Vector2f(pos.x + direction.x, pos.y - direction.y));
		} while (ball.isCollidingWith(object));
		Shape ballShape = ball.getCollisionShape();
		Shape objectShape = object.getCollisionShape();

		direction = ball.getDirection().copy();

		if (ballShape.getMinY() > objectShape.getMaxY() || ballShape.getMaxY() < objectShape.getMinY()) {
			direction.set(direction.x, -direction.y);
		} else {
			direction.set(-direction.x, direction.y);
		}

		if (object instanceof Paddle) {
			//Alter angle a bit depending on where it strikes the paddle. Middle - no change
			//This allows player to control the ball a bit by moving the paddle appropriately
			Paddle paddle = (Paddle) object;
			float halfPaddleLength = 50;
			Vector2f paddlePos = paddle.getPosition();
			Vector2f ballPos = ball.getPosition();
			double maxAngleChange = 30.0f / 180 * Math.PI; // add or remove 30 degrees if it hits the edges
			
			//position of paddle is given by leftmost edge. So we get the position where it hits from that point
			
			/*
			 * tan(angle) = y/x always where y = direction.y and x = direction.x
			 * So, newX = magnitude*cos(newAngle) and newY = magnitude*sin(newAngle)
			 * where newX = newDirection.x and newY = newDirection.y
			 * 
			 * I just did everything based on tan(angle) = y/x giving me positive or negative respectively and adding it
			 * and it works
			 * 
			 * TODO: Make sure the angle doesn't become some impossible value. This is possible if the ball is already coming at 
			 * an angle of say 75 degrees and we add another 25 to it!
			 */
			
			float anglePercentChange = (halfPaddleLength - (ballPos.x - paddlePos.x)) / halfPaddleLength;
			double curAngle = Math.atan2(direction.y, direction.x);
			double newAngle = curAngle + anglePercentChange * maxAngleChange;
			double magnitude = Math.sqrt(Math.pow(direction.x, 2) + Math.pow(direction.y, 2));// Magnitude of vector needs to remain constant
			direction.set((float) (magnitude * Math.cos(newAngle)), (float) (magnitude * Math.sin(newAngle)));
			levelData.playBop();
		} else {
			levelData.playBump();
		}
		ball.setDirection(direction);
	}

}
