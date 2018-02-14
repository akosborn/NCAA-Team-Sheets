package me.andrewosborn.persistence;

import me.andrewosborn.model.Game;
import me.andrewosborn.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
