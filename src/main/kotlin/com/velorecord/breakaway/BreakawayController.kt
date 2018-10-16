package com.velorecord.breakaway

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.velorecord.breakaway.strava.ApiEndpoints
import com.velorecord.breakaway.strava.Client
import com.velorecord.breakaway.strava.StravaIds
import com.velorecord.breakaway.views.SegmentLeaderboard
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class BreakawayController(val gson: Gson = Gson()) {

    @GetMapping("/boards")
    fun boards() : String {

        val yellowMaillot = JsonObject()
        yellowMaillot.addProperty("id", "yellowMaillot")
        yellowMaillot.addProperty("name", "gc")
        yellowMaillot.addProperty("jersey", "yellow")
        yellowMaillot.addProperty("hasGap", true)

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
        polkaMaillot.addProperty("hasGap", false)

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

    @GetMapping("/sprinters")
    fun sprinters(@RequestParam(value="ytd", required=false, defaultValue="false") ytd: Boolean) =
        Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.POLO_FIELD_SEGMENT.id, ytd = ytd))

    @GetMapping("/radius")
    fun radius(@RequestParam(value="ytd", required=false, defaultValue="false") ytd: Boolean) =
            Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.HAWK_HILL_SEGMENT.id, ytd = ytd))

    @GetMapping("/polka")
    fun polka(@RequestParam(value="ytd", required=false, defaultValue="false") ytd: Boolean) =
            Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.STINSON_PANTOLL_SEGMENT.id, ytd = ytd))

    @GetMapping("/rainbow")
    fun rainbow(@RequestParam(value="ytd", required=false, defaultValue="false") ytd: Boolean) =
            Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.FOUR_CORNERS_SEGMENT.id, ytd = ytd))

    @GetMapping("/camino_alto")
    fun fourCorners(@RequestParam(value="ytd", required=false, defaultValue="false") ytd: Boolean) =
            Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.CAMINO_ALTO_SEGMENT.id, ytd = ytd))

    @GetMapping("/gc")
    fun general(@RequestParam(value="ytd", required=false, defaultValue="false") ytd: Boolean) : String {

        val rainbowMap = HashMap<String, Pair<Long, Int>>()
        val listOf = listOf(
                Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.FOUR_CORNERS_SEGMENT.id, ytd = ytd)),
                Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.STINSON_PANTOLL_SEGMENT.id, ytd = ytd)),
                Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.CAMINO_ALTO_SEGMENT.id, ytd = ytd)),
                Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.HAWK_HILL_SEGMENT.id, ytd = ytd)),
                Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.POLO_FIELD_SEGMENT.id, ytd = ytd))
        )

        val mergeFun = { x: Pair<Long, Int>, y: Pair<Long, Int> -> Pair(x.first + y.first, x.second + y.second) }
        listOf.forEach { json ->
            val leaderboard = gson.fromJson<SegmentLeaderboard>(json, SegmentLeaderboard::class.java)
            leaderboard.entries.forEach { leaderboardEntry ->
                rainbowMap.merge(leaderboardEntry.athlete_name, Pair(leaderboardEntry.elapsed_time, 1), mergeFun)
                Unit
            }
        }

        val entrytoSegmentLeaderboardEntry = { entry: Map.Entry<String, Long> ->
            SegmentLeaderboard.SegmentLeaderboardEntry(entry.key, entry.value, entry.value, "", "", -1)
        }

        return gson.toJson(SegmentLeaderboard(
                -1, -1, "",
                rainbowMap
                        .filterValues { (_, numberOfRaces: Int) -> numberOfRaces == listOf.size }
                        .mapValues { entry -> entry.value.first }
                        .map { entry -> entrytoSegmentLeaderboardEntry(entry) }
                        .sortedWith(compareBy({ it.elapsed_time }))
                        .toTypedArray()
        ))
    }
}
