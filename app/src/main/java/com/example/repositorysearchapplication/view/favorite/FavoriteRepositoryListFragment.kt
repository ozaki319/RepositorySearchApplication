package com.example.repositorysearchapplication.view.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repositorysearchapplication.R
import com.example.repositorysearchapplication.databinding.FragmentFavoriteRepositoryListBinding
import com.example.repositorysearchapplication.model.adapter.RepositoryListAdapter
import com.example.repositorysearchapplication.model.database.RepositoryEntity
import com.example.repositorysearchapplication.view.dialog.MenuDialogFragment
import com.example.repositorysearchapplication.view.dialog.NewFolderDialogFragment
import com.example.repositorysearchapplication.viewmodel.FavoriteViewModel
import kotlinx.coroutines.launch

class FavoriteRepositoryListFragment : Fragment() {
    private var _binding: FragmentFavoriteRepositoryListBinding? = null
    private val binding get() = _binding!!
    private val _favoriteViewModel: FavoriteViewModel by activityViewModels()
    private val adapter = RepositoryListAdapter()

    // LiveDataのオブザーバクラス
    private inner class SelectFolderObserver : Observer<String> {
        override fun onChanged(value: String) {
            _favoriteViewModel.getFavoriteFolderRepository(value)
        }
    }

    private inner class FavoriteRepositoryListObserver : Observer<List<RepositoryEntity>> {
        override fun onChanged(value: List<RepositoryEntity>) {
            adapter.submitList(value)
            binding.txtNoRepository.isVisible = value.isEmpty()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoriteRepositoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // LiveDataにオブザーバを登録
        _favoriteViewModel.selectFolder.observe(
            viewLifecycleOwner,
            SelectFolderObserver(),
        )

        _favoriteViewModel.favoriteRepositoryList.observe(
            viewLifecycleOwner,
            FavoriteRepositoryListObserver(),
        )

        // お気に入りフォルダの取得
        _favoriteViewModel.getFavoriteFolderList()

        // Spinnerのアイテムが選択されたとき
        binding.spnFolder.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    val item = parent?.selectedItem.toString()
                    _favoriteViewModel.folderSelect(item)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        // RecyclerViewの設定
        binding.rvFavoriteRepositoryList.adapter = adapter
        binding.rvFavoriteRepositoryList.layoutManager =
            LinearLayoutManager(this.context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }

        // リストをクリックしたときの処理
        adapter.setOnItemClickListener(
            object : RepositoryListAdapter.OnRepositoryItemClickListener {
                override fun onItemClick(data: RepositoryEntity) {
                    _favoriteViewModel.selectRepository = data
                    findNavController().navigate(R.id.action_favoriteRepositoryListFragment_to_favoriteRepositoryDetailFragment)
                }
            },
        )

        // メニューボタンをクリックしたときの処理
        binding.btnMenu.setOnClickListener {
            val dialog = MenuDialogFragment()
            dialog.show(childFragmentManager, "dialog")
            childFragmentManager.setFragmentResultListener(
                "request_key",
                viewLifecycleOwner,
            ) { _, bundle ->
                when (bundle.getString("click")) {
                    "new_folder" -> {
                        showNewFolderDialog()
                    }
                    "folder_rename" -> {}
                    "delete_folder" -> {}
                }
            }
        }

        // ViewModelのFlowの購読
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    _favoriteViewModel.eventGetFavoriteFolderList.collect {
                        val favoriteFolderArray = _favoriteViewModel.favoriteFolderList.toTypedArray()
                        // Spinnerの設定
                        val adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                favoriteFolderArray,
                            )
                        binding.spnFolder.adapter = adapter
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // フォルダ新規作成ダイアログ
    private fun showNewFolderDialog()  {
        val dialog = NewFolderDialogFragment()
        dialog.show(childFragmentManager, "dialog")
        childFragmentManager.setFragmentResultListener(
            "request_key",
            viewLifecycleOwner,
        ) { _, bundle ->
            val folderName = bundle.getString("folder_name")!!
            _favoriteViewModel.insertNewFavoriteFolder(folderName)
        }
    }
}
