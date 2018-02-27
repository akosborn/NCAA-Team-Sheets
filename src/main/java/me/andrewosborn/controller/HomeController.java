package me.andrewosborn.controller;

import me.andrewosborn.persistence.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
        model.addAttribute("topTenQuadOnePlusTwoTeams", teamService.getTopTenOrderByQuadOneWinsPlusQuadTwoWinsDesc());

        return "home";
    }
}
