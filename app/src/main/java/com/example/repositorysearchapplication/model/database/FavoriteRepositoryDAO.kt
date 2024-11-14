package com.example.repositorysearchapplication.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteRepositoryDAO {
    @Query("SELECT * FROM favorite_repository WHERE saveFolderId = :folderId ORDER BY fullName ASC")
    suspend fun get(folderId: Int): List<RepositoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(repositoryData: RepositoryEntity)

    @Delete
    suspend fun delete(repositoryData: RepositoryEntity)
}
