package me.andrewosborn.util;

import me.andrewosborn.model.Team;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SportsReferenceUtil
{
    public static List<Date> getNeutralSiteGames(Team team, int month)
    {
        List<Date> neutralDates = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String urlString = "https://www.sports-reference.com/cbb/play-index/tgl_finder.cgi?" +
                "request=1&match=game&year_min=2018&year_max=2018&comp_schl_rk=eq&val_schl_rk=ANY&comp_opp_rk=eq&" +
                "val_opp_rk=ANY&game_month=" + month + "&school_id=" + team.getSportsReferenceName() + "&game_type=A&game_location=N&is_range=N&" +
                "order_by=date_game&order_by_asc=Y";
        Document document = null;
        try
        {
            document = Jsoup.connect(urlString).get();
            if (document.select("#form_description").text().contains(", for ,"))
            {
                throw new Exception(team.getName() + "'s Sports Reference name is incorrect");
            }

            Elements elements = document.select("td[data-stat=date_game]>a");
            for (Element element : elements)
            {
                String dateString = element.text();
                Date date = formatter.parse(dateString);
                neutralDates.add(date);
                System.out.println(team.getName() + " || " + date);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return neutralDates;
    }
}
