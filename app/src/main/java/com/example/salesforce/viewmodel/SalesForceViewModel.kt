package com.example.salesforce.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salesforce.db.entity.Favorites
import com.example.salesforce.db.entity.Movie
import com.example.salesforce.db.entity.RecentSearch
import com.example.salesforce.repository.MovieRepository
import com.example.salesforce.util.SalesforceConstants.EMPTY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SalesForceViewModel : ViewModel() {

    var currentSearch: String = EMPTY
    private val movieRepository: MovieRepository = MovieRepository.getInstance()
    var searchData: MutableLiveData<RecentSearch>? = movieRepository.searchedMovie
    var selectedMovieData: MutableLiveData<Movie>? = movieRepository.searchedMovieDetails
    var favorites: MutableLiveData<List<Favorites>>? = null

    fun fetchFavorites() {
        favorites = movieRepository.fetchAllFavorites() as MutableLiveData<List<Favorites>>
    }

    fun searchForTitle(movieTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when {
                movieTitle != currentSearch -> {
                    currentSearch = movieTitle
                    movieRepository.searchMovieTitle(movieTitle)
                }
            }
        }
    }

    fun fetchMovieDetails(id: String, shouldSaveToFavorites: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.fetchMovieDetails(id, shouldSaveToFavorites)
        }
    }

    fun removeMovieFromFavorites(id: String) {
        movieRepository.removeMovieFromFavorites(id)
    }

    fun clearLiveDataObjects() {
        movieRepository.clearLiveDataObjects()
    }
}