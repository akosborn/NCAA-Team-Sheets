package me.andrewosborn.util;

import me.andrewosborn.model.*;

import java.util.*;

public class TeamControllerUtil
{
    public static class RPIComparator implements Comparator<TeamGame>
    {
        @Override
        public int compare(TeamGame o1, TeamGame o2)
        {
            int o1RpiRank = o1.getOpponent().getRpiRank();
            int o2RpiRank = o2.getOpponent().getRpiRank();
            return o1RpiRank > o2RpiRank ? 1 : o1RpiRank == o2RpiRank ? 0 : -1;
        }
    }

    public static List<TeamGame> setTeamGameQuadrant(List<TeamGame> games)
    {
        for (TeamGame game : games)
        {
            int oppRpiRank = game.getOpponent().getRpiRank();
            Site site = game.getSite();

            Quadrant quadrant = null;
            int QUAD_TWO_HOME_MIN = 31;
            int QUAD_TWO_AWAY_MIN = 76;
            int QUAD_TWO_NEUTRAL_MIN = 51;
            int QUAD_THREE_HOME_MIN = 76;
            int QUAD_THREE_AWAY_MIN = 136;
            int QUAD_THREE_NEUTRAL_MIN = 101;
            int QUAD_FOUR_AWAY_MIN = 241;
            int QUAD_FOUR_NEUTRAL_MIN = 201;
            int QUAD_FOUR_HOME_MIN = 161;
            if (site.equals(Site.HOME))
                quadrant = getQuadrantClassification(oppRpiRank, QUAD_TWO_HOME_MIN, QUAD_THREE_HOME_MIN, QUAD_FOUR_HOME_MIN);
            else if (site.equals(Site.NEUTRAL))
                quadrant = getQuadrantClassification(oppRpiRank, QUAD_TWO_NEUTRAL_MIN, QUAD_THREE_NEUTRAL_MIN, QUAD_FOUR_NEUTRAL_MIN);
            else if (site.equals(Site.AWAY))
                quadrant = getQuadrantClassification(game.getOpponent().getRpiRank(), QUAD_TWO_AWAY_MIN, QUAD_THREE_AWAY_MIN, QUAD_FOUR_AWAY_MIN);

            game.setQuadrant(quadrant);
        }

        // sort by rpi from highest to lowest
        Collections.sort(games, new RPIComparator());

        return games;
    }

    private static Quadrant getQuadrantClassification(int oppRpi, int quadTwoMin, int quadThreeMin, int quadFourMin)
    {
        Quadrant quadrant = null;

        if (oppRpi < quadTwoMin)
            quadrant = Quadrant.ONE;
        else if (oppRpi >= quadTwoMin && oppRpi < quadThreeMin)
            quadrant = Quadrant.TWO;
        else if (oppRpi >= quadThreeMin && oppRpi < quadFourMin)
            quadrant = Quadrant.THREE;
        else if (oppRpi >= quadFourMin)
            quadrant = Quadrant.FOUR;

        return quadrant;
    }

    private static int[] getQuadrantRecord(List<TeamGame> games, Quadrant quadrant)
    {
        int wins = 0;
        int losses = 0;

        for (TeamGame game : games)
        {
            if (game.getQuadrant().equals(quadrant))
            {
                if (game.getResult().equals(Result.W))
                    wins++;
                else
                    losses++;
            }
        }

        return new int[]{wins, losses};
    }

    public static Team setQuadrantRecords(Team team)
    {
        List<TeamGame> teamGames = TeamControllerUtil.setTeamGameQuadrant(team.getGames());
        team.setGames(teamGames);

        int[] quadOneRecord = getQuadrantRecord(team.getGames(), Quadrant.ONE);
        team.setQuadOneWins(quadOneRecord[0]);
        team.setQuadOneLosses(quadOneRecord[1]);
        int[] quadTwoRecord = getQuadrantRecord(team.getGames(), Quadrant.TWO);
        team.setQuadTwoWins(quadTwoRecord[0]);
        team.setQuadTwoLosses(quadTwoRecord[1]);
        int[] quadThreeRecord = getQuadrantRecord(team.getGames(), Quadrant.THREE);
        team.setQuadThreeWins(quadThreeRecord[0]);
        team.setQuadThreeLosses(quadThreeRecord[1]);
        int[] quadFourRecord = getQuadrantRecord(team.getGames(), Quadrant.FOUR);
        team.setQuadFourWins(quadFourRecord[0]);
        team.setQuadFourLosses(quadFourRecord[1]);

        return team;
    }

    public static Team calculateRecord(Team team)
    {
        int homeWins = 0;
        int homeLosses = 0;
        int awayWins = 0;
        int awayLosses = 0;
        int neutralWins = 0;
        int neutralLosses = 0;

        for (TeamGame game : team.getGames())
        {
            Result result = game.getResult();
            Site site = game.getSite();

            if (site.equals(Site.HOME))
            {
                if (result.equals(Result.W))
                {
                    homeWins++;
                }
                else
                {
                    homeLosses++;
                }
            }
            else if (site.equals(Site.AWAY))
            {
                if (result.equals(Result.W))
                    awayWins++;
                else
                    awayLosses++;
            }
            else
            {
                if (result.equals(Result.W))
                    neutralWins++;
                else
                    neutralLosses++;
            }
        }

        int wins = homeWins + awayWins + neutralWins;
        int losses = homeLosses + awayLosses + neutralLosses;
        team.setWins(wins);
        team.setLosses(losses);
        team.setHomeWins(homeWins);
        team.setHomeLosses(homeLosses);
        team.setAwayWins(awayWins);
        team.setAwayLosses(awayLosses);
        team.setNeutralWins(neutralWins);
        team.setNeutralLosses(neutralLosses);
        team.setWinPct(calculateWinPct(wins, losses));

        return team;
    }

    private static float calculateWinPct(int wins, int losses)
    {
        float winPct = (float) wins / (wins + losses);
        int DECIMAL_PLACES = 10;
        return RoundingUtil.round(winPct, DECIMAL_PLACES);
    }

    public static List<TeamGame> addToTeamSchedule(Date date, List<TeamGame> games, Team opponent, int opponentScore,
                                                   int teamOneScore, Site site)
    {
        Result result = null;
        if (teamOneScore > opponentScore)
            result = Result.W;
        else
            result = Result.L;
        TeamGame teamGame = new TeamGame(date, opponent, teamOneScore, opponentScore, site, result);
        games.add(teamGame);

        return games;
    }
}
