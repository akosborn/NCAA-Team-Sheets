package me.andrewosborn.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    @OneToOne(cascade = {CascadeType.ALL})
    @JsonIgnoreProperties(value = "teams", allowSetters = true)
    private Conference conference;

    @OneToMany
    private List<Game> games;

    private float rpi;

    private float winPct;

    private float oppWinPct;

    private float oppOppWinPct;

    public Team(String name, String urlName, Conference conference)
    {
        this.name = name;
        this.urlName = urlName;
        this.conference = conference;
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

    public float getRpi()
    {
        return rpi;
    }

    public void setRpi(int rpi)
    {
        this.rpi = rpi;
    }

    public float getWinPct()
    {
        return winPct;
    }

    public void setWinPct(float winPct)
    {
        this.winPct = winPct;
    }

    public float getOppWinPct()
    {
        return oppWinPct;
    }

    public void setOppWinPct(float oppWinPct)
    {
        this.oppWinPct = oppWinPct;
    }

    public float getOppOppWinPct()
    {
        return oppOppWinPct;
    }

    public void setOppOppWinPct(float oppOppWinPct)
    {
        this.oppOppWinPct = oppOppWinPct;
    }
}
