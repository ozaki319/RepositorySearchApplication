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
    var selectRepository = RepositoryEntity("", "", "", "", "", "", "", "")
    var favoriteFolderList: List<String> = listOf()

    // ViewModelのイベントを通知するFlow
    private val channelInsertFavoriteRepository =
        Channel<RepositoryEntity>(capacity = Channel.UNLIMITED)
    var eventInsertFavoriteRepository = channelInsertFavoriteRepository.receiveAsFlow()
    private val channelGetFavoriteFolderList = Channel<List<String>>(capacity = Channel.UNLIMITED)
    var eventGetFavoriteFolderList = channelGetFavoriteFolderList.receiveAsFlow()

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

    // お気に入りフォルダリストを取得するメソッド
    fun getFavoriteFolderList() {
        viewModelScope.launch {
            favoriteFolderList = _favoriteRepository.getFavoriteFolderName()
            channelGetFavoriteFolderList.send(favoriteFolderList)
        }
    }

    // お気に入り登録するメソッド
    fun insertFavoriteRepository(
        data: RepositoryEntity,
        saveFolder: String,
    ) {
        viewModelScope.launch {
            val saveData =
                RepositoryEntity(
                    data.id,
                    data.fullName,
                    data.login,
                    data.language,
                    data.stargazersCount,
                    data.htmlUrl,
                    data.avatarUrl,
                    saveFolder,
                )
            _favoriteRepository.insertFavoriteRepository(saveData)
            channelInsertFavoriteRepository.send(saveData)
        }
    }
}
