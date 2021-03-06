package com.bowlingsystem.bowlingsystem.model.service.bowling.bowlingservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bowlingsystem.bowlingsystem.model.service.allocation.LaneAllocation;
import com.bowlingsystem.bowlingsystem.model.service.game.Game;
import com.bowlingsystem.bowlingsystem.model.service.game.GameDataRepository;
import com.bowlingsystem.bowlingsystem.model.service.game.GameModel;
import com.bowlingsystem.bowlingsystem.model.service.player.Player;
import com.bowlingsystem.bowlingsystem.model.service.player.PlayerDataRepository;
import com.bowlingsystem.bowlingsystem.model.service.player.PlayerModel;
import com.bowlingsystem.bowlingsystem.model.service.rules.RuleSystem;
import com.bowlingsystem.bowlingsystem.model.service.set.PlayerSetHistory;
import com.bowlingsystem.bowlingsystem.model.service.set.SetActions;

import static com.bowlingsystem.bowlingsystem.model.service.player.PlayerConstants.PLAYER_INACTIVE;
import static com.bowlingsystem.bowlingsystem.model.service.player.PlayerConstants.PLAYER_ACTIVE;
import static com.bowlingsystem.bowlingsystem.model.service.rules.RuleConstants.SPARE_BONUS;
import static com.bowlingsystem.bowlingsystem.model.service.rules.RuleConstants.STRIKE_BONUS;
import static com.bowlingsystem.bowlingsystem.model.service.rules.RuleConstants.TOTAL_PINS_RULE;
import static com.bowlingsystem.bowlingsystem.model.service.rules.RuleConstants.PIN_SCORE;
import static com.bowlingsystem.bowlingsystem.model.service.game.GameConstants.STATUS_FINISHED;
import static com.bowlingsystem.bowlingsystem.model.service.game.GameConstants.STATUS_ONGOING;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class BowlingServiceImpl implements BowlingService{

	private long _playerId;

	private long _gameId;
	
	@Autowired
	private PlayerModel _playerModel;
	
	private static int _singlePinScore;
	
	private static int _totalPins;
	
	private static int _strikeBonus;
	
	private static int _spareBonus;

	private List <PlayerSetHistory> _setHistoryList;
	
	private List <Player> _playerDataList;
	
	@Autowired
	private GameModel _gameModel;
	
	@Autowired
	private SetActions _setAction;
	
	@Autowired
	private RuleSystem _ruleSystem;
	
	@Autowired
	private LaneAllocation _laneAllocation;
		
	@Override
	/** This function is responsible for playing current set of player and calculating score
	 * for played sets w.r.t. defined rules for sets and scores and initiating nest set for player if valid
	 * @param long playerId
	 * @return List<PlayerSetHistory> returns list of play set history containing details of each trial of currently played set  
	 */
	public List<PlayerSetHistory> bowl(long playerId) {
		this._playerId = playerId;
		this._setPlayerDetails();
		_playerModel.validatePlayer(PLAYER_ACTIVE);
		this._setBowlingRules();
		this._playCurrentSet();
		this._prepareNextSet();
		_playerModel.storePlayerDetails();
		return _setHistoryList;
	}
	
	/** This function sets all values from rules that are related to bowling
	 */
	private void _setBowlingRules() {
		_totalPins = Integer.parseInt(this._ruleSystem.getRuleValue(TOTAL_PINS_RULE));
		_spareBonus = Integer.parseInt(this._ruleSystem.getRuleValue(SPARE_BONUS));
		_strikeBonus = Integer.parseInt(this._ruleSystem.getRuleValue(STRIKE_BONUS));
		_singlePinScore = Integer.parseInt(this._ruleSystem.getRuleValue(PIN_SCORE));
	}
	
	private void _setPlayerDetails() {
		_playerModel.setPlayer(this._playerId);
	}
	
	/** This function allows player to play the current set while following set rules,
	 *  sets and stores currentSet played by the player, prepares next trial of current set 
	 *  if valid by the rules, tracks scores, pinsKnocked, strikes of each set, 
	 *  consequently also updating the aggregated bowling details of the player  
	 */
	private void _playCurrentSet() {
		_setAction.initialiseSetRules();
		_setAction.setPlayerId(this._playerId);
		_setHistoryList = new ArrayList<>();
		int noOfTrials = SetActions.getNoOfTrials(); 
		int pinsStanding = _totalPins;
		boolean playedExtraTrial = false;
		for(int trialInc = 0; trialInc< noOfTrials; ++trialInc) {
			_setAction.setHistoryId(_playerModel.getCurrentSetID());
			_setAction.setHistoryData();
			_setAction.historyData.setPinsKnocked(this._getPinsKnocked(pinsStanding));
			pinsStanding -= _setAction.historyData.getPinsKnocked();
			this._calculateScore(pinsStanding);
			_playerModel.setTotalScore(_playerModel.getTotalScore() + _setAction.historyData.getCurrentScore());
			_setAction.saveHistoryData();
			_setHistoryList.add(_setAction.historyData);
			if(_playerModel.getCurrentSet() == SetActions.getNoOfSets() && pinsStanding == 0 && playedExtraTrial == false) {
				pinsStanding = _totalPins;
				noOfTrials += SetActions.getFinalSetTrial();
				playedExtraTrial = true;
			}
			if(pinsStanding == 0 || trialInc == noOfTrials-1) {
				break;
			}else {
				_setAction.setTrial(trialInc + 1);
				_setAction.setSetNumber(_playerModel.getCurrentSet());
				_playerModel.setCurrentSetId(_setAction.initialiseNewSet());
			}
		}
	}
	
	/** Randomly return any number of knocked pins between 0 and pins that have not been knocked in current set  
	 *@return int pinsKnocked
	 */
	private int _getPinsKnocked(int pinsStanding) {
		Random rand = new Random(); 
		return  rand.nextInt(pinsStanding); 
	}
	
	
	/** Calculate score for current trial of current set w.r.t. score rules and set details in playerSetHistory and player
	 */
	private void _calculateScore(int pinsStanding) {
 
		if(_setAction.historyData.getTrial() == 'A' ) {
			//If Strike then add strike bonus in score calculation
			if(pinsStanding == 0) {
				_setAction.historyData.setCurrentScore(_setAction.historyData.getPinsKnocked() * _singlePinScore + _strikeBonus);			
				_playerModel.setTotalStrikes(_playerModel.getTotalStrikes() + 1);				
			}else {
				// not Strike then score is aggregate of pinscore for each knocked pins
				_setAction.historyData.setCurrentScore(_setAction.historyData.getPinsKnocked() * _singlePinScore);			
				_playerModel.setMissedStrikes(_playerModel.getMissedStrikes() + 1);
			}

		}else if (pinsStanding == 0) {
			//If spare then add spare bonus in score calculation
			_setAction.historyData.setCurrentScore(_setAction.historyData.getPinsKnocked() * _singlePinScore + _spareBonus);			
		}else {
			//not spare then score is aggregate of pinscore for each knocked pins
			_setAction.historyData.setCurrentScore(_setAction.historyData.getPinsKnocked() * _singlePinScore);
		}
		
	}
	
	/** Deactivate player from playing if their last set has been played else
	 * prepare next set
	 */
	private void _prepareNextSet() {
		_setAction.setTrial(0);
		if(this._playerModel.getCurrentSet() == SetActions.getNoOfSets()) {
			this._playerModel.setPlayerStatus(PLAYER_INACTIVE);
		}else {
			_setAction.setSetNumber(this._playerModel.getCurrentSet() + 1);
			_playerModel.setCurrentSet(this._playerModel.getCurrentSet() + 1);
			this._playerModel.setCurrentSetId(_setAction.initialiseNewSet());
		}
	}

	public long getPlayerId() {
		return _playerId;
	}

	public void setPlayerId(long playerId) {
		this._playerId = playerId;
	}

	@Override
	/** This function validates whether the game is ready to be finished if yes then 
	 * it calculates winner, releases allocated lane of all players and deactivates game
	 * @param long gameId
	 * @return Game returns details of the game by gameId after finishing the game
	 */
	public Game endGame(long gameId) {
		this.setGameId(gameId);
		this._setGameDetails();
		this._gameModel.validateGame(STATUS_ONGOING);
		this._getPlayersOfGame();
		this._validateStatusOfPlayers();
		this._releaseLane();
		this._calculateWinner();
		return this._gameModel.getGameDetails();
	}
	
	private void _setGameDetails() {
		_gameModel.setGameDetails(this._gameId);
	}

	/** Gets all players of the game by gameId
	 */
	private void _getPlayersOfGame() {
		_playerDataList = _playerModel.getPlayersOfGame(this._gameId);
	}

	/** Checks to see if all players have completed playing the game, 
	 * if not throws InvalidStatusException as game can not be finished if
	 * players are still playing
	 */
	private void _validateStatusOfPlayers() {
		for(Player players: _playerDataList) {
			_playerModel.setPlayer(players);
			_playerModel.validatePlayer(PLAYER_INACTIVE);
		}		
	}
	
	/**
	 * Calculate winner on the basis of score of each players and set winner details of game in DB 
	 * If max scores of multiple players are equal, winner is the player with
	 * greater number of strikes 
	 **/
	private void _calculateWinner() {
		long winnerId = 0;
		int maxScore = 0;
		int missedStrike = 0;
		String winnerName = "";
		for(Player players: _playerDataList) {
			if(players.getTotalScore() > maxScore) {
				maxScore = players.getTotalScore();
				winnerId = players.getPlayerId();
				missedStrike = players.getMissedStrikes();
				winnerName = players.getName();
			}else if (players.getTotalScore() == maxScore && players.getMissedStrikes() < missedStrike) {
				maxScore = players.getTotalScore();
				winnerId = players.getPlayerId();
				missedStrike = players.getMissedStrikes();
				winnerName = players.getName();
			}
		}
		_gameModel.setWinnerDetails(winnerName, maxScore, winnerId);
		_gameModel.updateStatus(STATUS_FINISHED);
		_gameModel.storeGameDetails();
	}

	@Override
	/*
	 * Returns player score card w.r.t. playerId at any time of the game
	 * throws EntityNotFoundException if playerId does not exist
	 */
	public Player getPlayerScoreCard(long playerId) {
		// TODO Auto-generated method stub
		_playerModel.setPlayer(playerId);
		return _playerModel.getPlayer();
	}
	
	/*
	 * Release lanes of players after game is finished
	 * increment vacancy of each released lane
	 */
	private void _releaseLane() {
		for(Player players: _playerDataList) {
			this._laneAllocation.releaseLane(players.getAllocatedLane());
		}
	}

	@Override
	/*
	 * Returns game details w.r.t. gameId at any time of the game
	 * throws EntityNotFoundException if gameId does not exist
	 */
	public Game getGameDetails(long gameId) {
		this._gameModel.setGameDetails(gameId);
		return this._gameModel.getGameDetails();
	}

	@Override
	/*
	 * Returns all players and details participating in current same game w.r.t. gameId at any time of the game
	 */
	public List<Player> getGamePlayers(long gameId) {
		// TODO Auto-generated method stub
		this.setGameId(gameId);
		this._getPlayersOfGame();
		return this._playerDataList;
	}

	public long getGameId() {
		return _gameId;
	}

	public void setGameId(long gameId) {
		this._gameId = gameId;
	}
}
