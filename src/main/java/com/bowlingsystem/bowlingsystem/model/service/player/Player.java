package com.bowlingsystem.bowlingsystem.model.service.player;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static com.bowlingsystem.bowlingsystem.model.service.player.PlayerConstants.PLAYER_INACTIVE;

/**
 *Entity for player details
 * @author Shiwangi
 *
 */
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
	private long playerId;
    @Column(nullable = false)    
	private String name;
    @Column(nullable = false)
	private long gameId;
    @Column(nullable = false)
	private int allocatedLane = 0;
    @Column(nullable = false)
	private int totalScore = 0;
    @Column(nullable = false)
	private int totalStrikes = 0;
    @Column(nullable = false)
	private int missedStrikes = 0;
    @Column(nullable = false)
	private boolean currentPlayer = false;
    @Column(nullable = false)    
    private long currentSetId;
    @Column(nullable = false, columnDefinition = "int default 1")
    private int currentSet = 1;
    @Column(nullable = false)    
    private boolean isActive = PLAYER_INACTIVE;
    
    public Player(){
    	
    }
    
    public Player(String name, int allocatedLane, long gameId){
    	this.setName(name);
    	this.setGameId(gameId);
    	this.setAllocatedLane(allocatedLane);
    }
    
	public boolean isCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(boolean currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int getCurrentSet() {
		return currentSet;
	}
	public void setCurrentSet(int currentSet) {
		this.currentSet = currentSet;
	}
	public long getCurrentSetId() {
		return currentSetId;
	}
	public void setCurrentSetId(long currentSet) {
		this.currentSetId = currentSet;
	}
    
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getGameId() {
		return gameId;
	}
	public void setGameId(long gameId) {
		this.gameId = gameId;
	}
	public int getAllocatedLane() {
		return allocatedLane;
	}
	public void setAllocatedLane(int allocatedLane) {
		this.allocatedLane = allocatedLane;
	}
	public int getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	public int getTotalStrikes() {
		return totalStrikes;
	}
	public void setTotalStrikes(int totalStrikes) {
		this.totalStrikes = totalStrikes;
	}
	public int getMissedStrikes() {
		return missedStrikes;
	}
	public void setMissedStrikes(int missedStrikes) {
		this.missedStrikes = missedStrikes;
	}
}
