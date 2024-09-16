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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.example.repositorysearchapplication.R
import com.example.repositorysearchapplication.databinding.FragmentRepositoryDetailBinding
import com.example.repositorysearchapplication.view.dialog.OpenBrowserDialogFragment
import com.example.repositorysearchapplication.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

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

        // アバター画像、リポジトリ名をセット
        binding.txtRepositoryName.text = _searchViewModel.selectRepository.fullName
        binding.imgOwner.load(_searchViewModel.selectRepository.avatarUrl) {
            error(R.drawable.baseline_hide_image_24)
        }

        // WebViewの設定
        binding.wvRepositoryDetail.webViewClient =
            object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?,
                ): Boolean {
                    // リンクをクリックしたとき（ロードするとき）に外部ブラウザを開くかの確認ダイアログ
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

        // お気に入り登録ボタンをクリックしたときの処理
        binding.btnFavorite.setOnClickListener {
            _searchViewModel.insertFavoriteRepository(_searchViewModel.selectRepository)
        }

        // ViewModelのFlowの購読
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    _searchViewModel.eventInsertFavoriteRepository.collect {
                        Toast
                            .makeText(context, "お気に入りに登録しました", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
