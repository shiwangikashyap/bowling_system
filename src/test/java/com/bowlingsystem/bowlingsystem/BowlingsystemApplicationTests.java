package com.bowlingsystem.bowlingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.bowlingsystem.bowlingsystem.model.controller.GameService;
import com.bowlingsystem.bowlingsystem.model.service.allocation.FCFSDataRepository;
import com.bowlingsystem.bowlingsystem.model.service.allocation.LaneAllocation;
import com.bowlingsystem.bowlingsystem.model.service.game.Game;
import com.bowlingsystem.bowlingsystem.model.service.game.GameDataRepository;
import com.bowlingsystem.bowlingsystem.model.service.player.Player;
import com.bowlingsystem.bowlingsystem.model.service.player.PlayerDataRepository;
import com.bowlingsystem.bowlingsystem.model.service.rules.RuleSystem;
import com.bowlingsystem.bowlingsystem.model.service.set.PlayerSetHistory;
import com.bowlingsystem.bowlingsystem.model.service.set.PlayerSetHistoryDataRepository;
import com.bowlingsystem.bowlingsystem.util.param.GameParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.bowlingsystem.bowlingsystem.model.service.rules.RuleConstants.SPARE_BONUS;
import static com.bowlingsystem.bowlingsystem.model.service.rules.RuleConstants.STRIKE_BONUS;
import static com.bowlingsystem.bowlingsystem.model.service.rules.RuleConstants.TOTAL_PINS_RULE;
import static com.bowlingsystem.bowlingsystem.model.service.rules.RuleConstants.PIN_SCORE;
import static com.bowlingsystem.bowlingsystem.model.service.rules.RuleConstants.FINAL_SET_TRIAL_RULE;
import static com.bowlingsystem.bowlingsystem.model.service.rules.RuleConstants.SET_RULE;
import static com.bowlingsystem.bowlingsystem.model.service.rules.RuleConstants.TRIAL_RULE;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class BowlingsystemApplicationTests {

	@Autowired
	private MockMvc _mockMvc;
	
	@MockBean
	private GameDataRepository _gameDao;
	
	@MockBean 
	private PlayerDataRepository _playerDao;
	
	@MockBean
	private PlayerSetHistoryDataRepository _playerSetDao;

	@MockBean
	private LaneAllocation _laneAllocation;
	
	@MockBean
	private RuleSystem _ruleSystem;

	@BeforeEach
	/**
	 * Setting all rules for each case such that lane is always available for the players
	 * and all bowling and set rules are initialised before every api test
	 */
	public void init() {
		Mockito.when(_laneAllocation.assignLane()).thenReturn(1);
		Mockito.when(_laneAllocation.checkVacancy(0)).thenReturn(true);
		Mockito.when(_ruleSystem.getRuleValue(SPARE_BONUS)).thenReturn("5");
		Mockito.when(_ruleSystem.getRuleValue(STRIKE_BONUS)).thenReturn("10");
		Mockito.when(_ruleSystem.getRuleValue(TOTAL_PINS_RULE)).thenReturn("10");
		Mockito.when(_ruleSystem.getRuleValue(PIN_SCORE)).thenReturn("1");
		Mockito.when(_ruleSystem.getRuleValue(FINAL_SET_TRIAL_RULE)).thenReturn("1");
		Mockito.when(_ruleSystem.getRuleValue(SET_RULE)).thenReturn("1");
		Mockito.when(_ruleSystem.getRuleValue(TRIAL_RULE)).thenReturn("1");
	}
	
	@Test
	/**
	 * Case : When game is started 
	 * api : /api/bowling/start
	 * Check status 200
	 * @throws Exception
	 */
	public void testStartGame() throws Exception {

		String[] mockPlayerNames= {"Alex"};
		GameParams params = new GameParams(1,mockPlayerNames);		
		Player mockPlayer = new Player();
		mockPlayer.setPlayerId(1);
		mockPlayer.setAllocatedLane(1);
		mockPlayer.setGameId(1);
		mockPlayer.setName("Alex");

		
		Mockito.when(_playerDao.findById(Mockito.anyLong())).thenReturn(Optional.of(mockPlayer));
		String URI = "/api/bowling/start";
		String inputInJson = this._mapToJson(params);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(URI)
				.accept(MediaType.APPLICATION_JSON).content(inputInJson)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = _mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	
	@Test
	/**
	 * Case: Whether correct game details are returned for given gameId
	 * api : /api/bowling/game/1
	 * Check : Response matches
	 * Check : Response status code is 200
	 * @throws Exception
	 */
	public void testGetGame() throws Exception {
		Game mockGame = new Game();
		mockGame.setGameId(1);
		mockGame.setNoOfPlayers(2);
		mockGame.setGameStatus("ONGOING");
		mockGame.setWinner("Ashima");
		mockGame.setWinnerId(2);
		mockGame.setWinnerScore(20);
		Mockito.when(_gameDao.findById(Mockito.anyLong())).thenReturn(Optional.of(mockGame));
		
		String URI = "/api/bowling/game/1";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				URI).accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = _mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		String expectedJson = this._mapToJson(mockGame);
		String outputInJson = result.getResponse().getContentAsString();
		assertThat(outputInJson).isEqualTo(expectedJson);
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	/**
	 * Case: Whether correct player details are returned for given playerId
	 * api : api/bowling/playerScore/1
	 * Check : Response matches
	 * Check : Response status code is 200
	 * @throws Exception
	 */
	public void testGetPlayerScoreCard() throws Exception {
		Player mockPlayer = new Player();
		mockPlayer.setPlayerId(1);
		mockPlayer.setAllocatedLane(1);
		mockPlayer.setCurrentSet(1);
		mockPlayer.setCurrentSetId(1);
		mockPlayer.setActive(false);
		mockPlayer.setGameId(1);
		mockPlayer.setMissedStrikes(5);
		mockPlayer.setName("Alexa");
		mockPlayer.setTotalScore(10);
		mockPlayer.setTotalStrikes(0);
		
		Mockito.when(_playerDao.findById(Mockito.anyLong())).thenReturn(Optional.of(mockPlayer));
		
		String URI = "/api/bowling/playerScore/1";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				URI).accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = _mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		String expectedJson = this._mapToJson(mockPlayer);
		String outputInJson = result.getResponse().getContentAsString();
		assertThat(outputInJson).isEqualTo(expectedJson);
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	/**
	 * Case : whether correct players of game are returned for gameId 
	 * api : /api/bowling/game/1/players
	 * Check : response status code is 200
	 * Check : response matches
	 * @throws Exception
	 */
	public void testGetGamePlayers() throws Exception {

		Player mockPlayer = new Player();
		mockPlayer.setPlayerId(1);
		mockPlayer.setAllocatedLane(1);
		mockPlayer.setCurrentSet(1);
		mockPlayer.setCurrentSetId(1);
		mockPlayer.setActive(true);
		mockPlayer.setGameId(1);
		mockPlayer.setMissedStrikes(5);
		mockPlayer.setName("Alexa");
		mockPlayer.setTotalScore(10);
		mockPlayer.setTotalStrikes(0);
		
		Player mockPlayer2 = new Player();
		mockPlayer2.setPlayerId(2);
		mockPlayer2.setAllocatedLane(1);
		mockPlayer2.setCurrentSet(1);
		mockPlayer2.setCurrentSetId(1);
		mockPlayer2.setActive(false);
		mockPlayer2.setGameId(1);
		mockPlayer2.setMissedStrikes(5);
		mockPlayer2.setName("Sam");
		mockPlayer2.setTotalScore(10);
		mockPlayer2.setTotalStrikes(0);
		
		List<Player> listOfPlayers= new ArrayList<>();
		listOfPlayers.add(mockPlayer);
		listOfPlayers.add(mockPlayer2);
		Mockito.when(_playerDao.findByGameId(Mockito.anyLong())).thenReturn(listOfPlayers);
		
		String URI = "/api/bowling/game/1/players";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				URI).accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = _mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		String expectedJson = this._mapToJson(listOfPlayers);
		String outputInJson = result.getResponse().getContentAsString();
		assertThat(outputInJson).isEqualTo(expectedJson);
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	/**
	 * Case : On finishing game whether player with max score is declared as winner and the gmae is deactivated
	 * api : /api/bowling/finish
	 * Check : status is 200
	 * Check : response matches
	 * @throws Exception
	 */
	public void testFinishGame() throws Exception {
		Game mockGame = new Game();
		mockGame.setGameId(1);
		mockGame.setNoOfPlayers(2);
		mockGame.setGameStatus("ONGOING");
		Mockito.when(_gameDao.findById(Mockito.anyLong())).thenReturn(Optional.of(mockGame));

		Player mockPlayer = new Player();
		mockPlayer.setPlayerId(1);
		mockPlayer.setAllocatedLane(1);
		mockPlayer.setCurrentSet(1);
		mockPlayer.setCurrentSetId(1);
		mockPlayer.setActive(false);
		mockPlayer.setGameId(1);
		mockPlayer.setMissedStrikes(5);
		mockPlayer.setName("Alexa");
		mockPlayer.setTotalScore(10);
		mockPlayer.setTotalStrikes(0);
		
		Player mockPlayer2 = new Player();
		mockPlayer2.setPlayerId(2);
		mockPlayer2.setAllocatedLane(1);
		mockPlayer2.setCurrentSet(1);
		mockPlayer2.setCurrentSetId(1);
		mockPlayer2.setActive(false);
		mockPlayer2.setGameId(1);
		mockPlayer2.setMissedStrikes(5);
		mockPlayer2.setName("Sam");
		mockPlayer2.setTotalScore(20);
		mockPlayer2.setTotalStrikes(1);
		
		List<Player> listOfPlayers= new ArrayList<>();
		listOfPlayers.add(mockPlayer);
		listOfPlayers.add(mockPlayer2);
		Mockito.when(_playerDao.findByGameId(Mockito.anyLong())).thenReturn(listOfPlayers);
	
		String URI = "/api/bowling/finish";
		String inputInJson = this._mapToJson(mockGame.getGameId());
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(URI)
				.accept(MediaType.APPLICATION_JSON).content(inputInJson)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = _mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		
		mockGame.setWinner("Sam");
		mockGame.setWinnerId(2);
		mockGame.setWinnerScore(20);
		mockGame.setGameStatus("FINISHED");
		
		String expectedJson = this._mapToJson(mockGame);
		String outputInJson = result.getResponse().getContentAsString();
		assertThat(outputInJson).isEqualTo(expectedJson);
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	/**
	 * Case : Whether player plays successfully 
	 * api : /api/bowling/play
	 * Check : response status is 200
	 * @throws Exception
	 */
	public void testPlay() throws Exception {
		Player mockPlayer = new Player();
		mockPlayer.setPlayerId(1);
		mockPlayer.setAllocatedLane(1);
		mockPlayer.setGameId(1);
		mockPlayer.setName("Alex");
		mockPlayer.setActive(true);
		long params = mockPlayer.getPlayerId();
		PlayerSetHistory mockSetHistory = new PlayerSetHistory(mockPlayer.getPlayerId(),'A',1);
		Mockito.when(this._playerSetDao.findById(Mockito.anyLong())).thenReturn(Optional.of(mockSetHistory));
		Mockito.when(_playerDao.findById(Mockito.anyLong())).thenReturn(Optional.of(mockPlayer));
		String URI = "/api/bowling/play";
		String inputInJson = this._mapToJson(params);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(URI)
				.accept(MediaType.APPLICATION_JSON).content(inputInJson)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = _mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		String outputInJson = result.getResponse().getContentAsString();
		System.out.println(outputInJson);
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	/**
	 * Case : Whether inactive player can play game
	 * api : /api/bowling/play
	 * Check : response status is 409
	 * @throws Exception
	 */
	public void testPlayInInvalidStatus() throws Exception {

		Player mockPlayer = new Player();
		mockPlayer.setPlayerId(1);
		mockPlayer.setAllocatedLane(1);
		mockPlayer.setGameId(1);
		mockPlayer.setName("Alex");
		mockPlayer.setActive(false);
		long params = mockPlayer.getPlayerId();
		PlayerSetHistory mockSetHistory = new PlayerSetHistory(mockPlayer.getPlayerId(),'A',1);
		Mockito.when(this._playerSetDao.findById(Mockito.anyLong())).thenReturn(Optional.of(mockSetHistory));
		Mockito.when(_playerDao.findById(Mockito.anyLong())).thenReturn(Optional.of(mockPlayer));
		String URI = "/api/bowling/play";
		String inputInJson = this._mapToJson(params);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(URI)
				.accept(MediaType.APPLICATION_JSON).content(inputInJson)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = _mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}


	@Test
	/**
	 * Case : Whether inactive game can finish
	 * api : /api/bowling/play
	 * Check : response status is 409
	 * @throws Exception
	 */
	public void testFinishGameInInvalidStatus() throws Exception {
		Game mockGame = new Game();
		mockGame.setGameId(1);
		mockGame.setNoOfPlayers(2);
		mockGame.setGameStatus("FINISHED");
		Mockito.when(_gameDao.findById(Mockito.anyLong())).thenReturn(Optional.of(mockGame));
	
		Player mockPlayer = new Player();
		mockPlayer.setPlayerId(1);
		mockPlayer.setAllocatedLane(1);
		mockPlayer.setCurrentSet(1);
		mockPlayer.setCurrentSetId(1);
		mockPlayer.setActive(false);
		mockPlayer.setGameId(1);
		mockPlayer.setMissedStrikes(5);
		mockPlayer.setName("Alexa");
		mockPlayer.setTotalScore(10);
		mockPlayer.setTotalStrikes(0);
		
		Player mockPlayer2 = new Player();
		mockPlayer2.setPlayerId(2);
		mockPlayer2.setAllocatedLane(1);
		mockPlayer2.setCurrentSet(1);
		mockPlayer2.setCurrentSetId(1);
		mockPlayer2.setActive(false);
		mockPlayer2.setGameId(1);
		mockPlayer2.setMissedStrikes(5);
		mockPlayer2.setName("Sam");
		mockPlayer2.setTotalScore(20);
		mockPlayer2.setTotalStrikes(1);
		
		List<Player> listOfPlayers= new ArrayList<>();
		listOfPlayers.add(mockPlayer);
		listOfPlayers.add(mockPlayer2);
		Mockito.when(_playerDao.findByGameId(Mockito.anyLong())).thenReturn(listOfPlayers);
	
		String URI = "/api/bowling/finish";
		String inputInJson = this._mapToJson(mockGame.getGameId());
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(URI)
				.accept(MediaType.APPLICATION_JSON).content(inputInJson)
				.contentType(MediaType.APPLICATION_JSON);
	
		MvcResult result = _mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
	}
	
	/**
	 * Maps an Object into a JSON String.
	 */
	private String _mapToJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}

}
