package com.fillahaufi.medlit.ui.home.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.fillahaufi.medlit.R
import com.fillahaufi.medlit.databinding.ActivityMedDetailBinding

class MedDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookmark()
    }

    private fun bookmark() {
        binding.bookmark.setOnClickListener {
            Toast.makeText(this, "Fitur dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
    }
}