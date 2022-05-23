package com.fillahaufi.medlit.ui.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.fillahaufi.medlit.data.Result
import com.fillahaufi.medlit.data.local.UserPreference
import com.fillahaufi.medlit.databinding.ActivityRegisterBinding
import com.fillahaufi.medlit.ui.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(false)

        setupView()
        setupViewModel()
        goLogin()
        signUp()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, UserPreference.getInstance(dataStore))
        val vM: AuthViewModel by viewModels {
            factory
        }
        viewModel = vM
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

    private fun signUp() {
        binding.submit.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passEditText.text.toString()
            when {
                name.isEmpty() -> {
                    binding.name.error = "Masukkan nama"
                }
                email.isEmpty() -> {
                    binding.email.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.password.error = "Masukkan password"
                }
                else -> {
//                    viewModel.saveUser(User(name, email,password, false))
                    viewModel.signUp(name, email, password).observe(this, {
                        if (it != null) {
                            when(it) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    showLoading(false)
//                                    viewModel.saveUser(User(name, email, password, false))
                                    AlertDialog.Builder(this).apply {
                                        setTitle("Yeah!")
                                        setMessage(it.data.message.toString())
                                        setPositiveButton("Lanjut") { _, _ ->
                                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                            startActivity(intent)
                                        }
                                        create()
                                        show()
                                    }
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbRegister.visibility = View.VISIBLE
        }
        else {
            binding.pbRegister.visibility = View.GONE
        }
    }
}