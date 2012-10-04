package com.slickout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.slickout.Paddle.PAD_STATE;

public class GamePlayState extends BasicGameState {
	private ILevel level;
	private String levelFile;
	private PlayerInfo playerInfo;

	private int counter = 0;
	private String message = null;

	private static enum LEVEL_STATES {
		BALL_LAUNCH, NORMAL_GAME, LIFE_LOST, LEVEL_OVER, GAME_OVER, GAME_PAUSED
	};

	private LEVEL_STATES currentState;
	private LEVEL_STATES prevState;
	private CollisionManager collisionManager;
	private int stateId;
	
	private boolean isGamePaused = false;
	private Vector2f[] pausedDirection = null;
	
	
	public GamePlayState(int id){
		super();
		stateId = id;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		level.getBackground().render(g);
		level.getLeftBumper().render(g);
		level.getRightBumper().render(g);
		level.getTopBumper().render(g);

		for (Brick brick : level.getBricks()) {
			brick.render(g);
		}

		level.getPaddle().render(g);

		for (Ball ball : level.getBalls()) {
			ball.render(g);
		}

		g.drawString("Lives: " + playerInfo.getLives(), 700, 10);
		g.drawString("Score: " + playerInfo.getScore(), 500, 10);
//		g.drawString("Paddle Pos: " + level.getPaddle().position, 300, 30);
		
		if(message != null){
			g.drawString(message, 300, 300);
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if(key == Input.KEY_ESCAPE){ //switch state of game pause
			if(!isGamePaused){
				List<Ball> allBalls = level.getBalls();
				pausedDirection = new Vector2f[allBalls.size()];
				for(int i=0; i<allBalls.size(); i++){
					pausedDirection[i] = allBalls.get(i).direction;
					allBalls.get(i).setDirection(new Vector2f(0.0f, 0.0f));
				}
				prevState = currentState;
				currentState = LEVEL_STATES.GAME_PAUSED;
				isGamePaused = true;
			} else {
				currentState = prevState;
				isGamePaused = false;
			}
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		switch (currentState) {
		case BALL_LAUNCH: {
			if(pausedDirection != null){ //i.e. if it were paused while in launch mode
				pausedDirection = null;
				message = "";
				container.getInput().addMouseListener(level.getPaddle());
			}
			if (level.getBalls().size() == 0) {
				Ball ball = level.addNewBall();
				ball.setSpeed(0.0f);
				ball.setDirection(new Vector2f(0, 0));
			}

			Ball ball = level.getBalls().get(0);

			if (level.getPaddle().getState() == Paddle.PAD_STATE.STICKY) {
				Vector2f position = level.getPaddle().getPosition().copy();
				position.x += 40;
				position.y -= 20;
				ball.setPosition(position);
			} else {
				level.getPaddle().setState(Paddle.PAD_STATE.NORMAL);
				ball.setSpeed(0.5f);
				collisionManager.addCollidable(ball);

				Vector2f direction = new Vector2f(0, 1);
				Random r = new Random();
				direction.add(r.nextInt(45) * (r.nextBoolean() ? -1 : 1));
				ball.setDirection(direction);
				currentState = LEVEL_STATES.NORMAL_GAME;
			}
			break;
		}
		case NORMAL_GAME: {//i.e. if it were paused while in normal game mode
			List<Ball> removals = null;
			if(pausedDirection != null){
				List<Ball> allBalls = level.getBalls();
				for(int i=0; i<allBalls.size(); i++){
					allBalls.get(i).setDirection(pausedDirection[i]);
				}
				pausedDirection = null;
				container.getInput().addMouseListener(level.getPaddle());
				message = "";
			}

			for (Ball ball : level.getBalls()) {
				ball.update(container, game, delta);
				if (ball.getPosition().y > container.getHeight()) {
					if (removals == null) {
						removals = new ArrayList<Ball>();
					}
					removals.add(ball);
				}
			}

			if (removals != null) {
				for (Ball ball : removals) {
					level.getBalls().remove(ball);
					collisionManager.removeCollidable(ball);
				}
			}

			if (level.getBalls().size() == 0) {
				currentState = LEVEL_STATES.LIFE_LOST;
			}
			collisionManager.processCollisions();

			if (level.getBricks().size() == 0) {
				container.getInput().removeMouseListener(level.getPaddle());
				currentState = LEVEL_STATES.LEVEL_OVER;
			}
			break;
		}
		
		case GAME_PAUSED:
		{
			container.getInput().removeMouseListener(level.getPaddle());
			message = "Game Paused!";
			break;
		}
		case LIFE_LOST:
			playerInfo.decrementLives();
			if(playerInfo.getLives()==0){
				currentState = LEVEL_STATES.GAME_OVER;
				counter=3000;
			} else {
				currentState = LEVEL_STATES.BALL_LAUNCH;
				level.getPaddle().setState(PAD_STATE.STICKY);
			}
			level.playDrop();
			break;
		case LEVEL_OVER:
			if(counter==3000){
				container.getInput().removeMouseListener(level.getPaddle());
				message = "Congratulatoins!\nLevel Completed!";
			}
			counter -=delta;
			if(counter<0){
				game.enterState(SlickOut.LEVELSELECTORSTATE);
			}
			
			break;
		case GAME_OVER:
			if(counter==3000){
				container.getInput().removeMouseListener(level.getPaddle());
				message="GAME OVER\nScore: "+playerInfo.getScore();
			}
			counter-=delta;
			if(counter<0){
				game.enterState(SlickOut.MAINMENUSTATE);
			}
			
			break;
		}
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return stateId;
	}

	public void setLevelFile(String file) {
		levelFile = file;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		if (levelFile == null) {
			throw new SlickException("No level to load");
		}

		try {
			level = LevelImpl.loadLevel(new FileInputStream(new File(levelFile)));
		} catch (FileNotFoundException e) {
			throw new SlickException("Could not load file");
		}
		container.getInput().addMouseListener(level.getPaddle());
		currentState = LEVEL_STATES.BALL_LAUNCH;
		collisionManager = new CollisionManager();
		collisionManager.addCollidable(level.getLeftBumper());
		collisionManager.addCollidable(level.getRightBumper());
		collisionManager.addCollidable(level.getTopBumper());

		collisionManager.addCollidable(level.getPaddle());

		for (Brick brick : level.getBricks()) {
			collisionManager.addCollidable(brick);
		}

		collisionManager.addHandler(new BumperAndPadBallCollisionHandler(level));
		collisionManager.addHandler(new BrickBallCollisionHandler(level, collisionManager));

		playerInfo = GameInfo.getCurrentGameInfo().getPlayerInfo();
	}

}
