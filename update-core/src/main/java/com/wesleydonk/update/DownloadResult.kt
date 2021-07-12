package com.wesleydonk.update

sealed class DownloadResult {
    object Failed : DownloadResult()
    class InProgress(
        val percentage: Int
    ) : DownloadResult()

    class Completed(
        val filePath: String
    ) : DownloadResult()
}