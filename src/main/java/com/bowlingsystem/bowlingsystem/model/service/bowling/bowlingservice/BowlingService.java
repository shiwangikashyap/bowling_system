package com.bowlingsystem.bowlingsystem.model.service.bowling.bowlingservice;

import java.util.List;

import com.bowlingsystem.bowlingsystem.model.service.game.Game;
import com.bowlingsystem.bowlingsystem.model.service.player.Player;
import com.bowlingsystem.bowlingsystem.model.service.set.PlayerSetHistory;

public interface BowlingService {
	
	public List<PlayerSetHistory> bowl(long playerId);

	public Game endGame(long gameId);

	public Player getPlayerScoreCard(long playerId);

	public Game getGameDetails(long gameId);

	public List<Player> getGamePlayers(long gameId);
	
}
