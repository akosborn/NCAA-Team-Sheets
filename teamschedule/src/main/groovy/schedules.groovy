import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import me.andrewosborn.model.Game
import me.andrewosborn.model.Team
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import java.nio.file.Files
import java.nio.file.Paths
import java.text.DateFormat
import java.text.SimpleDateFormat

Map<Long, String> teamMap = new LinkedHashMap<>()

File csvFile = new File("C:\\Users\\andre\\Google Drive\\CBBQuadrantology\\db_list.csv")
CsvMapper mapper = new CsvMapper()
CsvSchema schema = CsvSchema.emptySchema().withHeader() // use first row as header
MappingIterator<Map<String, String>> it = mapper.readerFor(Map.class)
        .with(schema)
        .readValues(csvFile)
while (it.hasNext()) {
    Map<Long, String> rowAsMap = it.next()
    teamMap.put(Long.parseLong(rowAsMap.get("id")), rowAsMap.get("sportsReferenceName"))
}

teamMap.each {Long id, String sportsRefName ->
    Team team = new Team()
    team.sportsReferenceName = sportsRefName
    team.id = id

// Cache team schedule page
    Document doc = Jsoup.connect("https://www.sports-reference.com/cbb/schools/${team.sportsReferenceName}/2019-schedule.html").get()

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd")
    Elements gameRows = doc.select("#schedule > tbody > tr")
    List<Game> games = new ArrayList<>()

    for (Element row : gameRows) {
        Team opp = new Team()
        Game game = new Game()

        try{
            opp.sportsReferenceName = row.getElementsByAttributeValue("data-stat", "opp_name").first()
                    .select("a").attr("href").split("/")[3]
        }
        catch (IndexOutOfBoundsException ex){
            println(ex)
            continue
        }

        game.date = dateFormat.parse(row.getElementsByAttributeValue("data-stat", "date_game").first().attr("csk"))

        String siteStr = row.getElementsByAttributeValue("data-stat", "game_location").first().val()

        String ptsStr = row.getElementsByAttributeValue("data-stat", "pts").text()
        int pts = ptsStr ? Integer.parseInt(ptsStr) : 0
        String oppPtsStr = row.getElementsByAttributeValue("data-stat", "opp_pts").first().text()
        int oppPts = oppPtsStr ? Integer.parseInt(oppPtsStr) : 0
        if (siteStr == ""){
            game.homeTeam = team
            game.homeScore = pts

            game.awayTeam = opp
            game.awayScore = oppPts
        }
        else if(siteStr == "@"){
            game.awayTeam = team
            game.homeTeam = opp

            game.awayScore = pts
            game.homeScore = oppPts
        }
        else if(siteStr == "N"){
            game.neutralSite = true

            // Home/away is inconsequential in this case so arbitrarily defaults to home
            game.homeTeam = team
            game.homeScore = pts

            game.awayTeam = opp
            game.awayScore = oppPts
        }
        else
            throw new Exception("Invalid game site for ${row}")

        games.add(game)
    }

    ObjectMapper objectMapper = new ObjectMapper()
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))

    String outfileName = "${team.sportsReferenceName}-1819.json"
    println "Writing file ${outfileName}"
    String json = objectMapper.writeValueAsString(games)
    Files.write(Paths.get("C:/Users/andre/Google Drive/CBBQuadrantology/schedules/${outfileName}"), json.getBytes())
}
