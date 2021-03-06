package com.bowlingsystem.bowlingsystem.model.service.game;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bowlingsystem.bowlingsystem.util.exception.custom.EntityNotFoundException;
import com.bowlingsystem.bowlingsystem.util.exception.custom.InvalidStatusException;
/*
 * Model for handling manipulations related to game entity
 */
@Component
public class GameModel {

	@Autowired
	private GameDataRepository _gameDao;
	
	private Game _game;

	public long createNewGame(int noOfPlayers) {
		this._game = new Game(noOfPlayers);
		this.storeGameDetails();
		return this.getGameId();
	}
	
	public void storeGameDetails() {
		_gameDao.save(this._game);
	}
	
	public void updateStatus(String status) {
		this._game.setGameStatus(status);
	}
	
	public long getGameId() {
		return this._game.getGameId();	
	}
	
	public String getGameStatus() {
		return this._game.getGameStatus();
	}

	public Game getGameDetails() {
		return this._game;
	}
	
	public void setGameDetails(long gameId) {
		 Optional<Game> gameOptional= this._gameDao.findById(gameId);
		 if(gameOptional.isPresent()) {
			 this._game	= gameOptional.get();	
		 }else {
			 throw new EntityNotFoundException("Game does not exist");
		 }
	}

	public void setWinnerDetails(String winnerName, int winnerScore, long winnerId) {
		// TODO Auto-generated method stub
		this._game.setWinner(winnerName);
		this._game.setWinnerId(winnerId);
		this._game.setWinnerScore(winnerScore);
	}

	public void validateGame(String status) {
		if(!this._game.getGameStatus().equals(status)) {
			throw new InvalidStatusException("Game is in invalid state");
		}
		
	}
}
