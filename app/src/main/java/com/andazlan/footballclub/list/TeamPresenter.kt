package com.andazlan.footballclub.list

import com.andazlan.footballclub.ApiRepository
import com.andazlan.footballclub.TheSportsDBApi
import com.andazlan.footballclub.detail.CoroutineContextProvider
import com.andazlan.footballclub.model.TeamResponse
import com.google.gson.Gson
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class TeamPresenter(private val view: TeamView, private val apiRepository: ApiRepository, private val gson: Gson,
                    private val context: CoroutineContextProvider = CoroutineContextProvider()) {
    fun getTeamList(league: String?) {
        view.showLoading()
        /* Cara lama
        doAsync {
            val data = gson.fromJson(apiRepository.doRequest(TheSportsDBApi.getTeams(league)), TeamResponse::class.java)
            uiThread {
                view.hideLoading()
                view.showTeamList(data.teams)
            }
        }
        */

        // Menggunakan anko coroutines
        async(context.main) {
            //Proses request di background
            val data = bg {
                gson.fromJson(apiRepository.doRequest(TheSportsDBApi.getTeams(league)), TeamResponse::class.java)
            }

            view.showTeamList(data.await().teams)
            view.hideLoading()
        }
    }
}