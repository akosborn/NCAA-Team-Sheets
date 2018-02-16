package me.andrewosborn.util;

import me.andrewosborn.model.Game;
import me.andrewosborn.model.Team;

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
}
