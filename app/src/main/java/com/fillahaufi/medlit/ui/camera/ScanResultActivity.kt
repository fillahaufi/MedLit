package com.fillahaufi.medlit.ui.camera

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fillahaufi.medlit.data.Result
import com.fillahaufi.medlit.data.local.Medicine
import com.fillahaufi.medlit.data.local.UserPreference
import com.fillahaufi.medlit.databinding.ActivityScanResultBinding
import com.fillahaufi.medlit.ml.MedscanModel
import com.fillahaufi.medlit.ui.ViewModelFactory
import com.fillahaufi.medlit.ui.home.HomeActivity
import com.fillahaufi.medlit.ui.home.ui.detail.MedDetailActivity
import com.fillahaufi.medlit.ui.others.SearchResultActivity
import com.fillahaufi.medlit.utils.rotateBitmap
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.DecimalFormat
import java.io.Serializable
import kotlin.math.absoluteValue

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class ScanResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanResultBinding
    private lateinit var viewModel: ScanResultViewModel
    private lateinit var medList: List<String>
    private lateinit var medResult: String

    private lateinit var userToken: String
    private lateinit var userName: String
    private lateinit var userEmail: String

    companion object {
        const val CAMERA_X_RESULT = 200
        const val USER_TOKEN = "USER_TOKEN"
        const val USER_NAME = "USER_NAME"
        const val USER_EMAIL = "USER_EMAIL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getUserData()
        setupViewModel()
        startCameraX()
        loadLabel()
        setupView()
        resultCameraX()
        retakeCamera()
    }

    private fun getUserData() {
        userToken = intent.getStringExtra(HomeActivity.USER_TOKEN).toString()
        userName = intent.getStringExtra(HomeActivity.USER_NAME).toString()
        userEmail = intent.getStringExtra(HomeActivity.USER_EMAIL).toString()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, UserPreference.getInstance(dataStore))
        val vM: ScanResultViewModel by viewModels {
            factory
        }
        viewModel = vM
    }

    private fun loadLabel() {
        val filename = "label.txt"
        medList = application.assets.open(filename).bufferedReader().use { it.readLines() }

//        medList = inputString.split("/n")
    }

    private fun startCameraX() {
        val intentToCameraX = Intent(this, CameraActivity::class.java)
        intentToCameraX.putExtra(CameraActivity.USER_TOKEN, userToken)
        intentToCameraX.putExtra(CameraActivity.USER_NAME, userName)
        intentToCameraX.putExtra(CameraActivity.USER_EMAIL, userEmail)
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
            val result = BitmapFactory.decodeFile(myFile.path)

            binding.previewImageView.setImageBitmap(result)
//            outputGenerator(result)
            outputModel(result)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun outputModel(bitmap: Bitmap) {
        val resizeBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, true)
        val model = MedscanModel.newInstance(this)
//        val model = MedicineModel.newInstance(this)

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
//        Log.d("medList2", medList[0] + medList[1])
        val df = DecimalFormat("#.##")

        binding.goToDetail.text = medList[maxInd] + " (" + df.format(tab[maxInd]*100) + "%)"

        medResult = medList[maxInd]

        searchByName(medResult)

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

    private fun showMedicineDetail(data: Medicine) {
        val intentToDetail = Intent(this, MedDetailActivity::class.java)
        intentToDetail.putExtra(MedDetailActivity.MED_NAME, data.name)
        intentToDetail.putExtra(MedDetailActivity.MED_IMG, data.photo)
        intentToDetail.putExtra(MedDetailActivity.MED_PURPOSE, data.purpose)
        intentToDetail.putExtra(MedDetailActivity.MED_SE, data.side_effetcs)
        intentToDetail.putExtra(MedDetailActivity.MED_CONTRA, data.contraindication)
        intentToDetail.putExtra(MedDetailActivity.MED_DOSE, data.dosage)
        intentToDetail.putExtra(MedDetailActivity.MED_ING, data.ingredients)
        startActivity(intentToDetail)
    }

    private fun searchByName(query: String) {
        binding.goToDetail.setOnClickListener {
            viewModel.getSearchMedicines(query).observe(this@ScanResultActivity, {
                if (it != null) {
                    when(it) {
                        is Result.Loading -> {
//                            showLoading(true)
                        }
                        is Result.Success -> {
                            Log.d("testtttt", it.data.toString())
                            Log.d("queryy", query)
                            showMedicineDetail(it.data.first())
//                            val intentToSearch = Intent(this, SearchResultActivity::class.java)
//                            intentToSearch.putParcelableArrayListExtra("Medicines", it.data as ArrayList<Medicine>)
//                            startActivity(intentToSearch)
                            finish()
                        }
                        is Result.Error -> {
                            Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }

//    private fun showLoading(isLoading: Boolean) {
//        if (isLoading) {
//            binding.pbSearch.visibility = View.VISIBLE
//        }
//        else {
//            binding.pbSearch.visibility = View.GONE
//        }
//    }
}