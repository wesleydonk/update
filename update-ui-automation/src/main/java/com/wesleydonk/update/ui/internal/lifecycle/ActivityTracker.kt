package com.wesleydonk.update.ui.internal.lifecycle

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.flow.Flow

interface ActivityTracker {
    fun current(): Flow<AppCompatActivity?>
}