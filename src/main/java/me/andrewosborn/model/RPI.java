package me.andrewosborn.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class RPI
{
    public RPI()
    {}

    public RPI(float rpi, Date date)
    {
        this.rpi = rpi;
        this.date = date;
    }

    @Id
    @GeneratedValue
    private long id;

    private float rpi;

    private int rpiRank;

    private Date date;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public float getRpi()
    {
        return rpi;
    }

    public void setRpi(float rpi)
    {
        this.rpi = rpi;
    }

    public int getRpiRank()
    {
        return rpiRank;
    }

    public void setRpiRank(int rpiRank)
    {
        this.rpiRank = rpiRank;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
}
