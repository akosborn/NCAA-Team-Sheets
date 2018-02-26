package me.andrewosborn.controller;

import me.andrewosborn.persistence.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController
{
    private TeamService teamService;

    @Autowired
    public HomeController(TeamService teamService)
    {
        this.teamService = teamService;
    }

    @RequestMapping("/")
    public String home(Model model)
    {
        model.addAttribute("teams", teamService.getAllOrderByRpiDesc());
        model.addAttribute("topTenQuadOneTeams", teamService.getTopTenOrderByQuadOneWinsDesc());

        return "home";
    }
}
