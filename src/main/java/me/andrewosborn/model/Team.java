package me.andrewosborn.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
public class Team
{
    public Team(){}

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String urlName;
    private String sportsReferenceName;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JsonIgnoreProperties(value = "teams", allowSetters = true)
    private Conference conference;

    @OneToMany(mappedBy = "homeTeam", cascade = {CascadeType.ALL})
    private List<Game> homeGames;

    @OneToMany(mappedBy = "awayTeam", cascade = {CascadeType.ALL})
    private List<Game> awayGames;

    @Transient
    private Map<Integer, Set<Game>> homeQuadrantGames;

    @Transient
    private Map<Integer, Set<Game>> awayQuadrantGames;

    @Transient
    private Map<Integer, Set<Game>> neutralQuadrantGames;

    private int quadOneWins;

    private int quadTwoWins;

    private int quadThreeWins;

    private int quadFourWins;

    private int quadOneLosses;

    private int quadTwoLosses;

    private int quadThreeLosses;

    private int quadFourLosses;

    private int wins;

    private int losses;

    private int homeWins;

    private int homeLosses;

    private int awayWins;

    private int awayLosses;

    private int neutralWins;

    private int neutralLosses;

    private float rpi;

    private int rpiRank;

    private float winPct;

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

    public List<Game> getHomeGames()
    {
        return homeGames;
    }

    public void setHomeGames(List<Game> homeGames)
    {
        this.homeGames = homeGames;
    }

    public List<Game> getAwayGames()
    {
        return awayGames;
    }

    public void setAwayGames(List<Game> awayGames)
    {
        this.awayGames = awayGames;
    }

    public int getWins()
    {
        return wins;
    }

    public void setWins(int wins)
    {
        this.wins = wins;
    }

    public int getLosses()
    {
        return losses;
    }

    public void setLosses(int losses)
    {
        this.losses = losses;
    }

    public Conference getConference()
    {
        return conference;
    }

    public void setConference(Conference conference)
    {
        this.conference = conference;
    }

    public float getWinPct()
    {
        return winPct;
    }

    public void setWinPct(float winPct)
    {
        this.winPct = winPct;
    }

    public int getHomeWins()
    {
        return homeWins;
    }

    public void setHomeWins(int homeWins)
    {
        this.homeWins = homeWins;
    }

    public int getHomeLosses()
    {
        return homeLosses;
    }

    public void setHomeLosses(int homeLosses)
    {
        this.homeLosses = homeLosses;
    }

    public int getAwayWins()
    {
        return awayWins;
    }

    public void setAwayWins(int awayWins)
    {
        this.awayWins = awayWins;
    }

    public int getAwayLosses()
    {
        return awayLosses;
    }

    public void setAwayLosses(int awayLosses)
    {
        this.awayLosses = awayLosses;
    }

    public int getNeutralWins()
    {
        return neutralWins;
    }

    public void setNeutralWins(int neutralWins)
    {
        this.neutralWins = neutralWins;
    }

    public int getNeutralLosses()
    {
        return neutralLosses;
    }

    public void setNeutralLosses(int neutralLosses)
    {
        this.neutralLosses = neutralLosses;
    }

    public String getSportsReferenceName()
    {
        return sportsReferenceName;
    }

    public void setSportsReferenceName(String sportsReferenceName)
    {
        this.sportsReferenceName = sportsReferenceName;
    }

    public int getRpiRank()
    {
        return rpiRank;
    }

    public void setRpiRank(int rpiRank)
    {
        this.rpiRank = rpiRank;
    }

    public float getRpi()
    {
        return rpi;
    }

    public void setRpi(float rpi)
    {
        this.rpi = rpi;
    }

    public Map<Integer, Set<Game>> getHomeQuadrantGames()
    {
        return homeQuadrantGames;
    }

    public void setHomeQuadrantGames(Map<Integer, Set<Game>> homeQuadrantGames)
    {
        this.homeQuadrantGames = homeQuadrantGames;
    }

    public Map<Integer, Set<Game>> getAwayQuadrantGames()
    {
        return awayQuadrantGames;
    }

    public void setAwayQuadrantGames(Map<Integer, Set<Game>> awayQuadrantGames)
    {
        this.awayQuadrantGames = awayQuadrantGames;
    }

    public Map<Integer, Set<Game>> getNeutralQuadrantGames()
    {
        return neutralQuadrantGames;
    }

    public void setNeutralQuadrantGames(Map<Integer, Set<Game>> neutralQuadrantGames)
    {
        this.neutralQuadrantGames = neutralQuadrantGames;
    }

    public int getQuadOneWins()
    {
        return quadOneWins;
    }

    public void setQuadOneWins(int quadOneWins)
    {
        this.quadOneWins = quadOneWins;
    }

    public int getQuadTwoWins()
    {
        return quadTwoWins;
    }

    public void setQuadTwoWins(int quadTwoWins)
    {
        this.quadTwoWins = quadTwoWins;
    }

    public int getQuadThreeWins()
    {
        return quadThreeWins;
    }

    public void setQuadThreeWins(int quadThreeWins)
    {
        this.quadThreeWins = quadThreeWins;
    }

    public int getQuadFourWins()
    {
        return quadFourWins;
    }

    public void setQuadFourWins(int quadFourWins)
    {
        this.quadFourWins = quadFourWins;
    }

    public int getQuadOneLosses()
    {
        return quadOneLosses;
    }

    public void setQuadOneLosses(int quadOneLosses)
    {
        this.quadOneLosses = quadOneLosses;
    }

    public int getQuadTwoLosses()
    {
        return quadTwoLosses;
    }

    public void setQuadTwoLosses(int quadTwoLosses)
    {
        this.quadTwoLosses = quadTwoLosses;
    }

    public int getQuadThreeLosses()
    {
        return quadThreeLosses;
    }

    public void setQuadThreeLosses(int quadThreeLosses)
    {
        this.quadThreeLosses = quadThreeLosses;
    }

    public int getQuadFourLosses()
    {
        return quadFourLosses;
    }

    public void setQuadFourLosses(int quadFourLosses)
    {
        this.quadFourLosses = quadFourLosses;
    }
}
