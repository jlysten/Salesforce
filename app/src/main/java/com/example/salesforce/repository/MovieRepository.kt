package com.example.salesforce.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.salesforce.base.SalesforceApplication
import com.example.salesforce.db.dao.FavoritesDAO
import com.example.salesforce.db.entity.Favorites
import com.example.salesforce.db.entity.Movie
import com.example.salesforce.db.entity.RecentSearch
import com.example.salesforce.di.SalesforceComponentHolder
import com.example.salesforce.util.SalesforceConstants.API_KEY
import com.example.salesforce.util.SalesforceConstants.BASE_URL
import com.example.salesforce.util.SalesforceConstants.BASE_URL_INDIVIDIUAL_ITEM
import com.google.gson.Gson
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

open class MovieRepository {

    @Inject
    lateinit var app: Application

    @Inject
    lateinit var favoritesDAO: FavoritesDAO

    val searchedMovie: MutableLiveData<RecentSearch> = MutableLiveData<RecentSearch>()
    val searchedMovieDetails: MutableLiveData<Movie> = MutableLiveData<Movie>()

    init {
        SalesforceComponentHolder.component?.inject(this)
    }

    fun searchMovieTitle(movieTitle: String) {
        val url =
            "$BASE_URL$movieTitle$API_KEY"                                                          //can be appended as query in a custom request
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                try {
                    searchedMovie.value = Gson().fromJson(it.toString(), RecentSearch::class.java)
                } catch (e: Exception) {
                    Toast.makeText(
                        app.applicationContext,
                        "Error Occurred While Fetching Movie Details. Please Try Again Later",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            {
                searchedMovie.value = null
                Toast.makeText(
                    app.applicationContext,
                    "Error Occurred While Fetching Movie Details. Please Try Again Later",
                    Toast.LENGTH_SHORT
                )
                    .show()
            })
        (app as SalesforceApplication).queue?.add(jsonObjectRequest)
    }

    fun fetchMovieDetails(id: String, saveToFavorites: Boolean) {
        val url =
            "$BASE_URL_INDIVIDIUAL_ITEM$id$API_KEY"                                                 //can be appended as query in a custom request

        Log.e("SalesForce Thread :", "fetchMovieDetails  :  " + Thread.currentThread().name)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                try {
                    if (saveToFavorites) {
                        Log.e("SalesForce Thread :", "saving  :  " + Thread.currentThread().name)

                        val a = Gson().fromJson(it.toString(), Favorites::class.java)
                        a?.let {
                            insertMovieToFavorites(a)
                        }
                    } else {
                        Log.e(
                            "SalesForce Thread :",
                            "not saving  :  " + Thread.currentThread().name
                        )

                        val a = Gson().fromJson(it.toString(), Movie::class.java)
                        searchedMovieDetails.value = a
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        app.applicationContext,
                        "Error Occurred While Fetching Movie Details. Please Try Again Later",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            {
                Toast.makeText(
                    app.applicationContext,
                    "Error Occurred While Fetching Movie Details. Please Try Again Later",
                    Toast.LENGTH_SHORT
                ).show()
            })
        (app as SalesforceApplication).queue?.add(jsonObjectRequest)
    }

    private fun insertMovieToFavorites(a: Favorites) {
        Log.e("SalesForce Thread :", Thread.currentThread().name)
        favoritesDAO.insertMovieToFavorites(a)
    }

    fun fetchAllFavorites(): LiveData<List<Favorites>> {
        val favorites = MutableLiveData<List<Favorites>>()
        favorites.value = favoritesDAO.retrieveFavoriteMovies()
        return favorites
    }

    fun removeMovieFromFavorites(id: String) {
        favoritesDAO.removeMovieFromFavorites(id)
    }

    fun getAllFavoriteIDs(): List<String> = favoritesDAO.retrieveImDBIDOfFavoriteMovies()

    fun clearLiveDataObjects() {
        searchedMovieDetails.value = null
    }

    companion object {
        private var movieRepository: MovieRepository? = null

        fun getInstance(): MovieRepository {
            if (movieRepository == null) {
                movieRepository = MovieRepository()
            }
            return movieRepository as MovieRepository
        }
    }
}