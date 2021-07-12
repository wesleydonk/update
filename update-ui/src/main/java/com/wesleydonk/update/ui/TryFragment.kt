package com.wesleydonk.update.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wesleydonk.update.storage.RoomStorage
import com.wesleydonk.update.ui.databinding.FragmentTryBinding
import com.wesleydonk.update.ui.internal.DownloadStatus
import com.wesleydonk.update.ui.internal.IntentUtil
import com.wesleydonk.update.ui.internal.extensions.observeEvent
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import java.io.File

class TryFragment internal constructor() : Fragment(R.layout.fragment_try) {

    private val viewModel by viewModels<TryViewModel> {
        val context = requireContext()
        val storage = RoomStorage(requireContext())
        TryViewModel.Factory(storage, context)
    }

    private var binding: FragmentTryBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTryBinding.inflate(inflater, container, false).also {
            this@TryFragment.binding = it
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.downloadStatus.observe(viewLifecycleOwner) { status ->
            Log.d("TRY", status.toString())
            renderStatus(status)
        }

        viewModel.installApk.observeEvent(viewLifecycleOwner) { (filePath, mimeType) ->
            val intent = IntentUtil.apkInstall(requireContext(), filePath, mimeType)
            startActivity(intent)
        }

        binding?.btnDownload?.setOnClickListener {
            viewModel.startDownload()
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun renderStatus(status: DownloadStatus) {
        binding?.btnDownload?.isEnabled = status.downloadPercentage != null
        binding?.progressBar?.isVisible = status.downloadPercentage != null
        binding?.progressBar?.progress = status.downloadPercentage ?: 0
    }

    companion object {

        const val TAG = "TryFragment"

        fun newInstance(): TryFragment {
            return TryFragment()
        }
    }
}