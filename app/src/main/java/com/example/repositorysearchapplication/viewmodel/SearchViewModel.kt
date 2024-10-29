package com.example.repositorysearchapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.repositorysearchapplication.model.database.FavoriteFolderEntity
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

//    var selectRepository = RepositoryEntity("", "", "", "", "", "", "", "")
    var favoriteFolderList: List<String> = listOf()

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
            favoriteFolderList = _favoriteRepository.getFavoriteFolderName()
            channelGetFavoriteFolderList.send(1)
        }
    }

    // お気に入りフォルダを新規作成するメソッド
//    fun insertNewFavoriteFolder(folderName: String) {
//        viewModelScope.launch {
//            val check = _favoriteRepository.countFavoriteFolder(folderName) < 1
//            if (check) {
//                _favoriteRepository.insertFavoriteFolder(FavoriteFolderEntity(folderName))
//                insertFavoriteRepository(selectRepository, folderName)
//            } else {
//                channelNgNewFolder.send(1)
//            }
//        }
//    }
    fun insertNewFavoriteFolder(
        folderName: String,
        data: RepositoryEntity,
    ) {
        viewModelScope.launch {
            val check = _favoriteRepository.countFavoriteFolder(folderName) < 1
            if (check) {
                _favoriteRepository.insertFavoriteFolder(FavoriteFolderEntity(folderName))
                insertFavoriteRepository(data, folderName)
            } else {
                channelNgNewFolder.send(1)
            }
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
            channelInsertFavoriteRepository.send(1)
        }
    }
}
