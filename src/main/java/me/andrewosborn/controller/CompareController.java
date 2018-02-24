package me.andrewosborn.controller;

import me.andrewosborn.model.Result;
import me.andrewosborn.model.Team;
import me.andrewosborn.model.TeamGame;
import me.andrewosborn.persistence.TeamService;
import me.andrewosborn.util.TeamControllerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class CompareController
{
    private TeamService teamService;

    @Autowired
    public CompareController(TeamService teamService)
    {
        this.teamService = teamService;
    }

    @RequestMapping("/compare")
    public String compareTeams(@RequestParam(value = "team-one") String teamOneName,
                               @RequestParam(value = "team-two") String teamTwoName,
                               Model model)
    {
        Team teamOne = teamService.getByUrlName(teamOneName);
        Team teamTwo = teamService.getByUrlName(teamTwoName);
        Collections.sort(teamOne.getGames(), new TeamControllerUtil.RPIComparator());
        Collections.sort(teamTwo.getGames(), new TeamControllerUtil.RPIComparator());
        for (TeamGame game : teamOne.getGames())
        {
            if (game.getResult().equals(Result.W))
            {
                teamOne.setBestWin(game);
                break;
            }
        }

        for (int i = teamOne.getGames().size() - 1; i > 0; i--)
        {
            if (teamOne.getGames().get(i).getResult().equals(Result.L))
            {
                teamOne.setWorstLoss(teamOne.getGames().get(i));
                break;
            }
        }

        for (TeamGame game : teamTwo.getGames())
        {
            if (game.getResult().equals(Result.W))
            {
                teamTwo.setBestWin(game);
                break;
            }
        }

        for (int i = teamTwo.getGames().size() - 1; i > 0; i--)
        {
            if (teamTwo.getGames().get(i).getResult().equals(Result.L))
            {
                teamTwo.setWorstLoss(teamTwo.getGames().get(i));
                break;
            }
        }

        List<Team> teams = new ArrayList<>();
        teams.add(teamOne);
        teams.add(teamTwo);

        model.addAttribute("teams", teams);

        return "compare";
    }
}
