package com.example.repositorysearchapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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
    private val channelGetFavoriteFolderList = Channel<List<String>>(capacity = Channel.UNLIMITED)
    var eventGetFavoriteFolderList = channelGetFavoriteFolderList.receiveAsFlow()

    // お気に入りフォルダリストを取得するメソッド
    fun getFavoriteFolderList() {
        viewModelScope.launch {
            favoriteFolderList = _favoriteRepository.getFavoriteFolderName()
            channelGetFavoriteFolderList.send(favoriteFolderList)
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
                _favoriteRepository.getFavoriteFolderRepository(folderName)
        }
    }

    // お気に入りから削除するメソッド
    fun deleteFavoriteRepository(data: RepositoryEntity) {
        viewModelScope.launch {
            _favoriteRepository.deleteFavoriteRepository(data)
        }
    }
}
