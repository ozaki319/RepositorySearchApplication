package com.example.repositorysearchapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.repositorysearchapplication.model.database.RepositoryEntity
import com.example.repositorysearchapplication.model.repository.FavoriteRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val _favoriteRepository = FavoriteRepository(application)

    val favoriteRepositoryList: LiveData<List<RepositoryEntity>> = _favoriteRepository.getFavoriteRepository()
    var selectRepository = RepositoryEntity("", "", "", "", "", "", "")

    // お気に入りから削除するメソッド
    fun deleteFavoriteRepository(data: RepositoryEntity)  {
        viewModelScope.launch {
            _favoriteRepository.deleteFavoriteRepository(data)
        }
    }
}
