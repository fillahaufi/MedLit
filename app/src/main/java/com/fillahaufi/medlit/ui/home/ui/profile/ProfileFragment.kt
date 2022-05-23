package com.fillahaufi.medlit.ui.home.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.fillahaufi.medlit.data.local.UserPreference
import com.fillahaufi.medlit.databinding.FragmentProfileBinding
import com.fillahaufi.medlit.ui.ViewModelFactory
import com.fillahaufi.medlit.ui.welcome.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        setupViewModel()
//        viewModel =
//            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        logout()

        return root
    }

//    private fun setupViewModel() {
//        val factory: ViewModelFactory = ViewModelFactory.getInstance(context!!, UserPreference.getInstance(dataStore))
//        val vM: ProfileViewModel by viewModels {
//            factory
//        }
//        viewModel = vM
//    }

    private fun logout() {
        binding.logout.setOnClickListener {
            val intentToWelcome = Intent(activity, WelcomeActivity::class.java)
            startActivity(intentToWelcome)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}