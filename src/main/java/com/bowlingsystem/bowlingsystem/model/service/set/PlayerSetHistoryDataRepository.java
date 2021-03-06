package com.bowlingsystem.bowlingsystem.model.service.set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerSetHistoryDataRepository extends JpaRepository<PlayerSetHistory,Long>  {
	
}
