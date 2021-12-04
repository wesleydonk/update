package com.wesleydonk.update.fetcher.internal

import com.github.zafarkhaja.semver.Version as SemVersion
import co.infinum.princeofversions.Version
import co.infinum.princeofversions.VersionParser

internal class SemanticVersionParser : VersionParser {

    override fun parse(version: String): Version {
        return Version(SemVersion.valueOf(version))
    }
}
