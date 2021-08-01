package com.wesleydonk.update.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.wesleydonk.update.Version
import com.wesleydonk.update.ui.databinding.FragmentUpdateBinding
import com.wesleydonk.update.ui.internal.DownloadStatus
import com.wesleydonk.update.ui.internal.extensions.observeEvent
import com.wesleydonk.update.ui.internal.managers.apk.ApkManager
import com.wesleydonk.update.ui.internal.managers.apk.ApkManagerFactory
import kotlinx.coroutines.launch

class UpdateFragment internal constructor() : Fragment(R.layout.fragment_update) {

    private val viewModel by viewModels<UpdateViewModel> {
        val context = requireContext()
        UpdateViewModel.Factory(context, this)
    }

    private val apkManager: ApkManager by lazy { ApkManagerFactory(requireContext()) }

    private var binding: FragmentUpdateBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUpdateBinding.inflate(inflater, container, false).also {
            this@UpdateFragment.binding = it
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.downloadStatus.observe(viewLifecycleOwner) { status ->
            renderStatus(status)
        }

        viewModel.installableFile.observeEvent(viewLifecycleOwner) { installApk ->
            lifecycleScope.launch {
                apkManager.install(
                    installApk.filePath,
                    installApk.fileMimeType
                )
            }
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
        binding?.btnDownload?.isEnabled = status.downloadPercentage == null
        binding?.progressBar?.isVisible = status.downloadPercentage != null
        binding?.progressBar?.progress = status.downloadPercentage ?: 0
    }

    companion object {

        const val TAG = "UpdateFragment"

        fun newInstance(): UpdateFragment {
            return UpdateFragment()
        }
    }
}