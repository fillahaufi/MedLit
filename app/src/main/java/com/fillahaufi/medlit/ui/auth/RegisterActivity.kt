package com.fillahaufi.medlit.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fillahaufi.medlit.R
import com.fillahaufi.medlit.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        goLogin()
    }

    private fun goLogin() {
        binding.login.setOnClickListener {
            val intentToRegister = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intentToRegister)
            finish()
        }
    }

    private fun setupView() {
        supportActionBar?.hide()
    }
}