package com.velorecord.breakaway.strava

import io.netty.util.internal.StringUtil

object ApiEndpoints {

    const val STRAVA_URL = "https://www.strava.com/api/v3/"

    fun club(clubId: Long = StravaIds.RADIUS_CLUB.id) = STRAVA_URL + "clubs/$clubId"

    fun clubMembers(clubId: Long = StravaIds.RADIUS_CLUB.id) = STRAVA_URL + "clubs/$clubId/members?per_page=200&resource_state=3"

    fun athleteStats(athleteId: Long) = STRAVA_URL + "athletes/$athleteId/stats"

    fun segmentLeaderboard(segmentId: Long, clubId: Long = StravaIds.RADIUS_CLUB.id, ytd : Boolean = false, dateRange: String = "this_year") =
            STRAVA_URL + "segments/$segmentId/leaderboard?club_id=$clubId" + if (ytd) "&date_range=$dateRange" else StringUtil.EMPTY_STRING

}
