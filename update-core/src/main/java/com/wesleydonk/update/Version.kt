package com.wesleydonk.update

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Version(
    val id: String,
    val downloadUrl: String,
    val downloadId: Long?,
) : Parcelable
