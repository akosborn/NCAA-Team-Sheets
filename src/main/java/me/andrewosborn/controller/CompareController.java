package me.andrewosborn.controller;

import me.andrewosborn.model.Team;
import me.andrewosborn.persistence.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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
    public String compareTeams(@RequestParam(value = "teamOne") String teamOneName,
                               @RequestParam(value = "teamTwo") String teamTwoName,
                               Model model)
    {
        Team teamOne = teamService.getByName(teamOneName);
        Team teamTwo = teamService.getByName(teamTwoName);
        List<Team> teams = new ArrayList<>();
        teams.add(teamOne);
        teams.add(teamTwo);

        model.addAttribute("teams", teams);

        return "compare";
    }
}
