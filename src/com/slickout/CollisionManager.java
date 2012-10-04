package com.slickout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.newdawn.slick.SlickException;

public class CollisionManager {
	private Map<Integer, List<ICollidableObject>> collidables = null;
	private Map<Integer, List<Integer>> collisionsTypes = null;
	private Map<String, ICollisionHandler> collisionHandlers = null;

	public CollisionManager() {
		collidables = new HashMap<Integer, List<ICollidableObject>>();
		collisionsTypes = new HashMap<>();
		collisionHandlers = new HashMap<>();
	}

	public static String getKey(int type1, int type2) {
		return (type1 < type2) ? type1 + "-" + type2 : type2 + "-" + type1;
	}

	public void addCollidable(ICollidableObject collidable) {
		List<ICollidableObject> collidableList = collidables.get(collidable.getCollisionType());

		if (collidableList == null) {
			collidableList = new ArrayList<>();
			collidables.put(collidable.getCollisionType(), collidableList);
		}

		collidableList.add(collidable);
	}

	public void removeCollidable(ICollidableObject collidable) {
		List<ICollidableObject> collidableList = collidables.get(collidable.getCollisionType());

		if (collidableList != null) {
			collidableList.remove(collidable);
		}

	}

	public void addHandler(ICollisionHandler handler) {
		String key = getKey(handler.getCollider1Type(), handler.getCollider2Type());
		collisionHandlers.put(key, handler);
		addTypesToCollision(handler.getCollider1Type(), handler.getCollider2Type());
		addTypesToCollision(handler.getCollider2Type(), handler.getCollider1Type());
	}

	private void addTypesToCollision(int type1, int type2) {
		List<Integer> typeCollisions = collisionsTypes.get(type1);
		if (typeCollisions == null) {
			typeCollisions = new ArrayList<Integer>();
			collisionsTypes.put(type1, typeCollisions);
		}

		typeCollisions.add(type2);
	}

	public void processCollisions() throws SlickException {
		Set<String> allCollisionKeys = new HashSet<>();
		List<CollisionData> collisions = new ArrayList<>();
		Set<Integer> types = collisionsTypes.keySet();
		for (Integer type : types) {
			List<Integer> collidesWithTypes = collisionsTypes.get(type);

			for (Integer collidingType : collidesWithTypes) {
				if (!allCollisionKeys.contains(getKey(type, collidingType))) {
					List<ICollidableObject> collidableForType = collidables.get(type);
					List<ICollidableObject> collidableForCollidingType = collidables.get(collidingType);
					for (ICollidableObject collidable : collidableForType) {
						for (ICollidableObject collidesWith : collidableForCollidingType) {
							if(collidable.isCollidingWith(collidesWith)){
								CollisionData cd = new CollisionData();
								cd.handler = collisionHandlers.get(getKey(type, collidingType));
								cd.object1 = collidable;
								cd.object2 = collidesWith;
								
								collisions.add(cd);
							}

						}
					}
				}
				allCollisionKeys.add(getKey(type, collidingType));
			}
			
		}
		for(CollisionData cd: collisions){
			cd.handler.performCollision(cd.object1, cd.object2);
		}
	}
}

class CollisionData {
	public ICollisionHandler handler;
	public ICollidableObject object1;
	public ICollidableObject object2;
}
