package com.fillahaufi.medlit.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fillahaufi.medlit.databinding.ActivityLoginBinding
import com.fillahaufi.medlit.ui.home.HomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        logIn()
        goRegister()
    }

    private fun logIn() {
        binding.submit.setOnClickListener {
            val intentToHome = Intent (this@LoginActivity, HomeActivity::class.java)
            startActivity(intentToHome)
        }
    }

    private fun goRegister() {
        binding.register.setOnClickListener {
            val intentToRegister = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intentToRegister)
            finish()
        }
    }

    private fun setupView() {
        supportActionBar?.hide()
    }
}