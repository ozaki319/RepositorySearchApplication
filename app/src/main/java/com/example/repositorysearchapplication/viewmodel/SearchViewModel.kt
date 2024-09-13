package com.example.repositorysearchapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.repositorysearchapplication.model.database.RepositoryEntity
import com.example.repositorysearchapplication.model.repository.GetRepositoryDataRepository
import kotlinx.coroutines.launch

class SearchViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val _getRepositoryDataRepository = GetRepositoryDataRepository()

    var searchWord: String = ""
    var page: Int = 1
    val repositoryList = MutableLiveData<List<RepositoryEntity>>()
    var selectRepository = RepositoryEntity("", "", "", "", "", "")

    // 検索結果をクリアするメソッド
    fun clearRepositoryList() {
        page = 1
        repositoryList.value = listOf()
    }

    // 検索するメソッド
    fun addRepositoryList() {
        viewModelScope.launch {
            repositoryList.value =
                _getRepositoryDataRepository.getRepositoryList(
                    searchWord,
                    page,
                    repositoryList.value!!,
                )
            page += 1
        }
    }
}
