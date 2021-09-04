package com.example.salesforce.db

import android.content.Context
import androidx.room.Room

open class DatabaseCreator(context: Context) {

    open val database: SafesForceDatabase = Room.databaseBuilder(
        context,
        SafesForceDatabase::class.java,
        SafesForceDatabase.DATABASE_NAME
    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
}