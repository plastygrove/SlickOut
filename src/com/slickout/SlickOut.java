package com.slickout;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class SlickOut extends StateBasedGame {
	public final static int MAINMENUSTATE = 1;
	public final static int GAMEPLAYSTATE = 2;
	public final static int EXITSTATE = 3;
	public final static int LEVELSELECTORSTATE = 4;

	public static void main(String[] args) throws SlickException{
		AppGameContainer app = new AppGameContainer(new SlickOut());
		app.setDisplayMode(800, 600, false);
		app.start();
	}
	public SlickOut() {
		super("SlickOut");
		// TODO Auto-generated constructor stub
	}

	@Override
	
	public void initStatesList(GameContainer gc) throws SlickException{
//		PlayerInfo.createNewCurrentPlayerInfo();
		addState(new MainMenuGameState(MAINMENUSTATE));
		GamePlayState state = new GamePlayState(GAMEPLAYSTATE);
		state.setLevelFile("data/level1.lvl");
		addState(state);
		addState(new LevelSelector(LEVELSELECTORSTATE));
	}


}
