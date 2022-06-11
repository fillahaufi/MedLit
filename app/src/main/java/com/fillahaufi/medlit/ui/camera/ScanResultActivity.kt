package com.fillahaufi.medlit.ui.camera

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.fillahaufi.medlit.databinding.ActivityScanResultBinding
import com.fillahaufi.medlit.ml.MedicineModel
import com.fillahaufi.medlit.utils.rotateBitmap
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.absoluteValue

class ScanResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanResultBinding

    companion object {
        const val CAMERA_X_RESULT = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startCameraX()
        setupView()
        resultCameraX()
        retakeCamera()
    }

    private fun startCameraX() {
        val intentToCameraX = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intentToCameraX)
    }

    private fun retakeCamera() {
        binding.retakePicture.setOnClickListener { startCameraX() }
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun resultCameraX() {
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == CAMERA_X_RESULT) {
                val myFile = it.data?.getSerializableExtra("picture") as File
                val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
                val result = BitmapFactory.decodeFile(myFile.path)

            binding.previewImageView.setImageBitmap(result)
            }
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.previewImageView.setImageBitmap(result)
            outputGenerator(result)
        }
    }

    private fun outputGenerator(bitmap: Bitmap) {

        val resize = Bitmap.createScaledBitmap(bitmap, 256, 256, true)
//        val resize = Bitmap.createScaledBitmap(bitmap, 128, 128, true)
        val model = MedicineModel.newInstance(this)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 128, 128, 3), DataType.FLOAT32)

//      Creates inputs for reference.
        val tfImage = TensorImage.fromBitmap(resize)
        val byteBuffer = tfImage.buffer

        Log.d("shape", byteBuffer.toString())
        Log.d("shape2", inputFeature0.buffer.toString())

        inputFeature0.loadBuffer(byteBuffer)

//      Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val outputIndex = outputFeature0.floatArray.size - 1
        Log.d("index", outputIndex.toString())
        Log.d("array0", outputFeature0.floatArray[0].toString())
        Log.d("array1", outputFeature0.floatArray[1].toString())
        Log.d("array2", outputFeature0.floatArray[2].toString())
        Log.d("array3", outputFeature0.floatArray[3].toString())
        Log.d("array4", outputFeature0.floatArray[4].toString())
        Log.d("array5", outputFeature0.floatArray[5].toString())

        binding.goToDetail.text = outputFeature0.floatArray[outputIndex].toString()
        model.close()
    }

    private fun getResult(array: FloatArray) {

    }
}