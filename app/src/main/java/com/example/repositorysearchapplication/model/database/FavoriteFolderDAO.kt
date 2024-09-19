package com.example.repositorysearchapplication.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteFolderDAO {
    @Query("SELECT folderName FROM favorite_folder ORDER BY folderName ASC")
    suspend fun get(): List<String>

    @Query("SELECT COUNT(*) FROM favorite_folder WHERE folderName = :folderName")
    suspend fun count(folderName: String): Int

    @Query("UPDATE favorite_folder SET folderName = :newFolderName WHERE folderName = :currentFolderName")
    suspend fun update(
        newFolderName: String,
        currentFolderName: String,
    )

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(folderData: FavoriteFolderEntity)

    @Delete
    suspend fun delete(folderData: FavoriteFolderEntity)
}
