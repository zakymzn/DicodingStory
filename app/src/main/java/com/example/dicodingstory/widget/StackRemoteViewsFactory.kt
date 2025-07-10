package com.example.dicodingstory.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.dicodingstory.R
import com.example.dicodingstory.data.local.entity.StoryEntity
import com.example.dicodingstory.data.local.room.StoryDatabase

internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

    private val stories = mutableListOf<StoryEntity>()

    override fun onCreate() {}

    override fun onDataSetChanged() {
        val dao = StoryDatabase.getInstance(mContext).storyDao()
        val storyList = dao.getAllStoryWidget()
        stories.clear()
        stories.addAll(storyList)
    }

    override fun onDestroy() {
        stories.clear()
    }

    override fun getCount(): Int = stories.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.item_story_widget)
        val story = stories[position]

        rv.setTextViewText(R.id.tv_item_widget_name, story.name)

        val bitmap = try {
            Glide.with(mContext)
                .asBitmap()
                .load(story.photoUrl)
                .submit()
                .get()
        } catch (e: Exception) {
            null
        }

        if (bitmap != null) {
            rv.setImageViewBitmap(R.id.iv_item_widget_photo, bitmap)
        }

        val extras = bundleOf(
            StoryWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.iv_item_widget_photo, fillInIntent)

        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true
}