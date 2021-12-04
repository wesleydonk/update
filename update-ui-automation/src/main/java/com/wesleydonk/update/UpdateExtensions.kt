package com.wesleydonk.update

import android.app.Application
import androidx.fragment.app.FragmentManager
import com.wesleydonk.update.ui.AutomationStrategy
import com.wesleydonk.update.ui.internal.extensions.showUpdateDialogFragment
import com.wesleydonk.update.ui.internal.extensions.showUpdateFragment
import com.wesleydonk.update.ui.internal.lifecycle.ActivityLifecycleTracker
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

fun Update.synchronize(
    application: Application,
    strategy: AutomationStrategy,
) {
    val scope = MainScope()

    val applicationTracker = ActivityLifecycleTracker()
    application.registerActivityLifecycleCallbacks(applicationTracker)

    scope.launch {
        // sync the update
        synchronize()

        applicationTracker.stream()
            .filterNotNull()
            .combine(getLatestVersion()) { activity, version ->
                Triple(activity.supportFragmentManager, version, strategy)
            }
            .collect { (fragmentManager, version, strategy) ->
                showVersion(fragmentManager, version, strategy)
            }
    }
}

private fun showVersion(
    fragmentManager: FragmentManager,
    version: Version,
    strategy: AutomationStrategy
) {
    when (strategy) {
        AutomationStrategy.DIALOG_FRAGMENT ->
            version.showUpdateDialogFragment(fragmentManager)
        AutomationStrategy.FRAGMENT ->
            version.showUpdateFragment(fragmentManager)
    }
}