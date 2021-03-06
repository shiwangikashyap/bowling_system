package com.bowlingsystem.bowlingsystem.model.service.game;

public final class GameConstants {
	
	private GameConstants() {
		
	}
	/*
	 * Game is currently being played
	 */
	public static final String STATUS_ONGOING = "ONGOING";
	/*
	 * Game can not be played due to not enough lanes
	 */
	public static final String STATUS_WAITING = "WAITING";
	/*
	 * Game has ended
	 */
	public static final String STATUS_FINISHED = "FINISHED";
}
