package com.slickout;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class AnimationObject implements ILevelObject {
	protected String name;
	protected Animation animation;
	protected Vector2f position;
	
	public AnimationObject(String name, Animation animation, Vector2f position){
		this.name = name;
		this.position = position;
		this.animation = animation;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setPosition(Vector2f position) {
		this.position = position;
	}

	@Override
	public Vector2f getPosition() {
		return position;
	}

	@Override
	public void render(Graphics graphics) {
		animation.draw(position.x, position.y);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		animation.update(delta);
	}

}
