package me.andrewosborn.controller;

import me.andrewosborn.model.Conference;
import me.andrewosborn.model.Team;
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
        model.addAttribute(conferences);

        return "teams";
    }

    @RequestMapping(value = "/team/{teamName}")
    public String getTeamPage(Model model, @PathVariable String teamName)
    {
        TeamControllerUtil teamControllerUtil = new TeamControllerUtil();

        Team team = teamService.getByUrlName(teamName);
        Team updatedTeam = teamService.save(teamControllerUtil.setQuadrants(team));
        model.addAttribute(updatedTeam);

        return "team";
    }
}
