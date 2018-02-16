package me.andrewosborn.util;

import me.andrewosborn.exception.InvalidScheduleResultsException;
import me.andrewosborn.model.Game;
import me.andrewosborn.model.Team;

import java.text.DecimalFormat;
import java.util.List;

public class CalculateResult
{
    private static DecimalFormat decimalFormat = new DecimalFormat("#.000000");

    private enum GameSite
    {
        AWAY,
        HOME,
        NEUTRAL
    }

    public static Team calculateRecord(Team team)
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

        try
        {
            List<Game> homeGames = team.getHomeGames();
            List<Game> awayGames = team.getAwayGames();
            for (Game game : homeGames)
            {
                boolean win = false;
                int currentTeamScore = 0;
                int opponentTeamScore = 0;

                currentTeamScore = game.getHomeScore();
                opponentTeamScore = game.getAwayScore();

                if (currentTeamScore > opponentTeamScore)
                    win = true;

                if (game.isNeutralSite())
                {
                    if (win)
                    {
                        wins++;
                        neutralWins++;
                    }
                    else
                    {
                        losses++;
                        neutralLosses++;
                    }
                }
                else if (win)
                {
                    wins++;
                    homeWins++;
                }
                else
                {
                    losses++;
                    homeLosses++;
                }
            }

            for (Game game : awayGames)
            {
                boolean win = false;
                int currentTeamScore = 0;
                int opponentTeamScore = 0;

                opponentTeamScore = game.getHomeScore();
                currentTeamScore = game.getAwayScore();

                if (currentTeamScore > opponentTeamScore)
                    win = true;

                if (game.isNeutralSite())
                {
                    if (win)
                    {
                        wins++;
                        neutralWins++;
                    }
                    else
                    {
                        losses++;
                        neutralLosses++;
                    }
                }
                else if (win)
                {
                    wins++;
                    awayWins++;
                }
                else
                {
                    losses++;
                    awayLosses++;
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
        catch (InvalidScheduleResultsException e)
        {
            e.printStackTrace();
        }

        team.setWinPct(calculateWinPct(wins, losses));

        return team;
    }

    public static float calculateWinPct(int wins, int losses)
    {
        float winPct = (float) wins / (wins + losses);

        if (Float.isNaN(winPct))
            System.out.println(winPct);

        return RoundingUtil.round(winPct, 6);
    }
}
