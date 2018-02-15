package me.andrewosborn.util;

import me.andrewosborn.model.Team;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NCAAUtil
{
    public static List<Team> getDetailedRecords(String urlString, List<Team> teams)
    {
        Document document = null;
        List<String> teamsNotFound = new ArrayList<>();
        try
        {
            document = Jsoup.connect(urlString).get();
            for (Team team : teams)
            {
                String name = team.getName();
                if (name.contains("'"))
                {
                    teamsNotFound.add(name);
                    continue;
                }
                Elements elements = document.select("tr:contains(" + name + ")");
                if (elements.isEmpty())
                {
                    teamsNotFound.add(name);
                    continue;
                }
                Element element = elements.get(0);

                String overallRecord = element.child(4).text();
                String[] recordTokens = overallRecord.split("-");
                int overallWins = Integer.parseInt(recordTokens[0]);
                int overallLosses = Integer.parseInt(recordTokens[1]);

                String roadRecord = element.child(5).text();
                String[] roadTokens = roadRecord.split("-");
                int roadWins = Integer.parseInt(roadTokens[0]);
                int roadLosses = Integer.parseInt(roadTokens[1]);

                String neutralRecord = element.child(6).text();
                String[] neutralTokens = neutralRecord.split("-");
                int neutralWins = Integer.parseInt(neutralTokens[0]);
                int neutralLosses = Integer.parseInt(neutralTokens[1]);

                String homeRecord = element.child(7).text();
                String[] homeTokens = homeRecord.split("-");
                int homeWins = Integer.parseInt(homeTokens[0]);
                int homeLosses = Integer.parseInt(homeTokens[1]);

                team.setWins(overallWins);
                team.setLosses(overallLosses);
                team.setAwayWins(roadWins);
                team.setAwayLosses(roadLosses);
                team.setHomeWins(homeWins);
                team.setHomeLosses(homeLosses);
                team.setNeutralWins(neutralWins);
                team.setNeutralLosses(neutralLosses);
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println(teamsNotFound);

        return teams;
    }
}
