package com.wesleydonk.update

sealed class CheckVersionResult {
    object NoUpdate : CheckVersionResult()
    class NewUpdate(
        val id: String,
        val parameters: Map<String, String>
    ) : CheckVersionResult()
}