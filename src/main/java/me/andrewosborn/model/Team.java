package me.andrewosborn.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Team
{
    public Team(){}

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String urlName;

    @OneToOne
    private Conference conference;

    @OneToMany
    private List<Game> games;

    public Team(String name, String urlName)
    {
        this.name = name;
        this.urlName = urlName;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUrlName()
    {
        return urlName;
    }

    public void setUrlName(String urlName)
    {
        this.urlName = urlName;
    }

    public List<Game> getGames()
    {
        return games;
    }

    public void setGames(List<Game> games)
    {
        this.games = games;
    }

    public Conference getConference()
    {
        return conference;
    }

    public void setConference(Conference conference)
    {
        this.conference = conference;
    }
}
