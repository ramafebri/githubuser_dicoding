package com.example.githubuser.widget

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.example.githubuser.R
import com.example.githubuser.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.githubuser.helper.MappingHelper
import com.example.githubuser.model.FavoriteGituser
import java.net.URL

internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {
    private var favoriteGituser: ArrayList<FavoriteGituser>? = null
    private var cursor: Cursor? = null
    private var resolver: ContentResolver = mContext.contentResolver

    override fun onCreate() {
        cursor = resolver.query(
            CONTENT_URI, null, null, null, null
        )
        if (cursor != null){
            favoriteGituser = MappingHelper.mapCursorToArrayList(cursor)
        }
    }

    override fun onDataSetChanged() {
        cursor?.close()
        val identityToken = Binder.clearCallingIdentity()

        cursor = resolver.query(CONTENT_URI, null, null, null, null)
        if (cursor != null){
            favoriteGituser = MappingHelper.mapCursorToArrayList(cursor)
        }

        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int {
        return favoriteGituser?.size!!
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        val data = favoriteGituser?.get(position)

        val bitmapImage: Bitmap?
        val urlImage = URL(data?.avatarUrl)
        bitmapImage = BitmapFactory.decodeStream(urlImage.openConnection().getInputStream())

        if (bitmapImage != null) {
            rv.setImageViewBitmap(R.id.imageView, bitmapImage)
        }

        val extras = bundleOf(
            FavoritWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}