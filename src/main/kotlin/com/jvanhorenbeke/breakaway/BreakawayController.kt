package com.jvanhorenbeke.breakaway

import com.google.gson.Gson
import com.jvanhorenbeke.breakaway.strava.ApiEndpoints
import com.jvanhorenbeke.breakaway.strava.Client
import com.jvanhorenbeke.breakaway.strava.StravaIds
import com.jvanhorenbeke.breakaway.views.AthleteStats
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class BreakawayController {

    @GetMapping("/sprinters/{clubId}/{year}")
    fun sprinters(@PathVariable("clubId") clubId: String, @PathVariable("year") year: String) =
            Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.POLO_FIELD_SEGMENT.id))

    @GetMapping("/radius/{clubId}/{year}")
    fun radius(@PathVariable("clubId") clubId: String, @PathVariable("year") year: String) =
            Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.HAWK_HILL_SEGMENT.id))

    @GetMapping("/polka/{clubId}/{year}")
    fun polka(@PathVariable("clubId") clubId: String, @PathVariable("year") year: String) =
            Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.PANTOLL_SEGMENT.id))

    @GetMapping("/general/{clubId}/{year}")
    fun general(@PathVariable("clubId") clubId: String, @PathVariable("year") year: String) = "general"

    @GetMapping("/test")
    fun test() : String {

        val gson = Gson()

        val map = HashMap<String, Long>()
        map["Jelle V."] = 9022454
        map["Evan P."] = 9757503
        map["Philip G."] = 3014007

        val athleteStatsStr = Client.execute(ApiEndpoints.athleteStats(9022454))

        val athleteStats = gson.fromJson<AthleteStats>(athleteStatsStr, AthleteStats::class.java)
        return athleteStats.ytd_ride_totals.distance.toString()

    }
}
