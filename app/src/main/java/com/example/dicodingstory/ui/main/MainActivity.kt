package com.example.dicodingstory.ui.main

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dicodingstory.R
import com.example.dicodingstory.data.Result
import com.example.dicodingstory.databinding.ActivityMainBinding
import com.example.dicodingstory.ui.maps.MapsActivity
import com.example.dicodingstory.ui.settings.SettingsActivity
import com.example.dicodingstory.ui.upload.UploadActivity
import com.example.dicodingstory.ui.welcome.WelcomeActivity
import com.example.dicodingstory.utils.LocaleHelper
import com.example.dicodingstory.utils.UserPreferences
import com.example.dicodingstory.utils.dataStore
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: UserPreferences

    override fun attachBaseContext(newBase: Context) {
        val language = LocaleHelper.getSavedLanguage(newBase)
        val context = LocaleHelper.setLocale(newBase, language)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.materialToolbar
        val rvStoryList = binding.rvStoryList
        val fabAddNewStory = binding.fabAddNewStory
        val pbMain = binding.pbMain
        val swipeRefreshLayout = binding.swipeRefreshLayout
        val ivNoData = binding.ivNoData
        val tvNoData = binding.tvNoData

        preferences = UserPreferences.getInstance(applicationContext.dataStore)

        val mainViewModelFactory: MainViewModelFactory = MainViewModelFactory.getInstance(this@MainActivity)
        val mainViewModel: MainViewModel by viewModels {
            mainViewModelFactory
        }
        val mainAdapter = MainAdapter()

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_maps -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.action_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.action_logout -> {
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.logout))
                        setMessage(getString(R.string.text_logout_message))
                        setPositiveButton(getString(R.string.logout)) { _, _ ->
                            lifecycleScope.launch {
                                preferences.clearSessionToken()
                            }
                        }
                        setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
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

        lifecycleScope.launch {
            preferences.getSessionToken().collect { token ->
                Log.d("MainActivity", "token : $token")
                if (token.isNullOrEmpty()) {
                    val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                    return@collect
                }
            }
        }

//        mainViewModel.getAllStories().observe(this) { result ->
//            if (result != null) {
//                when (result) {
//                    is Result.Loading -> {
//                        pbMain.visibility = View.VISIBLE
//                        ivNoData.visibility = View.GONE
//                        tvNoData.visibility = View.GONE
//                    }
//
//                    is Result.Success -> {
//                        pbMain.visibility = View.GONE
//                        val stories = result.data
//
//                        if (stories.isNotEmpty()) {
//                            rvStoryList.visibility = View.VISIBLE
//                            ivNoData.visibility = View.GONE
//                            tvNoData.visibility = View.GONE
//                            mainAdapter.submitList(stories)
//                        } else {
//                            rvStoryList.visibility = View.GONE
//                            ivNoData.visibility = View.VISIBLE
//                            tvNoData.visibility = View.VISIBLE
//                        }
//                    }
//
//                    is Result.Error -> {
//                        pbMain.visibility = View.GONE
//                        Toast.makeText(this, "Error : ${result.error}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }

        mainViewModel.getAllStories().observe(this, {
            mainAdapter.submitData(lifecycle, it)
        })

        rvStoryList.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = mainAdapter
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false

            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        fabAddNewStory.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }

        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivNoData, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
}