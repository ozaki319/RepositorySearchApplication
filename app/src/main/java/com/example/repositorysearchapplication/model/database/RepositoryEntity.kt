package com.example.repositorysearchapplication.model.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "favorite_repository",
    primaryKeys = ["id", "saveFolderId"],
    foreignKeys =
        [
            ForeignKey(
                entity = FavoriteFolderEntity::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("saveFolderId"),
                onDelete = ForeignKey.CASCADE,
            ),
        ],
)
@Parcelize
data class RepositoryEntity(
    val id: String,
    val fullName: String,
    val login: String,
    val language: String,
    val stargazersCount: String,
    val htmlUrl: String,
    val avatarUrl: String,
    val saveFolderId: Int,
) : Parcelable
