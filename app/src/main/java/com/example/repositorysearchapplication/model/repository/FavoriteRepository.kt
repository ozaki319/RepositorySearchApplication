package com.example.repositorysearchapplication.model.repository

import android.app.Application
import androidx.room.withTransaction
import com.example.repositorysearchapplication.model.database.AppDatabase
import com.example.repositorysearchapplication.model.database.FavoriteFolderEntity
import com.example.repositorysearchapplication.model.database.RepositoryEntity

class FavoriteRepository(
    application: Application,
) {
    private val _db: AppDatabase = AppDatabase.getDatabase(application)

    suspend fun getFavoriteRepository(folderName: String): List<RepositoryEntity> {
        val favoriteRepositoryDAO = _db.createFavoriteRepositoryDAO()
        return favoriteRepositoryDAO.get(folderName)
    }

    suspend fun updateFavoriteRepository(
        newFolderName: String,
        currentFolderName: String,
    ) {
        val favoriteRepositoryDAO = _db.createFavoriteRepositoryDAO()
        return favoriteRepositoryDAO.update(newFolderName, currentFolderName)
    }

    suspend fun insertFavoriteRepository(repositoryData: RepositoryEntity) {
        val favoriteRepositoryDAO = _db.createFavoriteRepositoryDAO()
        return favoriteRepositoryDAO.insert(repositoryData)
    }

    suspend fun deleteFavoriteRepository(repositoryData: RepositoryEntity) {
        val favoriteRepositoryDAO = _db.createFavoriteRepositoryDAO()
        return favoriteRepositoryDAO.delete(repositoryData)
    }

    suspend fun deleteAllFavoriteRepository(folderName: String) {
        val favoriteRepositoryDAO = _db.createFavoriteRepositoryDAO()
        return favoriteRepositoryDAO.deleteAll(folderName)
    }

    suspend fun getFavoriteFolderName(): List<String> {
        val favoriteFolderDAO = _db.createFavoriteFolderDAO()
        return favoriteFolderDAO.get()
    }

//    suspend fun countFavoriteFolder(folderName: String): Int {
//        val favoriteFolderDAO = _db.createFavoriteFolderDAO()
//        return favoriteFolderDAO.count(folderName)
//    }

    //    suspend fun updateFavoriteFolderName(
//        newFolderName: String,
//        currentFolderName: String,
//    ) {
//        val favoriteFolderDAO = _db.createFavoriteFolderDAO()
//        return favoriteFolderDAO.update(newFolderName, currentFolderName)
//    }
    suspend fun updateFavoriteFolderName(
        newFolderName: String,
        currentFolderName: String,
    ): Boolean {
        var response = false
        val favoriteFolderDAO = _db.createFavoriteFolderDAO()
        _db.withTransaction {
            if (favoriteFolderDAO.count(newFolderName) < 1) {
                favoriteFolderDAO.update(newFolderName, currentFolderName)
                response = true
            }
        }
        return response
    }

    //    suspend fun insertFavoriteFolder(folderData: FavoriteFolderEntity) {
//        val favoriteFolderDAO = _db.createFavoriteFolderDAO()
//        return favoriteFolderDAO.insert(folderData)
//    }
    suspend fun insertFavoriteFolder(folderName: String): Boolean {
        var response = false
        val favoriteFolderDAO = _db.createFavoriteFolderDAO()
        _db.withTransaction {
            if (favoriteFolderDAO.count(folderName) < 1) {
                favoriteFolderDAO.insert(FavoriteFolderEntity(folderName))
                response = true
            }
        }
        return response
    }

    suspend fun deleteFavoriteFolder(folderData: FavoriteFolderEntity) {
        val favoriteFolderDAO = _db.createFavoriteFolderDAO()
        return favoriteFolderDAO.delete(folderData)
    }
}
