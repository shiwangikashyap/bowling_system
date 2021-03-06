package com.bowlingsystem.bowlingsystem.model.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bowlingsystem.bowlingsystem.model.service.bowling.bowlingservice.BowlingService;
import com.bowlingsystem.bowlingsystem.model.service.bowling.configurationservice.GameConfiguration;
import com.bowlingsystem.bowlingsystem.model.service.game.Game;
import com.bowlingsystem.bowlingsystem.model.service.player.Player;
import com.bowlingsystem.bowlingsystem.model.service.set.PlayerSetHistory;
/**
 * 
 * @author Shiwangi
 *
 */
@Service
public class GameServiceImpl implements GameService {

	@Autowired
	private GameConfiguration _gameConfig;
	
	@Autowired
	private BowlingService _bowling;
	
	@Override
	public List<PlayerSetHistory> play(long playerId) {
		// TODO Auto-generated method stub
		return _bowling.bowl(playerId);
	}

	@Override
	public long start(String[] names) {
		Long gameId =  _gameConfig.configureGame(names);
		_gameConfig.initialiseGame();
		return gameId;
	}

	@Override
	public Game finish(long gameId) {	
		return _bowling.endGame(gameId);
	}

	@Override
	public Player getPlayerScoreCard(long playerId) {
		// TODO Auto-generated method stub
		return _bowling.getPlayerScoreCard(playerId);
	}

	@Override
	public Game getGameDetails(long gameId) {
		// TODO Auto-generated method stub
		return _bowling.getGameDetails(gameId);
	}

	@Override
	public List<Player> getGamePlayers(long gameId) {
		// TODO Auto-generated method stub
		return _bowling.getGamePlayers(gameId);
	}

}
