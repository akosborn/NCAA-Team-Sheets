package me.andrewosborn.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController
{
    @RequestMapping("/")
    public String home()
    {
        getTeamsFromURL();

        return "Welcome to CBB Team Sheets";
    }

    private void getTeamsFromURL()
    {

    }
}
