package com.wesleydonk.update

internal interface Parser {
    fun parse(version: VersionApiModel): Version
}

internal class DefaultParser : Parser {

    override fun parse(version: VersionApiModel): Version {
        return Version(
            id = version.id,
            downloadUrl = version.downloadUrl,
            downloadId = null,
        )
    }
}
