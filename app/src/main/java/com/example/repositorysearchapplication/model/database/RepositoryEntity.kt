package com.example.repositorysearchapplication.model.database

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_repository", primaryKeys = ["id", "saveFolder"])
@Parcelize
data class RepositoryEntity(
    val id: String,
    val fullName: String,
    val login: String,
    val language: String,
    val stargazersCount: String,
    val htmlUrl: String,
    val avatarUrl: String,
    val saveFolder: String,
) : Parcelable
