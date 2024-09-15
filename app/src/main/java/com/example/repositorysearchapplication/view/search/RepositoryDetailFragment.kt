package com.example.repositorysearchapplication.view.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.repositorysearchapplication.databinding.FragmentRepositoryDetailBinding
import com.example.repositorysearchapplication.view.dialog.OpenBrowserDialogFragment
import com.example.repositorysearchapplication.viewmodel.SearchViewModel

class RepositoryDetailFragment : Fragment() {
    private var _binding: FragmentRepositoryDetailBinding? = null
    private val binding get() = _binding!!
    private val _searchViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRepositoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // WebViewの設定
        binding.wvRepositoryDetail.webViewClient =
            object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?,
                ): Boolean {
                    val dialog = OpenBrowserDialogFragment()
                    dialog.show(childFragmentManager, "dialog")
                    childFragmentManager.setFragmentResultListener(
                        "request_key",
                        viewLifecycleOwner,
                    ) { key, bundle ->
                        if (bundle.getBoolean("click")) {
                            val url = request?.url
                            val intent = Intent(Intent.ACTION_VIEW, url)
                            startActivity(intent)
                        }
                    }
                    return true
                }
            }
        binding.wvRepositoryDetail.settings.javaScriptEnabled = true
        binding.wvRepositoryDetail.loadUrl(_searchViewModel.selectRepository.htmlUrl)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
