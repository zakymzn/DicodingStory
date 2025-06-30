package com.example.dicodingstory.ui.main

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingstory.data.local.entity.StoryEntity
import com.example.dicodingstory.databinding.ItemStoryBinding
import com.example.dicodingstory.ui.detail.DetailActivity
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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

    class MyViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(story: StoryEntity) {
            val today = LocalDateTime.now()
            val parsedDateTime = OffsetDateTime.parse(story.createdAt).atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()

            Glide.with(this@MyViewHolder.itemView.context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)

            binding.apply {
                tvItemName.text = "${story.name}"
                tvItemPostTime.text = if (ChronoUnit.YEARS.between(parsedDateTime, today) > 0) {
                    "${ChronoUnit.YEARS.between(parsedDateTime, today)} tahun yang lalu"
                } else if (ChronoUnit.MONTHS.between(parsedDateTime, today) in 1..12) {
                    "${ChronoUnit.MONTHS.between(parsedDateTime, today)} bulan yang lalu"
                } else if (ChronoUnit.WEEKS.between(parsedDateTime, today) in 1..4) {
                    "${ChronoUnit.WEEKS.between(parsedDateTime, today)} minggu yang lalu"
                } else if (ChronoUnit.DAYS.between(parsedDateTime, today) in 1..30) {
                    "${ChronoUnit.DAYS.between(parsedDateTime, today)} hari yang lalu"
                } else if (ChronoUnit.HOURS.between(parsedDateTime, today) in 1..24) {
                    "${ChronoUnit.HOURS.between(parsedDateTime, today)} jam yang lalu"
                } else if (ChronoUnit.MINUTES.between(parsedDateTime, today) in 1..60) {
                    "${ChronoUnit.MINUTES.between(parsedDateTime, today)} menit yang lalu"
                } else if (ChronoUnit.SECONDS.between(parsedDateTime, today) in 1..60) {
                    "${ChronoUnit.SECONDS.between(parsedDateTime, today)} detik yang lalu"
                } else {
                    "Baru saja"
                }
                itemStory.setOnClickListener {
                    val intent = Intent(this@MyViewHolder.itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_STORY, story)
                    this@MyViewHolder.itemView.context.startActivity(intent)
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
                return oldItem == oldItem
            }
        }
    }
}