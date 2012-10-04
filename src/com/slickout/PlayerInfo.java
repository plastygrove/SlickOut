package com.slickout;

public class PlayerInfo {
/*	private static PlayerInfo _instance = null;

	public static PlayerInfo getCurrentPlayerInfo() {
		return _instance;
	}

	public static PlayerInfo createNewCurrentPlayerInfo() {
		_instance = new PlayerInfo();
		return _instance;
	}
*/
	private String name;
	private int lives;
	private int score;

	public PlayerInfo() {
		name = "AAA";
		lives = 3;
		score = 0;
	}

	public final String getName() {
		return name;
	}

	public final int getLives() {
		return lives;
	}

	public final int getScore() {
		return score;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public void incrementLives() {
		lives++;
	}

	public void decrementLives() {
		lives--;
	}

	public final void addScore(int score) {
		this.score += score;
	}

	public final void decreaseScore(int score) {
		this.score -= score;
	}

}
