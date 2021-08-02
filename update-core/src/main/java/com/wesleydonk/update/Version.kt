package com.wesleydonk.update

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Version(
    val version: String,
    val downloadUrl: String,
    val downloadId: Long?,
) : Parcelable
