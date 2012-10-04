package com.slickout;

import java.util.List;

import org.newdawn.slick.SlickException;

public interface ILevel {
	public ImageObject getBackground();

	public List<Ball> getBalls();

	public Paddle getPaddle();

	public CollidableImageObject getLeftBumper();

	public CollidableImageObject getRightBumper();

	public CollidableImageObject getTopBumper();

	public List<Brick> getBricks();

	public Ball addNewBall() throws SlickException;

	public void playBip() throws SlickException;

	public void playBop() throws SlickException;

	public void playBump() throws SlickException;

	public void playDrop() throws SlickException;
}
