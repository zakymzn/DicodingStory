package com.example.dicodingstory.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dicodingstory.R
import com.example.dicodingstory.data.Result
import com.example.dicodingstory.databinding.ActivityLoginBinding
import com.example.dicodingstory.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val edLoginEmail = binding.edLoginEmail
        val edLoginPassword = binding.edLoginPassword
        val buttonLogin = binding.buttonLogin
        val pbLogin = binding.pbLogin

        val loginViewModelFactory: LoginViewModelFactory = LoginViewModelFactory.getInstance(this)
        val loginViewModel: LoginViewModel by viewModels {
            loginViewModelFactory
        }

        buttonLogin.setOnClickListener {
            val email = edLoginEmail.text.toString()
            val password = edLoginPassword.text.toString()

            loginViewModel.loginAccount(email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            pbLogin.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            pbLogin.visibility = View.GONE
                            val response = result.data

                            if (response.error == false) {
                                Toast.makeText(this, getString(R.string.text_login_success), Toast.LENGTH_SHORT)
                                    .show()
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                        }

                        is Result.Error -> {
                            pbLogin.visibility = View.GONE
                            Toast.makeText(this, "Error : ${result.error}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvLoginTitle, View.ALPHA, 1f).setDuration(100)
        val message = ObjectAnimator.ofFloat(binding.tvLoginMessage, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(100)
        val emailEdit = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(100)
        val passwordEdit = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(100)
        val buttonLogin = ObjectAnimator.ofFloat(binding.buttonLogin, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(title, message, email, emailEdit, password, passwordEdit, buttonLogin)
            start()
        }
    }
}