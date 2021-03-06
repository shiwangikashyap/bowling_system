package com.bowlingsystem.bowlingsystem.model.service.rules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface RulesDataRepository extends JpaRepository<Rules,String>  {

    @Query(value = "select rule_value from rules where rule_name = :ruleName", nativeQuery = true)
    String getRuleValue(@Param("ruleName") String ruleName);
}
