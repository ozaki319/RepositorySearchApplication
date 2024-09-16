package com.example.repositorysearchapplication.model.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.repositorysearchapplication.model.database.AppDatabase
import com.example.repositorysearchapplication.model.database.RepositoryEntity

class FavoriteRepository(
    application: Application,
) {
    private val _db: AppDatabase = AppDatabase.getDatabase(application)

    fun getFavoriteRepository(): LiveData<List<RepositoryEntity>>  {
        val favoriteRepositoryDAO = _db.createFavoriteRepositoryDAO()
        return favoriteRepositoryDAO.getALl()
    }

    suspend fun insertFavoriteRepository(repositoryData: RepositoryEntity)  {
        val favoriteRepositoryDAO = _db.createFavoriteRepositoryDAO()
        return favoriteRepositoryDAO.insert(repositoryData)
    }

    suspend fun deleteFavoriteRepository(repositoryData: RepositoryEntity)  {
        val favoriteRepositoryDAO = _db.createFavoriteRepositoryDAO()
        return favoriteRepositoryDAO.delete(repositoryData)
    }
}
