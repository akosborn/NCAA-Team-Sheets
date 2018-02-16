package me.andrewosborn.util;

import me.andrewosborn.model.Team;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static void parseNittyGrittyPDF(String urlString)
    {
        List<String> rawTeamLines = new ArrayList<>();
        List<String> pages = new ArrayList<>();
        try
        {
            URL url = new URL(urlString);
            InputStream inputStream = url.openStream();
            PDDocument pddDocument = PDDocument.load(inputStream);
            PDFTextStripper textStripper = new PDFTextStripper();
            int numberOfPages = pddDocument.getNumberOfPages();

            for (int i = 1; i < numberOfPages; i++)
            {
                textStripper.setStartPage(i);
                textStripper.setEndPage(i);
                String page = textStripper.getText(pddDocument);
                pages.add(page);
            }

            pddDocument.close();

            for (String page : pages)
            {
                String[] lines = page.split("Quadrant4");
                String teamsOnlyString = lines[1];
                rawTeamLines.addAll(Arrays.asList(teamsOnlyString.split("\\r?\\n")));
            }

            for (String line : rawTeamLines)
            {
                if (line.equals(""))
                    continue;

                // Split only spaces proceeded by numbers
                String[] columns = line.split("[ ]+(?=\\d)");
                String name = columns[0];
                String recordString = columns[4];
                String awayRecord = columns[8];
                // TODO: 2/15/2018 Finish parsing Nitty Gritty PDF
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
