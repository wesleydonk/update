package com.wesleydonk.update.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wesleydonk.update.ui.databinding.FragmentTryBinding
import com.wesleydonk.update.ui.internal.DownloadStatus
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class TryFragment internal constructor() : Fragment(R.layout.fragment_try) {

    private val viewModel by viewModels<TryViewModel>()

    private val binding by viewBinding(FragmentTryBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.downloadStatus.observe(viewLifecycleOwner) { status ->
            renderStatus(status)
        }

        binding.btnDownload.setOnClickListener {
            viewModel.startDownload()
        }
    }

    private fun renderStatus(status: DownloadStatus) {
        binding.btnDownload.isEnabled = status.downloadPercentage != null
        binding.progressBar.isVisible = status.downloadPercentage != null
        binding.progressBar.progress = status.downloadPercentage ?: 0
    }

    companion object {

        const val TAG = "TryFragment"

        fun newInstance(): TryFragment {
            return TryFragment()
        }
    }
}