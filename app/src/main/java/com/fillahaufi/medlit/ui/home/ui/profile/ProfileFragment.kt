package com.fillahaufi.medlit.ui.home.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fillahaufi.medlit.databinding.FragmentProfileBinding
import com.fillahaufi.medlit.ui.welcome.WelcomeActivity

class ProfileFragment : Fragment() {

    private lateinit var notificationsViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        logout()

        return root
    }

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