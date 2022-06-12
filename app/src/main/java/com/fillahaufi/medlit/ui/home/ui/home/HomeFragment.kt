package com.fillahaufi.medlit.ui.home.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fillahaufi.medlit.R
import com.fillahaufi.medlit.data.Result
import com.fillahaufi.medlit.data.local.Medicine
import com.fillahaufi.medlit.databinding.FragmentHomeBinding
import com.fillahaufi.medlit.ui.ViewModelFactory
import com.fillahaufi.medlit.ui.camera.ListMedicineAdapter
import com.fillahaufi.medlit.ui.home.IHomeFragment
import com.fillahaufi.medlit.ui.home.ui.detail.MedDetailActivity
import com.fillahaufi.medlit.ui.others.BookmarkActivity
import com.fillahaufi.medlit.ui.others.SearchResultActivity

class HomeFragment : Fragment() {

    private var interactionListener: IHomeFragment? = null

    private lateinit var rvMedicine: RecyclerView
    private lateinit var adapter: ListMedicineAdapter
    private var data = ArrayList<Medicine>()

    private val listDummy = ArrayList<Medicine>()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        when(context) {
            is IHomeFragment -> {
                interactionListener = context
            }
            else -> throw RuntimeException("$context has to implement IHomeFragment")
        }
    }

    private lateinit var viewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        setupViewModel()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        listDummy.removeAll(listMedicines.toSet())
//        listDummy.addAll(listMedicines)

        searchMed()
        goToBookmark()
        setName()
        setItemRow()

        interactionListener?.getHomeFragmentData().let {
            getAllMedicines(it?.token.toString())
            showRecyclerList()
        }

    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(context!!, null)
        val vM: HomeViewModel by viewModels {
            factory
        }
        viewModel = vM
    }

    private fun searchMed() {
        binding.goToSearch.setOnClickListener {
            val intentToSearch = Intent(activity, SearchResultActivity::class.java)
            startActivity(intentToSearch)
        }
    }

    private fun setItemRow() {
        binding.rvMedicine.layoutParams.height = getScreenHeight() - 700
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    private fun getAllMedicines(authHeader: String) {
        Log.d("authHeaderrr", authHeader)
        viewModel.getAllMedicines("Bearer $authHeader").observe(this, {
            if (it != null) {
                when(it) {
                    is Result.Loading -> {
                        Log.d("apakah loading", "yessss")
                        showLoading(true)
                    }
                    is Result.Success -> {
                        Log.d("apakah sukses", "yessss")
                        changeData(it.data as ArrayList<Medicine>)
                        showLoading(false)
                    }
                    is Result.Error -> {
                        Toast.makeText(activity, it.error, Toast.LENGTH_SHORT).show()
                        Log.d("apakah error", "yesss")
                        showLoading(false)
                    }
                }
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbHome.visibility = View.VISIBLE
        }
        else {
            binding.pbHome.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun changeData(list: ArrayList<Medicine>) {
        data.clear()
        data.addAll(list)
        adapter.notifyDataSetChanged()
    }

//    private val listMedicines: ArrayList<Medicine>
//        get() {
//            val dataId = resources.getIntArray(R.array.data_id)
//            val dataName = resources.getStringArray(R.array.data_name)
//            val dataPhoto = resources.getStringArray(R.array.data_img)
//            val listMed = ArrayList<Medicine>()
//            for (i in dataName.indices) {
//                val med = Medicine(dataId[i], dataName[i], dataPhoto[i], false)
//                listMed.add(med)
//            }
//            return listMed
//        }

    private fun showRecyclerList() {
        rvMedicine = view!!.findViewById(R.id.rv_medicine)
        rvMedicine.setHasFixedSize(true)

        if (context?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvMedicine.layoutManager = GridLayoutManager(activity, 2)
        } else {
            rvMedicine.layoutManager = GridLayoutManager(activity, 2)
        }
        adapter = ListMedicineAdapter(data)
        rvMedicine.adapter = adapter
        adapter.setOnItemClickCallback(object : ListMedicineAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Medicine) {
                showMedicineDetail(data)
//                showSelectedHero(data)
            }
        })
    }

    private fun showMedicineDetail(data: Medicine) {
        val intentToDetail = Intent(activity, MedDetailActivity::class.java)
        intentToDetail.putExtra(MedDetailActivity.MED_NAME, data.name)
        intentToDetail.putExtra(MedDetailActivity.MED_IMG, data.photo)
        intentToDetail.putExtra(MedDetailActivity.MED_PURPOSE, data.purpose)
        intentToDetail.putExtra(MedDetailActivity.MED_SE, data.side_effetcs)
        intentToDetail.putExtra(MedDetailActivity.MED_CONTRA, data.contraindication)
        intentToDetail.putExtra(MedDetailActivity.MED_DOSE, data.dosage)
        intentToDetail.putExtra(MedDetailActivity.MED_ING, data.ingredients)
        startActivity(intentToDetail)
    }

    private fun showSelectedHero(med: Medicine) {
        Toast.makeText(activity, "Kamu memilih " + med.name, Toast.LENGTH_SHORT).show()
    }

    private fun setName() {
        interactionListener?.getHomeFragmentData().let {
            binding.textGreeting.text = it?.name
        }
    }

    private fun goToBookmark() {
        binding.bookmark.setOnClickListener {
            val intentToBookmark = Intent(activity, BookmarkActivity::class.java)
            startActivity(intentToBookmark)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}