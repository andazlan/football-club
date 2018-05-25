package com.andazlan.footballclub.detail

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.andazlan.footballclub.ApiRepository
import com.andazlan.footballclub.R
import com.andazlan.footballclub.R.color.colorAccent
import com.andazlan.footballclub.R.color.colorPrimary
import com.andazlan.footballclub.db.MyDatabaseOpenHelper
import com.andazlan.footballclub.model.Favorite
import com.andazlan.footballclub.model.Team
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class TeamDetailActivity: AppCompatActivity(), TeamDetailView {
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var teamBadge: ImageView
    private lateinit var teamName: TextView
    private lateinit var teamFormedYear: TextView
    private lateinit var teamStadium: TextView
    private lateinit var teamDescription: TextView

    private lateinit var presenter: TeamDetailPresenter
    private lateinit var team: Team
    private lateinit var id: String

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    val Context.database: MyDatabaseOpenHelper get() = MyDatabaseOpenHelper.getInstance(applicationContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        linearLayout {
            lparams(width = matchParent, height = wrapContent)
            orientation = LinearLayout.VERTICAL
            backgroundColor = Color.WHITE
            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(colorAccent, android.R.color.holo_green_light, android.R.color.holo_orange_light,
                        android.R.color.holo_red_light)
                scrollView {
                    isVerticalScrollBarEnabled = false
                    relativeLayout {
                        lparams(width = matchParent, height = wrapContent)
                        linearLayout {
                            lparams(width = matchParent, height = wrapContent)
                            padding = dip(10)
                            orientation = LinearLayout.VERTICAL
                            gravity = Gravity.CENTER_HORIZONTAL
                            teamBadge = imageView {  }.lparams(height = dip(75))
                            teamName = textView {
                                this.gravity = Gravity.CENTER
                                textSize = 20f
                                textColor = ContextCompat.getColor(context, colorAccent)
                            }.lparams {
                                topMargin = dip(5)
                            }
                            teamFormedYear = textView {
                                this.gravity = Gravity.CENTER
                            }
                            teamStadium = textView {
                                this.gravity = Gravity.CENTER
                                textColor = ContextCompat.getColor(context, colorPrimary)
                            }
                            teamDescription = textView {
                                textColor = ContextCompat.getColor(context, colorPrimary)
                            }.lparams {
                                topMargin = dip(20)
                            }
                            textView().lparams {
                                topMargin = dip(20)
                            }
                        }
                        progressBar = progressBar { }.lparams {
                            centerHorizontally()
                        }
                    }
                }
            }
        }
        supportActionBar?.title = "Team Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        id = intent.getStringExtra("id")
        val request = ApiRepository()
        val gson = Gson()
        presenter = TeamDetailPresenter(this, request, gson)
        presenter.getTeamDetail(id)
        swipeRefresh.onRefresh {
            presenter.getTeamDetail(id)
        }

        favoriteState()
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showTeamDetail(teams: List<Team>) {
        team = Team(teams[0].teamId, teams[0].teamName, teams[0].teamBadge)
        swipeRefresh.isRefreshing = false
        Picasso.with(this).load(teams[0].teamBadge).into(teamBadge)
        teamName.text = teams[0].teamName
        teamDescription.text = teams[0].description
        teamFormedYear.text = teams[0].formedYear
        teamStadium.text = teams[0].stadium
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_favorite -> {
                if (isFavorite) removeFromFavorite() else addToFavorite()
                isFavorite = !isFavorite
                setFavorite()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun addToFavorite() {
        try {
            database.use {
                insert(Favorite.TABLE_FAVORITE,
                        Favorite.TEAM_ID to team.teamId,
                        Favorite.TEAM_NAME to team.teamName,
                        Favorite.TEAM_BADGE to team.teamBadge)
            }
            snackbar(swipeRefresh, "Added to favorite").show()
        }
        catch (e: SQLiteConstraintException) {
            snackbar(swipeRefresh, e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite() {
        try {
            database.use {
                delete(Favorite.TABLE_FAVORITE, "TEAM_ID = {id}", "id" to id)
            }
            snackbar(swipeRefresh, "Removed to favorite").show()
        }
        catch (e: SQLiteConstraintException) {
            snackbar(swipeRefresh, e.localizedMessage).show()
        }
    }

    private fun setFavorite() {
        if (isFavorite) {
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
        }
        else{
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
        }
    }

    private fun favoriteState() {
        database.use {
            val result = select(Favorite.TABLE_FAVORITE).whereArgs("(TEAM_ID = {id})", "id" to id)
            val favorite = result.parseList(classParser<Favorite>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    fun View.visible() {
        visibility = View.VISIBLE
    }

    fun View.invisible() {
        visibility = View.INVISIBLE
    }
}