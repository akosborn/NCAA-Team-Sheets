package me.andrewosborn.controller;

import me.andrewosborn.model.TeamGame;
import me.andrewosborn.persistence.TeamGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController
{
    private TeamGameService teamGameService;

    @Autowired
    public AdminController(TeamGameService teamGameService)
    {
        this.teamGameService = teamGameService;
    }

    @RequestMapping("")
    public String getAdminHomePage()
    {
        return "/secure/admin";
    }

    @RequestMapping("/util/games")
    public String getGamesByDate(@RequestParam(value = "year") int year,
                                 @RequestParam(value = "month") int month,
                                 @RequestParam(value = "day") int day,
                                 Model model)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date date = calendar.getTime();

        List<TeamGame> games = teamGameService.getAllByDate(date);
        model.addAttribute("games", games);

        return "/secure/games";
    }
}
