package me.andrewosborn.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SportsReferenceUtil
{
    public static List<String> getNeutralSiteGamesByMonth(Date date)
    {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        List<String> opponents = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String urlString = "https://www.sports-reference.com/cbb/play-index/tgl_finder.cgi?" +
                "request=1&match=game&year_min=2018&year_max=2018&comp_schl_rk=eq&val_schl_rk=ANY&comp_opp_rk=eq&" +
                "val_opp_rk=ANY&game_month=" + localDate.getMonthValue() + "&game_type=A&game_location=N&is_range=N&" +
                "order_by=date_game&order_by_asc=Y";
        Document document = null;
        try
        {
            document = Jsoup.connect(urlString).get();
            Elements elements = document.select("td[data-stat=date_game]");
            for (Element element : elements)
            {
                Element tr = element.parent();
                Date gameDate = formatter.parse(tr.child(1).text());
                if (gameDate.equals(date))
                {
                    String teamOne = tr.child(2).child(0).attr("href").split("/")[3];
                    opponents.add(teamOne);
                    String teamTwo = tr.child(4).child(0).attr("href").split("/")[3];
                    opponents.add(teamTwo);
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return opponents;
    }
}
