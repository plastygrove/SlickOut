package com.slickout;

import org.newdawn.slick.geom.Shape;

public interface ICollidableObject {
	public Shape getNormalCollisionShape();

	public Shape getCollisionShape();

	public int getCollisionType();

	public boolean isCollidingWith(ICollidableObject collidable);
}
