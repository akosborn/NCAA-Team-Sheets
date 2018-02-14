package me.andrewosborn.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Game
{
    public Game()
    {}

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JsonIgnoreProperties(value = {"homeGames", "awayGames"}, allowSetters = true)
    private Team homeTeam;

    @ManyToOne
    @JsonIgnoreProperties(value = {"awayGames", "homeGames"}, allowSetters = true)
    private Team awayTeam;

    private int homeScore;

    private int awayScore;

    private Date date;

    private boolean neutralSite;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public Team getHomeTeam()
    {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam)
    {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam()
    {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam)
    {
        this.awayTeam = awayTeam;
    }

    public int getHomeScore()
    {
        return homeScore;
    }

    public void setHomeScore(int homeScore)
    {
        this.homeScore = homeScore;
    }

    public int getAwayScore()
    {
        return awayScore;
    }

    public void setAwayScore(int awayScore)
    {
        this.awayScore = awayScore;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public boolean isNeutralSite()
    {
        return neutralSite;
    }

    public void setNeutralSite(boolean neutralSite)
    {
        this.neutralSite = neutralSite;
    }
}
