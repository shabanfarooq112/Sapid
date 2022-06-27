package com.horizam.sapid.fragments;

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.horizam.sapid.App
import com.horizam.sapid.Constants
import com.horizam.sapid.R
import com.horizam.sapid.activities.EditProfileActivity
import com.horizam.sapid.activities.QrscanActivity
import com.horizam.sapid.callbacks.DrawerHandler
import com.horizam.sapid.databinding.FragmentProfile1HomeBinding


class HomeBusinessFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentProfile1HomeBinding
    private lateinit var drawerHandlerCallback: DrawerHandler

    var direct: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfile1HomeBinding.inflate(inflater, container, false)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        clickListeners()
        initComponents()
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        changeDirectButtonState(direct)
    }

    private fun initComponents() {
        binding.tvNameProfile1.text = Constants.USER_NAME
        binding.tvBioEditProfile2.text = Constants.USER_BIO
    }

    private fun clickListeners() {
        binding.ivChangeFragmentNext.setOnClickListener { navController.navigate(R.id.twohome_fragment) }
        binding.ivQrcode.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    QrscanActivity::class.java
                )
            )
        }
        binding.navIconHome.setOnClickListener {
            drawerHandlerCallback.openDrawer()
        }
        binding.btnVcfProfile.setOnClickListener {
            startActivity(Intent(App.getAppContext(), EditProfileActivity::class.java))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        drawerHandlerCallback = context as DrawerHandler
    }

    private fun changeDirectButtonState(mDirect: Int) {
        if (mDirect == 0) {
            binding.btnDirectOnOff.text = Constants.DIRECT_OFF
            binding.btnDirectOnOff.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.white)
            binding.btnDirectOnOff.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
            direct = 1
        } else {
            binding.btnDirectOnOff.text = Constants.DIRECT_ON
            binding.btnDirectOnOff.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.Black)
            binding.btnDirectOnOff.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            direct = 0
        }

    }
}