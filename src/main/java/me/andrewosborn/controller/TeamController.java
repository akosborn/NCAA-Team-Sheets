package me.andrewosborn.controller;

import me.andrewosborn.model.Conference;
import me.andrewosborn.model.Quadrant;
import me.andrewosborn.model.Team;
import me.andrewosborn.model.TeamGame;
import me.andrewosborn.persistence.ConferenceService;
import me.andrewosborn.persistence.TeamService;
import me.andrewosborn.util.TeamControllerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class TeamController
{
    private TeamService teamService;
    private ConferenceService conferenceService;

    @Autowired
    public TeamController(TeamService teamService, ConferenceService conferenceService)
    {
        this.teamService = teamService;
        this.conferenceService = conferenceService;
    }

    @RequestMapping(value = "/teams")
    public String getTeamsPage(Model model)
    {
        List<Conference> conferences = conferenceService.getAll();
        model.addAttribute("conferences", conferences);

        return "conferences";
    }

    @RequestMapping(value = "/team/{teamName}")
    public String getTeamPage(Model model, @PathVariable String teamName)
    {
        Team team = teamService.getByUrlName(teamName);
        List<TeamGame> teamGames = TeamControllerUtil.setTeamGameQuadrant(team.getGames());
        team.setGames(teamGames);

        int[] quadOneRecord = TeamControllerUtil.getQuadrantRecord(team.getGames(), Quadrant.ONE);
        team.setQuadOneWins(quadOneRecord[0]);
        team.setQuadOneLosses(quadOneRecord[1]);
        int[] quadTwoRecord = TeamControllerUtil.getQuadrantRecord(team.getGames(), Quadrant.TWO);
        team.setQuadTwoWins(quadTwoRecord[0]);
        team.setQuadTwoLosses(quadTwoRecord[1]);
        int[] quadThreeRecord = TeamControllerUtil.getQuadrantRecord(team.getGames(), Quadrant.THREE);
        team.setQuadThreeWins(quadThreeRecord[0]);
        team.setQuadThreeLosses(quadThreeRecord[1]);
        int[] quadFourRecord = TeamControllerUtil.getQuadrantRecord(team.getGames(), Quadrant.FOUR);
        team.setQuadFourWins(quadFourRecord[0]);
        team.setQuadFourLosses(quadFourRecord[1]);

        model.addAttribute(team);

        return "team";
    }
}
