package com.bowlingsystem.bowlingsystem.model.service.bowling.configurationservice;

public interface GameConfiguration {
	public long configureGame(String[] playerNames);
	
	public void initialiseGame();
}
