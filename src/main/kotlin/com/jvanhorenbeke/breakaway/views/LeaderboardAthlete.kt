package com.jvanhorenbeke.breakaway.views

data class LeaderboardAthlete(
        val profilePicture: String = "https://dgalywyr863hv.cloudfront.net/pictures/athletes/9022454/4377256/3/large.jpg",
        val rider: String,
        val distance: Int,
        val points: Int,
        val athleteId: Long
)
{
}
