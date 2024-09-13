package com.example.repositorysearchapplication.model.database

data class RepositoryEntity(
    val id: String,
    val fullName: String,
    val login: String,
    val language: String,
    val stargazersCount: String,
    val htmlUrl: String,
)
