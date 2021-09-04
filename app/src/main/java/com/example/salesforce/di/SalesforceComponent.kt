package com.example.salesforce.di

import com.example.salesforce.adapters.MovieAdapter
import com.example.salesforce.repository.MovieRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [SalesforceModule::class])
interface SalesforceComponent {

    fun inject(movieRepository: MovieRepository)
    fun inject(movieRepository: MovieAdapter)
}