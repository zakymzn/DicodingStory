package com.example.dicodingstory.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.getString
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingstory.R
import com.example.dicodingstory.data.local.entity.StoryEntity
import com.example.dicodingstory.databinding.ItemStoryBinding
import com.example.dicodingstory.ui.detail.DetailActivity
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class MainAdapter : ListAdapter<StoryEntity, MainAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    class MyViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(story: StoryEntity) {
            val context = this@MyViewHolder.itemView.context
            val today = LocalDateTime.now()
            val parsedDateTime = OffsetDateTime.parse(story.createdAt).atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()

            Glide.with(context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)

            binding.apply {
                tvItemName.text = "${story.name}"
                tvItemPostTime.text = if (ChronoUnit.YEARS.between(parsedDateTime, today) > 0) {
                    "${ChronoUnit.YEARS.between(parsedDateTime, today)} ${getString(context, R.string.years_ago)}"
                } else if (ChronoUnit.MONTHS.between(parsedDateTime, today) in 1..12) {
                    "${ChronoUnit.MONTHS.between(parsedDateTime, today)} ${getString(context, R.string.months_ago)}"
                } else if (ChronoUnit.WEEKS.between(parsedDateTime, today) in 1..4) {
                    "${ChronoUnit.WEEKS.between(parsedDateTime, today)} ${getString(context, R.string.weeks_ago)}"
                } else if (ChronoUnit.DAYS.between(parsedDateTime, today) in 1..30) {
                    "${ChronoUnit.DAYS.between(parsedDateTime, today)} ${getString(context, R.string.days_ago)}"
                } else if (ChronoUnit.HOURS.between(parsedDateTime, today) in 1..24) {
                    "${ChronoUnit.HOURS.between(parsedDateTime, today)} ${getString(context, R.string.hours_ago)}"
                } else if (ChronoUnit.MINUTES.between(parsedDateTime, today) in 1..60) {
                    "${ChronoUnit.MINUTES.between(parsedDateTime, today)} ${getString(context, R.string.minutes_ago)}"
                } else if (ChronoUnit.SECONDS.between(parsedDateTime, today) in 1..60) {
                    "${ChronoUnit.SECONDS.between(parsedDateTime, today)} ${getString(context, R.string.seconds_ago)}"
                } else {
                    getString(context, R.string.just_now)
                }
                itemStory.setOnClickListener {
                    val intent = Intent(itemStory.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_STORY, story)

                    val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemStory.context as Activity,
                        Pair(ivItemPhoto, "detail_photo"),
                        Pair(tvItemName, "detail_name"),
                        Pair(tvItemPostTime, "detail_post_time"),
                    )

                    itemStory.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<StoryEntity> = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return true
            }
        }
    }
}