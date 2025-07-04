package com.example.dicodingstory.ui.story

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dicodingstory.R
import com.example.dicodingstory.data.Result
import com.example.dicodingstory.databinding.ActivityStoryBinding
import com.example.dicodingstory.ui.account.AccountActivity
import com.example.dicodingstory.ui.upload.UploadActivity
import com.example.dicodingstory.ui.welcome.WelcomeActivity
import com.example.dicodingstory.utils.SessionManager
import com.example.dicodingstory.utils.dataStore
import kotlinx.coroutines.launch

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.materialToolbar
        val rvStoryList = binding.rvStoryList
        val fabAddNewStory = binding.fabAddNewStory
        val pbStory = binding.pbStory
        val swipeRefreshLayout = binding.swipeRefreshLayout
        val ivNoData = binding.ivNoData
        val tvNoData = binding.tvNoData

        val sessionManager = SessionManager.getInstance(applicationContext.dataStore)

        val storyViewModelFactory: StoryViewModelFactory = StoryViewModelFactory.getInstance(this)
        val storyViewModel: StoryViewModel by viewModels {
            storyViewModelFactory
        }
        val storyAdapter = StoryAdapter()

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_account -> {
                    val intent = Intent(this, AccountActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_logout -> {
                    AlertDialog.Builder(this).apply {
                        setTitle("Logout")
                        setMessage("Apakah kamu yakin ingin logout?")
                        setPositiveButton("Keluar") { _, _ ->
                            lifecycleScope.launch {
                                sessionManager.clearSessionToken()
                                val intent = Intent(this@StoryActivity, WelcomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }
                        }
                        setNegativeButton("Batal") { dialog, _ ->
                            dialog.dismiss()
                        }
                        create()
                        show()
                    }
                    true
                }
                else -> false
            }
        }

        storyViewModel.getAllStories().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        pbStory.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        pbStory.visibility = View.GONE
                        val stories = result.data

                        if (stories.isNotEmpty()) {
                            rvStoryList.visibility = View.VISIBLE
                            ivNoData.visibility = View.GONE
                            tvNoData.visibility = View.GONE
                            storyAdapter.submitList(stories)
                        } else {
                            rvStoryList.visibility = View.GONE
                            ivNoData.visibility = View.VISIBLE
                            tvNoData.visibility = View.VISIBLE
                            playAnimation()
                        }
                    }

                    is Result.Error -> {
                        pbStory.visibility = View.GONE
                        Toast.makeText(this, "Error : ${result.error}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        rvStoryList.apply {
            layoutManager = GridLayoutManager(this@StoryActivity, 2)
            adapter = storyAdapter
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false

            val intent = Intent(this, StoryActivity::class.java)
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        fabAddNewStory.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivNoData, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
}