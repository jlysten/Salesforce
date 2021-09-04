package com.example.salesforce.di

import android.app.Application
import com.example.salesforce.db.DatabaseCreator
import com.example.salesforce.db.dao.FavoritesDAO
import com.example.salesforce.repository.MovieRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class SalesforceModule(private val app: Application, private val dbCreator: DatabaseCreator = DatabaseCreator(app)) {

    @Provides
    @Singleton
    open fun provideApplication(): Application = app

    @Provides
    @Singleton
    open fun provideMovieRepository(): MovieRepository = MovieRepository.getInstance()

    @Provides
    @Singleton
    open fun provideFavoriteMoviesDAO(): FavoritesDAO = dbCreator.database.favoritesDAO()

}