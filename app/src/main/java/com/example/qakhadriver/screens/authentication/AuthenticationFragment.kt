package com.example.qakhadriver.screens.authentication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.qakhadriver.R
import com.example.qakhadriver.screens.signin.SignInFragment
import com.example.qakhadriver.utils.IPositiveNegativeListener
import com.example.qakhadriver.utils.LocationHelper
import com.example.qakhadriver.utils.addFragment

class AuthenticationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_authentication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        initView()
    }

    private fun initView() {
        addFragment(SignInFragment.newInstance(), R.id.containerViewAuthentication)
    }

    private fun checkPermission() {
        if (!LocationHelper.isPlayServicesAvailable(requireContext())) {
            Toast.makeText(
                requireContext(),
                getString(R.string.play_services_did_not_installed),
                Toast.LENGTH_SHORT
            )
                .show()
        } else enableGPS()
    }

    private fun enableGPS() {
        if (!LocationHelper.isHaveLocationPermission(requireContext())) {
            ActivityCompat.requestPermissions(
                activity as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
            return
        }
        if (LocationHelper.isLocationProviderEnabled(requireContext()))
            showDialogEnableGPS()
    }

    private fun showDialogEnableGPS() {
        LocationHelper.showPositiveDialogWithListener(
            requireContext(),
            getString(R.string.enable_gps_service),
            getString(R.string.content_location),
            object : IPositiveNegativeListener {
                override fun onPositive() {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            },
            getString(R.string.turn_on),
            false
        )
    }

    companion object {

        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101

        fun newInstance() = AuthenticationFragment()
    }
}
