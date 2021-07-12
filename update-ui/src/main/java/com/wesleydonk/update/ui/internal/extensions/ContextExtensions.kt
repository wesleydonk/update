package com.wesleydonk.update.ui.internal.extensions

import androidx.fragment.app.FragmentActivity
import com.wesleydonk.update.Version
import com.wesleydonk.update.ui.TryFragment

fun Version.showTryFragment(activity: FragmentActivity) {
    activity.supportFragmentManager.beginTransaction()
        .add(android.R.id.content, TryFragment.newInstance(), TryFragment.TAG)
        .addToBackStack(null)
        .commitAllowingStateLoss()
}