package com.wesleydonk.update.ui.internal.extensions

import androidx.fragment.app.FragmentActivity
import com.wesleydonk.update.Version
import com.wesleydonk.update.ui.UpdateFragment

fun Version.showTryFragment(activity: FragmentActivity) {
    activity.supportFragmentManager.beginTransaction()
        .add(android.R.id.content, UpdateFragment.newInstance(), UpdateFragment.TAG)
        .addToBackStack(null)
        .commitAllowingStateLoss()
}