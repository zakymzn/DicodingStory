package com.example.dicodingstory.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.dicodingstory.R
import com.example.dicodingstory.data.Result
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.data.local.entity.StoryEntity
import com.example.dicodingstory.databinding.ActivityDetailBinding
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.materialToolbar
        val tvName = binding.tvName
        val tvPostTime = binding.tvPostTime
        val tvDescription = binding.tvDescription

        val story = intent.getParcelableExtra<StoryEntity>(EXTRA_STORY)

        if (story != null) {
            val today = LocalDateTime.now()
            val parsedDateTime = OffsetDateTime.parse(story.createdAt).atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()

            Glide.with(this)
                .load(story.photoUrl)
                .into(binding.ivPhoto)

            binding.apply {
                toolbar.title = "${story.name}"
                tvName.text = "${story.name}"
                tvPostTime.text = if (ChronoUnit.YEARS.between(parsedDateTime, today) > 0) {
                    "Diunggah ${ChronoUnit.YEARS.between(parsedDateTime, today)} tahun yang lalu"
                } else if (ChronoUnit.MONTHS.between(parsedDateTime, today) in 1..12) {
                    "Diunggah ${ChronoUnit.MONTHS.between(parsedDateTime, today)} bulan yang lalu"
                } else if (ChronoUnit.DAYS.between(parsedDateTime, today) in 1..30) {
                    "Diunggah ${ChronoUnit.DAYS.between(parsedDateTime, today)} hari yang lalu"
                } else if (ChronoUnit.HOURS.between(parsedDateTime, today) in 1..24) {
                    "Diunggah ${ChronoUnit.HOURS.between(parsedDateTime, today)} jam yang lalu"
                } else if (ChronoUnit.MINUTES.between(parsedDateTime, today) in 1..60) {
                    "Diunggah ${ChronoUnit.MINUTES.between(parsedDateTime, today)} menit yang lalu"
                } else if (ChronoUnit.SECONDS.between(parsedDateTime, today) in 1..60) {
                    "Diunggah ${ChronoUnit.SECONDS.between(parsedDateTime, today)} detik yang lalu"
                } else {
                    "Baru saja diunggah"
                }
                tvDescription.text = "${story.description}"
            }
        }

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}