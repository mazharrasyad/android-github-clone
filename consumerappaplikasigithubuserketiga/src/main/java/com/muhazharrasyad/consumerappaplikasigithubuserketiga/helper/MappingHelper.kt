package com.muhazharrasyad.consumerappaplikasigithubuserketiga.helper

import android.database.Cursor
import com.muhazharrasyad.consumerappaplikasigithubuserketiga.db.DatabaseContract
import com.muhazharrasyad.consumerappaplikasigithubuserketiga.model.Github

object MappingHelper {

    fun mapCursorToArrayList(favoritesCursor: Cursor?): ArrayList<Github> {
        val favoritesList = ArrayList<Github>()

        favoritesCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.NAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
                favoritesList.add(Github(username, name, avatar))
            }
        }
        return favoritesList
    }

    fun mapCursorToObject(favoritesCursor: Cursor?): Github {
        var favorite = Github()
        favoritesCursor?.apply {
            moveToFirst()
            val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
            val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.NAME))
            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
            favorite = Github(username, name, avatar)
        }
        return favorite
    }
}