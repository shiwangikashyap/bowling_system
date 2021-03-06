package com.bowlingsystem.bowlingsystem.model.service.rules;

/*
 * Contains constants of all rule names 
 * 
 */
public final class RuleConstants {

	private RuleConstants() {
		
	}
	
	/**
	 * No of max trials in each set
	 */
	public static final String TRIAL_RULE = "MAX_SET_TRIAL_COUNT";
	/*
	 * Max Set for each player
	 */
	public static final String SET_RULE = "MAX_SET_COUNT";
	/*
	 * No of additional trials in final set player if rolls a spare or strike 
	 */
	public static final String FINAL_SET_TRIAL_RULE = "ADDITIONAL_SET_TRIAL_RULE";
	/*
	 * Scores per knocked pin
	 */
	public static final String PIN_SCORE = "SINGLE_PIN_SCORE";
	/*
	 * Additional score for bonus
	 */
	public static final String SPARE_BONUS = "SPARE_BONUS";
	/*
	 * Additional score for strike
	 */
	public static final String STRIKE_BONUS = "STRIKE_BONUS";
	/*
	 * Total Pins in beginning of every set
	 */
	public static final String TOTAL_PINS_RULE = "TOTAL_PINS_RULE";
}
