package me.andrewosborn.persistence;

import me.andrewosborn.model.Game;
import me.andrewosborn.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>
{
    List<Game> findAllByAwayTeamOrHomeTeam(Team awayTeam, Team homeTeam);

    List<Game> findAllByHomeTeam(Team team);

    List<Game> findAllByAwayTeam(Team team);

    Game findByAwayTeamAndHomeTeam(Team awayTeam, Team homeTeam);

    List<Game> findByDateAndHomeTeamOrDateAndAwayTeam(Date date, Team awayTeam, Date sameDate, Team homeTeam);
}
