package com.andazlan.footballclub.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.andazlan.footballclub.model.Favorite
import org.jetbrains.anko.db.*

class MyDatabaseOpenHelper(context: Context) : ManagedSQLiteOpenHelper(context, "FavoriteTeam.db",
        null, 1) {
    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(context: Context): MyDatabaseOpenHelper {
            if (instance == null) {
                instance = MyDatabaseOpenHelper(context.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(Favorite.TABLE_FAVORITE, true,
                Favorite.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                Favorite.TEAM_ID to TEXT + UNIQUE,
                Favorite.TEAM_NAME to TEXT,
                Favorite.TEAM_BADGE to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(Favorite.TABLE_FAVORITE, true)
    }
}