package com.example.repositorysearchapplication.model.applicationservice

import android.app.Application
import androidx.room.withTransaction
import com.example.repositorysearchapplication.model.database.AppDatabase
import com.example.repositorysearchapplication.model.database.FavoriteFolderEntity
import com.example.repositorysearchapplication.model.database.RepositoryEntity
import com.example.repositorysearchapplication.model.repository.FavoriteRepository

// お気に入り関連のアプリケーションサービス
class FavoriteApplicationService(
    application: Application,
) {
    private val _db: AppDatabase = AppDatabase.getDatabase(application)
    private val _favoriteRepository = FavoriteRepository(application)

    // お気に入りフォルダリストを取得するメソッド
    suspend fun getFavoriteFolderList(): List<String> = _favoriteRepository.getFavoriteFolderList()

    // GitHubリポジトリを作成済みフォルダにお気に入り登録するメソッド
    suspend fun registerFavoriteRepository(
        folderName: String,
        repositoryToRegister: RepositoryEntity,
    ) {
        _db.withTransaction {
            val saveFolderId = _favoriteRepository.getFavoriteFolderId(folderName)
            val saveData = registerSaveFolderId(repositoryToRegister, saveFolderId)
            _favoriteRepository.insertFavoriteRepository(saveData)
        }
    }

    // GitHubリポジトリを新規作成フォルダにお気に入り登録するメソッド
    // 戻り値は成否 (true: 登録成功, false: 登録失敗=同名のフォルダが既に存在) を表す
    suspend fun registerFavoriteRepositoryIntoNewFolder(
        newFolderName: String,
        repositoryToRegister: RepositoryEntity,
    ): Boolean =
        try {
            _db.withTransaction {
                val saveFolderId =
                    _favoriteRepository
                        .insertFavoriteFolder(FavoriteFolderEntity(0, newFolderName))
                        .toInt()
                val saveData = registerSaveFolderId(repositoryToRegister, saveFolderId)
                _favoriteRepository.insertFavoriteRepository(saveData)
                true
            }
        } catch (e: Exception) {
            false
        }

    // 選択されたフォルダのリポジトリリストを取得するメソッド
    suspend fun getFavoriteRepositoryList(folderName: String): List<RepositoryEntity> =
        _db.withTransaction {
            val folderId = _favoriteRepository.getFavoriteFolderId(folderName)
            val repositoryList = _favoriteRepository.getFavoriteRepository(folderId)
            return@withTransaction repositoryList
        }

    // お気に入りフォルダを新規作成するメソッド
    suspend fun insertFavoriteFolder(newFolderName: String): Boolean =
        try {
            _favoriteRepository.insertFavoriteFolder(FavoriteFolderEntity(0, newFolderName))
            true
        } catch (e: Exception) {
            false
        }

    // お気に入りフォルダの名前を変更するメソッド
    suspend fun updateFavoriteFolderName(
        newFolderName: String,
        currentFolderName: String,
    ): Boolean =
        try {
            _favoriteRepository.updateFavoriteFolderName(newFolderName, currentFolderName)
            true
        } catch (e: Exception) {
            false
        }

    // お気に入りフォルダを削除するメソッド
    suspend fun deleteFavoriteFolder(folderName: String) {
        _db.withTransaction {
            val id = _favoriteRepository.getFavoriteFolderId(folderName)
            _favoriteRepository.deleteFavoriteFolder(FavoriteFolderEntity(id, folderName))
        }
    }

    // お気に入りリポジトリをフォルダから削除するメソッド
    suspend fun deleteFavoriteRepository(data: RepositoryEntity) {
        _favoriteRepository.deleteFavoriteRepository(data)
    }

    // リポジトリ保存先フォルダIDの登録
    private fun registerSaveFolderId(
        repositoryToRegister: RepositoryEntity,
        saveFolderId: Int,
    ): RepositoryEntity =
        RepositoryEntity(
            repositoryToRegister.id,
            repositoryToRegister.fullName,
            repositoryToRegister.login,
            repositoryToRegister.language,
            repositoryToRegister.stargazersCount,
            repositoryToRegister.htmlUrl,
            repositoryToRegister.avatarUrl,
            saveFolderId,
        )
}
