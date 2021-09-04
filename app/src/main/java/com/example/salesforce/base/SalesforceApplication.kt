package com.example.salesforce.base

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley.newRequestQueue
import com.example.salesforce.di.SalesforceComponentHolder
import com.facebook.stetho.Stetho

class SalesforceApplication : Application() {

    var queue: RequestQueue? = null

    override fun onCreate() {
        super.onCreate()
        queue = newRequestQueue(this)
        SalesforceComponentHolder.setup(this)

        Stetho.initialize(Stetho.newInitializerBuilder(this)
            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
            .build())
    }
}