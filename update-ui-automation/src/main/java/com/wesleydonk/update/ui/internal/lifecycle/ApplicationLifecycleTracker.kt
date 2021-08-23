package com.wesleydonk.update.ui.internal.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ApplicationLifecycleTracker(
    private val scope: CoroutineScope
) : Application.ActivityLifecycleCallbacks, ActivityTracker {

    private val activityFlow = MutableStateFlow<AppCompatActivity?>(null)

    override fun current(): Flow<AppCompatActivity?> {
        return activityFlow
    }

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        scope.launch {
            activityFlow.emit(activity as? AppCompatActivity)
        }
    }

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityResumed(activity: Activity) = Unit

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) = Unit

    override fun onActivityDestroyed(activity: Activity) {
        scope.launch {
            activityFlow.emit(null)
        }
    }
}