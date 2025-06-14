package com.example.dicodingstory.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.dicodingstory.R
import com.example.dicodingstory.databinding.ActivityMainBinding
import com.example.dicodingstory.ui.welcome.WelcomeActivity
import com.example.dicodingstory.utils.SessionManager
import com.example.dicodingstory.utils.dataStore
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sessionManager = SessionManager.getInstance(applicationContext.dataStore)

        lifecycleScope.launch {
            sessionManager.getSessionToken().collect { token ->
                if (token.isNullOrEmpty()) {
                    startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
                }
            }
        }
    }
}