package com.wesleydonk.update

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.wesleydonk.update.fetcher.PrinceOfVersionFetcher
import com.wesleydonk.update.ui.internal.extensions.showUpdateDialogFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        // Collect stored update results
        lifecycleScope.launch {
            update.getLatestVersion().collect { version ->
                Log.i("Latest", "Latest: $version")
                version.showUpdateDialogFragment(supportFragmentManager)
            }
        }

        // Synchronize remote updates
        update.synchronize()
    }
}