package com.wesleydonk.update.fetcher.internal

import co.infinum.princeofversions.Version
import co.infinum.princeofversions.VersionParser

import com.github.zafarkhaja.semver.Version as SemVersion

internal class SemanticVersionParser : VersionParser {

    override fun parse(version: String): Version {
        return Version(SemVersion.valueOf(version))
    }
}