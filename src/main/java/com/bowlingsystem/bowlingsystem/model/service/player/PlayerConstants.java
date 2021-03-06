package com.bowlingsystem.bowlingsystem.model.service.player;

public final class PlayerConstants {

	private PlayerConstants() {
		
	}

	/**
	 * Player is currently playing
	 */
	public static final boolean PLAYER_ACTIVE = true;
	
	/**
	 * Player is not playing currently (either player has finished playing or
	 * game did not start due to lane vacancy rule)
	 */
	public static final boolean PLAYER_INACTIVE = false;

}
