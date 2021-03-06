package com.bowlingsystem.bowlingsystem.model.service.bowling.configurationservice;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import static com.bowlingsystem.bowlingsystem.model.service.game.GameConstants.STATUS_ONGOING;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bowlingsystem.bowlingsystem.model.service.allocation.LaneAllocation;
import com.bowlingsystem.bowlingsystem.model.service.game.GameModel;
import com.bowlingsystem.bowlingsystem.model.service.player.PlayerModel;
import com.bowlingsystem.bowlingsystem.model.service.set.SetActions;
import static com.bowlingsystem.bowlingsystem.model.service.player.PlayerConstants.PLAYER_ACTIVE;
import static com.bowlingsystem.bowlingsystem.model.service.player.PlayerConstants.PLAYER_INACTIVE;
import com.bowlingsystem.bowlingsystem.util.response.Response;

@Component
public class BowlingGameConfiguration implements GameConfiguration {
	@Autowired
	private GameModel _gameModel;

	@Autowired
	private PlayerModel _playerModel;

	@Autowired
	private LaneAllocation _laneAllocation;
	
	@Autowired
	private SetActions _setActionObj;
	
	private HashMap <String, Long> _playerIdMapping;
	
	private List<Long> _playerList;
		
	BowlingGameConfiguration(){
		_playerList = new ArrayList<>();
	}
	
	@Override
	/** This function configures the game : creating game , creating players , allocating lane if available
	 * @param String[] playerNames : Names of players who will play the game
	 * @return long gameId : Primary key of game entity
	 */
	public long configureGame(String[] playerNames) {
		int countOfPlayers = playerNames.length;
		long gameId = this._createGame(countOfPlayers);
		HashMap <String, Integer> playerLaneMapping = this._allocateLane(countOfPlayers,playerNames);
		this._storePlayerDetails(playerLaneMapping, gameId);
		return gameId;
	}

	/** This function creates new game and stores in DB 
	 * @param int countOfPlayers : no of players in the game
	 * @return long gameId : Primary key of game entity
	 */
	private long _createGame(int countOfPlayers) {
		return  _gameModel.createNewGame(countOfPlayers);
	}

	/** This function checks whether there are enough vacant lanes to accomodate all players of a game at a time
	 * and assigns one lane to each player, if lane is available for all players then game is said to be ongoing other wise game 
	 * is in waiting (waiting for lanes to be available) 
	 * @param int countOfPlayers : no of players in the game
	 * @param  String[] playerNames : names of all players
	 * @return long gameId : Primary key of game entity
	 */
	private HashMap<String, Integer> _allocateLane(int countOfPlayers, String[] playerNames) {
		HashMap<String,Integer> playerLaneMapping = new HashMap<String,Integer>();
		if(_laneAllocation.checkVacancy(countOfPlayers)) {
			for(int i =0;i<playerNames.length;i++) {
				int laneId = _laneAllocation.assignLane();
				playerLaneMapping.put(playerNames[i], laneId);
			}
			_gameModel.updateStatus(STATUS_ONGOING);
			_gameModel.storeGameDetails();
		}else {
			for(int i =0;i<playerNames.length;i++) {
				playerLaneMapping.put(playerNames[i], 0);
			}
		}
		return playerLaneMapping;
	}

	/** This function saves gameId and lane allocated  of each player in DB 
	 * @param HashMap<String, Integer> playerLaneMappings : no of players in the game
	 * @param long gameId : names of all players
	 */
	private void _storePlayerDetails(HashMap<String, Integer> playerLaneMappings,long gameId) {
		_playerIdMapping = new HashMap<>();
		for(String playerName: playerLaneMappings.keySet()) {
			long playerId = _playerModel.createPlayer(playerName, playerLaneMappings.get(playerName), gameId);
			_playerIdMapping.put(playerName, playerId);
			_playerList.add(playerId);
		}
	}
	
	/** This function initialises the game by setting starting set and activating their status 
	 * (if game is in ongoing status i.e. lane is allocated to each player) for each player
	 */
	public void initialiseGame() {
		boolean playerStatus = PLAYER_INACTIVE;
		if(_gameModel.getGameStatus().equals(STATUS_ONGOING)) {
			playerStatus = PLAYER_ACTIVE;
		}
		for(long playerId:this._playerList) {
			_setActionObj.setPlayerId(playerId);
			long currentSetId = _setActionObj.initialiseNewSet();
			_playerModel.setPlayer(playerId);
			_playerModel.setCurrentSetId(currentSetId);
			_playerModel.setPlayerStatus(playerStatus);
			_playerModel.storePlayerDetails();
		}
	}
	
	
}
