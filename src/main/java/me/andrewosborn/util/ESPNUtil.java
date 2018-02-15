package me.andrewosborn.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ESPNUtil
{
    public static List<String> getScheduleUrls(String urlString)
    {
        List<String> neutralSiteOpponents = new ArrayList<>();
        Document document = null;
        try
        {
            document = Jsoup.connect(urlString).get();
            List<String> teamNameList = document.select(".team-name").eachText();

            for (String teamName : teamNameList)
            {
                if (teamName.contains("*"))
                {
                    teamName = teamName.split("\\*")[0];

                    if (teamName.contains("#"))
                    {
                        String[] tokens = teamName.split(" ", 2);
                        teamName = tokens[1];
                    }

                    neutralSiteOpponents.add(teamName);
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return neutralSiteOpponents;
    }
}
