package com.velorecord.breakaway.views

data class SegmentLeaderboard(
        val entry_count: Int,
        val effort_count: Int,
        val kom_type: String,
        val entries: Array<SegmentLeaderboardEntry>
) {
    data class SegmentLeaderboardEntry(
            val athlete_name: String,
            val elapsed_time: Long,
            val moving_time: Long,
            val start_date: String,
            val start_date_local: String,
            val rank: Int
    )
}