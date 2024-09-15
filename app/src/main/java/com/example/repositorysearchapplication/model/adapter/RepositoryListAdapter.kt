package com.example.repositorysearchapplication.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.repositorysearchapplication.R
import com.example.repositorysearchapplication.databinding.RepositoryListItemBinding
import com.example.repositorysearchapplication.model.database.RepositoryEntity

class RepositoryListAdapter : ListAdapter<RepositoryEntity, RepositoryListViewHolder>(RepositoryListDiffCallback) {
    // リスナ変数を定義
    private lateinit var listener: OnRepositoryItemClickListener

    // クリックイベントリスナ用のインターフェース
    interface OnRepositoryItemClickListener {
        fun onItemClick(data: RepositoryEntity)
    }

    // クリックイベントリスナ
    fun setOnItemClickListener(listener: OnRepositoryItemClickListener) {
        this.listener = listener
    }

    override fun onBindViewHolder(
        holder: RepositoryListViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener.onItemClick(item)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RepositoryListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RepositoryListItemBinding.inflate(inflater, parent, false)
        return RepositoryListViewHolder(binding)
    }
}

class RepositoryListViewHolder(
    private val binding: RepositoryListItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: RepositoryEntity) {
        binding.txtRepositoryName.text = item.fullName
        binding.txtStargazersCount.text = item.stargazersCount
        binding.txtLanguage.text = item.language
        if (item.language == "null") {
            binding.txtLanguage.visibility = View.GONE
        }
        binding.imgOwner.load(item.avatarUrl) {
            error(R.drawable.baseline_hide_image_24)
        }
    }
}

private object RepositoryListDiffCallback : DiffUtil.ItemCallback<RepositoryEntity>() {
    override fun areContentsTheSame(
        oldItem: RepositoryEntity,
        newItem: RepositoryEntity,
    ): Boolean = oldItem == newItem

    override fun areItemsTheSame(
        oldItem: RepositoryEntity,
        newItem: RepositoryEntity,
    ): Boolean = oldItem.id == newItem.id
}
