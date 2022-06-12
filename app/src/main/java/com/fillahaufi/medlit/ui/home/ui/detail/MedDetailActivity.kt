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
    private lateinit var medPurp: String
    private lateinit var medSE: String
    private lateinit var medContra: String
    private lateinit var medDose: String
    private lateinit var medIng: String

    companion object {
        const val MED_NAME = "MED_NAME"
        const val MED_IMG = "MED_IMG"
        const val MED_PURPOSE = "MED_PURPOSE"
        const val MED_SE = "MED_SE"
        const val MED_CONTRA = "MED_CONTRA"
        const val MED_DOSE = "MED_DOSE"
        const val MED_ING = "MED_ING"
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
        medPurp = intent.getStringExtra(MED_PURPOSE).toString()
        medSE = intent.getStringExtra(MED_SE).toString()
        medContra = intent.getStringExtra(MED_CONTRA).toString()
        medDose = intent.getStringExtra(MED_DOSE).toString()
        medIng = intent.getStringExtra(MED_ING).toString()

        binding.genericNameValue.text = medName
        binding.medName.text = medName
        Glide.with(this)
            .load(medImg)
            .into(binding.detailImg)
        binding.usedForValue.text = medPurp
        binding.purposeValue.text = medPurp
        binding.sideEffectsValue.text = medSE
        binding.contraindicationValue.text = medContra
        binding.dosageValue.text = medDose
        binding.ingredientsValue.text = medIng
    }

    private fun bookmark() {
        binding.bookmark.setOnClickListener {
            Toast.makeText(this, "Fitur dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
    }
}