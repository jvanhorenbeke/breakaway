package com.velorecord.breakaway

import com.google.gson.Gson
import com.jvanhorenbeke.breakaway.strava.model.SegmentLeaderBoard
import com.velorecord.breakaway.strava.ApiEndpoints
import com.velorecord.breakaway.strava.Client
import com.velorecord.breakaway.strava.StravaIds
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class BreakawayController(val gson: Gson = Gson()) {

    private fun enrichAthleteInfo(segmentLeaderBoard: SegmentLeaderBoard) : String {
        val map = HashMap<String, Long>()
        map["Jelle V."] = 9022454
        map["Evan P."] = 9757503
        map["Philip G."] = 3014007
        map["Tyler W."] = 1000123
        map["Pablo S."] = 1000123
        map["Antoine C."] = 1000123

        //create a map of positions based on the segment leaderboard. Try to obtain more data given the Name / AthleteId

        return segmentLeaderBoard.toString()
    }

    @GetMapping("/sprinters/{clubId}/{year}")
    fun sprinters(@PathVariable("clubId") clubId: String,
                  @PathVariable("year") year: String)
            : String {

        val segmentLeaderBoardStr = Client.execute(ApiEndpoints.segmentLeaderboard(StravaIds.POLO_FIELD_SEGMENT.id))
        val segmentLeaderBoard = gson.fromJson<SegmentLeaderBoard>(segmentLeaderBoardStr, SegmentLeaderBoard::class.java)

        return enrichAthleteInfo(segmentLeaderBoard)
    }

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

        val athleteStatsStr = Client.execute(ApiEndpoints.athleteStats(9022454))

        val athleteStats = gson.fromJson<AthleteStats>(athleteStatsStr, AthleteStats::class.java)
        return athleteStats.ytd_ride_totals.distance.toString()

    }
}
