package com.bowlingsystem.bowlingsystem.model.service.allocation;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
/**
 *Entity for lane
 * @author Shiwangi
 *
 */
public class Lane {

    @Id
	private int lane;
	private int vacancy;
	
	Lane(){
		
	}
	
	public int getLane() {
		return lane;
	}
	public void setLane(int lane) {
		this.lane = lane;
	}
	public int getVacancy() {
		return vacancy;
	}
	public void setVacancy(int vacancy) {
		this.vacancy = vacancy;
	}
}
