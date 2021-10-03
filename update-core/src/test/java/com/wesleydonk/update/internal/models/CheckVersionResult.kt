package com.wesleydonk.update.internal.models

import com.wesleydonk.update.CheckVersionResult

fun fakeVersionResult() = CheckVersionResult(
    id = "fake",
    parameters = mapOf(
        "download_url" to "fake_url",
    )
)