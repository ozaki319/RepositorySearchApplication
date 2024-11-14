package com.example.repositorysearchapplication.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_folder", indices = [Index(value = ["folder_name"], unique = true)])
data class FavoriteFolderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "folder_name") val folderName: String,
)
