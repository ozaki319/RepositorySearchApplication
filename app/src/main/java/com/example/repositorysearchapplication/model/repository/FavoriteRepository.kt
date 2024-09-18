package com.example.repositorysearchapplication.model.repository

import android.app.Application
import com.example.repositorysearchapplication.model.database.AppDatabase
import com.example.repositorysearchapplication.model.database.FavoriteFolderEntity
import com.example.repositorysearchapplication.model.database.RepositoryEntity

class FavoriteRepository(
    application: Application,
) {
    private val _db: AppDatabase = AppDatabase.getDatabase(application)

//    fun getFavoriteRepository(): LiveData<List<RepositoryEntity>> {
//        val favoriteRepositoryDAO = _db.createFavoriteRepositoryDAO()
//        return favoriteRepositoryDAO.getALl()
//    }

    suspend fun getFavoriteFolderRepository(folderName: String): List<RepositoryEntity> {
        val favoriteRepositoryDAO = _db.createFavoriteRepositoryDAO()
        return favoriteRepositoryDAO.getFolderData(folderName)
    }

    suspend fun insertFavoriteRepository(repositoryData: RepositoryEntity) {
        val favoriteRepositoryDAO = _db.createFavoriteRepositoryDAO()
        return favoriteRepositoryDAO.insert(repositoryData)
    }

    suspend fun deleteFavoriteRepository(repositoryData: RepositoryEntity) {
        val favoriteRepositoryDAO = _db.createFavoriteRepositoryDAO()
        return favoriteRepositoryDAO.delete(repositoryData)
    }

//    fun getFavoriteFolder(): LiveData<List<FavoriteFolderEntity>> {
//        val favoriteFolderDAO = _db.createFavoriteFolderDAO()
//        return favoriteFolderDAO.getAll()
//    }

    suspend fun getFavoriteFolderName(): List<String> {
        val favoriteFolderDAO = _db.createFavoriteFolderDAO()
        return favoriteFolderDAO.getFolderName()
    }

    suspend fun insertFavoriteFolder(folderData: FavoriteFolderEntity) {
        val favoriteFolderDAO = _db.createFavoriteFolderDAO()
        return favoriteFolderDAO.insert(folderData)
    }

    suspend fun deleteFavoriteFolder(folderData: FavoriteFolderEntity) {
        val favoriteFolderDAO = _db.createFavoriteFolderDAO()
        return favoriteFolderDAO.delete(folderData)
    }
}
