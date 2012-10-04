package com.slickout;

public class GameInfo {
	private static GameInfo _instance = null;
	private PlayerInfo playerInfo;
	private boolean[] completedLevels;
	private boolean allLevelsCompleted;
	
	public static GameInfo getCurrentGameInfo(){
		return _instance;
	}
	
	public static GameInfo createNewGameInfo(){
		_instance = new GameInfo();
		return getCurrentGameInfo();
	}
	
	private GameInfo(){
		playerInfo = new PlayerInfo();
		completedLevels = new boolean[6];
		for (boolean level :completedLevels) {
			level=false;
		}
		
		allLevelsCompleted=false;
	}
	
	public PlayerInfo getPlayerInfo(){
		return playerInfo;
	}
	
	public void setCompletedLevel(int levelId){
		completedLevels[levelId-1] = true;
		allLevelsCompleted = true;
		for(boolean level:completedLevels){
			allLevelsCompleted &=level;
		}
	}
	
	public boolean isLevelCompleted(int levelId){
		return completedLevels[levelId-1];
	}
	
	public boolean allLevelsCompleted(){
		return allLevelsCompleted;
	}
			

}
