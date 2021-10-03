package com.wesleydonk.update.fetcher

import android.content.Context
import com.wesleydonk.update.CheckVersionResult
import com.wesleydonk.update.Fetcher

@Suppress("unused", "UNUSED_PARAMETER")
class PrinceOfVersionFetcher(
    context: Context,
    url: String,
) : Fetcher {

    override suspend fun latestVersionResult(): CheckVersionResult? = null
}