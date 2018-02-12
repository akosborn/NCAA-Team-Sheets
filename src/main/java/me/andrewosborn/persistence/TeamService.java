package me.andrewosborn.persistence;

import me.andrewosborn.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService
{
    private TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository)
    {
        this.teamRepository = teamRepository;
    }

    public List<Team> save(List<Team> teams)
    {
        return teamRepository.save(teams);
    }
}
