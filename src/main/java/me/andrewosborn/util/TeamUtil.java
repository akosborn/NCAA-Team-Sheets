package me.andrewosborn.util;

import me.andrewosborn.model.*;

import java.util.Date;
import java.util.List;

public class TeamUtil
{
    public static Team setTeamGames(Team team, List<Game> homeGames, List<Game> awayGames)
    {
        if (!homeGames.isEmpty())
            team.setHomeGames(homeGames);
        if (!awayGames.isEmpty())
            team.setAwayGames(awayGames);

        return team;
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
