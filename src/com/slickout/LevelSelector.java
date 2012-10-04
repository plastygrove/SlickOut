package com.slickout;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class LevelSelector extends BasicGameState {
	private int stateId;
	private Image background;
	private Image ended;
	private int option;
	private int optionSelected;
	private int counter;

	public LevelSelector(int id) {
		super();
		stateId = id;
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		super.mouseMoved(oldx, oldy, newx, newy);
		int line = 0;
		if (newy > 150 && newy < 325) {
			line = 0;
		} else if (newy > 375 && newy < 550) {
			line = 1;
		}

		int row = -1;

		for (int i = 0; i < 3; i++) {
			int minx = 50 + (250 * i);
			int maxx = minx + 200;

			if (newx > minx && newx < maxx) {
				row = i + 1;
			}
		}

		option = (line * 3) + row;

	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		super.mouseClicked(button, x, y, clickCount);
		optionSelected = option;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		background = new Image("data/levelselector.jpg");
		ended = new Image("data/levelend.png");
		counter = 5000;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		optionSelected = -1;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		background.draw();

		for (int i = 0; i < 6; i++) {
			if (GameInfo.getCurrentGameInfo().isLevelCompleted(i + 1)) {
				int x = 50 + (250 * ((i < 3) ? i : i - 3));
				int y = 150 + (225 * ((i < 3) ? 0 : 1));
				ended.draw(x, y);
			}
		}

		if (GameInfo.getCurrentGameInfo().allLevelsCompleted()) {
			g.drawString("CONGRATULATIONS... YOU'VE FINISHED THIS TUTORIAL", 200, 300);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(!GameInfo.getCurrentGameInfo().allLevelsCompleted()){
			if(optionSelected != -1 && !GameInfo.getCurrentGameInfo().isLevelCompleted(optionSelected)){
				String levelfile = "data/level"+optionSelected+".lvl";
				// obtain the game state
				GamePlayState gameplay = (GamePlayState) game.getState(1);
 
				gameplay.setLevelFile(levelfile);
 
				GameInfo.getCurrentGameInfo().setCompletedLevel(optionSelected);
 
				game.enterState(1);
			}
		}else{
			counter -= delta;
 
			if(counter < 0){
				game.enterState(0);
			}
		}
	}

	@Override
	public int getID() {
		return stateId;
	}

}
