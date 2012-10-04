package com.slickout;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public class CollidableImageObject extends ImageObject implements ICollidableObject {
	protected Shape collisionShape;
	protected int collisionType;

	public CollidableImageObject(String name, Image image, Vector2f position, Shape collisionShape, int collisionType) {
		super(name, image, position);
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

}
