package com.example.salesforce.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.salesforce.db.dao.FavoritesDAO
import com.example.salesforce.db.entity.Favorites
import com.example.salesforce.db.entity.Movie

@Database(entities = [Favorites::class, Movie::class], version = 1, exportSchema = false)
abstract class SafesForceDatabase : RoomDatabase() {

    abstract fun favoritesDAO(): FavoritesDAO

    companion object {
        internal const val DATABASE_NAME = "SalesforceDB"
    }
}
