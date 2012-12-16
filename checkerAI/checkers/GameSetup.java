package checkers;

import java.io.Serializable;

public class GameSetup implements Serializable{
	private int color;
	private String name;
	private int seconds;
	public GameSetup(int color, String name){
		this.color = color;
		this.name = name;
	}
	/**
	 * @return Returns the color.
	 */
	protected int getColor() {
		return color;
	}
	protected String getName() {
		return name;
	}
	protected int getSecondsPerPlayer(){
		return seconds;
	}
	protected void setSecondsPerPlayer(int seconds){
		this.seconds = seconds;
	}
}
