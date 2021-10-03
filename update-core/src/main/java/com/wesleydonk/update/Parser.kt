package com.wesleydonk.update

class DownloadUrlMissingException : Throwable()

interface Parser {
    fun parse(version: CheckVersionResult): Version
}

class DefaultParser : Parser {

    override fun parse(version: CheckVersionResult): Version {
        val downloadUrl = version.parameters["download_url"]
            ?: throw DownloadUrlMissingException()
        return Version(
            id = version.id,
            downloadUrl = downloadUrl,
            downloadId = null,
        )
    }
}