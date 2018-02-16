package me.andrewosborn.util;

import me.andrewosborn.model.Game;
import me.andrewosborn.model.Team;

/***
 * 1) calculate winning percentages
 */
public class RPICalculation
{
    private static float HOME_WIN_MULTIPLIER = 0.6f;
    private static float AWAY_WIN_MULTIPLIER = 1.4f;
    private static float NEUTRAL_WIN_MULTIPLIER = 1f;
    private static float HOME_LOSS_MULTIPLIER = 1.4f;
    private static float AWAY_LOSS_MULTIPLIER = 0.6f;
    private static float NEUTRAL_LOSS_MULTIPLIER = 1f;

    private static float WIN_PCT_RPI_PCT = 0.25f;
    private static float OPP_WIN_PCT_RPI_PCT = 0.50f;
    private static float OPP_OPP_WIN_PCT_RPI_PCT = 0.25f;

    public static float calculateRPI(Team team)
    {
        float weightedWinPct = team.getWeightedWinPct();
        float oppAvgWinPct = team.getOppWinPct();
        float oppOppWinPct = team.getOppOppWinPct();

        return (weightedWinPct * WIN_PCT_RPI_PCT) + (oppAvgWinPct * OPP_WIN_PCT_RPI_PCT) + (oppOppWinPct * OPP_OPP_WIN_PCT_RPI_PCT);
    }

    public static float calculateWeightedWinPct(Team team)
    {
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

    public static float calculateOpponentsAvgWinPct(Team team)
    {
        float oppAvgWinPct = 0.0f;
        int totalGames = team.getHomeGames().size() + team.getAwayGames().size();

        for (Game homeGame : team.getHomeGames())
        {
            Team opponent = homeGame.getAwayTeam();
            oppAvgWinPct += opponent.getWinPct();
        }

        for (Game awayGame : team.getAwayGames())
        {
            Team opponent = awayGame.getHomeTeam();
            oppAvgWinPct += opponent.getWinPct();
        }

        return oppAvgWinPct / totalGames;
    }

    public static float calculateAvgOppOppWinPct(Team team)
    {
        float oppOppAvgWinPct = 0.0f;
        int totalGames = team.getHomeGames().size() + team.getAwayGames().size();

        for (Game homeGame : team.getHomeGames())
        {
            Team opponent = homeGame.getAwayTeam();
            oppOppAvgWinPct += opponent.getOppWinPct();
        }

        for (Game awayGame : team.getAwayGames())
        {
            Team opponent = awayGame.getHomeTeam();
            oppOppAvgWinPct += opponent.getOppOppWinPct();
        }

        return oppOppAvgWinPct / totalGames;
    }
}
