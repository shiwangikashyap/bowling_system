package com.bowlingsystem.bowlingsystem.model.service.set;

import static com.bowlingsystem.bowlingsystem.model.service.rules.RuleConstants.FINAL_SET_TRIAL_RULE;
import static com.bowlingsystem.bowlingsystem.model.service.rules.RuleConstants.SET_RULE;
import static com.bowlingsystem.bowlingsystem.model.service.rules.RuleConstants.TRIAL_RULE;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bowlingsystem.bowlingsystem.model.service.rules.RuleSystem;
import com.bowlingsystem.bowlingsystem.util.exception.custom.EntityNotFoundException;


@Component
public class SetActions {

	private static int _noOfSets;
	private static int _noOfTrials;
	private long _playerId;
	private long _historyId = 0;
	private static int _finalSetTrial;
	private int _setNumber = 1;
	private int _trial = 0;
	
	
	@Autowired
	private RuleSystem _ruleSystem;
	
	@Autowired
	PlayerSetHistoryDataRepository playerSetHistoryData;
	
	public PlayerSetHistory historyData;
	
	public static int getNoOfSets() {
		return _noOfSets;
	}

	public static void setNoOfSets(int noOfSets) {
		_noOfSets = noOfSets;
	}

	public static int getNoOfTrials() {
		return _noOfTrials;
	}

	public static void setNoOfTrials(int noOfTrials) {
		_noOfTrials = noOfTrials;
	}
	
	
	public int getSetNumber() {
		return _setNumber;
	}

	public void setSetNumber(int setNumber) {
		this._setNumber = setNumber;
	}

	public int getTrial() {
		return _trial;
	}

	public void setTrial(int trial) {
		this._trial = trial;
	}

	/**
	 * Sets all the set related rules 
	 */
	private void _getSetRules() {
		_noOfSets =  Integer.parseInt(this._ruleSystem.getRuleValue(SET_RULE));
		_noOfTrials = Integer.parseInt(this._ruleSystem.getRuleValue(TRIAL_RULE));
		_finalSetTrial = Integer.parseInt(this._ruleSystem.getRuleValue(FINAL_SET_TRIAL_RULE));
	}
	
	/**
	 * Initialises set rules
	 */	
	public void initialiseSetRules() {
		this._getSetRules();
	}

	/**
	 * Initialises new set for player
	 */	
	public long initialiseNewSet() {
			this.initialiseSetRules();
			return this._initialiseSet();
	}

	/**
	 * get setHistory from DB historyId, if not present throws EntityNotFoundException 
	 */
	public void setHistoryData() {
	Optional<PlayerSetHistory> optionalSetHistory = this.playerSetHistoryData.findById(this.getHistoryId());
			if(optionalSetHistory.isPresent()) {
				this.historyData = optionalSetHistory.get();
			}else {
				throw new EntityNotFoundException("Set no." + this.getHistoryId() + " Not Found for player");
			}	
	}
	
	/*
	 * initialise new set
	 * @return long historyId : Id of newly created set
	 */
	private long _initialiseSet() {
		if(_noOfSets > 0 && _noOfTrials > 0) {
			historyData = new PlayerSetHistory(this._playerId,(char)('A'+ _trial),_setNumber);
			this.saveHistoryData();		
			return historyData.getHistoryId();
		}
		return 0;
	}
	
	public void saveHistoryData() {
		this.playerSetHistoryData.save(historyData);
	}

	public long getPlayerId() {
		return _playerId;
	}

	public void setPlayerId(long playerId) {
		this._playerId = playerId;
	}

	public long getHistoryId() {
		return _historyId;
	}

	public void setHistoryId(long historyId) {
		this._historyId = historyId;
	}

	public static int getFinalSetTrial() {
		return _finalSetTrial;
	}

	public static void setFinalSetTrial(int finalSetTrial) {
		_finalSetTrial = finalSetTrial;
	}

}
