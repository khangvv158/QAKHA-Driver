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
import com.example.qakhadriver.data.model.Driver
import com.example.qakhadriver.data.model.Image
import com.example.qakhadriver.data.repository.ProfileRepositoryImpl
import com.example.qakhadriver.data.repository.TokenRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.screens.container.ContainerFragment
import com.example.qakhadriver.screens.signin.OnSignInSuccessListener
import com.example.qakhadriver.screens.signin.SignInFragment
import com.example.qakhadriver.utils.*

class AuthenticationFragment : Fragment(), AuthenticationContract.View, OnSignInSuccessListener {

    private val presenter by lazy {
        AuthenticationPresenter(
            TokenRepositoryImpl.getInstance(
                SharedPrefsImpl.getInstance(requireContext())
            ),
            ProfileRepositoryImpl.getInstance()
        )
    }

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

    override fun onSignInSuccess() {
        presenter.checkSignedInByToken()
    }

    override fun onCheckSignedInByTokenSuccess() {
        /*Code offline here*/
        presenter.getProfile()
    }

    override fun onCheckSignedInByTokenFailure() {
        navigateSignInFragment()
    }

    override fun onGetProfileSuccess(driver: Driver) {
        navigateContainerFragment(driver)
    }

    override fun onError(message: String) {
        makeText(message)
    }

    private fun navigateContainerFragment(driver: Driver) {
        replaceFragmentSlideAnim(
            ContainerFragment.newInstance(driver),
            R.id.containerViewAuthentication
        )
    }

    private fun navigateSignInFragment() {
        replaceFragmentSlideAnim(SignInFragment.newInstance().apply {
            registerOnSignInSuccessListener(this@AuthenticationFragment)
        }, R.id.containerViewAuthentication)
    }

    private fun initView() {
        presenter.apply {
            setView(this@AuthenticationFragment)
            checkSignedInByToken()
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

    companion object {

        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101

        fun newInstance() = AuthenticationFragment()
    }
}
