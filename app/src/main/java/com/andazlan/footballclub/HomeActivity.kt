package com.andazlan.footballclub

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.andazlan.footballclub.favorite.FavoriteFragment
import com.andazlan.footballclub.list.TeamFragment
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay

class HomeActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bottomNav = findViewById(R.id.bottom_navigation)

        bottomNav.setOnNavigationItemSelectedListener({ item ->
            when (item.itemId) {
                R.id.teams -> {
                    loadTeamFragment(savedInstanceState)
                }
                R.id.favorites -> {
                    loadFavoriteFragment(savedInstanceState)
                }
            }
            true
        })

        bottomNav.selectedItemId = R.id.teams

        val capital = async { amountOfCapital() }
        val income = async { amountOfIncome() }

        async {
            println("Your profit is ${income.await()} - ${capital.await()}")
        }
    }

    suspend fun amountOfIncome(): Int {
        delay(10000)
        return 12000
    }

    suspend fun amountOfCapital(): Int {
        delay(10000)
        return 10000
    }

    private fun loadTeamFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container_main, TeamFragment(), TeamFragment::class.simpleName)
                    .commit()
        }
    }

    private fun loadFavoriteFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container_main, FavoriteFragment(), FavoriteFragment::class.simpleName)
                    .commit()
        }
    }
}
