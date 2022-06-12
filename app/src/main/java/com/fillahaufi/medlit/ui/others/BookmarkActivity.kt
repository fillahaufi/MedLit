package com.fillahaufi.medlit.ui.others

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fillahaufi.medlit.R
import com.fillahaufi.medlit.data.local.Medicine
import com.fillahaufi.medlit.data.local.UserPreference
import com.fillahaufi.medlit.databinding.ActivityBookmarkBinding
import com.fillahaufi.medlit.ui.ViewModelFactory
import com.fillahaufi.medlit.ui.camera.ListMedicineAdapter
import com.fillahaufi.medlit.ui.home.ui.detail.MedDetailActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class BookmarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookmarkBinding
    private lateinit var viewModel: BookmarkViewModel
    private lateinit var rvBookmark: RecyclerView
    private lateinit var adapter: ListMedicineAdapter
    private val listDummy = ArrayList<Medicine>()
    private var data = ArrayList<Medicine>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listDummy.addAll(listMedicines)

        setupView()
        setupViewModel()
        backButton()
        showRecyclerList()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, UserPreference.getInstance(dataStore))
        val vM: BookmarkViewModel by viewModels {
            factory
        }
        viewModel = vM
    }

    private fun backButton() {
        binding.floatingActionButton2.setOnClickListener {
            this.finish()
        }
    }

    private fun showRecyclerList() {
        rvBookmark = binding.bookList
        rvBookmark.setHasFixedSize(true)

        if (this.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvBookmark.layoutManager = GridLayoutManager(this, 2)
        } else {
            rvBookmark.layoutManager = GridLayoutManager(this, 2)
        }

        adapter = ListMedicineAdapter(data)
        rvBookmark.adapter = adapter

        viewModel.getBookmarkedUsers().observe(this, { bookmarkedUsers ->
            changeData(bookmarkedUsers as ArrayList<Medicine>)
        })

        adapter.setOnItemClickCallback(object : ListMedicineAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Medicine) {
                showMedicineDetail(data)
//                showSelectedHero(data)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun changeData(list: ArrayList<Medicine>) {
        data.clear()
        data.addAll(list)
        adapter.notifyDataSetChanged()
    }

    private val listMedicines: ArrayList<Medicine>
        get() {
            val dataId = resources.getIntArray(R.array.data_id)
            val dataName = resources.getStringArray(R.array.data_name)
            val dataPhoto = resources.getStringArray(R.array.data_img)
            val listMed = ArrayList<Medicine>()
            for (i in dataName.indices) {
                val med = Medicine(dataId[i], dataName[i], dataPhoto[i], false)
                listMed.add(med)
            }
            return listMed
        }

    private fun showMedicineDetail(data: Medicine) {
        val intentToDetail = Intent(this, MedDetailActivity::class.java)
        intentToDetail.putExtra(MedDetailActivity.MED_NAME, data.name)
        intentToDetail.putExtra(MedDetailActivity.MED_IMG, data.photo)
        startActivity(intentToDetail)
    }


    private fun setupView() {
        supportActionBar?.hide()
    }
}