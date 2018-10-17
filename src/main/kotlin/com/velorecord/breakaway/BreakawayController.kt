package com.velorecord.breakaway

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.velorecord.breakaway.strava.ApiEndpoints
import com.velorecord.breakaway.strava.Client
import com.velorecord.breakaway.strava.StravaIds
import com.velorecord.breakaway.views.SegmentLeaderboard
import com.velorecord.breakaway.views.StatsBoardDefinition
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class BreakawayController(val gson: Gson = Gson()) {

    @GetMapping("/boards")
    fun boards(@RequestParam(value="main_view", required=false, defaultValue="true") isMainView: Boolean) : String {

        val mainView = arrayOf(
                StatsBoardDefinition("yellowMaillot", "gc", "yellow", true),
                StatsBoardDefinition("radiusMaillot", "radius", "blue", true),
                StatsBoardDefinition("greenMaillot", "sprinters", "green"),
                StatsBoardDefinition("polkaMaillot", "polka", "polkadot")
        )

        val extendedView = arrayOf(
                StatsBoardDefinition("combativeMaillot", "combative", "combative"),
                StatsBoardDefinition("unkownMaillot", "caminoalto", "unknown"),
                StatsBoardDefinition("rainbowMaillot", "rainbow", "rainbow")
        )

        return gson.toJson(if (isMainView) mainView else extendedView)
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

    @GetMapping("/combative")
    fun combative(@RequestParam(value="ytd", required=false, defaultValue="false") ytd: Boolean) =
            Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.EXTENDED_PRESIDIO_SPRINT.id, ytd = ytd))

    @GetMapping("/caminoalto")
    fun caminoAlto(@RequestParam(value="ytd", required=false, defaultValue="false") ytd: Boolean) =
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
                        .filterKeys { key -> !key.contentEquals("Strava A.") }
                        .filterValues { (_, numberOfRaces: Int) -> numberOfRaces == listOf.size }
                        .mapValues { entry -> entry.value.first }
                        .map { entry -> entrytoSegmentLeaderboardEntry(entry) }
                        .sortedWith(compareBy({ it.elapsed_time }))
                        .toTypedArray()
        ))
    }
}
