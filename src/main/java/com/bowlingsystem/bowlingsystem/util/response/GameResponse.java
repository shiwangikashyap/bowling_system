package com.bowlingsystem.bowlingsystem.util.response;

import java.util.HashMap;

public class GameResponse implements Response {

	private long gameID;
	private String gameStatus;
	private HashMap <String,Long> playerNameIdMapping;
	private String [] playerNames;
	
	public GameResponse(long gameId, String gameStatus, HashMap <String,Long> playerIdMapping,String[] playerNames) {
		this.setGameID(gameId);
		this.setGameStatus(gameStatus);
		if(playerIdMapping != null) {
			this.setPlayerNameIdMapping(playerIdMapping);
		}else {
			this.setPlayerNames(playerNames);
		}
	}

	public long getGameID() {
		return gameID;
	}

	public void setGameID(long gameID) {
		this.gameID = gameID;
	}

	public String getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public HashMap<String, Long> getPlayerNameIdMapping() {
		return playerNameIdMapping;
	}

	public void setPlayerNameIdMapping(HashMap<String, Long> playerNameIdMapping) {
		this.playerNameIdMapping = playerNameIdMapping;
	}

	public String[] getPlayerNames() {
		return playerNames;
	}

	public void setPlayerNames(String[] playerNames) {
		this.playerNames = playerNames;
	}	

}
