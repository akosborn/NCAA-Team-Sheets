package me.andrewosborn.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TeamGame
{
    public TeamGame()
    {}

    @Id
    @GeneratedValue
    private Long id;
    
    private Team teamOne;

    private Team opponent;

    private int oneScore;

    private int opponentScore;

    private Site site;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Team getTeamOne()
    {
        return teamOne;
    }

    public void setTeamOne(Team teamOne)
    {
        this.teamOne = teamOne;
    }

    public Team getOpponent()
    {
        return opponent;
    }

    public void setOpponent(Team opponent)
    {
        this.opponent = opponent;
    }

    public int getOneScore()
    {
        return oneScore;
    }

    public void setOneScore(int oneScore)
    {
        this.oneScore = oneScore;
    }

    public int getOpponentScore()
    {
        return opponentScore;
    }

    public void setOpponentScore(int opponentScore)
    {
        this.opponentScore = opponentScore;
    }

    public Site getSite()
    {
        return site;
    }

    public void setSite(Site site)
    {
        this.site = site;
    }
}
