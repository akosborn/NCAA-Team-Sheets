package me.andrewosborn.persistence;

import me.andrewosborn.model.Game;
import me.andrewosborn.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GameService
{
    private GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository)
    {
        this.gameRepository = gameRepository;
    }

    public Game save(Game game)
    {
        return gameRepository.save(game);
    }

    public List<Game> getByTeam(Team team)
    {
        return gameRepository.findAllByAwayTeamOrHomeTeam(team, team);
    }

    public List<Game> getHomeGamesByTeam(Team team)
    {
        return gameRepository.findAllByHomeTeam(team);
    }

    public List<Game> getAwayGamesByTeam(Team team)
    {
        return gameRepository.findAllByAwayTeam(team);
    }

    public Game getByTeams(Team teamOne, Team teamTwo)
    {
        return gameRepository.findByAwayTeamAndHomeTeam(teamOne, teamTwo);
    }

    public List<Game> getByTeamAndDate(Team team, Date date)
    {
        return gameRepository.findByDateAndHomeTeamOrDateAndAwayTeam(date, team, date, team);
    }

    public Game getByHomeTeamAndAwayTeamAndDate(Team homeTeam, Team awayTeam, Date date)
    {
        return gameRepository.findByHomeTeamAndAwayTeamAndDate(homeTeam, awayTeam, date);
    }

    public List<Game> getAll()
    {
        return gameRepository.findAll();
    }
}
