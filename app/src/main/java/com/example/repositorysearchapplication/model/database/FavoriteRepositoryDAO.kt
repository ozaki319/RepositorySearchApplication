package com.example.repositorysearchapplication.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteRepositoryDAO {
    @Query("SELECT * FROM favorite_repository WHERE saveFolder = :folderName ORDER BY fullName ASC")
    suspend fun get(folderName: String): List<RepositoryEntity>

    @Query("UPDATE favorite_repository SET saveFolder = :newFolderName WHERE saveFolder = :currentFolderName")
    suspend fun update(
        newFolderName: String,
        currentFolderName: String,
    )

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(repositoryData: RepositoryEntity)

    @Delete
    suspend fun delete(repositoryData: RepositoryEntity)

    @Query("DELETE FROM favorite_repository WHERE saveFolder = :folderName")
    suspend fun deleteAll(folderName: String)
}
