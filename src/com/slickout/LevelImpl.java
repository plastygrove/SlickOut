package com.slickout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class LevelImpl implements ILevel {
	protected ImageObject background;
	protected String[] ballArgs;

	public void setRightBumper(CollidableImageObject rightBumper) {
		this.rightBumper = rightBumper;
	}

	public void setBallArgs(String[] ballArgs) {
		this.ballArgs = ballArgs;
	}

	public void setLeftBumper(CollidableImageObject leftBumper) {
		this.leftBumper = leftBumper;
	}

	public void setTopBumper(CollidableImageObject topBumper) {
		this.topBumper = topBumper;
	}

	public void setPaddle(Paddle paddle) {
		this.paddle = paddle;
	}

	protected CollidableImageObject rightBumper;
	protected CollidableImageObject leftBumper;
	protected CollidableImageObject topBumper;

	protected List<Brick> bricks;
	protected List<Ball> balls;
	protected Paddle paddle;
	
	//Sounds obtained from http://www.partnersinrhyme.com/soundfx/PDsoundfx/beep.shtml
	protected Sound bip;
	protected Sound bop;
	protected Sound bump;
	protected Sound drop;

	private LevelImpl() {
		balls = new ArrayList<Ball>();
	}

	public final void setBackground(ImageObject image) {
		background = image;
	}

	@Override
	public final ImageObject getBackground() {
		return background;
	}

	@Override
	public final List<Ball> getBalls() {
		return balls;
	}

	@Override
	public final Paddle getPaddle() {
		return paddle;
	}

	@Override
	public final CollidableImageObject getLeftBumper() {
		return leftBumper;
	}

	@Override
	public final CollidableImageObject getRightBumper() {
		return rightBumper;
	}

	@Override
	public final CollidableImageObject getTopBumper() {
		return topBumper;
	}

	@Override
	public final List<Brick> getBricks() {
		return bricks;
	}

	public static ILevel loadLevel(InputStream is) throws SlickException {
		LevelImpl level = new LevelImpl();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		level.setBackground(createImage(readNextValidLine(br)));
		level.setLeftBumper(createCollidableImage(readNextValidLine(br)));
		level.setRightBumper(createCollidableImage(readNextValidLine(br)));
		level.setTopBumper(createCollidableImage(readNextValidLine(br)));
		level.setPaddle(createPaddle(readNextValidLine(br)));

		level.setBallArgs(readNextValidLine(br));
		
		level.bip = new Sound("data/bip.wav");
		level.bop = new Sound("data/bop.wav");
		level.bump = new Sound("data/bump.wav");
		level.drop = new Sound("data/drop.wav");

		try {
			List<Brick> bricks = new ArrayList<>();
			while (br.ready()) {
				bricks.add(createBrick(readNextValidLine(br)));
			}
			level.setBricks(bricks);
		} catch (IOException e) {
			throw new SlickException("Could not load Bricks", e);
		}

		return level;
	}

	public void setBricks(List<Brick> bricks) {
		this.bricks = bricks;
	}

	private static ImageObject createImage(String[] args) throws SlickException {
		String name = args[0];
		String[] imageData = args[1].split(";");

		if (!imageData[0].trim().equalsIgnoreCase("IMAGE")) {
			throw new SlickException("Invalid Image or not an image");
		}
		String path = imageData[1];
		String[] coords = args[2].split(",");

		Vector2f position = new Vector2f(Integer.parseInt(coords[0].trim()), Integer.parseInt(coords[1].trim()));

		return new ImageObject(name, new Image(path), position);

	}

	private static CollidableImageObject createCollidableImage(String[] args) throws SlickException {
		ImageObject image = createImage(args);

		String[] collisionData = args[3].split(";");

		int collisionType = Integer.parseInt(collisionData[0]);
		Shape shape = null;

		if (collisionData[1].trim().equalsIgnoreCase("RECTANGLE")) {
			String[] size = collisionData[2].split(",");
			shape = new Rectangle(Integer.parseInt(size[0]), Integer.parseInt(size[1]), Integer.parseInt(size[2]), Integer.parseInt(size[3]));
		} else if (collisionData[1].trim().equalsIgnoreCase("CIRCLE")) {
			String[] size = collisionData[2].split(",");
			shape = new Circle(Integer.parseInt(size[0]), Integer.parseInt(size[1]), Integer.parseInt(size[2]));
		}

		return new CollidableImageObject(image.getName(), image.getImage(), image.getPosition(), shape, collisionType);
	}

	private static CollidableAnimationObject createCollidableAnimation(String[] args) throws SlickException {
		String name = args[0];

		String[] imageData = args[1].split(";");

		if (!imageData[0].trim().equalsIgnoreCase("ANIMATION")) {
			throw new SlickException("Animation tag is invalid");
		}

		String[] animationData = imageData[2].split(",");
		SpriteSheet ss = new SpriteSheet(new Image(imageData[1]), Integer.parseInt(animationData[0]), Integer.parseInt(animationData[1]));
		Animation animation = new Animation(ss, Integer.parseInt(animationData[2]));

		String[] coords = args[2].split(",");
		Vector2f position = new Vector2f(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
		String[] collisionData = args[3].split(";");

		int collisionType = Integer.parseInt(collisionData[0]);
		Shape shape = null;

		if (collisionData[1].trim().equalsIgnoreCase("RECTANGLE")) {
			String[] size = collisionData[2].split(",");
			shape = new Rectangle(Integer.parseInt(size[0]), Integer.parseInt(size[1]), Integer.parseInt(size[2]), Integer.parseInt(size[3]));
		} else if (collisionData[1].trim().equalsIgnoreCase("CIRCLE")) {
			String[] size = collisionData[2].split(",");
			shape = new Circle(Integer.parseInt(size[0]), Integer.parseInt(size[1]), Integer.parseInt(size[2]));
		}

		return new CollidableAnimationObject(name, animation, position, shape, collisionType);

	}

	private static Paddle createPaddle(String[] args) throws SlickException {
		CollidableAnimationObject animation = createCollidableAnimation(args);
		return new Paddle(animation.getName(), animation.getAnimation(), animation.getPosition(), animation.getNormalCollisionShape(), animation.getCollisionType());

	}

	@Override
	public Ball addNewBall() throws SlickException {
		Ball ball = createBall(ballArgs);
		balls.add(ball);

		return ball;
	}

	private static Ball createBall(String[] args) throws SlickException {
		CollidableImageObject image = createCollidableImage(args);
		return new Ball(image.getName(), image.getImage(), image.getPosition(), Float.parseFloat(args[4]), new Vector2f(0, 0), image.getNormalCollisionShape(), image.getCollisionType());
	}

	private static Brick createBrick(String[] args) throws SlickException {
		CollidableAnimationObject brickAnimation = createCollidableAnimation(args);
		String[] brickData = args[4].split(";");
		String[] colorData = brickData[0].split(",");
		int numberOfHits = Integer.parseInt(brickData[1]);
		Color color = new Color(Integer.parseInt(colorData[0]), Integer.parseInt(colorData[1]), Integer.parseInt(colorData[2]));

		return new Brick(brickAnimation.getName(), brickAnimation.getAnimation(), brickAnimation.getPosition(), numberOfHits, color, brickAnimation.getNormalCollisionShape(),
				brickAnimation.getCollisionType());
	}

	private static String[] readNextValidLine(BufferedReader br) throws SlickException {
		boolean read = false;
		String[] args = null;

		while (!read) {
			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e) {
				throw new SlickException("Could not read level", e);
			}

			if (!(line.startsWith("#") || line.isEmpty())) {
				read = true;
				args = line.split("\\|");
			}
		}
		return args;
	}

	@Override
	public void playBip() throws SlickException {
		if(bip == null){
			bip = new Sound("data/bip.wav");
		}
		bip.play();
	}

	@Override
	public void playBop() throws SlickException {
		if(bop == null){
			bop = new Sound("data/bop.wav");
		}
		bop.play();
	}

	@Override
	public void playBump() throws SlickException {
		if(bump == null){
			bump = new Sound("data/bump.wav");
		}
		bump.play();
	}

	@Override
	public void playDrop() throws SlickException {
		if(drop == null){
			drop = new Sound("data/drop.wav");
		}
		drop.play();
	}

}
