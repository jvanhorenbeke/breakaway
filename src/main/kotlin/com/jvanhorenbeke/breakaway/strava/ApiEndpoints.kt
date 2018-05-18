package com.jvanhorenbeke.breakaway.strava

object ApiEndpoints {

    const val STRAVA_URL = "https://www.strava.com/api/v3/"

    fun club(clubId: Int = 197635) = STRAVA_URL + "clubs/$clubId"

    fun clubMembers(clubId: Int = 197635) = STRAVA_URL + "clubs/$clubId/members?per_page=200&resource_state=3"

    fun athleteStats(athleteId: Int) = STRAVA_URL + "athletes/$athleteId/stats"

    fun segmentLeaderboard(segmentId: Int, clubId: Int = 197635) = STRAVA_URL + "segments/$segmentId/leaderboard?club_id=$clubId"


}
