package com.wesleydonk.update.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.wesleydonk.update.ui.databinding.DialogFragmentUpdateBinding
import com.wesleydonk.update.ui.internal.DownloadStatus
import com.wesleydonk.update.ui.internal.IntentUtil
import com.wesleydonk.update.ui.internal.extensions.observeEvent
import com.wesleydonk.update.ui.internal.managers.apk.ApkManager
import com.wesleydonk.update.ui.internal.managers.apk.ApkManagerFactory
import com.wesleydonk.update.ui.internal.managers.apk.ApkManagerImpl
import kotlinx.coroutines.launch

class UpdateDialogFragment : DialogFragment(R.layout.dialog_fragment_update) {

    private val viewModel by viewModels<UpdateViewModel> {
        val context = requireContext()
        UpdateViewModel.Factory(context)
    }

    private var binding: DialogFragmentUpdateBinding? = null

    private val apkManager: ApkManager by lazy { ApkManagerFactory(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(STYLE_NO_TITLE, 0);
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogFragmentUpdateBinding.inflate(inflater, container, false).also {
            this@UpdateDialogFragment.binding = it
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.downloadStatus.observe(viewLifecycleOwner) { status ->
            renderStatus(status)
        }

        viewModel.installApk.observeEvent(viewLifecycleOwner) { installApk ->
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
        binding?.progressBar?.isInvisible = status.downloadPercentage == null
        binding?.progressBar?.progress = status.downloadPercentage ?: 0
    }

    companion object {

        const val TAG = "UpdateDialogFragment"

        fun newInstance(): UpdateDialogFragment {
            return UpdateDialogFragment()
        }
    }
}