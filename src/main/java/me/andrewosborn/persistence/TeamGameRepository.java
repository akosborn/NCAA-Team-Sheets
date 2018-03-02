package me.andrewosborn.persistence;

import me.andrewosborn.model.TeamGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TeamGameRepository extends JpaRepository<TeamGame, Long>
{
    List<TeamGame> findAllByDateOrderByDateDesc(Date date);

    TeamGame findByDateAndOpponentUrlName(Date date, String urlName);
}
