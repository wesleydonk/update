package com.wesleydonk.update

interface Fetcher {

    suspend fun getLatestVersion(): VersionApiModel?
}
