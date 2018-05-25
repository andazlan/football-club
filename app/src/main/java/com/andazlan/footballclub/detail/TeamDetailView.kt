package com.andazlan.footballclub.detail

import com.andazlan.footballclub.model.Team

interface TeamDetailView {
    fun showLoading()
    fun hideLoading()
    fun showTeamDetail(data: List<Team>)
}