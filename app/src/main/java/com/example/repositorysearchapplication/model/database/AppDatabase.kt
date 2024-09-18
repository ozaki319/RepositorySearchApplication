package com.example.repositorysearchapplication.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RepositoryEntity::class, FavoriteFolderEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private var _instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (_instance == null) {
                _instance =
                    Room
                        .databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "favorite_repository_db",
                        ).build()
            }
            return _instance!!
        }
    }

    abstract fun createFavoriteRepositoryDAO(): FavoriteRepositoryDAO

    abstract fun createFavoriteFolderDAO(): FavoriteFolderDAO
}
