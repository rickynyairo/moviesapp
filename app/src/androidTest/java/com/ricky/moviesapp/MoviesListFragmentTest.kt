package com.ricky.moviesapp

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ricky.moviesapp.entity.Movie
import com.ricky.moviesapp.ui.MoviesAdapter
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class MoviesListFragmentTest {

    private val movieList = listOf(
        Movie("tt1234567", "Movie 1", "2021", "movie", "Poster 1")
    )

    private lateinit var listener: MoviesAdapter.OnItemClickListener

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        activityRule.scenario.onActivity { activity ->
            val navController = TestNavHostController(activity)
            navController.setGraph(R.navigation.nav_graph)
            navController.setCurrentDestination(R.id.moviesListFragment)
            Navigation.setViewNavController(
                activity.findViewById(R.id.nav_host_fragment_content_main),
                navController
            )
            listener = mock(MoviesAdapter.OnItemClickListener::class.java)
            val adapter = MoviesAdapter(movieList, listener)
            activity.findViewById<RecyclerView>(R.id.saved_movies_recycler_view).adapter = adapter
        }
    }

    @Test
    fun testMoviesList_isDisplayed() {
        onView(withId(R.id.saved_movies_recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun testMoviesList_scrollToPosition() {
        onView(withId(R.id.saved_movies_recycler_view)).perform(
            scrollToPosition<RecyclerView.ViewHolder>(
                2
            )
        )
    }

    @Test
    fun testMoviesList_clickItem() {
        onView(withId(R.id.saved_movies_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        onView(withId(R.id.title_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.year_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.poster_image_view)).check(matches(isDisplayed()))
    }
}
