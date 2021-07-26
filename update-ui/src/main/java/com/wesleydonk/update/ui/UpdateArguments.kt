package com.wesleydonk.update.ui

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import com.wesleydonk.update.Version

data class UpdateArguments(
    val version: Version
) {
    fun toBundle(): Bundle {
        return bundleOf(
            EXTRAS_VERSION to version
        )
    }

    companion object {
        private const val EXTRAS_VERSION = "EXTRAS_VERSION"

        fun fromHandle(savedStateHandle: SavedStateHandle): UpdateArguments {
            return UpdateArguments(
                requireNotNull(savedStateHandle.get<Version>(EXTRAS_VERSION))
            )
        }
    }
}