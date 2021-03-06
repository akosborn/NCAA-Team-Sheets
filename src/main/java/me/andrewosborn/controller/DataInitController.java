package me.andrewosborn.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import me.andrewosborn.model.*;
import me.andrewosborn.persistence.ConferenceService;
import me.andrewosborn.persistence.GameService;
import me.andrewosborn.persistence.TeamGameService;
import me.andrewosborn.persistence.TeamService;
import me.andrewosborn.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.context.annotation.Profile;
=======
import org.springframework.stereotype.Controller;
>>>>>>> dev
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

<<<<<<< HEAD
@Profile("dev")
@RequestMapping("/util")
@RestController
=======
@RequestMapping("/admin/util")
@Controller
>>>>>>> dev
public class DataInitController
{
    private TeamService teamService;
    private ConferenceService conferenceService;
    private GameService gameService;
    private TeamGameService teamGameService;

    @Autowired
    public DataInitController(TeamService teamService, ConferenceService conferenceService, GameService gameService,
                              TeamGameService teamGameService)
    {
        this.teamService = teamService;
        this.conferenceService = conferenceService;
        this.gameService = gameService;
        this.teamGameService = teamGameService;
    }

    @RequestMapping("/add-games")
    public String home(@RequestParam(value = "year") int year,
                       @RequestParam(value = "month") int month,
                       @RequestParam(value = "day") int day,
                       RedirectAttributes redirectAttributes)
    {
        // save games in range of dates from game urls
        LocalDate fromDate = LocalDate.of(year, month, day);
        LocalDate toDate = LocalDate.of(year, month, day);
        List<String> gameUrls = getGameUrlsByDates(getDatesInRange(fromDate, toDate));
        List<Game> games = saveGames(gameUrls);

        // add games newly-added day to teams' schedules, calculate records, and save
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        for (Game game : games)
        {
            Team homeTeam = game.getHomeTeam();
            Team awayTeam = game.getAwayTeam();

            String conference = homeTeam.getConference().getName();

            if (conference.equals("Horizon") || conference.equals("Metro Atlantic Athletic") ||
                    conference.equals("Missouri Valley") || conference.equals("Ohio Valley") ||
                    conference.equals("Summit League") || conference.equals("West Coast") ||
                    conference.equals("Southern") || conference.equals("Northeast") ||
                    conference.equals("Colonial Athletic"))
                game.setNeutralSite(true);

            List<TeamGame> homeTeamGames = TeamControllerUtil.addToTeamSchedule(game.getDate(), homeTeam.getGames(), awayTeam,
                    game.getAwayScore(), game.getHomeScore(), game.isNeutralSite() ? Site.NEUTRAL : Site.HOME);
            homeTeam.setGames(homeTeamGames);
            homeTeam = TeamControllerUtil.calculateRecord(homeTeam);
            teamService.save(homeTeam);

            List<TeamGame> awayTeamGames = TeamControllerUtil.addToTeamSchedule(game.getDate(), awayTeam.getGames(), homeTeam,
                    game.getHomeScore(), game.getAwayScore(), game.isNeutralSite() ? Site.NEUTRAL : Site.AWAY);
            awayTeam.setGames(awayTeamGames);
            awayTeam = TeamControllerUtil.calculateRecord(awayTeam);
            teamService.save(awayTeam);
        }

        redirectAttributes.addAttribute("year", year);
        redirectAttributes.addAttribute("month", month);
        redirectAttributes.addAttribute("day", day);

        if (games.size() > 0)
        {
            return "redirect:/admin/util/neutral";
        }
        else
        {
            return "redirect:/admin/util/games";
        }
    }

    @RequestMapping("/neutral")
    public String setNeutral(@RequestParam(value = "year") int year,
                             @RequestParam(value = "month") int month,
                             @RequestParam(value = "day") int day)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date date = calendar.getTime();

        List<String> oppUrls = SportsReferenceUtil.getNeutralSiteGamesByMonth(date);

        for (String opponent : oppUrls)
        {
            TeamGame game = teamGameService.getByDateAndUrlName(date, opponent);
            if (game != null)
            {
                game.setSite(Site.NEUTRAL);
                teamGameService.save(game);
            }
        }

        return "redirect:/admin/util/rpi";
    }

    @RequestMapping("/rpi")
    public String calculateRpi()
    {
        for (Team team : teamService.getAll())
        {
            team = TeamControllerUtil.calculateRecord(team);
            teamService.save(team);
        }

        // Calculate RPI
        for (Team team : teamService.getAll())
        {
            float rpi = RpiUtil.calculateRPI(team);
            team.setRpi(rpi);
            teamService.save(team);
        }

        // Set rpi rank using counter
        List<Team> teamsByRpi = teamService.getAllOrderByRpiDesc();
        for (int i = 0; i < teamsByRpi.size(); i++)
        {
            Team team = teamsByRpi.get(i);
            team.setRpiRank(i + 1);
            teamService.save(team);
        }

        for (Team team : teamService.getAll())
        {
            List<TeamGame> games = TeamControllerUtil.setTeamGameQuadrant(team.getGames());
            team.setGames(games);
            team = TeamControllerUtil.setQuadrantRecords(team);
            teamService.save(team);
        }

        return "redirect:/admin/util/sos";
    }

    @RequestMapping("/sos")
    public String setStrengthOfSchedule()
    {
        for (Team team : teamService.getAll())
        {
            float strengthOfSchedule = RpiUtil.calculateStrengthOfSchedule(team);
            team.setStrengthOfSchedule(strengthOfSchedule);
            teamService.save(team);
        }

        List<Team> teamsBySoS = teamService.getAllOrderBySoSDesc();
        for (int i = 0; i < teamsBySoS.size(); i++)
        {
            Team team = teamsBySoS.get(i);
            team.setStrengthOfScheduleRank(i + 1);
            teamService.save(team);
        }

        return  "redirect:/";
    }

    private List<LocalDate> getDatesInRange(LocalDate fromDate, LocalDate toDate)
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

    private List<String> getGameUrlsByDates(List<LocalDate> dates)
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

    private List<Game> saveGames(List<String> gameUrls)
    {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String baseUrl = "http://data.ncaa.com";
        List<Game> games = new ArrayList<>();
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

                if (!gamePOJO.getFinalMessage().contains("Final"))
                    continue;

                Game game = new Game();
                game.setDate(gamePOJO.getDate());
                game.setHomeTeam(homeTeam);
                game.setAwayTeam(awayTeam);
                game.setHomeScore(gamePOJO.getHomeTeam().getScore());
                game.setAwayScore(gamePOJO.getAwayTeam().getScore());

                // Move to next gameUrl if game is already in database
                if (gameService.getByHomeTeamAndAwayTeamAndDate(homeTeam, awayTeam, gamePOJO.getDate()) != null)
                    continue;
                games.add(gameService.save(game));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return games;
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

        @SerializedName("finalMessage")
        private String finalMessage;

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

        public String getFinalMessage()
        {
            return finalMessage;
        }

        public void setFinalMessage(String finalMessage)
        {
            this.finalMessage = finalMessage;
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
