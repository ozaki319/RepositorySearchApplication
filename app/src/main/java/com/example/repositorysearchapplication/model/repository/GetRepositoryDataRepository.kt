package com.example.repositorysearchapplication.model.repository

import android.net.Uri
import com.example.repositorysearchapplication.model.database.RepositoryEntity
import org.json.JSONException
import org.json.JSONObject

class GetRepositoryDataRepository {
    private val _getJsonDataRepository = GetJsonDataRepository()

    suspend fun getRepositoryList(
        q: String,
        page: Int,
        oldList: List<RepositoryEntity>,
    ): List<RepositoryEntity> {
        // URIの生成
        val uri =
            Uri
                .parse("https://api.github.com/search/repositories")
                .buildUpon()
                .appendQueryParameter("q", q)
                .appendQueryParameter("page", page.toString())
                .appendQueryParameter("sort", "stars")
                .build()
        // 取得したデータを格納するリスト
        val newList = mutableListOf<RepositoryEntity>()
        // APIから取得したデータをJSONオブジェクトとして定義
        val jsonData = _getJsonDataRepository.getJsonData(uri)
        try {
            val jsonObj = JSONObject(jsonData)
            val itemsArray = jsonObj.getJSONArray("items")
            for (i in 0 until itemsArray.length()) {
                // 必要なオブジェクトを抽出
                val itemObj = itemsArray.getJSONObject(i)
                val ownerObj = itemObj.getJSONObject("owner")
                // 必要な値を抽出
                val id = itemObj.getString("id")
                val fullName = itemObj.getString("full_name")
                val login = ownerObj.getString("login")
                val language = itemObj.getString("language")
                val stargazersCount = itemObj.getString("stargazers_count")
                val htmlUrl = itemObj.getString("html_url")
                val avatarUrl = ownerObj.getString("avatar_url")
                // リストに追加
                newList.add(
                    RepositoryEntity(
                        id,
                        fullName,
                        login,
                        language,
                        stargazersCount,
                        htmlUrl,
                        avatarUrl,
                        0,
                    ),
                )
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return (oldList + newList)
    }
}
