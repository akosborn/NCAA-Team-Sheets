package me.andrewosborn.controller;

import me.andrewosborn.model.Conference;
import me.andrewosborn.model.Team;
import me.andrewosborn.util.ControllerUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HomeController
{
    List<Conference> conferences;
    List<Team> teams;

    @RequestMapping("/")
    public String home()
    {
        getConferencesFromURL();

        return "Welcome to CBB Team Sheets";
    }

    private void getConferencesFromURL()
    {
        try
        {
            String urlString = "https://www.ncaa.com/standings/basketball-men/d1/2017";
            Document document = Jsoup.connect(urlString).get();

            conferences = new ArrayList<>();
            teams = new ArrayList<>();
            List<Element> conferenceElements =
                    document.getElementsByClass("ncaa-standings-conference-name");
            for (Element confElement : conferenceElements)
            {
                String confName = confElement.text();
                String confURLName = ControllerUtil.toSlug(confName);

                Conference conference = new Conference(confName, confURLName);
                conferences.add(conference);

                Element conferenceTable = confElement.nextElementSibling();
                Elements confTableRows = conferenceTable.select("tbody>tr");
                for (int i = 0; i < confTableRows.size(); i++)
                {
                    if (i > 0)
                    {
                        Element teamRow = confTableRows.get(i);
                        Element teamNameCell = teamRow.children().first().children().first();
                        Element link = teamNameCell.select("a").first();
                        String teamURL = link.attr("href");
                        String teamURLName = teamURL.split("/")[2];
                        String teamName = link.select("span").text();

                        Team team = new Team(teamName, teamURLName);
                        teams.add(team);
                    }
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
