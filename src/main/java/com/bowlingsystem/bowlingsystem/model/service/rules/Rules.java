package com.bowlingsystem.bowlingsystem.model.service.rules;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
/**
 *Entity for rules
 * @author Shiwangi
 *
 */
public class Rules {

	@Id
    @Column(nullable = false)
	private String rule_name;
    @Column(nullable = false)
	private String rule_value;
	public String getRule_name() {
		return rule_name;
	}
	public void setRule_name(String rule_name) {
		this.rule_name = rule_name;
	}
	public String getRule_value() {
		return rule_value;
	}
	public void setRule_value(String rule_value) {
		this.rule_value = rule_value;
	}
	
}
