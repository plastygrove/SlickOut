package com.slickout;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public class CollidableAnimationObject extends AnimationObject implements ICollidableObject {

	private Shape collisionShape;
	private int collisionType;
	
	public CollidableAnimationObject(String name, Animation animation, Vector2f position, Shape collisionShape, int collisionType){
		super(name, animation, position);
		this.collisionShape = collisionShape;
		this.collisionType = collisionType;
	}
	
	@Override
	public Shape getNormalCollisionShape() {
		return collisionShape;
	}

	@Override
	public Shape getCollisionShape() {
		return collisionShape.transform(Transform.createTranslateTransform(position.x, position.y));
	}

	@Override
	public int getCollisionType() {
		return collisionType;
	}

	@Override
	public boolean isCollidingWith(ICollidableObject collidable) {
		return this.getCollisionShape().intersects(collidable.getCollisionShape());
	}

	@Override
	public void render(Graphics graphics) {
		super.render(graphics);
		graphics.draw(getCollisionShape());
	}

	public Animation getAnimation() {
		return animation;
	}

}
