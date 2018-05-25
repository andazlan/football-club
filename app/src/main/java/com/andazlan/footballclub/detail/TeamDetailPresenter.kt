package com.andazlan.footballclub.detail

import com.andazlan.footballclub.ApiRepository
import com.andazlan.footballclub.TheSportsDBApi
import com.andazlan.footballclub.model.TeamResponse
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class TeamDetailPresenter(private val view: TeamDetailView, private val apiRepository: ApiRepository, private val gson: Gson) {
    fun getTeamDetail(teamId: String) {
        view.showLoading()
        doAsync {
            val data = gson.fromJson(apiRepository.doRequest(TheSportsDBApi.getTeamDetail(teamId)), TeamResponse::class.java)
            uiThread {
                view.hideLoading()
                view.showTeamDetail(data.teams)
            }
        }
    }
}