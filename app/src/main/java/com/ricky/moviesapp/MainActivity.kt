package com.ricky.moviesapp

import android.app.SearchManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.ricky.moviesapp.api.OmdbApiClient
import com.ricky.moviesapp.databinding.ActivityMainBinding
import com.ricky.moviesapp.persistence.DatabaseProvider
import com.ricky.moviesapp.persistence.MoviesRepository
import com.ricky.moviesapp.ui.MoviesViewModel
import com.ricky.moviesapp.ui.MoviesViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    internal lateinit var moviesViewModel: MoviesViewModel
    lateinit var fab: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val moviesRepository = MoviesRepository(
            OmdbApiClient(),
            DatabaseProvider.getMoviesDao()
        )
        moviesViewModel = ViewModelProvider(
            this,
            MoviesViewModelFactory(moviesRepository)
        )[MoviesViewModel::class.java]

        fab = binding.fab
        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        var searchView: SearchView? = null
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(componentName)
        )
        searchView.maxWidth = Int.MAX_VALUE
        searchView.queryHint = getString(R.string.search_hint)
//        searchView.setQuery("", false)
        val queryTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(query: String): Boolean {
                    if (query.isNullOrBlank()) {
                        // clear filter
                        CoroutineScope(Dispatchers.Default).launch {
                            moviesViewModel.getSavedMovies()
                        }
                        return true
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        moviesViewModel.searchMovies(query)
                    }
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }
            }
        searchView.setOnQueryTextListener(queryTextListener)
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                moviesViewModel.getSavedMovies()
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_search -> {
                if (!isInternetConnected()) {
                    Snackbar.make(
                        item.actionView!!,
                        "Please connect to the internet and try again",
                        Snackbar.LENGTH_LONG
                    )
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show()
                } else {
                    item.expandActionView()
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun isInternetConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetwork
        return networkInfo != null
    }
}