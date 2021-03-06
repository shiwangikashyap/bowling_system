package com.bowlingsystem.bowlingsystem.model.service.allocation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface FCFSDataRepository extends JpaRepository<Lane,Integer> {

    @Query(value = "select 1 from lane having sum(vacancy) >= :noOfPlayer", nativeQuery = true)
    Integer isVacant(@Param("noOfPlayer") Integer noOfPlayer);
    
    @Query(value = "select * from lane where vacancy > :minVacancy order by lane asc LIMIT 1", nativeQuery = true)
    Lane getVacantLane(@Param("minVacancy") Integer minVacancy);
}
