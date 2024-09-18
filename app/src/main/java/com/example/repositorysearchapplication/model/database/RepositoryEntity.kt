package com.example.repositorysearchapplication.model.database

import androidx.room.Entity

@Entity(tableName = "favorite_repository", primaryKeys = ["id", "saveFolder"])
data class RepositoryEntity(
    val id: String,
    val fullName: String,
    val login: String,
    val language: String,
    val stargazersCount: String,
    val htmlUrl: String,
    val avatarUrl: String,
    val saveFolder: String,
)
