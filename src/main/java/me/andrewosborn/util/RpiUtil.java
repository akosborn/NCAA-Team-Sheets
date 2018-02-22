package me.andrewosborn.util;

import me.andrewosborn.model.Game;
import me.andrewosborn.model.Result;
import me.andrewosborn.model.Team;
import me.andrewosborn.model.TeamGame;

/***
 * @see <a href="https://en.wikipedia.org/wiki/Rating_percentage_index#Basketball_formula"</a>
 */
public class RpiUtil
{
    public static float calculateRPI(Team team)
    {
        float WIN_PCT_RPI_PCT = 0.25f;
        float OPP_WIN_PCT_RPI_PCT = 0.50f;
        float OPP_OPP_WIN_PCT_RPI_PCT = 0.25f;

        float weightedWinPct = calculateWeightedWinPct(team);
        float oppAvgWinPct = calculateOpponentsAvgWinPct(team);
        float oppOppWinPct = calculateAvgOppOppWinPct(team);

        return (weightedWinPct * WIN_PCT_RPI_PCT) + (oppAvgWinPct * OPP_WIN_PCT_RPI_PCT) + (oppOppWinPct * OPP_OPP_WIN_PCT_RPI_PCT);
    }

    private static float calculateWeightedWinPct(Team team)
    {
        float HOME_WIN_MULTIPLIER = 0.6f;
        float AWAY_WIN_MULTIPLIER = 1.4f;
        float NEUTRAL_WIN_MULTIPLIER = 1f;
        float HOME_LOSS_MULTIPLIER = 1.4f;
        float AWAY_LOSS_MULTIPLIER = 0.6f;
        float NEUTRAL_LOSS_MULTIPLIER = 1f;

        float weightedHomeWins = team.getHomeWins() * HOME_WIN_MULTIPLIER;
        float weightedAwayWins = team.getAwayWins() * AWAY_WIN_MULTIPLIER;
        float weightedNeutralWins = team.getNeutralWins() * NEUTRAL_WIN_MULTIPLIER;

        float weightedHomeLosses = team.getHomeLosses() * HOME_LOSS_MULTIPLIER;
        float weightedAwayLosses = team.getAwayLosses() * AWAY_LOSS_MULTIPLIER;
        float weightedNeutralLosses = team.getNeutralLosses() * NEUTRAL_LOSS_MULTIPLIER;

        float weightedWins = weightedHomeWins + weightedAwayWins + weightedNeutralWins;
        float weightedLosses = weightedHomeLosses + weightedAwayLosses + weightedNeutralLosses;

        return weightedWins / (weightedWins + weightedLosses);
    }

    private static float calculateOpponentsAvgWinPct(Team team)
    {
        float winPctSum = 0.0f;
        int numGames = team.getGames().size();

        for (TeamGame game : team.getGames())
        {
            Team opp = game.getOpponent();
            int oppWins = opp.getWins();
            int oppLosses = opp.getLosses();
            for (TeamGame oppGame : opp.getGames())
            {
                if (oppGame.getOpponent().getName().equals(team.getName()))
                {
                    if (oppGame.getResult().equals(Result.W))
                        oppWins--;
                    else
                        oppLosses--;
                }
            }
            // calculate win percentage sans current team
            winPctSum += (float) oppWins / (oppWins + oppLosses);
        }

        return winPctSum / numGames;
    }

    private static float calculateAvgOppOppWinPct(Team team)
    {
        float oppOppWinPctSum = 0.0f;
        int totalGames = team.getHomeGames().size() + team.getAwayGames().size();

        for (Game homeGame : team.getHomeGames())
        {
            Team opponent = homeGame.getAwayTeam();
            oppOppWinPctSum += calculateOpponentsAvgWinPct(opponent);
        }

        for (Game awayGame : team.getAwayGames())
        {
            Team opponent = awayGame.getHomeTeam();
            oppOppWinPctSum += calculateOpponentsAvgWinPct(opponent);
        }

        return oppOppWinPctSum / totalGames;
    }
}
