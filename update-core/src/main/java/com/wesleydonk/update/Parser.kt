package com.wesleydonk.update

internal interface Parser {
    fun parse(model: VersionApiModel): Version
}

internal class DefaultParser : Parser {

    override fun parse(model: VersionApiModel): Version {
        return Version(
            id = model.id,
            downloadUrl = model.downloadUrl,
            downloadId = null,
        )
    }
}
