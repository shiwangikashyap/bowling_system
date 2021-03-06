package com.bowlingsystem.bowlingsystem.model.service.game;

import static com.bowlingsystem.bowlingsystem.model.service.game.GameConstants.STATUS_WAITING;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
/**
 *Entity for game details
 * @author Shiwangi
 *
 */
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long gameId;
    @Column(nullable = false)
	private int noOfPlayers;
    @Column( nullable = true, columnDefinition = "varchar(50) default null")
	private String winner;
    @Column( nullable = false, columnDefinition = "int default 0")
    private int winnerScore;
    @Column(nullable = false, columnDefinition = "varchar(50) ")
    private String GameStatus = STATUS_WAITING;
    @Column( nullable = false)
    private long winnerId;

	public Game() {
		
	}
    
    public Game (int noOfPlayers) {
		this.setNoOfPlayers(noOfPlayers);
	}
    
	public long getGameId() {
		return gameId;
	}
	public void setGameId(long gameId) {
		this.gameId = gameId;
	}
	public int getNoOfPlayers() {
		return noOfPlayers;
	}

	public String getWinner() {
		return winner;
	}
	public void setWinner(String winner) {
		this.winner = winner;
	}
	public int getWinnerScore() {
		return winnerScore;
	}
	public void setWinnerScore(int winnerScore) {
		this.winnerScore = winnerScore;
	}
	public String getGameStatus() {
		return GameStatus;
	}
	public void setGameStatus(String gameStatus) {
		GameStatus = gameStatus;
	}

	public long getWinnerId() {
		return winnerId;
	}

	public void setWinnerId(long winnerId) {
		this.winnerId = winnerId;
	}

	public void setNoOfPlayers(int noOfPlayers) {
		this.noOfPlayers = noOfPlayers;
	}
}
