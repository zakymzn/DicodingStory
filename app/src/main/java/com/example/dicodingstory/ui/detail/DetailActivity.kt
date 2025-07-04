package com.example.dicodingstory.ui.detail

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.dicodingstory.R
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
        val tvDetailName = binding.tvDetailName
        val tvDetailPostTime = binding.tvDetailPostTime
        val tvDetailDescription = binding.tvDetailDescription

        val story = intent.getParcelableExtra<StoryEntity>(EXTRA_STORY)

        if (story != null) {
            val today = LocalDateTime.now()
            val parsedDateTime = OffsetDateTime.parse(story.createdAt).atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()

            Glide.with(this)
                .load(story.photoUrl)
                .into(binding.ivDetailPhoto)

            binding.apply {
                toolbar.title = "${story.name}"
                tvDetailName.text = "${story.name}"
                tvDetailPostTime.text = if (ChronoUnit.YEARS.between(parsedDateTime, today) > 0) {
                    "${getString(R.string.uploaded)} ${ChronoUnit.YEARS.between(parsedDateTime, today)} ${getString(R.string.years_ago)}"
                } else if (ChronoUnit.MONTHS.between(parsedDateTime, today) in 1..12) {
                    "${getString(R.string.uploaded)} ${ChronoUnit.MONTHS.between(parsedDateTime, today)} ${getString(R.string.months_ago)}"
                } else if (ChronoUnit.WEEKS.between(parsedDateTime, today) in 1..4) {
                    "${getString(R.string.uploaded)} ${ChronoUnit.WEEKS.between(parsedDateTime, today)} ${getString(R.string.weeks_ago)}"
                } else if (ChronoUnit.DAYS.between(parsedDateTime, today) in 1..30) {
                    "${getString(R.string.uploaded)} ${ChronoUnit.DAYS.between(parsedDateTime, today)} ${getString(R.string.days_ago)}"
                } else if (ChronoUnit.HOURS.between(parsedDateTime, today) in 1..24) {
                    "${getString(R.string.uploaded)} ${ChronoUnit.HOURS.between(parsedDateTime, today)} ${getString(R.string.hours_ago)}"
                } else if (ChronoUnit.MINUTES.between(parsedDateTime, today) in 1..60) {
                    "${getString(R.string.uploaded)} ${ChronoUnit.MINUTES.between(parsedDateTime, today)} ${getString(R.string.minutes_ago)}"
                } else if (ChronoUnit.SECONDS.between(parsedDateTime, today) in 1..60) {
                    "${getString(R.string.uploaded)} ${ChronoUnit.SECONDS.between(parsedDateTime, today)} ${getString(R.string.seconds_ago)}"
                } else {
                    getString(R.string.just_uploaded)
                }
                tvDetailDescription.text = "${story.description}"
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