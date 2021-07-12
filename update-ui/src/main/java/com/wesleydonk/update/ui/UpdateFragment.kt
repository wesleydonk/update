package com.wesleydonk.update.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wesleydonk.update.ui.databinding.FragmentUpdateBinding
import com.wesleydonk.update.ui.internal.DownloadStatus
import com.wesleydonk.update.ui.internal.IntentUtil
import com.wesleydonk.update.ui.internal.extensions.observeEvent

class UpdateFragment internal constructor() : DialogFragment(R.layout.fragment_update) {

    private val viewModel by viewModels<UpdateViewModel> {
        val context = requireContext()
        UpdateViewModel.Factory(context)
    }

    private var binding: FragmentUpdateBinding? = null

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

        viewModel.installApk.observeEvent(viewLifecycleOwner) { installApk ->
            val intent =
                IntentUtil.apkInstall(
                    requireContext(),
                    installApk.filePath,
                    installApk.fileMimeType
                )
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
        binding?.btnDownload?.isEnabled = status.downloadPercentage == null
        binding?.progressBar?.isVisible = status.downloadPercentage != null
        binding?.progressBar?.progress = status.downloadPercentage ?: 0
    }

    companion object {

        const val TAG = "TryFragment"

        fun newInstance(): UpdateFragment {
            return UpdateFragment()
        }
    }
}