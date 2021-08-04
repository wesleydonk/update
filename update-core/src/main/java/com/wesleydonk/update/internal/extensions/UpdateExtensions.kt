package com.wesleydonk.update.internal.extensions

import com.wesleydonk.update.Version
import com.wesleydonk.update.internal.database.model.VersionModel

internal fun Version.toModel(): VersionModel = VersionModel(id, downloadUrl, downloadId)

internal fun VersionModel.fromModel(): Version = Version(version, downloadUrl, downloadId)