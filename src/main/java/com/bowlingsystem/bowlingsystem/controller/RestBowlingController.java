package com.bowlingsystem.bowlingsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bowlingsystem.bowlingsystem.model.controller.GameService;
import com.bowlingsystem.bowlingsystem.model.service.game.Game;
import com.bowlingsystem.bowlingsystem.model.service.player.Player;
import com.bowlingsystem.bowlingsystem.model.service.set.PlayerSetHistory;
import com.bowlingsystem.bowlingsystem.util.param.GameParams;
/**
 * @author Shiwangi
 * This is the Rest controller all the REST API requests are first routed here
 * */
@RestController
@RequestMapping(value="/api/bowling")
public class RestBowlingController {

	@Autowired
	private GameService gameService;
	
	/**
	 * This api commences the game, is responsible for configuring and initialising it
	 * @param GameParams input takes player names as input
	 * @return long returns gameId
	 */
	@PostMapping(value = "/start",consumes = MediaType.APPLICATION_JSON_VALUE ,produces=MediaType.APPLICATION_JSON_VALUE) 
	public long start(@RequestBody GameParams input) {
		return this.gameService.start(input.getPlayer_details());
	}

	/**
	 * This api allows an active player to play the next set
	 * @param long playerId
	 * @return List<PlayerSetHistory> returns the currently played set of the player
	 */
	@PostMapping(value = "/play",consumes = MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE) 
	public List<PlayerSetHistory> play(@RequestBody long playerId) {
		return this.gameService.play(playerId);
	}

	/**
	 * This api finishes an active game (based on gameId) and returns the winner details
	 * @param long gameId
	 * @return Game returns game and winner details
	 */
	@PostMapping(value = "/finish" ,consumes = MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE) 
	public Game finish(@RequestBody long gameId) {		
		return this.gameService.finish(gameId);
	}

	/**
	 * This api gets score and player details at any time w.r.t. playerId
	 * @param long playerId
	 * @return Player contains player bowling details 
	 */
	@GetMapping(value = "playerScore/{playerId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Player getPlayerScore(@PathVariable long playerId) {
		return this.gameService.getPlayerScoreCard(playerId);
	}

	/**
	 * This api gets game details w.r.t. gameId at any time
	 * @param long gameId
	 * @return Game contains game details 
	 */
	@GetMapping(value = "game/{gameId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Game getGameDetails(@PathVariable long gameId) {
		return this.gameService.getGameDetails(gameId);
	}

	/**
	 * This api gets all players and their details of a game i.e. w.r.t. gameId at any time
	 * @param long gameId
	 * @return List<Player> contains list of all players that are in the game
	 */
	@GetMapping(value = "game/{gameId}/players", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Player> getGamePlayers(@PathVariable long gameId){
		return this.gameService.getGamePlayers(gameId);
	}
}
