package com.example.repositorysearchapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.repositorysearchapplication.model.applicationservice.FavoriteApplicationService
import com.example.repositorysearchapplication.model.database.RepositoryEntity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val _favoriteApplicationService = FavoriteApplicationService(application)

    val favoriteRepositoryList = MutableLiveData<List<RepositoryEntity>>()
    var favoriteFolderList: List<String> = listOf()
    val selectFolder = MutableLiveData<String>()
    var focusFolderName = ""
    var indexSelectFolder = 0
    val dbReady = MutableLiveData<Boolean>()

    // ViewModelのイベントを通知するFlow
    private val channelGetFavoriteFolderList = Channel<Int>(capacity = Channel.UNLIMITED)
    var eventGetFavoriteFolderList = channelGetFavoriteFolderList.receiveAsFlow()
    private val channelUpdateFavoriteFolder = Channel<Int>(capacity = Channel.UNLIMITED)
    var eventUpdateFavoriteFolder = channelUpdateFavoriteFolder.receiveAsFlow()
    private val channelNgNewFolder = Channel<Int>(capacity = Channel.UNLIMITED)
    var eventNgNewFolder = channelNgNewFolder.receiveAsFlow()
    private val channelNgRenameFolder = Channel<Int>(capacity = Channel.UNLIMITED)
    var eventNgRenameFolder = channelNgRenameFolder.receiveAsFlow()

    // お気に入りフォルダリストを取得するメソッド
    fun getFavoriteFolderList() {
        viewModelScope.launch {
            dbReady.value = false
            favoriteFolderList = _favoriteApplicationService.getFavoriteFolderList()
            indexSelectFolder = favoriteFolderList.indexOf(focusFolderName)
            channelGetFavoriteFolderList.send(1)
            if (favoriteFolderList.isEmpty()) {
                selectFolder.value = ""
            }
            dbReady.value = true
        }
    }

    // フォルダが選択されたときのメソッド
    fun folderSelect(folderName: String) {
        selectFolder.value = folderName
    }

    // 選択されたフォルダのリポジトリリストを取得するメソッド
    fun getFavoriteFolderRepository(folderName: String) {
        viewModelScope.launch {
            dbReady.value = false
            favoriteRepositoryList.value =
                _favoriteApplicationService.getFavoriteRepositoryList(folderName)
            dbReady.value = true
        }
    }

    // お気に入りフォルダを新規作成するメソッド
    fun insertNewFavoriteFolder(folderName: String) {
        viewModelScope.launch {
            dbReady.value = false
            val check = _favoriteApplicationService.insertFavoriteFolder(folderName)
            if (check) {
                focusFolderName = folderName
                channelUpdateFavoriteFolder.send(1)
            } else {
                channelNgNewFolder.send(1)
            }
            dbReady.value = true
        }
    }

    // お気に入りフォルダの名前を変更するメソッド
    fun updateFavoriteFolderName(
        newFolderName: String,
        currentFolderName: String,
    ) {
        viewModelScope.launch {
            dbReady.value = false
            val check =
                _favoriteApplicationService.updateFavoriteFolderName(
                    newFolderName,
                    currentFolderName,
                )
            if (check) {
                focusFolderName = newFolderName
                channelUpdateFavoriteFolder.send(1)
            } else {
                channelNgRenameFolder.send(1)
            }
            dbReady.value = true
        }
    }

    // お気に入りフォルダを削除するメソッド
    fun deleteFavoriteFolder(folderName: String) {
        viewModelScope.launch {
            dbReady.value = false
            _favoriteApplicationService.deleteFavoriteFolder(folderName)
            focusFolderName = ""
            channelUpdateFavoriteFolder.send(1)
            dbReady.value = true
        }
    }

    // お気に入りリポジトリをフォルダから削除するメソッド
    fun deleteFavoriteRepository(data: RepositoryEntity) {
        viewModelScope.launch {
            dbReady.value = false
            _favoriteApplicationService.deleteFavoriteRepository(data)
            dbReady.value = true
        }
    }
}
