package com.example.repositorysearchapplication.view.favorite

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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.repositorysearchapplication.R
import com.example.repositorysearchapplication.databinding.FragmentFavoriteRepositoryDetailBinding
import com.example.repositorysearchapplication.view.dialog.DeleteFavoriteDialogFragment
import com.example.repositorysearchapplication.view.dialog.OpenBrowserDialogFragment
import com.example.repositorysearchapplication.viewmodel.FavoriteViewModel

class FavoriteRepositoryDetailFragment : Fragment() {
    private var _binding: FragmentFavoriteRepositoryDetailBinding? = null
    private val binding get() = _binding!!
    private val _favoriteViewModel: FavoriteViewModel by activityViewModels()
    private val args: FavoriteRepositoryDetailFragmentArgs by navArgs()

    // LiveDataのオブザーバクラス
    private inner class DbReadyObserver : Observer<Boolean> {
        override fun onChanged(value: Boolean) {
            binding.btnDelete.isEnabled = value
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoriteRepositoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // LiveDataにオブザーバを登録
        _favoriteViewModel.dbReady.observe(
            viewLifecycleOwner,
            DbReadyObserver(),
        )

        // アバター画像、リポジトリ名をセット
        binding.txtRepositoryName.text = args.selectRepository.fullName
        binding.imgOwner.load(args.selectRepository.avatarUrl) {
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
//        binding.wvRepositoryDetail.loadUrl(_favoriteViewModel.selectRepository.htmlUrl)
        binding.wvRepositoryDetail.loadUrl(args.selectRepository.htmlUrl)

        // お気に入りから削除ボタンをクリックしたときの処理
        binding.btnDelete.setOnClickListener {
            showDeleteFavoriteDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // お気に入り削除確認ダイアログ
    private fun showDeleteFavoriteDialog() {
        val dialog = DeleteFavoriteDialogFragment()
        dialog.show(childFragmentManager, "dialog")
        childFragmentManager.setFragmentResultListener(
            "request_key",
            viewLifecycleOwner,
        ) { _, bundle ->
            if (bundle.getBoolean("click")) {
                _favoriteViewModel.deleteFavoriteRepository(args.selectRepository)
                findNavController().popBackStack()
            }
        }
    }
}
