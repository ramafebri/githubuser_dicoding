package com.example.consumerapp.helper

import android.database.Cursor
import com.example.consumerapp.database.DatabaseContract
import com.example.consumerapp.model.FavoriteGituser
import com.example.consumerapp.model.ListGituser

object MappingHelper {
    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<FavoriteGituser> {
        val favoriteList = ArrayList<FavoriteGituser>()
        favoriteCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                val type = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.TYPE))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR_URL))
                favoriteList.add(FavoriteGituser(id, username, type, avatar))
            }
        }
        return favoriteList
    }

    fun mapCursorToObject(notesCursor: Cursor?): FavoriteGituser {
        var favoriteGituser = FavoriteGituser()
        notesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
            val type = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.TYPE))
            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR_URL))
            favoriteGituser = FavoriteGituser(id, username, type, avatar)
        }
        return favoriteGituser
    }

    fun convertToListGituser(gituser: FavoriteGituser): ListGituser{
        return ListGituser(
            gistsUrl = "",
            reposUrl = "",
            followingUrl = "",
            starredUrl = "",
            login = gituser.login,
            followersUrl = "",
            type = gituser.type,
            url = "",
            subscriptionsUrl = "",
            score = 0.0,
            receivedEventsUrl = "",
            avatarUrl = gituser.avatarUrl,
            eventsUrl = "",
            htmlUrl = "",
            siteAdmin = false,
            id = 0,
            gravatarId = "",
            nodeId = "",
            organizationsUrl = "",
        )
    }
}