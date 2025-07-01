package com.example.dicodingstory.ui.account

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dicodingstory.R
import com.example.dicodingstory.data.local.entity.AccountEntity
import com.example.dicodingstory.databinding.ActivityAccountBinding

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.materialToolbar
        val tvAccountName = binding.tvAccountName

        val accountViewModelFactory: AccountViewModelFactory = AccountViewModelFactory.getInstance(this)
        val accountViewModel: AccountViewModel by viewModels {
            accountViewModelFactory
        }

        accountViewModel.getAccount().observe(this) { account ->
            if (account != null) {
                tvAccountName.text = account.name
            }
        }

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}