package me.andrewosborn.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import me.andrewosborn.exception.InvalidScheduleResultsException;
import me.andrewosborn.model.Conference;
import me.andrewosborn.model.Game;
import me.andrewosborn.model.Team;
import me.andrewosborn.persistence.ConferenceService;
import me.andrewosborn.persistence.GameService;
import me.andrewosborn.persistence.TeamService;
import me.andrewosborn.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.NonUniqueResultException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestMapping("/util")
@RestController
public class DataInitController
{
    private TeamService teamService;
    private ConferenceService conferenceService;
    private GameService gameService;

    @Autowired
    public DataInitController(TeamService teamService, ConferenceService conferenceService, GameService gameService)
    {
        this.teamService = teamService;
        this.conferenceService = conferenceService;
        this.gameService = gameService;
    }

    @RequestMapping("/init")
    public List<Team> home()
    {
        saveData(LocalDate.of(2017, 11, 10), LocalDate.now().minusDays(1));

        return teamService.getAll();
    }

    @RequestMapping("/teams-games")
    public List<Team> teamsGames()
    {
        for (Team team : teamService.getAll())
        {
            teamService.save(setTeamGames(team));
        }

        return teamService.getAll();
    }

    @RequestMapping("/add-games")
    public String addGames()
    {
        return "Saved no game, as no date is specified";
    }

    @RequestMapping("/calculate-record")
    public Team calculateTeamRecord(@RequestParam(value = "team") String teamName)
    {
        Team team = teamService.getByName(teamName);
        try
        {
            CalculateResult.calculateWins(team);
        } catch (InvalidScheduleResultsException e)
        {
            e.printStackTrace();
        }

        return team;
    }

    @RequestMapping("/calculate-all-records")
    public String calculateTeamRecords()
    {
        List<Team> teams = teamService.getAll();
        try
        {
            for (Team team : teams)
            {
                teamService.save(CalculateResult.calculateWins(team));
            }
        } catch (InvalidScheduleResultsException e)
        {
            e.printStackTrace();
        }

        return "Calculated all teams' records.";
    }

    @RequestMapping("/read-url")
    public List<String> readUrl()
    {
        List<String> neutralSiteOpponents = ESPNUtil.getScheduleUrls(
                "http://www.espn.com/mens-college-basketball/team/schedule/_/id/261/vermont-catamounts");
        Team currentTeam = teamService.getByName("Vermont");
        for (String teamName : neutralSiteOpponents)
        {
            Team team = teamService.getByName(teamName);
            if (team != null)
            {
                gameService.getByTeams(currentTeam, team);
            }
        }

        return neutralSiteOpponents;
    }

    @RequestMapping("/parse-records")
    public String parseRecords()
    {
        NCAAUtil.parseNittyGrittyPDF("https://extra.ncaa.org/solutions/rpi/Stats%20Library/Feb.%2014,%202018%20Nitty%20Gritty.pdf");

        return "Hi";
    }

    @RequestMapping("/parse-neutral")
    public String parseNeutralGames()
    {
        for (Team team : teamService.getAll())
        {
            List<Date> neutralDates = SportsReferenceUtil.getNeutralSiteGames(team);
            for (Date date : neutralDates)
            {
                try
                {
                    List<Game> games = gameService.getByTeamAndDate(team, date);
                    for (Game game : games)
                    {
                        if (game.isNeutralSite())
                            continue;
                        game.setNeutralSite(true);
                        gameService.save(game);
                    }
                }
                catch (NonUniqueResultException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return "Neutral games successfully parsed";
    }

    private Team setTeamGames(Team team)
    {
        List<Game> homeGames = gameService.getHomeGamesByTeam(team);
        List<Game> awayGames = gameService.getAwayGamesByTeam(team);

        if (!homeGames.isEmpty())
            team.setHomeGames(homeGames);
        if (!awayGames.isEmpty())
            team.setAwayGames(awayGames);

        return team;
    }

    private void saveData(LocalDate fromDate, LocalDate toDate)
    {
        saveConferencesWithTeams();
        List<String> gameUrls = getGameUrls(loopThroughDates(fromDate, toDate));
        saveGames(gameUrls);
    }

    private void saveConferencesWithTeams()
    {
        try
        {
            String urlString = "https://www.ncaa.com/standings/basketball-men/d1/2017";
            Document document = Jsoup.connect(urlString).get();

            List<Element> conferenceElements =
                    document.getElementsByClass("ncaa-standings-conference-name");
            for (Element confElement : conferenceElements)
            {
                String confName = confElement.text();
                String confURLName = ControllerUtil.toSlug(confName);

                Conference conference = new Conference(confName, confURLName);
                List<Team> confTeams = new ArrayList<>();

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

                        Team team = new Team(teamName, teamURLName, conference);
                        confTeams.add(team);
                    }
                }

                conference.setTeams(confTeams);
                conferenceService.save(conference);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private List<LocalDate> loopThroughDates(LocalDate fromDate, LocalDate toDate)
    {
        List<LocalDate> dates = new ArrayList<>();

        for (LocalDate date = fromDate;
             date.isBefore(toDate) || date.isEqual(toDate);
             date = date.plusDays(1))
        {
            dates.add(date);
        }

        return dates;
    }

    private List<String> getGameUrls(List<LocalDate> dates)
    {
        List<String> gameURLs = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("00");
        for (LocalDate date : dates)
        {
            int year = date.getYear();
            String month = decimalFormat.format(date.getMonthValue());
            String day = decimalFormat.format(date.getDayOfMonth());
            String dateString = year + "/" + month + "/" + day;
            String stringURL = "http://data.ncaa.com/sites/default/files/data/scoreboard/basketball-men/d1/"
                    + dateString + "/scoreboard.json";
            try
            {
                JSONObject json = JsonUtil.readJsonFromUrl(stringURL);
                JSONObject object = json.getJSONArray("scoreboard").getJSONObject(0);
                JSONArray games = object.getJSONArray("games");

                int numberOfGames = games.length();
                for (int i = 0; i < numberOfGames; i++)
                {
                    gameURLs.add(games.getString(i));
                }

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return gameURLs;
    }

    private void saveGames(List<String> gameUrls)
    {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String baseUrl = "http://data.ncaa.com";
        for (String gameUrl : gameUrls)
        {
            String url = baseUrl + gameUrl;
            try
            {
                GamePOJO gamePOJO = gson.fromJson(JsonUtil.readUrl(url), GamePOJO.class);
                Team homeTeam = teamService.getByUrlName(gamePOJO.getHomeTeam().slugName);
                Team awayTeam = teamService.getByUrlName(gamePOJO.getAwayTeam().slugName);
                // Don't save game if one or more of the teams is not Division 1
                if (homeTeam == null || awayTeam == null)
                {
                    continue;
                }

                Game game = new Game();
                game.setDate(gamePOJO.getDate());
                game.setHomeTeam(homeTeam);
                game.setAwayTeam(awayTeam);
                game.setHomeScore(gamePOJO.getHomeTeam().getScore());
                game.setAwayScore(gamePOJO.getAwayTeam().getScore());

                gameService.save(game);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private class GamePOJO
    {
        public GamePOJO()
        {}

        @SerializedName("home")
        private TeamPOJO homeTeam;

        @SerializedName("away")
        private TeamPOJO awayTeam;

        @SerializedName("startDate")
        private Date date;

        public TeamPOJO getHomeTeam()
        {
            return homeTeam;
        }

        public void setHomeTeam(TeamPOJO homeTeam)
        {
            this.homeTeam = homeTeam;
        }

        public TeamPOJO getAwayTeam()
        {
            return awayTeam;
        }

        public void setAwayTeam(TeamPOJO awayTeam)
        {
            this.awayTeam = awayTeam;
        }

        public Date getDate()
        {
            return date;
        }

        public void setDate(Date date)
        {
            this.date = date;
        }
    }

    private class TeamPOJO
    {
        public TeamPOJO()
        {}

        @SerializedName("nameSeo")
        private String slugName;

        @SerializedName("currentScore")
        private int score;

        public String getSlugName()
        {
            return slugName;
        }

        public void setSlugName(String slugName)
        {
            this.slugName = slugName;
        }

        public int getScore()
        {
            return score;
        }

        public void setScore(int score)
        {
            this.score = score;
        }
    }
}
