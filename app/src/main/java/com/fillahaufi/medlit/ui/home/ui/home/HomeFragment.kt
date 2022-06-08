package com.fillahaufi.medlit.ui.home.ui.home

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fillahaufi.medlit.R
import com.fillahaufi.medlit.data.local.Medicine
import com.fillahaufi.medlit.databinding.FragmentHomeBinding
import com.fillahaufi.medlit.ui.camera.ListMedicineAdapter
import com.fillahaufi.medlit.ui.home.IHomeFragment
import com.fillahaufi.medlit.ui.home.ui.detail.MedDetailActivity

class HomeFragment : Fragment() {

    private var interactionListener: IHomeFragment? = null
    private lateinit var rvMedicine: RecyclerView
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

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listDummy.addAll(listMedicines)

        goToBookmark()
        setName()
        setItemRow()
        showRecyclerList()

    }

    private fun setItemRow() {
        binding.rvMedicine.layoutParams.height = getScreenHeight() - 700
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    private val listMedicines: ArrayList<Medicine>
        get() {
            val dataId = resources.getStringArray(R.array.data_id)
            val dataName = resources.getStringArray(R.array.data_name)
            val dataPhoto = resources.getStringArray(R.array.data_img)
            val listMed = ArrayList<Medicine>()
            for (i in dataName.indices) {
                val med = Medicine(dataId[i], dataName[i], dataPhoto[i])
                listMed.add(med)
            }
            return listMed
        }

    private fun showRecyclerList() {
        rvMedicine = view!!.findViewById(R.id.rv_medicine)
        rvMedicine.setHasFixedSize(true)

        if (context?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvMedicine.layoutManager = GridLayoutManager(activity, 2)
        } else {
            rvMedicine.layoutManager = GridLayoutManager(activity, 2)
        }
        val listHeroAdapter = ListMedicineAdapter(listDummy)
        rvMedicine.adapter = listHeroAdapter
        listHeroAdapter.setOnItemClickCallback(object : ListMedicineAdapter.OnItemClickCallback {
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
            Toast.makeText(activity, "Fitur dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}