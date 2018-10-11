package com.velorecord.breakaway

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.velorecord.breakaway.strava.ApiEndpoints
import com.velorecord.breakaway.strava.Client
import com.velorecord.breakaway.strava.StravaIds
import com.velorecord.breakaway.views.AthleteStats
import com.velorecord.breakaway.views.LeaderboardAthlete
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class BreakawayController(val gson: Gson = Gson()) {

    @GetMapping("/boards/{clubId}")
    fun boards(@PathVariable("clubId") clubId: String) : String {

        val yellowMaillot = JsonObject()
        yellowMaillot.addProperty("id", "yellowMaillot")
        yellowMaillot.addProperty("name", "gc")
        yellowMaillot.addProperty("jersey", "yellow")
        yellowMaillot.addProperty("hasGap", false)

        val radiusMaillot = JsonObject()
        radiusMaillot.addProperty("id", "radiusMaillot")
        radiusMaillot.addProperty("name", "radius")
        radiusMaillot.addProperty("jersey", "blue")
        radiusMaillot.addProperty("hasGap", true)

        val greenMaillot = JsonObject()
        greenMaillot.addProperty("id", "greenMaillot")
        greenMaillot.addProperty("name", "sprinters")
        greenMaillot.addProperty("jersey", "green")
        greenMaillot.addProperty("hasGap", false)

        val polkaMaillot = JsonObject()
        polkaMaillot.addProperty("id", "polkaMaillot")
        polkaMaillot.addProperty("name", "polka")
        polkaMaillot.addProperty("jersey", "polkadot")
        polkaMaillot.addProperty("hasGap", true)

        val rainbowMaillot = JsonObject()
        rainbowMaillot.addProperty("id", "rainbowMaillot")
        rainbowMaillot.addProperty("name", "rainbow")
        rainbowMaillot.addProperty("jersey", "rainbow")
        rainbowMaillot.addProperty("hasGap", false)

        val jsonArray = JsonArray()
        jsonArray.add(radiusMaillot)
        jsonArray.add(greenMaillot)
        jsonArray.add(polkaMaillot)
        jsonArray.add(rainbowMaillot)
        jsonArray.add(yellowMaillot)

        return gson.toJson(jsonArray)
    }

//    @GetMapping("/sprinters/{clubId}/{year}")
    @GetMapping("/sprinters/{clubId}")
    fun sprinters(@PathVariable("clubId") clubId: String) =
        Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.POLO_FIELD_SEGMENT.id))

    @GetMapping("/radius/{clubId}")
    fun radius(@PathVariable("clubId") clubId: String) =
            Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.HAWK_HILL_SEGMENT.id))

    @GetMapping("/polka/{clubId}")
    fun polka(@PathVariable("clubId") clubId: String) =
            Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.PANTOLL_SEGMENT.id))

    @GetMapping("/rainbow/{clubId}")
    fun rainbow(@PathVariable("clubId") clubId: String) =
            Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.CAMINO_ALTO_SEGMENT.id))

    @GetMapping("/gc/{clubId}")
    fun general(@PathVariable("clubId") clubId: String) =
            Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.FOUR_CORNERS_SEGMENT.id))

    @GetMapping("/test")
    fun test() : String {
        val nameToId = HashMap<String, Long>()
        nameToId["Jelle V."] = 9022454
        nameToId["Evan P."] = 9757503
        nameToId["Philip G."] = 3014007
        nameToId["Tyler W."] = 1000123
        nameToId["Pablo S."] = 1000123
        nameToId["Antoine C."] = 1000123
        nameToId["Peter C."] = 1000123
        nameToId["Diego B."] = 1000123

        val idToName = HashMap<Long, String>()
        idToName[9022454] = "Jelle V."
        idToName[9757503] = "Evan P."
        idToName[3014007] = "Philip G."
        idToName[1000123] = "Tyler W."
        idToName[1000123] = "Pablo S."
        idToName[1000123] = "Antoine C."
        idToName[1000123] = "Peter C."
        idToName[1000123] = "Diego B."

        val map = nameToId.values.map {
            val athleteStatsStr = Client.execute(ApiEndpoints.athleteStats(9022454))
            val athleteStats = gson.fromJson<AthleteStats>(athleteStatsStr, AthleteStats::class.java)
            LeaderboardAthlete(
                    rider = idToName[it].orEmpty(),
                    distance = athleteStats.all_ride_totals.distance,
                    points = 0,
                    athleteId = it,
                    elapsedTime = athleteStats.all_ride_totals.elapsed_time.toLong()
            )
        }

        return gson.toJson(map)
    }

}
