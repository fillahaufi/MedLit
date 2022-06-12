package com.fillahaufi.medlit.ui.others

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fillahaufi.medlit.R
import com.fillahaufi.medlit.data.Result
import com.fillahaufi.medlit.data.local.Medicine
import com.fillahaufi.medlit.data.local.UserPreference
import com.fillahaufi.medlit.databinding.ActivitySearchResultBinding
import com.fillahaufi.medlit.ui.ViewModelFactory
import com.fillahaufi.medlit.ui.camera.ListMedicineAdapter
import com.fillahaufi.medlit.ui.home.ui.detail.MedDetailActivity
import java.lang.Error

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SearchResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchResultBinding
    private lateinit var viewModel: SearchResultViewModel
    private lateinit var rvSearch: RecyclerView
    private lateinit var adapter: ListMedicineAdapter
    private var data = ArrayList<Medicine>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)
        setupData()
        setupView()
        setupViewModel()
        showRecyclerList()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, UserPreference.getInstance(dataStore))
        val vM: SearchResultViewModel by viewModels {
            factory
        }
        viewModel = vM
    }

    private fun setupView() {
        supportActionBar?.title = "Search"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getSearchMedicines(query).observe(this@SearchResultActivity, {
                    if(it != null) {
                        when(it) {
                            is Result.Loading -> {
//                                if (data != null) {
//                                    showLoading(false)
//                                }
                                showLoading(true)
                            }
                            is Result.Success -> {
                                Log.d("SearchedUsers act", it.data.toString())
                                changeData(it.data as ArrayList<Medicine>)
//                                showRecycleList(it.data as ArrayList<User>)
                                showLoading(false)
                            }
                            is Result.Error -> {
                                showLoading(false)
                            }
                        }
                    }
                })

                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return true
    }

    private fun showRecyclerList() {
        rvSearch = findViewById(R.id.rv_search)
        rvSearch.setHasFixedSize(true)

        if (this.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvSearch.layoutManager = GridLayoutManager(this, 2)
        } else {
            rvSearch.layoutManager = GridLayoutManager(this, 2)
        }
        adapter = ListMedicineAdapter(data)
        rvSearch.adapter = adapter
        adapter.setOnItemClickCallback(object : ListMedicineAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Medicine) {
                showMedicineDetail(data)
//                showSelectedHero(data)
            }
        })
    }

    private fun setupData() {
        val challenge = ArrayList<Medicine>()
        if (this.intent.getParcelableArrayListExtra<Medicine>("Medicines") != null) {
            challenge.addAll(this.intent.getParcelableArrayListExtra("Medicines")!!)
        }
        Log.d("argsOnSearch", challenge.toString())
        data.clear()
        data.addAll(challenge)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbSearch.visibility = View.VISIBLE
        }
        else {
            binding.pbSearch.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun changeData(list: ArrayList<Medicine>) {
        data.clear()
        data.addAll(list)
        adapter.notifyDataSetChanged()
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
}