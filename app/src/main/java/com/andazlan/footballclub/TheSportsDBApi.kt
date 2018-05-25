package com.andazlan.footballclub

import java.net.URLEncoder

object TheSportsDBApi {
    fun getTeams(league: String?) : String {
        return BuildConfig.BASE_URL +
                "api/v1/json/${BuildConfig.API_KEY}" + "/search_all_teams.php?l=" +
                URLEncoder.encode(league, "UTF-8")
    }

    fun getTeamDetail(teamId: String): String {
        return BuildConfig.BASE_URL +
                "api/v1/json/${BuildConfig.API_KEY}/lookupteam.php?id=" + teamId
    }


}