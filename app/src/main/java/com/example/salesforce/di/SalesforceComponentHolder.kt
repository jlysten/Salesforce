package com.example.salesforce.di

import android.app.Application
import dagger.internal.DaggerCollections

object SalesforceComponentHolder {

    var component: SalesforceComponent? = null

    fun setup(app: Application, module: SalesforceModule = SalesforceModule(app)){
       component = DaggerSalesforceComponent.builder().salesforceModule(module).build()
    }
}