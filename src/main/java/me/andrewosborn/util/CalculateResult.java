package me.andrewosborn.util;

import me.andrewosborn.exception.InvalidScheduleResultsException;
import me.andrewosborn.model.Game;
import me.andrewosborn.model.Team;

import java.util.List;

public class CalculateResult
{
    private enum GameSite
    {
        AWAY,
        HOME,
        NEUTRAL
    }

    public static void calculateWins(Team team) throws InvalidScheduleResultsException
    {
        int wins = 0;
        int losses = 0;
        int homeWins = 0;
        int homeLosses = 0;
        int awayWins = 0;
        int awayLosses = 0;
        int neutralWins = 0;
        int neutralLosses = 0;
        GameSite site = null;

        List<Game> games = team.getGames();
        for (Game game : games)
        {
            boolean win = false;
            int currentTeamScore = 0;
            int opponentTeamScore = 0;
            if (game.getHomeTeam() == team)
            {
                currentTeamScore = game.getHomeScore();
                opponentTeamScore = game.getAwayScore();
                site = GameSite.HOME;
            }
            else if (game.getAwayTeam() == team)
            {
                currentTeamScore = game.getAwayScore();
                opponentTeamScore = game.getHomeScore();
                site = GameSite.AWAY;
            }

            if (game.isNeutralSite())
            {
                site = GameSite.NEUTRAL;
            }

            if (currentTeamScore > opponentTeamScore)
            {
                win = true;
            }

            if (win)
            {
                wins++;

                if (site == GameSite.HOME)
                {
                    homeWins++;
                }
                else if (site == GameSite.AWAY)
                {
                    awayWins++;
                }
                else
                {
                    neutralWins++;
                }
            }
            else
            {
                losses++;

                if (site == GameSite.HOME)
                {
                    homeLosses++;
                }
                else if (site == GameSite.AWAY)
                {
                    awayLosses++;
                }
                else if (site == GameSite.NEUTRAL)
                {
                    neutralLosses++;
                }
            }
        }

        if (wins == (homeWins + awayWins + neutralWins) && losses == (homeLosses + awayLosses + neutralLosses))
        {
            team.setWins(wins);
            team.setLosses(losses);
            team.setHomeWins(homeWins);
            team.setHomeLosses(homeLosses);
            team.setAwayWins(awayWins);
            team.setAwayLosses(awayLosses);
            team.setNeutralWins(neutralWins);
            team.setNeutralLosses(neutralLosses);
        }
        else
        {
            throw new InvalidScheduleResultsException("Total wins != cumulative wins OR total losses != cumulative losses.");
        }
    }
}
