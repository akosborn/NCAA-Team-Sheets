package me.andrewosborn.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
public class Conference
{
    public Conference(){}

    @Id
    @GeneratedValue
    private long id;

    private String name;
    private String urlName;

    @OneToMany
    @JsonIgnoreProperties(value = "conference", allowSetters = true)
    private List<Team> teams;

    public Conference(String name, String urlName)
    {
        this.name = name;
        this.urlName = urlName;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
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

    public List<Team> getTeams()
    {
        return teams;
    }

    public void setTeams(List<Team> teams)
    {
        this.teams = teams;
    }
}
