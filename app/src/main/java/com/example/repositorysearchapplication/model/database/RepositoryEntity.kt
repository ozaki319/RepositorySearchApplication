package com.example.repositorysearchapplication.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_repository")
data class RepositoryEntity(
    @PrimaryKey val id: String,
    val fullName: String,
    val login: String,
    val language: String,
    val stargazersCount: String,
    val htmlUrl: String,
    val avatarUrl: String,
)
