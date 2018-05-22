package com.jvanhorenbeke.breakaway.views

data class AthleteStats(
    val biggest_ride_distance: Double,
    val biggest_climb_elevation_gain: Double,
    val recent_ride_totals: RecentRideTotals,
    val recent_run_totals: RecentRunTotals,
    val recent_swim_totals: RecentSwimTotals,
    val ytd_ride_totals: YtdRideTotals,
    val ytd_run_totals: YtdRunTotals,
    val ytd_swim_totals: YtdSwimTotals,
    val all_ride_totals: AllRideTotals,
    val all_run_totals: AllRunTotals,
    val all_swim_totals: AllSwimTotals
) {
    data class AllRunTotals(
        val count: Int,
        val distance: Int,
        val moving_time: Int,
        val elapsed_time: Int,
        val elevation_gain: Int
    )

    data class RecentSwimTotals(
        val count: Int,
        val distance: Double,
        val moving_time: Int,
        val elapsed_time: Int,
        val elevation_gain: Double,
        val achievement_count: Int
    )

    data class RecentRideTotals(
        val count: Int,
        val distance: Double,
        val moving_time: Int,
        val elapsed_time: Int,
        val elevation_gain: Double,
        val achievement_count: Int
    )

    data class YtdRunTotals(
        val count: Int,
        val distance: Int,
        val moving_time: Int,
        val elapsed_time: Int,
        val elevation_gain: Int
    )

    data class YtdSwimTotals(
        val count: Int,
        val distance: Int,
        val moving_time: Int,
        val elapsed_time: Int,
        val elevation_gain: Int
    )

    data class RecentRunTotals(
        val count: Int,
        val distance: Double,
        val moving_time: Int,
        val elapsed_time: Int,
        val elevation_gain: Double,
        val achievement_count: Int
    )

    data class AllSwimTotals(
        val count: Int,
        val distance: Int,
        val moving_time: Int,
        val elapsed_time: Int,
        val elevation_gain: Int
    )

    data class YtdRideTotals(
        val count: Int,
        val distance: Int,
        val moving_time: Int,
        val elapsed_time: Int,
        val elevation_gain: Int
    )

    data class AllRideTotals(
        val count: Int,
        val distance: Int,
        val moving_time: Int,
        val elapsed_time: Int,
        val elevation_gain: Int
    )
}
