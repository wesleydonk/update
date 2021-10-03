package com.wesleydonk.update

interface Parser {
    fun parse(version: CheckVersionResult): Version
}

class DefaultParser : Parser {
    override fun parse(version: CheckVersionResult): Version {
        return Version(
            version.id,
            version.parameters["download_url"].orEmpty(),
            0L
        )
    }
}