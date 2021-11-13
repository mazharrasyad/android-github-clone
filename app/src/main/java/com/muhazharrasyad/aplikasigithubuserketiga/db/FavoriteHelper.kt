package com.muhazharrasyad.aplikasigithubuserketiga.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.muhazharrasyad.aplikasigithubuserketiga.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.muhazharrasyad.aplikasigithubuserketiga.db.DatabaseContract.FavoriteColumns.Companion.USERNAME

class FavoriteHelper(context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteHelper? = null

        fun getInstance(context: Context): FavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            null
        )
    }

    fun queryByUsername(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$USERNAME = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun delete(username: String): Int {
        return database.delete(DATABASE_TABLE, "$USERNAME = '$username'", null)
    }
}