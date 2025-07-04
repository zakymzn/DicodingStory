package com.example.dicodingstory.ui.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dicodingstory.R
import com.example.dicodingstory.databinding.ActivityWelcomeBinding
import com.example.dicodingstory.ui.login.LoginActivity
import com.example.dicodingstory.ui.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        playAnimation()
    }

    private fun setupAction() {
        binding.buttonLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.buttonRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivWelcome, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvWelcomeTitle, View.ALPHA, 1f).setDuration(100)
        val desc = ObjectAnimator.ofFloat(binding.tvWelcomeDesc, View.ALPHA, 1f).setDuration(100)
        val buttonLogin = ObjectAnimator.ofFloat(binding.buttonLogin, View.ALPHA, 1f).setDuration(100)
        val buttonRegister = ObjectAnimator.ofFloat(binding.buttonRegister, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(buttonLogin, buttonRegister)
        }

        AnimatorSet().apply {
            playSequentially(title, desc, together)
            start()
        }
    }
}