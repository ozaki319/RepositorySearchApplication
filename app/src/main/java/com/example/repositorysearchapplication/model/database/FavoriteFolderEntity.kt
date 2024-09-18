package com.example.repositorysearchapplication.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_folder")
data class FavoriteFolderEntity(
    @PrimaryKey val folderName: String,
)
