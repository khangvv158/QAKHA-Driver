package com.example.qakhadriver.screens.signin

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.qakhadriver.R
import com.example.qakhadriver.data.repository.SignRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.screens.signup.SignUpFragment
import com.example.qakhadriver.utils.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : Fragment(), SignInContract.View {

    private val presenter by lazy {
        SignInPresenter(
            SignRepositoryImpl.getInstance(
                SharedPrefsImpl.getInstance(requireContext())
            )
        )
    }
    private val locationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }
    private var onSignInSuccessListener: OnSignInSuccessListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        handleEvents()
    }

    override fun onStop() {
        presenter.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        onSignInSuccessListener = null
        super.onDestroy()
    }

    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (!LocationHelper.isHaveLocationPermission(requireContext())) {
                ActivityCompat.requestPermissions(
                    activity as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                )
                return
            }
            if (LocationHelper.isLocationProviderEnabled(requireContext())) {
                showDialogEnableGPS()
            } else {
                locationProviderClient.lastLocation.addOnCompleteListener {
                    // call first
                }
            }
        }
    }

    override fun onSignInSuccess() {
        progressBar.gone()
        onSignInSuccessListener?.onSignInSuccess()
    }

    override fun onSignInFailure(message: String) {
        emailTextInputLayout.error = Constants.SPACE_STRING
        passwordTextInputLayout.error = message
        progressBar.gone()
    }

    override fun onError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        progressBar.gone()
    }

    override fun onSignInRoleFailure() {
        Toast.makeText(
            context,
            getString(R.string.you_cannot_sign_in_with_this_account),
            Toast.LENGTH_LONG
        ).show()
    }

    fun registerOnSignInSuccessListener(onSignInSuccessListener: OnSignInSuccessListener) {
        this.onSignInSuccessListener = onSignInSuccessListener
    }

    private fun initViews() {
        presenter.setView(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleEvents() {
        textViewSignUp.setOnClickListener {
            addFragmentSlideAnim(SignUpFragment.newInstance(), R.id.containerViewAuthentication)
        }
        passwordEditText.setOnTouchListener { _, _ ->
            passwordTextInputLayout.error = null
            passwordTextInputLayout.isErrorEnabled = false
            false
        }
        emailEditText.setOnTouchListener { _, _ ->
            emailTextInputLayout.error = null
            emailTextInputLayout.isErrorEnabled = false
            false
        }
        signInButton.setOnClickListener {
            hideKeyboard()
            checkPermission()
        }
        imageViewLoginGoogle.setOnClickListener {
            makeText(getString(R.string.coming_soon))
        }
        imageViewLoginFacebook.setOnClickListener {
            makeText(getString(R.string.coming_soon))
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
        if (LocationHelper.isLocationProviderEnabled(requireContext())) {
            showDialogEnableGPS()
        } else {
            presenter.signIn(emailEditText.text.toString(), passwordEditText.text.toString())
            progressBar.show()
        }
    }

    private fun showDialogEnableGPS() {
        LocationHelper.showPositiveDialogWithListener(
            requireContext(),
            getString(R.string.enable_gps_service),
            getString(R.string.content_location),
            object : IPositiveNegativeListener {
                override fun onPositive() {
                    startActivityForResult(
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                    )
                }
            },
            getString(R.string.turn_on),
            true
        )
    }

    companion object {

        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 111

        fun newInstance() = SignInFragment()
    }
}
