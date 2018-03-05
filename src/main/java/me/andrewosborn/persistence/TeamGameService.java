package me.andrewosborn.persistence;

import me.andrewosborn.model.TeamGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TeamGameService
{
    private TeamGameRepository repository;

    @Autowired
    public TeamGameService(TeamGameRepository repository)
    {
        this.repository = repository;
    }

    public List<TeamGame> getAllByDate(Date date)
    {
        return repository.findAllByDateOrderByDateDesc(date);
    }

    public TeamGame getByDateAndUrlName(Date date, String urlName)
    {
        return repository.findByDateAndOpponentUrlName(date, urlName);
    }

    public TeamGame save(TeamGame teamGame)
    {
        return repository.save(teamGame);
    }
}
