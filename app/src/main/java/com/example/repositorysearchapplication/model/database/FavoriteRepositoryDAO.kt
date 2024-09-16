package com.example.repositorysearchapplication.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteRepositoryDAO {
    @Query("SELECT * FROM favorite_repository ORDER BY fullName ASC")
    fun getALl(): LiveData<List<RepositoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(repositoryData: RepositoryEntity)

    @Delete
    suspend fun delete(repositoryData: RepositoryEntity)
}
