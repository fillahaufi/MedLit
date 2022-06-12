package com.fillahaufi.medlit.ui.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.fillahaufi.medlit.data.Result
import com.fillahaufi.medlit.data.local.User
import com.fillahaufi.medlit.data.local.UserPreference
import com.fillahaufi.medlit.databinding.ActivityLoginBinding
import com.fillahaufi.medlit.ui.ViewModelFactory
import com.fillahaufi.medlit.ui.home.HomeActivity
import com.fillahaufi.medlit.ui.home.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setupView()
        setContentView(binding.root)
        showLoading(false)

        setupViewModel()
//        dummyLogIn()
        logIn()
        goRegister()
    }

    private fun dummyLogIn() {
        binding.submit.setOnClickListener {
            val intentToMain = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intentToMain)
        }
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, UserPreference.getInstance(dataStore))
        val vM: AuthViewModel by viewModels {
            factory
        }
        viewModel = vM
    }

    private fun logIn() {
        binding.submit.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.email.error = "Masukkan Email"
                }
                password.isEmpty() -> {
                    binding.password.error = "Masukkan password"
                }
                else -> {
                    viewModel.logIn(email, password).observe(this, {
                        if (it != null) {
                            when(it) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    showLoading(false)
//                                    Log.d("apakahhhh sudahh loginn", it.data.message.toString())
                                    val name = it.data.loginResult?.name
                                    val token = it.data.loginResult?.token
                                    val user = User(
                                        name.toString(),
                                        email,
                                        password,
                                        token.toString(),
                                        true
                                    )

//                                    Log.d("nama dan token", name.toString() + token)
                                    viewModel.saveUser(user)
                                    viewModel.login()
                                    val intent = Intent(this, HomeActivity::class.java)
                                    intent.putExtra(HomeActivity.USER_TOKEN, token)
                                    intent.putExtra(HomeActivity.USER_NAME, name)
                                    intent.putExtra(HomeActivity.USER_EMAIL, email)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                }
                                is Result.Error -> {
                                    showLoading(false)
                                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    })
                }
            }
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
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbLogin.visibility = View.VISIBLE
        }
        else {
            binding.pbLogin.visibility = View.GONE
        }
    }
}