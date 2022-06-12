package com.fillahaufi.medlit.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.fillahaufi.medlit.R
import com.fillahaufi.medlit.databinding.ActivityHomeBinding
import com.fillahaufi.medlit.ui.camera.CameraActivity
import com.fillahaufi.medlit.ui.camera.ScanResultActivity
import com.fillahaufi.medlit.ui.home.ui.home.HomeFragment

class HomeActivity : AppCompatActivity(), IHomeFragment {

    private lateinit var binding: ActivityHomeBinding
    lateinit var userToken: String
    lateinit var userName: String
    lateinit var userEmail: String

    companion object {
        const val USER_TOKEN = "USER_TOKEN"
        const val USER_NAME = "USER_NAME"
        const val USER_EMAIL = "USER_EMAIL"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setupView()
        setMidMenuDisabled()
        startCameraX()
    }

    private fun startCameraX() {
        binding.fbScan.setOnClickListener {
            val intentToCameraX = Intent(this, ScanResultActivity::class.java)
            intentToCameraX.putExtra(ScanResultActivity.USER_TOKEN, userToken)
            intentToCameraX.putExtra(ScanResultActivity.USER_NAME, userName)
            intentToCameraX.putExtra(ScanResultActivity.USER_EMAIL, userEmail)
            startActivity(intentToCameraX)
        }
    }

    private fun sendDataToFragment() {
        Log.d("nama dan token", userName + userToken)
        val homeFragment = HomeFragment()
        val bundle = Bundle()
        bundle.putString("name", userName)
        bundle.putString("token", userToken)
        homeFragment.arguments = bundle
    }

    private fun setMidMenuDisabled() {
        binding.navView.menu.findItem(R.id.navigation_dashboard).isEnabled = false
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    override fun getHomeFragmentData(): HomeFragmentData {
        userToken = intent.getStringExtra(USER_TOKEN).toString()
        userName = intent.getStringExtra(USER_NAME).toString()
        userEmail = intent.getStringExtra(USER_EMAIL).toString()

        return HomeFragmentData(
            name = userName,
            email = userEmail,
            token = userToken,
        )
    }
}