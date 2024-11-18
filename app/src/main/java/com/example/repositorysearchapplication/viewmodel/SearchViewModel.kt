package com.example.repositorysearchapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.repositorysearchapplication.model.applicationservice.FavoriteApplicationService
import com.example.repositorysearchapplication.model.database.RepositoryEntity
import com.example.repositorysearchapplication.model.repository.GetRepositoryDataRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val _getRepositoryDataRepository = GetRepositoryDataRepository()
    private val _favoriteApplicationService = FavoriteApplicationService(application)

    var searchWord: String = ""
    var page: Int = 1
    val repositoryList = MutableLiveData<List<RepositoryEntity>>()
    val searchStatus = MutableLiveData<Boolean>()
    var favoriteFolderList: List<String> = listOf()
    val dbReady = MutableLiveData<Boolean>()

    // ViewModelのイベントを通知するFlow
    private val channelGetFavoriteFolderList = Channel<Int>(capacity = Channel.UNLIMITED)
    var eventGetFavoriteFolderList = channelGetFavoriteFolderList.receiveAsFlow()
    private val channelNgNewFolder = Channel<Int>(capacity = Channel.UNLIMITED)
    var eventNgNewFolder = channelNgNewFolder.receiveAsFlow()
    private val channelInsertFavoriteRepository =
        Channel<Int>(capacity = Channel.UNLIMITED)
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

    // お気に入りフォルダリストを取得するメソッド
    fun getFavoriteFolderList() {
        viewModelScope.launch {
            dbReady.value = false
            favoriteFolderList = _favoriteApplicationService.getFavoriteFolderList()
            channelGetFavoriteFolderList.send(1)
            dbReady.value = true
        }
    }

    // GitHubリポジトリを作成済みフォルダにお気に入り登録するメソッド
    fun insertFavoriteRepository(
        folderName: String,
        data: RepositoryEntity,
    ) {
        viewModelScope.launch {
            dbReady.value = false
            _favoriteApplicationService.registerFavoriteRepository(folderName, data)
            channelInsertFavoriteRepository.send(1)
            dbReady.value = true
        }
    }

    // GitHubリポジトリを新規作成フォルダにお気に入り登録するメソッド
    fun insertNewFavoriteFolder(
        folderName: String,
        data: RepositoryEntity,
    ) {
        viewModelScope.launch {
            dbReady.value = false
            val check = _favoriteApplicationService.registerFavoriteRepositoryIntoNewFolder(folderName, data)
            if (check) {
                channelInsertFavoriteRepository.send(1)
            } else {
                channelNgNewFolder.send(1)
            }
            dbReady.value = true
        }
    }
}
