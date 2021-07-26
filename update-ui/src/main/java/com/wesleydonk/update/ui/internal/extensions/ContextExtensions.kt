package com.wesleydonk.update.ui.internal.extensions

import androidx.fragment.app.FragmentManager
import com.wesleydonk.update.Version
import com.wesleydonk.update.ui.UpdateDialogFragment
import com.wesleydonk.update.ui.UpdateFragment

fun Version.showUpdateFragment(fragmentManager: FragmentManager) {
    fragmentManager.beginTransaction()
        .add(android.R.id.content, UpdateFragment.newInstance(), UpdateFragment.TAG)
        .addToBackStack(null)
        .commitAllowingStateLoss()
}

fun Version.showUpdateDialogFragment(fragmentManager: FragmentManager) {
    UpdateDialogFragment.newInstance()
        .show(fragmentManager, UpdateDialogFragment.TAG)
}