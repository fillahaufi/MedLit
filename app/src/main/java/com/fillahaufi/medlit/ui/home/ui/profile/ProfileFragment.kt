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
import com.fillahaufi.medlit.ui.SplashActivity
import com.fillahaufi.medlit.ui.ViewModelFactory
import com.fillahaufi.medlit.ui.home.IHomeFragment
import com.fillahaufi.medlit.ui.home.ui.home.HomeViewModel
import com.fillahaufi.medlit.ui.welcome.WelcomeActivity

class ProfileFragment : Fragment() {

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

        setupViewModel()
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setProfile()
        logout()

        return root
    }

    private fun setProfile() {
        interactionListener?.getHomeFragmentData().let {
            binding.fullname.text = it?.name
            binding.email.text = it?.email
        }
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(context!!, null)
        val vM: ProfileViewModel by viewModels {
            factory
        }
        viewModel = vM
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
            viewModel.logout()
            val intentToWelcome = Intent(activity, SplashActivity::class.java)
            startActivity(intentToWelcome)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}