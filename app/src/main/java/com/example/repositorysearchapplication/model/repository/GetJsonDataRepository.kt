package com.example.repositorysearchapplication.model.repository

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

class GetJsonDataRepository {
    suspend fun getJsonData(uri: Uri): String {
        val response =
            withContext(Dispatchers.IO) {
                // APIから取得した情報を入れるための変数
                var httpResult = ""

                try {
                    val urlObj = URL(uri.toString())
                    val br = BufferedReader(InputStreamReader(urlObj.openStream()))
                    httpResult = br.readText()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return@withContext httpResult
            }
        return response
    }
}
