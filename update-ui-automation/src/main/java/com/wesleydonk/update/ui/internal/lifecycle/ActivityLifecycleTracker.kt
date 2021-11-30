package com.wesleydonk.update.ui.internal.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapNotNull
import java.lang.ref.WeakReference

class ActivityLifecycleTracker : Application.ActivityLifecycleCallbacks, ActivityTracker {

    private val currentActivity = MutableStateFlow<WeakReference<FragmentActivity>?>(null)

    override val current: Activity?
        get() = currentActivity.value?.get()

    override fun offer(activity: Activity) {
        val current = current
        if (current == null || current != activity) {
            currentActivity.tryEmit(WeakReference(activity as? FragmentActivity))
        }
    }

    override fun stream(): Flow<FragmentActivity> {
        return currentActivity.asStateFlow().mapNotNull { reference -> reference?.get() }
    }

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        offer(activity)
    }

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityResumed(activity: Activity) {
        offer(activity)
    }

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) = Unit

    override fun onActivityDestroyed(activity: Activity) = Unit
}
