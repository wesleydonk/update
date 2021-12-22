package com.wesleydonk.update

internal interface Parser {
    fun parse(version: CheckVersionResult): Version
}

internal class DefaultParser : Parser {
    override fun parse(version: CheckVersionResult): Version {
        return Version(
            version.id,
            version.downloadUrl,
            0L
        )
    }
}
