package com.fillahaufi.medlit.ui.home.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.bumptech.glide.Glide
import com.fillahaufi.medlit.databinding.ActivityMedDetailBinding

class MedDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedDetailBinding
    private lateinit var medName: String
    private lateinit var medImg: String

    companion object {
        const val MED_NAME = "MED_NAME"
        const val MED_IMG = "MED_IMG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setIncomingData()

        bookmark()
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setIncomingData() {
        medName = intent.getStringExtra(MED_NAME).toString()
        medImg = intent.getStringExtra(MED_IMG).toString()

        binding.medName.text = medName
        Glide.with(this)
            .load(medImg)
            .into(binding.detailImg)
    }

    private fun bookmark() {
        binding.bookmark.setOnClickListener {
            Toast.makeText(this, "Fitur dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
    }
}