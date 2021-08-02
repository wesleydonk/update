package com.wesleydonk.update

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.wesleydonk.update.fetcher.PrinceOfVersionFetcher
import com.wesleydonk.update.storage.RoomStorage
import com.wesleydonk.update.ui.internal.extensions.showUpdateDialogFragment
import com.wesleydonk.update.ui.internal.extensions.showUpdateFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "<link to config>"
        val fetcher = PrinceOfVersionFetcher(this, url)
        val storage = RoomStorage(this)
        val parser = DefaultParser()

        val config = UpdateConfig.Builder()
            .storage(storage)
            .fetcher(fetcher)
            .parser(parser)
            .build(this)

        val update = Update.Builder()
            .config(config)
            .build()

        lifecycleScope.launch {
            val version = update.checkLatestVersion()
            version?.showUpdateDialogFragment(supportFragmentManager)
        }
    }
}