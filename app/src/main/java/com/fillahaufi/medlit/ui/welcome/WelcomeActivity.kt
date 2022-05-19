package com.fillahaufi.medlit.ui.welcome

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import com.fillahaufi.medlit.R
import com.fillahaufi.medlit.databinding.ActivityWelcomeBinding
import com.fillahaufi.medlit.ui.auth.LoginActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAnimation()
        getStarted()
    }

    private fun getStarted() {
        binding.getStart.setOnClickListener {
            val intentToLogin = Intent(this@WelcomeActivity, LoginActivity::class.java)
            startActivity(intentToLogin)
        }
    }

    private fun setupAnimation() {
        Handler().postDelayed({
            binding.mlWelcome.transitionToEnd()
        }, 1000)
    }

    private fun setupView() {
        supportActionBar?.hide()
    }
}