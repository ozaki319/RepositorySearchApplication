package com.example.repositorysearchapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.repositorysearchapplication.model.database.FavoriteFolderEntity
import com.example.repositorysearchapplication.model.database.RepositoryEntity
import com.example.repositorysearchapplication.model.repository.FavoriteRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val _favoriteRepository = FavoriteRepository(application)

    val favoriteRepositoryList = MutableLiveData<List<RepositoryEntity>>()
    var selectRepository = RepositoryEntity("", "", "", "", "", "", "", "")
    var favoriteFolderList: List<String> = listOf()
    val selectFolder = MutableLiveData<String>()
    var focusFolderName = ""
    var indexSelectFolder = 0

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
            favoriteFolderList = _favoriteRepository.getFavoriteFolderName()
            indexSelectFolder = favoriteFolderList.indexOf(focusFolderName)
            channelGetFavoriteFolderList.send(1)
            if (favoriteFolderList.isEmpty()) {
                selectFolder.value = ""
            }
        }
    }

    // フォルダが選択されたときのメソッド
    fun folderSelect(folderName: String) {
        selectFolder.value = folderName
    }

    // 選択されたフォルダのリポジトリリストを取得するメソッド
    fun getFavoriteFolderRepository(folderName: String) {
        viewModelScope.launch {
            favoriteRepositoryList.value =
                _favoriteRepository.getFavoriteRepository(folderName)
        }
    }

    // お気に入りフォルダを新規作成するメソッド
    fun insertNewFavoriteFolder(folderName: String) {
        viewModelScope.launch {
            val check = _favoriteRepository.countFavoriteFolder(folderName) < 1
            if (check) {
                _favoriteRepository.insertFavoriteFolder(FavoriteFolderEntity(folderName))
                focusFolderName = folderName
                channelUpdateFavoriteFolder.send(1)
            } else {
                channelNgNewFolder.send(1)
            }
        }
    }

    // お気に入りフォルダの名前を変更するメソッド
    fun updateFavoriteFolderName(
        newFolderName: String,
        currentFolderName: String,
    ) {
        viewModelScope.launch {
            val check = _favoriteRepository.countFavoriteFolder(newFolderName) < 1
            if (check) {
                _favoriteRepository.updateFavoriteFolderName(newFolderName, currentFolderName)
                updateFavoriteRepository(newFolderName, currentFolderName)
                focusFolderName = newFolderName
                channelUpdateFavoriteFolder.send(1)
            } else {
                channelNgRenameFolder.send(1)
            }
        }
    }

    // お気に入りリポジトリの保存フォルダ名を変更するメソッド
    private fun updateFavoriteRepository(
        newFolderName: String,
        currentFolderName: String,
    ) {
        viewModelScope.launch {
            _favoriteRepository.updateFavoriteRepository(newFolderName, currentFolderName)
        }
    }

    // お気に入りフォルダを削除するメソッド
    fun deleteFavoriteFolder(folderName: String) {
        viewModelScope.launch {
            _favoriteRepository.deleteFavoriteFolder(FavoriteFolderEntity(folderName))
            _favoriteRepository.deleteAllFavoriteRepository(folderName)
            focusFolderName = ""
            channelUpdateFavoriteFolder.send(1)
        }
    }

    // お気に入りリポジトリをフォルダから削除するメソッド
    fun deleteFavoriteRepository(data: RepositoryEntity) {
        viewModelScope.launch {
            _favoriteRepository.deleteFavoriteRepository(data)
        }
    }
}
