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

    // ViewModelのイベントを通知するFlow
    private val channelGetFavoriteFolderList = Channel<Int>(capacity = Channel.UNLIMITED)
    var eventGetFavoriteFolderList = channelGetFavoriteFolderList.receiveAsFlow()
    private val channelUpdateFavoriteFolder = Channel<Int>(capacity = Channel.UNLIMITED)
    var eventUpdateFavoriteFolder = channelUpdateFavoriteFolder.receiveAsFlow()

    // お気に入りフォルダリストを取得するメソッド
    fun getFavoriteFolderList() {
        viewModelScope.launch {
            favoriteFolderList = _favoriteRepository.getFavoriteFolderName()
            channelGetFavoriteFolderList.send(1)
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
            _favoriteRepository.insertFavoriteFolder(FavoriteFolderEntity(folderName))
            channelUpdateFavoriteFolder.send(1)
        }
    }

    // お気に入りフォルダの名前を変更するメソッド
    fun updateFavoriteFolderName(
        newFolderName: String,
        currentFolderName: String,
    ) {
        viewModelScope.launch {
            _favoriteRepository.updateFavoriteFolderName(newFolderName, currentFolderName)
            channelUpdateFavoriteFolder.send(1)
        }
    }

    // お気に入りリポジトリの保存フォルダ名を変更するメソッド
    fun updateFavoriteRepository(
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
