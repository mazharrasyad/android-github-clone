package com.muhazharrasyad.aplikasigithubuserketiga.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.muhazharrasyad.aplikasigithubuserketiga.db.DatabaseContract.FavoriteColumns
import com.muhazharrasyad.aplikasigithubuserketiga.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbfavorite"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                " (${FavoriteColumns.USERNAME} TEXT NOT NULL," +
                " ${FavoriteColumns.NAME} TEXT NOT NULL," +
                " ${FavoriteColumns.AVATAR} TEXT NOT NULL," +
                " ${FavoriteColumns.FAVORITE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}