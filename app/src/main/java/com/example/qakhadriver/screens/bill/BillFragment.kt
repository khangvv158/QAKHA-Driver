package com.example.qakhadriver.screens.bill

import android.Manifest
import android.app.Activity
import android.content.Context
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
import androidx.core.os.bundleOf
import com.example.qakhadriver.R
import com.example.qakhadriver.data.model.Driver
import com.example.qakhadriver.data.model.DriverFirebase
import com.example.qakhadriver.data.repository.ProfileRepositoryImpl
import com.example.qakhadriver.data.repository.TokenRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.screens.bill.adapter.WorkingPagerAdapter
import com.example.qakhadriver.screens.bill.tabs.done.DoneFragment
import com.example.qakhadriver.screens.bill.tabs.freepick.FreePickFragment
import com.example.qakhadriver.service.FirebaseLocationService
import com.example.qakhadriver.utils.*
import kotlinx.android.synthetic.main.fragment_bill.*

class BillFragment : Fragment(), BillContract.View {

    private val workingPagerAdapter by lazy {
        WorkingPagerAdapter(childFragmentManager, requireContext())
    }
    private val presenter by lazy {
        BillPresenter(
            ProfileRepositoryImpl.getInstance(),
            TokenRepositoryImpl.getInstance(
                SharedPrefsImpl.getInstance(requireContext())
            )
        )
    }
    private lateinit var driver: Driver
    private var flagStatusDriver = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getParcelable<Driver>(BUNDLE_DRIVER)?.let {
            driver = it
        }
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
        initViews()
        initData()
        handleEvents()
    }

    override fun onGetStatusOffline() {
        flagStatusDriver = 0
        buttonStatus.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.ColorRedBrick
            )
        )
    }

    override fun onGoOnlineSuccess() {
        flagStatusDriver = 1
        buttonStatus.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorGreenHaze
            )
        )
        startServiceToOnline(DriverFirebase(driver.idDriver, driver.name))
    }

    override fun onGoOfflineSuccess() {
        flagStatusDriver = 0
        buttonStatus.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.ColorRedBrick
            )
        )
        stopServiceToOffline()
    }

    override fun onGoOfflineFailure() {
        makeText(getString(R.string.content_offline_failure))
    }

    override fun onGetStatusOnline() {
        flagStatusDriver = 1
        buttonStatus.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorGreenHaze
            )
        )
    }

    override fun onError(message: String) {
        makeText(message)
    }

    private fun initWorkingViewPager() {
        viewPagerWorking.apply {
            offscreenPageLimit = OFF_SCREEN_PAGE_LIMIT
            adapter = workingPagerAdapter.apply {
                addFragment(FreePickFragment.newInstance(driver))
                addFragment(DoneFragment.newInstance())
            }
        }
        tabLayoutWorking.setupWithViewPager(viewPagerWorking)
    }

    private fun initViews() {
        presenter.setView(this)
    }

    private fun initData() {
        presenter.getStatus()
    }

    private fun handleEvents() {
        buttonStatus.setOnClickListener {
            if (flagStatusDriver == 0) {
                requireContext().showDialogWithListener(
                    getString(R.string.lets_start_working),
                    getString(R.string.fast_delivery),
                    object : IPositiveNegativeListener {
                        override fun onPositive() {
                            presenter.goOnline(driver.idDriver)
                        }
                    },
                    Constants.ONLINE,
                    true
                )
            } else {
                requireContext().showDialogWithListener(
                    getString(R.string.finish_work),
                    getString(R.string.do_you_want_to_end_work),
                    object : IPositiveNegativeListener {
                        override fun onPositive() {
                            presenter.goOffline(driver.idDriver)
                        }
                    },
                    Constants.OFFLINE,
                    true
                )
            }
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

    private fun startServiceToOnline(driverFirebase: DriverFirebase) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            activity?.startService(
                Intent(requireContext(), FirebaseLocationService::class.java).apply {
                    putExtra(FirebaseLocationService.BUNDLE_DRIVER, driverFirebase)
                })
        } else {
            activity?.startForegroundService(
                Intent(requireContext(), FirebaseLocationService::class.java).apply {
                    putExtra(FirebaseLocationService.BUNDLE_DRIVER, driverFirebase)
                })
        }
    }

    private fun stopServiceToOffline() {
        activity?.stopService(Intent(requireContext(), FirebaseLocationService::class.java))
    }

    companion object {

        private const val BUNDLE_DRIVER = "BUNDLE_DRIVER"
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 102
        private const val OFF_SCREEN_PAGE_LIMIT = 3

        fun newInstance(driver: Driver) = BillFragment().apply {
            arguments = bundleOf(BUNDLE_DRIVER to driver)
        }
    }
}
