package com.bowlingsystem.bowlingsystem.util.param;

import java.util.Arrays;

public class GameParams {

	private int no_of_players =0;
	private String [] player_details=null;
	
	public GameParams(int no_of_players, String[] player_details){
		this.no_of_players = no_of_players;
		this.player_details = player_details;
	}

	public int getNo_of_players() {
		return no_of_players;
	}

	public String[] getPlayer_details() {
		return player_details;
	}
	
	@Override
	public String toString() {
		
		return "no = " + this.no_of_players + " players = " + Arrays.toString(this.player_details);
	}
}
