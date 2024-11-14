package com.example.repositorysearchapplication.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteFolderDAO {
    @Query("SELECT folder_name FROM favorite_folder ORDER BY folder_name ASC")
    suspend fun getList(): List<String>

    @Query("SELECT id FROM favorite_folder WHERE folder_name = :folderName")
    suspend fun getId(folderName: String): Int

    @Query("SELECT COUNT(*) FROM favorite_folder WHERE folder_name = :folderName")
    suspend fun count(folderName: String): Int

    @Query("UPDATE favorite_folder SET folder_name = :newFolderName WHERE folder_name = :currentFolderName")
    suspend fun update(
        newFolderName: String,
        currentFolderName: String,
    )

    @Insert
    suspend fun insert(folderData: FavoriteFolderEntity): Long

    @Delete
    suspend fun delete(folderData: FavoriteFolderEntity)
}
