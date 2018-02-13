package me.andrewosborn.persistence;

import me.andrewosborn.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
