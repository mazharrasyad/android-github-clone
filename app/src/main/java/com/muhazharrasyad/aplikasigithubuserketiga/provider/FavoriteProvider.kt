package com.muhazharrasyad.aplikasigithubuserketiga.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.muhazharrasyad.aplikasigithubuserketiga.db.DatabaseContract
import com.muhazharrasyad.aplikasigithubuserketiga.db.DatabaseContract.AUTHORITY
import com.muhazharrasyad.aplikasigithubuserketiga.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.muhazharrasyad.aplikasigithubuserketiga.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.muhazharrasyad.aplikasigithubuserketiga.db.FavoriteHelper

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val FAVORITE = 1
        private const val FAVORITE_USERNAME = 2
        private lateinit var favoriteHelper: FavoriteHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAVORITE_USERNAME)
        }
    }

    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query(uri: Uri, strings: Array<String>?, s: String?, strings1: Array<String>?, s1: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAVORITE -> favoriteHelper.queryAll()
            FAVORITE_USERNAME -> favoriteHelper.queryByUsername(uri.lastPathSegment.toString())
            else -> null
        }
    }


    override fun getType(uri: Uri): String? {
        return null
    }


    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (FAVORITE) {
            sUriMatcher.match(uri) -> favoriteHelper.insert(contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when (FAVORITE_USERNAME) {
            sUriMatcher.match(uri) -> favoriteHelper.delete(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }

}