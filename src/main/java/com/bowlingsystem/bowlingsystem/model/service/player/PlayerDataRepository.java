package com.bowlingsystem.bowlingsystem.model.service.player;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PlayerDataRepository extends JpaRepository <Player,Long>{
    List<Player> findByGameId(long gameId);
}
