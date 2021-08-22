package com.wesleydonk.update

import android.app.Application
import com.wesleydonk.update.fetcher.PrinceOfVersionFetcher
import com.wesleydonk.update.ui.AutomationStrategy

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val url = "<link to config file>"
        val fetcher = PrinceOfVersionFetcher(this, url)
        val parser = DefaultParser()

        val config = UpdateConfig.Builder()
            .fetcher(fetcher)
            .parser(parser)
            .build(this)

        val update = Update.Builder()
            .config(config)
            .build()

        update.synchronize(this, AutomationStrategy.DIALOG_FRAGMENT)
    }
}