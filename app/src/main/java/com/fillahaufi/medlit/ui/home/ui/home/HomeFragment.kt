package com.fillahaufi.medlit.ui.home.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fillahaufi.medlit.databinding.FragmentHomeBinding
import com.fillahaufi.medlit.ui.home.IHomeFragment

class HomeFragment : Fragment() {

    private var interactionListener: IHomeFragment? = null

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

        goToBookmark()
        setName()

        return root
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