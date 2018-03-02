package me.andrewosborn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminController
{
    @RequestMapping("")
    public String getAdminHomePage()
    {
        return "/secure/admin";
    }
}
