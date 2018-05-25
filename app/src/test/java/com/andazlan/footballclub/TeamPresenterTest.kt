package com.andazlan.footballclub

import com.andazlan.footballclub.detail.TestContextProvider
import com.andazlan.footballclub.list.TeamPresenter
import com.andazlan.footballclub.list.TeamView
import com.andazlan.footballclub.model.Team
import com.andazlan.footballclub.model.TeamResponse
import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class TexamPresenterTest {
    @Mock
    private lateinit var view: TeamView

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var apiRepository: ApiRepository

    private lateinit var presenter: TeamPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = TeamPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun testGetTeamList() {
        val teams: MutableList<Team> = mutableListOf()
        val response = TeamResponse(teams)
        val league = "English Premiere League"

        `when`(gson.fromJson(apiRepository.doRequest(TheSportsDBApi.getTeams(league)), TeamResponse::class.java))
                .thenReturn(response)

        presenter.getTeamList(league)

        verify(view).showLoading()
        verify(view).showTeamList(teams)
        verify(view).hideLoading()
    }
}