package com.fillahaufi.medlit.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.fillahaufi.medlit.R
import com.fillahaufi.medlit.data.local.UserPreference
import com.fillahaufi.medlit.databinding.ActivitySplashBinding
import com.fillahaufi.medlit.ui.home.HomeActivity
import com.fillahaufi.medlit.ui.welcome.WelcomeActivity
import com.fillahaufi.medlit.ui.welcome.WelcomeViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: WelcomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, UserPreference.getInstance(dataStore))
        val vM: WelcomeViewModel by viewModels {
            factory
        }
        viewModel = vM

//        viewModel.logout()
//        setupDelay()


        viewModel.getUser().observe(this, { user ->
            if (user.isLoggedin) {
                viewModel.login()
                viewModel.setUserToken(user.token, user.name, user.email)
                val intentToMain = Intent(this, HomeActivity::class.java)
                Log.d("tokennn", user.token)
                intentToMain.putExtra(HomeActivity.USER_TOKEN, user.token)
                intentToMain.putExtra(HomeActivity.USER_NAME, user.name)
                intentToMain.putExtra(HomeActivity.USER_EMAIL, user.email)
                intentToMain.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                Handler().postDelayed({
                    startActivity(intentToMain)
                }, 3000)
            } else {
//                setupView()
                setupDelay()
            }
        })
    }

    private fun setupDelay() {
        Handler().postDelayed({
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)
            finish()
        }, 3000)
    }

    private fun setupView() {
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}