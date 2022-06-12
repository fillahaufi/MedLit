package com.fillahaufi.medlit.ui.camera

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fillahaufi.medlit.data.local.Medicine
import com.fillahaufi.medlit.databinding.ActivityScanResultBinding
import com.fillahaufi.medlit.ml.MedicineModel
import com.fillahaufi.medlit.ui.welcome.WelcomeActivity
import com.fillahaufi.medlit.utils.rotateBitmap
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.DecimalFormat
import kotlin.math.absoluteValue

class ScanResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanResultBinding
    private lateinit var medList: List<String>

    companion object {
        const val CAMERA_X_RESULT = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startCameraX()
        loadLabel()
        setupView()
        resultCameraX()
        retakeCamera()
    }

    private fun loadLabel() {
        val filename = "label.txt"
        medList = application.assets.open(filename).bufferedReader().use { it.readLines() }
//        medList = inputString.split("/n")
    }

    private fun startCameraX() {
        val intentToCameraX = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intentToCameraX)
    }

    private fun retakeCamera() {
        binding.retakePicture.setOnClickListener {
            recreate()
        }
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
//            outputGenerator(result)
            outputModel(result)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun outputModel(bitmap: Bitmap) {
        val resizeBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, true)
        val model = MedicineModel.newInstance(this)

        val bytebuffer = ByteBuffer.allocateDirect(4*128*128*3)
        bytebuffer.order(ByteOrder.nativeOrder())
        val intValues  = IntArray(128*128)
        bitmap.getPixels(intValues,0,resizeBitmap.width,0,0,resizeBitmap.width,resizeBitmap.height)
        var pixel = 0
        for( i in 0 .. 127){
            for(j in 0..127){
                val tmpVal = intValues[pixel++]
                bytebuffer.putFloat(((tmpVal shr 16) and  0xFF)*(1.0f/255.0f))
                bytebuffer.putFloat(((tmpVal shr 8) and  0xFF)*(1.0f/255.0f))
                bytebuffer.putFloat((tmpVal and  0xFF)*(1.0f/255.0f))
            }
        }
//        val bytebuffer = normalizeBitmap(bitmap, 128, 40.0f, 255.0f)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 128, 128, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(bytebuffer)
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        val tab = outputFeature0.floatArray
        val maxInd = getMax(tab)

//        binding.goToDetail.text = medList[maxInd]
        Log.d("medList2", medList.toString())
        val df = DecimalFormat("#.##")

        binding.goToDetail.text = medList[maxInd] + " (" + df.format(tab[maxInd]*100) + "%)"
        Log.d("presenstage", tab[maxInd].toString())
        model.close()
    }

    fun normalizeBitmap(source: Bitmap, size: Int, mean: Float, std: Float): FloatArray {
        val output = FloatArray(size * size * 3)
        val intValues = IntArray(source.height * source.width)
        source.getPixels(intValues, 0, source.width, 0, 0, source.width, source.height)
        for (i in intValues.indices) {
            val `val` = intValues[i]
            output[i * 3] = ((`val` shr 16 and 0xFF) - mean) / std
            output[i * 3 + 1] = ((`val` shr 8 and 0xFF) - mean) / std
            output[i * 3 + 2] = ((`val` and 0xFF) - mean) / std
        }
        return output
    }

    private fun getMax(array: FloatArray): Int {
        var ind = 0
        var min = 0.0f

        for (i in array.indices) {
            if (array[i] > min) {
                ind = i
                min = array[i]
            }
        }

        Log.d("index", ind.toString())

        return ind
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
}