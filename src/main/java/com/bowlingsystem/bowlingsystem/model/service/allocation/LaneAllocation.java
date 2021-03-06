package com.bowlingsystem.bowlingsystem.model.service.allocation;
/**
 * Defines all methods that must be implemented by any lane allocation algorithm
 * @author Shiwangi
 *
 */
public interface LaneAllocation {

	public boolean checkVacancy(int numberOfPlayers);
	
	public int assignLane();
	
	public void releaseLane(int laneNo);
}
