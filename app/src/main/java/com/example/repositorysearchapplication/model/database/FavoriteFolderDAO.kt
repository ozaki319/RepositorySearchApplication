package com.example.repositorysearchapplication.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteFolderDAO {
//    @Query("SELECT * FROM favorite_folder ORDER BY folderName ASC")
//    fun getAll(): LiveData<List<FavoriteFolderEntity>>

    @Query("SELECT folderName FROM favorite_folder ORDER BY folderName ASC")
    suspend fun getFolderName(): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(folderData: FavoriteFolderEntity)

    @Delete
    suspend fun delete(folderData: FavoriteFolderEntity)
}
