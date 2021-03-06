package com.bowlingsystem.bowlingsystem.model.controller;

import java.util.List;

import com.bowlingsystem.bowlingsystem.model.service.game.Game;
import com.bowlingsystem.bowlingsystem.model.service.player.Player;
import com.bowlingsystem.bowlingsystem.model.service.set.PlayerSetHistory;
/**
 * 
 * @author Shiwangi
 * This interface defines all the required game services
 */
public interface GameService {

	public long start(String[] names);
	
	public List<PlayerSetHistory> play(long playerId);
	
	public Game finish(long gameId);

	public Player getPlayerScoreCard(long playerId);

	public Game getGameDetails(long gameId);
	
	public List<Player> getGamePlayers(long gameId);
	
}
