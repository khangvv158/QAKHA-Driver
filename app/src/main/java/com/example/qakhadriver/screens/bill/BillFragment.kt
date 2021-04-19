package com.example.qakhadriver.screens.bill

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.qakhadriver.R
import com.example.qakhadriver.data.model.DriverFirebase
import com.example.qakhadriver.screens.bill.adapter.WorkingPagerAdapter
import com.example.qakhadriver.screens.bill.tabs.done.DoneFragment
import com.example.qakhadriver.screens.bill.tabs.freepick.FreePickFragment
import com.example.qakhadriver.service.FirebaseLocationService
import com.example.qakhadriver.utils.IPositiveNegativeListener
import com.example.qakhadriver.utils.LocationHelper
import kotlinx.android.synthetic.main.fragment_bill.*

class BillFragment : Fragment(), BillContract.View {

    private val workingPagerAdapter by lazy {
        WorkingPagerAdapter(childFragmentManager, requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bill, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        initWorkingViewPager()
        handleEvents()
    }

    private fun initWorkingViewPager() {
        viewPagerWorking.apply {
            offscreenPageLimit = OFF_SCREEN_PAGE_LIMIT
            adapter = workingPagerAdapter.apply {
                addFragment(FreePickFragment.newInstance())
                addFragment(DoneFragment.newInstance())
            }
        }
        tabLayoutWorking.setupWithViewPager(viewPagerWorking)
    }

    private fun handleEvents() {
        buttonStatus.setOnClickListener {
            buttonStatus.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorGreenHaze
                )
            )
            startServiceToOnline()
        }
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

    private fun startServiceToOnline() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            activity?.startService(
                Intent(requireContext(), FirebaseLocationService::class.java).apply {
                    putExtra(FirebaseLocationService.BUNDLE_DRIVER, DriverFirebase(2, 0f, 0f))
                })
        } else {
            activity?.startForegroundService(
                Intent(requireContext(), FirebaseLocationService::class.java).apply {
                    putExtra(FirebaseLocationService.BUNDLE_DRIVER, DriverFirebase(2, 0f, 0f))
                })
        }
    }

    private fun stopServiceToOffline() {
        activity?.stopService(Intent(requireContext(), FirebaseLocationService::class.java))
    }

    companion object {

        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 102
        private const val OFF_SCREEN_PAGE_LIMIT = 3

        fun newInstance() = BillFragment()
    }
}
