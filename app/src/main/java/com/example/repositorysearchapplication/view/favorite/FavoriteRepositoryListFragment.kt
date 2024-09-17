package com.example.repositorysearchapplication.view.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repositorysearchapplication.R
import com.example.repositorysearchapplication.databinding.FragmentFavoriteRepositoryListBinding
import com.example.repositorysearchapplication.model.adapter.RepositoryListAdapter
import com.example.repositorysearchapplication.model.database.RepositoryEntity
import com.example.repositorysearchapplication.viewmodel.FavoriteViewModel

class FavoriteRepositoryListFragment : Fragment() {
    private var _binding: FragmentFavoriteRepositoryListBinding? = null
    private val binding get() = _binding!!
    private val _favoriteViewModel: FavoriteViewModel by activityViewModels()
    private val adapter = RepositoryListAdapter()

    // LiveDataのオブザーバクラス
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
        _favoriteViewModel.favoriteRepositoryList.observe(
            viewLifecycleOwner,
            FavoriteRepositoryListObserver(),
        )

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
