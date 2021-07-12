package com.wesleydonk.update

interface Parser {
    fun parse(version: CheckVersionResult.NewUpdate): Version
}

class DefaultParser : Parser {
    override fun parse(version: CheckVersionResult.NewUpdate): Version {
        return Version(
            version.version,
            version.parameters["download_url"].orEmpty(),
            0L
        )
    }
}