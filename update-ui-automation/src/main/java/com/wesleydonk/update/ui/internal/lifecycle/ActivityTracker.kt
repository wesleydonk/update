package com.wesleydonk.update.ui.internal.lifecycle

import android.app.Activity
import kotlinx.coroutines.flow.Flow

interface ActivityTracker {
    val current: Activity?

    fun stream(): Flow<Activity>
    fun offer(activity: Activity)
}
