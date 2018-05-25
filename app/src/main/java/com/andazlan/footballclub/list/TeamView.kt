package com.andazlan.footballclub.list

import com.andazlan.footballclub.model.Team

interface TeamView {
    fun showLoading()
    fun hideLoading()
    fun showTeamList(data: List<Team>)
}