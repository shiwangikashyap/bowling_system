package com.bowlingsystem.bowlingsystem.model.service.player;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bowlingsystem.bowlingsystem.util.exception.custom.EntityNotFoundException;
import com.bowlingsystem.bowlingsystem.util.exception.custom.InvalidStatusException;

/*
 * Model for handling manipulations related to player entity
 */
@Component
public class PlayerModel {

	@Autowired
	private PlayerDataRepository _playerDao;
	
	private Player _player;
	
	public long createPlayer(String playerName, int laneNo, long gameId) {
		_player = new Player(playerName,laneNo,gameId);
		this.storePlayerDetails();
		return this.getPlayerId();
	}
	
	public void storePlayerDetails() {
		this._playerDao.save(_player);
	}
	
	public long getPlayerId() {
		return this._player.getPlayerId();
	}
	
	public void setCurrentSet(int currentSet) {
		this._player.setCurrentSet(currentSet);
	}
	
	public void setPlayer(long playerId) {
		Optional<Player> playerOptional = _playerDao.findById(playerId);
		if(playerOptional.isPresent()) {
			_player = playerOptional.get();
		}else {
			throw new EntityNotFoundException("Player no. " + playerId + " does not exist");
		}
	}
	
	public void setPlayer(Player player) {
		this._player = player;
	}

	public Player getPlayer() {
		return _player;
	}
	
	public boolean getPlayerStatus() {
		return this._player.isActive();
	}
	
	public long getCurrentSetID() {
		return this._player.getCurrentSetId();
	}
	
	public void validatePlayer(boolean status) {
		if(this._player.isActive() != status) {
			throw new InvalidStatusException("This action cannot be performed as player no. " + _player.getPlayerId() + " is in invalid state");
		}
	}
	
	public int getTotalStrikes() {
		return this._player.getTotalStrikes();
	}
	
	public void setTotalStrikes(int totalStrikes) {
		this._player.setTotalStrikes(totalStrikes);
	}

	public int getTotalScore() {
		// TODO Auto-generated method stub
		return this._player.getTotalScore();
	}

	public void setTotalScore(int totalScore) {
		// TODO Auto-generated method stub
		this._player.setTotalScore(totalScore);
	}

	public int getCurrentSet() {
		// TODO Auto-generated method stub
		return this._player.getCurrentSet();
	}

	public void setCurrentSetId(long initialiseNewSet) {
		// TODO Auto-generated method stub
		this._player.setCurrentSetId(initialiseNewSet);
	}

	public int getMissedStrikes() {
		// TODO Auto-generated method stub
		return this._player.getMissedStrikes();
	}

	public void setMissedStrikes(int missedStrike) {
		// TODO Auto-generated method stub
		this._player.setMissedStrikes(missedStrike);
	}

	public void setPlayerStatus(boolean status) {
		// TODO Auto-generated method stub
		this._player.setActive(status);
	}

	public List<Player> getPlayersOfGame(long gameId) {
		// TODO Auto-generated method stub
		return this._playerDao.findByGameId(gameId);
	}
}
