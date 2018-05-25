package com.andazlan.footballclub
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {
    @Rule
    @JvmField val activityRule = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun testRecyclerViewBehavior() {
        onView(withId(R.id.list_team)).check(matches(isDisplayed()))
        onView(withId(R.id.list_team)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
        onView(withId(R.id.list_team))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click()))
    }

    @Test
    fun testAppBehavior() {
        onView(withId(R.id.spn_league)).check(matches(isDisplayed()))
        onView(withId(R.id.spn_league)).perform(click())
        onView(withText("Spanish La Liga")).perform(click())
        onView(withText("Barcelona")).check(matches(isDisplayed()))
        onView(withId(R.id.add_to_favorite)).check(matches(isDisplayed()))
        onView(withId(R.id.add_to_favorite)).perform(click())
        onView(withText("Added to favorite")).check(matches(isDisplayed())).perform(ViewActions.pressBack())
    }
}