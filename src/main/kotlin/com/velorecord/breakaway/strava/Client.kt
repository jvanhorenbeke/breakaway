package com.velorecord.breakaway.strava

import org.apache.http.client.fluent.Request
import org.apache.http.client.utils.URIBuilder

object Client {

    fun execute(url: String) : String {
        val builder = URIBuilder(url)
        val uri = builder.build()
        return Request.Get(uri).setHeader("Authorization", "Bearer ").execute().returnContent().asString()
    }
}
