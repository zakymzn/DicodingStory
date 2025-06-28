package com.example.dicodingstory.ui.main

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
import com.example.dicodingstory.ui.upload.UploadActivity
import com.example.dicodingstory.ui.welcome.WelcomeActivity
import com.example.dicodingstory.utils.SessionManager
import com.example.dicodingstory.utils.dataStore
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.materialToolbar
        val rvStoryList = binding.rvStoryList
        val fabAddNewStory = binding.fabAddNewStory
        val pbMain = binding.pbMain
        val swipeRefreshLayout = binding.swipeRefreshLayout

        sessionManager = SessionManager.getInstance(applicationContext.dataStore)

        lifecycleScope.launch {
            sessionManager.getSessionToken().collect { token ->
                Log.d("MainActivity", "token : $token")
                if (token.isNullOrEmpty()) {
                    val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                    return@collect
                } else {
                    val mainViewModelFactory: MainViewModelFactory = MainViewModelFactory.getInstance(this@MainActivity)
                    val mainViewModel: MainViewModel by viewModels {
                        mainViewModelFactory
                    }
                    val mainAdapter = MainAdapter()

                    mainViewModel.getAllStories().observe(this@MainActivity) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    pbMain.visibility = View.VISIBLE
                                }

                                is Result.Success -> {
                                    pbMain.visibility = View.GONE
                                    val stories = result.data

                                    if (stories.isNotEmpty()) {
                                        mainAdapter.submitList(stories)
                                    }
                                }

                                is Result.Error -> {
                                    pbMain.visibility = View.GONE
                                    Toast.makeText(this@MainActivity, "Error : ${result.error}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                    rvStoryList.apply {
                        layoutManager = GridLayoutManager(this@MainActivity, 2)
                        adapter = mainAdapter
                    }
                }
            }
        }

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_logout -> {
                    AlertDialog.Builder(this@MainActivity).apply {
                        setTitle("Logout")
                        setMessage("Apakah kamu yakin ingin logout?")
                        setPositiveButton("Keluar") { _, _ ->
                            lifecycleScope.launch {
                                sessionManager.clearSessionToken()
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
    }
}