package com.example.repositorysearchapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.repositorysearchapplication.model.database.RepositoryEntity
import com.example.repositorysearchapplication.model.repository.FavoriteRepository
import com.example.repositorysearchapplication.model.repository.GetRepositoryDataRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val _getRepositoryDataRepository = GetRepositoryDataRepository()
    private val _favoriteRepository = FavoriteRepository(application)

    var searchWord: String = ""
    var page: Int = 1
    val repositoryList = MutableLiveData<List<RepositoryEntity>>()
    val searchStatus = MutableLiveData<Boolean>()
    var selectRepository = RepositoryEntity("", "", "", "", "", "", "")

    // ViewModelのイベントを通知するFlow
    private val channelInsertFavoriteRepository = Channel<RepositoryEntity>(capacity = Channel.UNLIMITED)
    var eventInsertFavoriteRepository = channelInsertFavoriteRepository.receiveAsFlow()

    // 検索結果をクリアするメソッド
    fun clearRepositoryList() {
        page = 1
        repositoryList.value = listOf()
    }

    // 検索するメソッド
    fun addRepositoryList() {
        viewModelScope.launch {
            searchStatus.value = true
            repositoryList.value =
                _getRepositoryDataRepository.getRepositoryList(
                    searchWord,
                    page,
                    repositoryList.value!!,
                )
            page += 1
            searchStatus.value = false
        }
    }

    // お気に入り登録するメソッド
    fun insertFavoriteRepository(data: RepositoryEntity) {
        viewModelScope.launch {
            _favoriteRepository.insertFavoriteRepository(data)
            channelInsertFavoriteRepository.send(data)
        }
    }
}
