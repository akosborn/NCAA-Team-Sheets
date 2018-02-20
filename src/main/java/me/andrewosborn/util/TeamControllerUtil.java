package me.andrewosborn.util;

import me.andrewosborn.model.Game;
import me.andrewosborn.model.Team;

import java.util.*;

public class TeamControllerUtil
{
    private static Integer QUAD_ONE_KEY = 1;
    private static Integer QUAD_TWO_KEY = 2;
    private static Integer QUAD_THREE_KEY = 3;
    private static Integer QUAD_FOUR_KEY = 4;

    private int quadOneWinCount = 0;
    private int quadOneLossCount = 0;
    private int quadTwoWinCount = 0;
    private int quadTwoLossCount = 0;
    private int quadThreeWinCount = 0;
    private int quadThreeLossCount = 0;
    private int quadFourWinCount = 0;
    private int quadFourLossCount = 0;

    static class HomeRpiComparator implements Comparator<Game>
    {
        @Override
        public int compare(Game o1, Game o2)
        {
            int o1Rpi = o1.getAwayTeam().getRpiRank();
            int o2Rpi = o2.getAwayTeam().getRpiRank();
            return o1Rpi > o2Rpi ? 1 : o1Rpi == o2Rpi ? 0 : -1;
        }
    }

    static class AwayRpiComparator implements Comparator<Game>
    {
        @Override
        public int compare(Game o1, Game o2)
        {
            int o1Rpi = o1.getHomeTeam().getRpiRank();
            int o2Rpi = o2.getHomeTeam().getRpiRank();
            return o1Rpi > o2Rpi ? 1 : o1Rpi == o2Rpi ? 0 : -1;
        }
    }

    public Team setQuadrants(Team team)
    {
        int QUAD_TWO_HOME_MIN = 31;
        int QUAD_TWO_AWAY_MIN = 76;
        int QUAD_TWO_NEUTRAL_MIN = 51;
        int QUAD_THREE_HOME_MIN = 76;
        int QUAD_THREE_AWAY_MIN = 136;
        int QUAD_THREE_NEUTRAL_MIN = 101;
        int QUAD_FOUR_AWAY_MIN = 241;
        int QUAD_FOUR_NEUTRAL_MIN = 201;

        Map<Integer, Set<Game>> neutralGamesMap = new HashMap<>();
        neutralGamesMap.put(QUAD_ONE_KEY, new TreeSet<>());
        neutralGamesMap.put(QUAD_TWO_KEY, new TreeSet<>());
        neutralGamesMap.put(QUAD_THREE_KEY, new TreeSet<>());
        neutralGamesMap.put(QUAD_FOUR_KEY, new TreeSet<>());

        Map<Integer, Set<Game>> homeGamesMap = new HashMap<>();
        homeGamesMap.put(QUAD_ONE_KEY, new TreeSet<>(new HomeRpiComparator()));
        homeGamesMap.put(QUAD_TWO_KEY, new TreeSet<>(new HomeRpiComparator()));
        homeGamesMap.put(QUAD_THREE_KEY, new TreeSet<>(new HomeRpiComparator()));
        homeGamesMap.put(QUAD_FOUR_KEY, new TreeSet<>(new HomeRpiComparator()));

        Map<Integer, Set<Game>> awayGamesMap = new HashMap<>();
        awayGamesMap.put(QUAD_ONE_KEY, new TreeSet<>(new AwayRpiComparator()));
        awayGamesMap.put(QUAD_TWO_KEY, new TreeSet<>(new AwayRpiComparator()));
        awayGamesMap.put(QUAD_THREE_KEY, new TreeSet<>(new AwayRpiComparator()));
        awayGamesMap.put(QUAD_FOUR_KEY, new TreeSet<>(new AwayRpiComparator()));

        List<Game> homeGames = team.getHomeGames();
        for (Game game : homeGames)
        {
            int oppRpi = game.getAwayTeam().getRpiRank();
            int oppScore = game.getAwayScore();
            int teamScore = game.getHomeScore();
            boolean win = teamScore > oppScore;
            if (game.isNeutralSite())
                neutralGamesMap = addToListByQuadrant(neutralGamesMap, game, win, oppRpi, QUAD_TWO_NEUTRAL_MIN, QUAD_THREE_NEUTRAL_MIN, QUAD_FOUR_NEUTRAL_MIN);
            else
                homeGamesMap = addToListByQuadrant(homeGamesMap, game, win, oppRpi, QUAD_TWO_HOME_MIN, QUAD_THREE_HOME_MIN, QUAD_FOUR_NEUTRAL_MIN);
        }

        List<Game> awayGames = team.getAwayGames();
        for (Game game : awayGames)
        {
            int oppRpi = game.getHomeTeam().getRpiRank();
            int oppScore = game.getHomeScore();
            int teamScore = game.getAwayScore();
            boolean win = teamScore > oppScore;
            if (game.isNeutralSite())
                neutralGamesMap = addToListByQuadrant(neutralGamesMap, game, win, oppRpi, QUAD_TWO_NEUTRAL_MIN, QUAD_THREE_NEUTRAL_MIN, QUAD_FOUR_NEUTRAL_MIN);
            else
                awayGamesMap = addToListByQuadrant(awayGamesMap, game, win, oppRpi, QUAD_TWO_AWAY_MIN, QUAD_THREE_AWAY_MIN, QUAD_FOUR_AWAY_MIN);
        }

        team.setNeutralQuadrantGames(neutralGamesMap);
        team.setHomeQuadrantGames(homeGamesMap);
        team.setAwayQuadrantGames(awayGamesMap);
        team.setQuadOneWins(quadOneWinCount);
        team.setQuadOneLosses(quadOneLossCount);
        team.setQuadTwoWins(quadTwoWinCount);
        team.setQuadTwoLosses(quadTwoLossCount);
        team.setQuadThreeWins(quadThreeWinCount);
        team.setQuadFourWins(quadFourWinCount);
        team.setQuadFourLosses(quadFourLossCount);

        return team;
    }

    private Map<Integer, Set<Game>> addToListByQuadrant(Map<Integer, Set<Game>> quadrants, Game game, boolean win,
                                                               int oppRpi, int quadTwoMin, int quadThreeMin, int quadFourMin)
    {
        if (oppRpi < quadTwoMin)
        {
            quadrants.get(QUAD_ONE_KEY).add(game);
            if (win)
                quadOneWinCount++;
            else
                quadOneLossCount++;
        }
        else if (oppRpi >= quadTwoMin && oppRpi < quadThreeMin)
        {
            quadrants.get(QUAD_TWO_KEY).add(game);
            if (win)
                quadTwoWinCount++;
            else
                quadTwoLossCount++;
        }
        else if (oppRpi >= quadThreeMin && oppRpi < quadFourMin)
        {
            quadrants.get(QUAD_THREE_KEY).add(game);
            if (win)
                quadThreeWinCount++;
            else
                quadThreeLossCount++;
        }
        else if (oppRpi >= quadFourMin)
        {
            quadrants.get(QUAD_FOUR_KEY).add(game);
            if (win)
                quadFourWinCount++;
            else
                quadFourLossCount++;
        }

        return quadrants;
    }
}
