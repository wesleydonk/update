package com.wesleydonk.update

data class Version(
    val version: String,
    val downloadUrl: String,
    val downloadId: Long?,
)