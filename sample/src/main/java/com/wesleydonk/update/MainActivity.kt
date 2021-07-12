package com.wesleydonk.update

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.wesleydonk.update.fetcher.TryoutAppsFetcher
import com.wesleydonk.update.storage.RoomStorage
import com.wesleydonk.update.ui.TryFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "https://google.nl"
        val fetcher = TryoutAppsFetcher(this, url)
        val storage = RoomStorage(this)

        val config = TryConfig.Builder()
            .storage(RoomStorage(this))
            .fetcher(fetcher)
            .parser(DefaultParser())

        val tryNow = Try.Builder(config)
            .build(this)

        lifecycleScope.launch {
            tryNow.checkVersion()?.let { version ->
                showTryFragment()
            }
        }

    }

    private fun showTryFragment() {
        supportFragmentManager.beginTransaction()
            .add(TryFragment.newInstance(), TryFragment.TAG)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }
}