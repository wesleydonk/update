package com.wesleydonk.update

@Deprecated("Unnecessary mapping which can be done in a fetcher instance")
interface Parser {
    fun parse(version: CheckVersionResult): Version
}

@Deprecated("Unnecessary mapping which can be done in a fetcher instance")
class DefaultParser : Parser {
    override fun parse(version: CheckVersionResult): Version {
        return Version(
            version.id,
            version.downloadUrl,
            0L
        )
    }
}