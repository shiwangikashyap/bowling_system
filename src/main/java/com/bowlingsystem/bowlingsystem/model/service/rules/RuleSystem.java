package com.bowlingsystem.bowlingsystem.model.service.rules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bowlingsystem.bowlingsystem.util.exception.custom.EntityNotFoundException;
@Component
public class RuleSystem {

	@Autowired
	private RulesDataRepository _rulesData;
	
	/*
	 * Singleton class as rules will be same for all instances
	 */
	private RuleSystem() {
		
	}
	
	/* Returns value of rule if rule exists in DB else throws EntityNotFoundException
	 * @param String ruleName : name of the rule whose value should be retrieved
	 * @return String ruleValue : value fo rule for given rule name
	 */
	public String getRuleValue(String ruleName) {
		String ruleValue =  this._rulesData.getRuleValue(ruleName);
		if(ruleValue != null) {
			return ruleValue;
		}else {
			throw new EntityNotFoundException("Rule Not Found , Rule Name :" + ruleName ); 
		}
	}
}
