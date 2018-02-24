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

    public List<Team> saveAll(List<Team> teams)
    {
        return teamRepository.save(teams);
    }

    public List<Team> getAll()
    {
        return teamRepository.findAll();
    }

    public Team getByUrlName(String urlName)
    {
        return teamRepository.findOneByUrlName(urlName);
    }

    public Team getByName(String name)
    {
        return teamRepository.findOneByName(name);
    }

    public Team save(Team team)
    {
        return teamRepository.save(team);
    }

    public List<Team> getAllOrderByRpiDesc()
    {
        return teamRepository.findAllByOrderByRpiDesc();
    }

    public List<Team> getAllOrderByName()
    {
        return teamRepository.findAllByOrderByNameAsc();
    }
}
