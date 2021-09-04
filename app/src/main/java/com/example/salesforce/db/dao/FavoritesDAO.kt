package com.example.salesforce.db.dao

import androidx.room.*
import com.example.salesforce.db.entity.Favorites

@Dao
interface FavoritesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieToFavorites(vararg movie: Favorites)

    @Query("SELECT * FROM favorites_table")
    fun retrieveFavoriteMovies(): List<Favorites>


    @Query("SELECT imdbID FROM favorites_table WHERE imdbID!=:imdBId")
    fun retrieveImDBIDOfFavoriteMovies(imdBId: String = ""): List<String>

    @Query("DELETE FROM favorites_table WHERE imdbID=:id")
    fun removeMovieFromFavorites(id: String)

}
