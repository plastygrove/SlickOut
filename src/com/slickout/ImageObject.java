package com.slickout;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class ImageObject implements ILevelObject {

	protected String name;
	protected Image image;
	protected Vector2f position;

	public ImageObject(String name, Image image, Vector2f position){
		this.name = name;
		this.image = image;
		this.position = position;
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
		image.draw(position.x, position.y);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
	}
	
	public Image getImage(){
		return image;
	}

}
