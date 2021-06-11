package com.example.qakhadriver.screens.signin

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.qakhadriver.R
import com.example.qakhadriver.data.repository.SignRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.data.source.remote.schema.request.EmailRequest
import com.example.qakhadriver.screens.forgotpassword.ForgotPasswordFragment
import com.example.qakhadriver.screens.signup.SignUpFragment
import com.example.qakhadriver.screens.signup.activate.ActivateFragment
import com.example.qakhadriver.utils.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_sign_in.*
import org.greenrobot.eventbus.EventBus


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
    private var emailRequest: EmailRequest? = null

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
                }
            }
        }
    }

    override fun onSignInSuccess() {
        progressBar.gone()
        onSignInSuccessListener?.onSignInSuccess()
    }

    override fun onSignInFailure(message: String) {
        progressBar.gone()
        if (message == "Your account has not been activated. Please check your email for the activation code.") {
            addFragmentSlideAnim(
                ActivateFragment.newInstance(emailRequest),
                R.id.containerViewAuthentication
            )
        } else if (message == "Your account has not activation from admin. Please contact with QAKHA Team.") {
            Toast.makeText(
                requireContext(),
                getString(R.string.content_account_has_not_activation_from_admin),
                Toast.LENGTH_LONG
            ).show()
        } else {
            emailTextInputLayout.error = Constants.SPACE_STRING
            passwordTextInputLayout.error = message
        }
    }

    override fun onError(message: String) {
        progressBar.gone()
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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
        forgotPasswordTextView.setOnSafeClickListener {
            addFragmentSlideAnim(
                ForgotPasswordFragment.newInstance(),
                R.id.containerViewAuthentication
            )
        }
        textViewSignUp.setOnSafeClickListener {
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
        signInButton.setOnSafeClickListener {
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
            if (emailEditText.text.toString().isNotBlank()) {
                emailRequest = EmailRequest(emailEditText.text.toString())
            }
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
                    if (isAdded) {
                        startActivityForResult(
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                        )
                    }
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
