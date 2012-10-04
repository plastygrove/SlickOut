package com.slickout;

import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class BrickBallCollisionHandler implements ICollisionHandler {
	private Random r;
	private ILevel levelData;
	private CollisionManager manager;

	public BrickBallCollisionHandler(ILevel levelData, CollisionManager manager) {
		r = new Random();

		this.levelData = levelData;
		this.manager = manager;
	}

	@Override
	public int getCollider1Type() {
		return 3;
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
		Brick brick = null;

		if (collidable1 instanceof Ball) {
			ball = (Ball) collidable1;
			brick = (Brick) collidable2;
		} else {
			ball = (Ball) collidable2;
			brick = (Brick) collidable1;
		}

		Vector2f direction = ball.getDirection().copy();
		direction.set(direction.x * -1, direction.y * -1);

		do {
			Vector2f pos = ball.getPosition();
			ball.setPosition(new Vector2f(pos.x + direction.x, pos.y - direction.y));
		} while (ball.isCollidingWith(brick));

		Shape ballShape = ball.getCollisionShape();
		Shape objectShape = brick.getCollisionShape();

		direction = ball.getDirection().copy();
		if (ballShape.getMinY() > objectShape.getMaxY() || ballShape.getMaxY() < objectShape.getMinY()) {
			direction.set(direction.x, -direction.y);
		} else {
			direction.set(-direction.x, direction.y);
		}

		direction.add(r.nextInt(10) * (r.nextBoolean() ? -1 : 1));
		ball.setDirection(direction);
		brick.decreaseHit();

		GameInfo.getCurrentGameInfo().getPlayerInfo().addScore(100);

		//Change the color of the bricks depending on the number of hits left. I've only added 2 colors here
		switch (brick.getHitsLeft()) {
		case 0:
			levelData.getBricks().remove(brick);
			manager.removeCollidable(brick);
			break;
		case 1: {
			SpriteSheet ss = new SpriteSheet(Brick.RED_BRICK, brick.animation.getWidth(), brick.animation.getHeight());
			brick.animation = new Animation(ss, brick.animation.getDuration(0));
			break;
		}

		case 2: {
			SpriteSheet ss = new SpriteSheet(Brick.BLUE_BRICK, brick.animation.getWidth(), brick.animation.getHeight());
			brick.animation = new Animation(ss, brick.animation.getDuration(0));
			break;
		}

		}
		
		levelData.playBip();
	}

}
