package com.bowlingsystem.bowlingsystem.model.service.set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
/**
 *Entity for PlayerSetHistory
 * @author Shiwangi
 *
 */
public class PlayerSetHistory {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
	private long historyId;
	@Column(nullable = false)
	private long playerId;
	@Column(nullable = false)	
	private char trial;
	@Column(nullable = false, columnDefinition = "int default 1")
	private int setId;
	@Column(nullable = true, columnDefinition = "int default 0")
	private int pinsKnocked;
	@Column(nullable = false)
	private int score = 0;
	@Column(nullable = false, columnDefinition = "int default 0")
	private int currentStrikes = 0;
	
	public PlayerSetHistory(long playerId,char trial,int set) {
		this.setPlayerId(playerId);
		this.setTrial(trial);
		this.setSet(set);
	}
	
	public PlayerSetHistory() {
		
	}
	
	public void setSet(int set) {
		// TODO Auto-generated method stub
		this.setId = set;
	}
	
	public long getSetId() {
		return setId;
	}
	
	public long getHistoryId() {
		return historyId;
	}
	public void setHistoryId(long historyId) {
		this.historyId = historyId;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public char getTrial() {
		return trial;
	}
	public void setTrial(char trial) {
		this.trial = trial;
	}
	public int getPinsKnocked() {
		return this.pinsKnocked;
	}
	public void setPinsKnocked(int pinsKnocked) {
		this.pinsKnocked = pinsKnocked;
	}
	public int getCurrentScore() {
		return score;
	}
	public void setCurrentScore(int currentScore) {
		this.score = currentScore;
	}
	public int getCurrentStrikes() {
		return currentStrikes;
	}
	public void setCurrentStrikes(int currentStrikes) {
		this.currentStrikes = currentStrikes;
	}
	
	@Override
	public String toString() {	
		return "SET_NO:" + this.getSetId() + " TRIAL_NO:" + this.getTrial() +  " PINS_KNOCKED:" + this.getPinsKnocked() + " SCORE:" + this.getCurrentScore();
	}

}
