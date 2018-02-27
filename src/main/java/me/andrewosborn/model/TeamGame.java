package me.andrewosborn.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@Entity
public class TeamGame
{
    public TeamGame()
    {}

    public TeamGame(Date date, Team opponent, int oneScore, int opponentScore, Site site, Result result)
    {
        this.date = date;
        this.opponent = opponent;
        this.oneScore = oneScore;
        this.opponentScore = opponentScore;
        this.site = site;
        this.result = result;
    }

    @Id
    @GeneratedValue
    private Long id;

    private Date date;

    @ManyToOne
    @JsonIgnoreProperties("games")
    private Team opponent;

    private int oneScore;

    private int opponentScore;

    private Site site;

    private Result result;

    private Quadrant quadrant;

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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

    public Result getResult()
    {
        return result;
    }

    public void setResult(Result result)
    {
        this.result = result;
    }

    public Quadrant getQuadrant()
    {
        return quadrant;
    }

    public void setQuadrant(Quadrant quadrant)
    {
        this.quadrant = quadrant;
    }
}
