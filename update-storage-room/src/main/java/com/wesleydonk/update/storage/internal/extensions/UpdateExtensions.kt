package com.wesleydonk.update.storage.internal.extensions

import com.wesleydonk.update.Version
import com.wesleydonk.update.storage.internal.model.VersionModel

internal fun Version.toModel(): VersionModel = VersionModel(version, downloadUrl, downloadId)

internal fun VersionModel.fromModel(): Version = Version(version, downloadUrl, downloadId)