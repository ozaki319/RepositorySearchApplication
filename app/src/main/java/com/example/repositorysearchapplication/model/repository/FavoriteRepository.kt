package com.example.repositorysearchapplication.model.repository

import android.app.Application
import com.example.repositorysearchapplication.model.database.AppDatabase
import com.example.repositorysearchapplication.model.database.FavoriteFolderEntity
import com.example.repositorysearchapplication.model.database.RepositoryEntity

class FavoriteRepository(
    application: Application,
) {
    private val _db: AppDatabase = AppDatabase.getDatabase(application)
    private val favoriteRepositoryDAO = _db.createFavoriteRepositoryDAO()
    private val favoriteFolderDAO = _db.createFavoriteFolderDAO()

    suspend fun getFavoriteRepository(folderId: Int): List<RepositoryEntity> = favoriteRepositoryDAO.get(folderId)

    suspend fun insertFavoriteRepository(repositoryData: RepositoryEntity) = favoriteRepositoryDAO.insert(repositoryData)

    suspend fun deleteFavoriteRepository(repositoryData: RepositoryEntity) = favoriteRepositoryDAO.delete(repositoryData)

    suspend fun getFavoriteFolderList(): List<String> = favoriteFolderDAO.getList()

    suspend fun getFavoriteFolderId(folderName: String) = favoriteFolderDAO.getId(folderName)

    suspend fun updateFavoriteFolderName(
        newFolderName: String,
        currentFolderName: String,
    ) = favoriteFolderDAO.update(newFolderName, currentFolderName)

    suspend fun insertFavoriteFolder(folderData: FavoriteFolderEntity) = favoriteFolderDAO.insert(folderData)

    suspend fun deleteFavoriteFolder(folderData: FavoriteFolderEntity) = favoriteFolderDAO.delete(folderData)
}
