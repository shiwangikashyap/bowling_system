package com.bowlingsystem.bowlingsystem.model.service.allocation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bowlingsystem.bowlingsystem.util.exception.custom.EntityNotFoundException;

@Component
/**
 *Implements first come first serve algorithm where the first vacant lane is allocated to the player
 * @author Shiwangi
 *
 */
public class FCFSAllocation implements LaneAllocation {

	@Autowired
	private FCFSDataRepository _fcfsDataObject;
	
	private Lane _laneObj;
	
	@Override
	/*
	 * @param boolean : if vacant lane is available for each player of a game then returns true else false
	 */
	public boolean checkVacancy(int numberOfPlayers) {
		// TODO Auto-generated method stub
		if(this._fcfsDataObject.isVacant(numberOfPlayers) != null)
			return true;
		return false;
	}

	@Override
	/*
	 * Checks for vacant lane for player, occupies it and updates lane vacancy in DB
	 * @param int laneId : lane no that should be allocated to the player
	 */
	public int assignLane() {
		// TODO Auto-generated method stub
		this._getVacantLane();
		this._occupyLane();
		this.storeLaneDetails();
		return this.getLaneId();
	}
	
	/*
	 * Checks for vacant lane for player
	 */
	private void _getVacantLane() {
		_laneObj =  this._fcfsDataObject.getVacantLane(0);
	}
	
	/*
	 * Books lane for player decreases vacancy
	 */
	private void _occupyLane() {
		_laneObj.setVacancy(_laneObj.getVacancy() - 1);

	}

	@Override
	/* Releases lane and updates vacancy in DB, lane can now be allocated to other player
	 * @param int laneNo : number of lane that has been released
	 */
	public void releaseLane(int laneNo) {
		this.setLaneDetails(laneNo);
		this._releaseLane();
		this.storeLaneDetails();
	}
	
	/*
	 * release lane for player increases vacancy of lane
	 */
	private void _releaseLane() {
		this._laneObj.setVacancy(this._laneObj.getVacancy()+1);
	}
	
	/*
	 * store lane details in DB
	 */
	public void storeLaneDetails() {
		this._fcfsDataObject.save(_laneObj);
	}
	
	public int getLaneId() {
		return _laneObj.getLane();

	}
	
	/*
	 * get lane details by id from db throws EntityNotFoundException if not found
	 * @param long laneNo
	 */
	public void setLaneDetails(int laneNo) {
		Optional<Lane> optionalLane = this._fcfsDataObject.findById(laneNo);
		if(optionalLane.isPresent()) {
			this._laneObj	=	optionalLane.get();	
		}else {
			throw new EntityNotFoundException("Lane no : " + laneNo + " does not exist");
		}
		
	}

}
