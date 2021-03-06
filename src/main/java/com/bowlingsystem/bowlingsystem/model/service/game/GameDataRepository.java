package com.bowlingsystem.bowlingsystem.model.service.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface GameDataRepository extends JpaRepository<Game,Long> {

}
