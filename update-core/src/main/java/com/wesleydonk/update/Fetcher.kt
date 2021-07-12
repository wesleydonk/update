package com.wesleydonk.update

interface Fetcher {

    suspend fun latestVersionResult(): CheckVersionResult
}