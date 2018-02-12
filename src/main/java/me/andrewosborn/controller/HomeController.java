package me.andrewosborn.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;

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
