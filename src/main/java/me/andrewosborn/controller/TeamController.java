package me.andrewosborn.controller;

import me.andrewosborn.model.Team;
import me.andrewosborn.persistence.TeamService;
import me.andrewosborn.util.TeamControllerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/team")
@Controller
public class TeamController
{
    private TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService)
    {
        this.teamService = teamService;
    }

    @RequestMapping(value = "/{teamName}")
    public String getTeamPage(Model model, @PathVariable String teamName)
    {
        TeamControllerUtil teamControllerUtil = new TeamControllerUtil();

        Team team = teamService.getByUrlName(teamName);
        Team updatedTeam = teamService.save(teamControllerUtil.setQuadrants(team));
        model.addAttribute(updatedTeam);

        return "team";
    }
}
