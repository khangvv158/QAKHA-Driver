package com.example.qakhadriver.screens.authentication

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.qakhadriver.R
import com.example.qakhadriver.data.model.Driver
import com.example.qakhadriver.data.model.Event
import com.example.qakhadriver.data.model.Image
import com.example.qakhadriver.data.repository.ProfileRepositoryImpl
import com.example.qakhadriver.data.repository.TokenRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.screens.container.ContainerFragment
import com.example.qakhadriver.screens.me.MeFragment
import com.example.qakhadriver.screens.signin.OnSignInSuccessListener
import com.example.qakhadriver.screens.signin.SignInFragment
import com.example.qakhadriver.screens.signup.imagesignup.ImageSignUpFragment
import com.example.qakhadriver.utils.*
import kotlinx.android.synthetic.main.fragment_image_sign_up.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception

class AuthenticationFragment : Fragment(), AuthenticationContract.View, OnSignInSuccessListener {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }

    private val presenter by lazy {
        AuthenticationPresenter(
            TokenRepositoryImpl.getInstance(
                SharedPrefsImpl.getInstance(requireContext())
            ),
            ProfileRepositoryImpl.getInstance()
        )
    }
    private var driver: Driver? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            driver?.let { checkPermission(it) }
        }
    }

    override fun onSignInSuccess() {
        presenter.checkSignedInByToken()
    }

    override fun onCheckSignedInByTokenSuccess() {
        presenter.getProfile()
    }

    override fun onCheckSignedInByTokenFailure() {
        navigateSignInFragment()
    }

    override fun onGetProfileSuccess(driver: Driver) {
        this.driver = driver
        navigateContainerFragment(driver)
    }

    override fun onError(message: String) {
        makeText(message)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventBusSignOutSuccess(event: Event<Int>) {
        if (event.keyEvent == MeFragment.EVENT_SIGN_OUT) {
            navigateSignInFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_authentication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onStop() {
        presenter.onStop()
        super.onStop()
    }

    private fun initView() {
        presenter.apply {
            setView(this@AuthenticationFragment)
            checkSignedInByToken()
        }
    }

    private fun navigateContainerFragment(driver: Driver) {
        checkPermission(driver)
    }

    private fun navigateSignInFragment() {
        replaceFragmentSlideAnim(SignInFragment.newInstance().apply {
            registerOnSignInSuccessListener(this@AuthenticationFragment)
        }, R.id.containerViewAuthentication)
    }

    private fun checkPermission(driver: Driver) {
        if (!LocationHelper.isPlayServicesAvailable(requireContext())) {
            Toast.makeText(
                requireContext(),
                getString(R.string.play_services_did_not_installed),
                Toast.LENGTH_SHORT
            )
                .show()
        } else enableGPS(driver)
    }

    private fun enableGPS(driver: Driver) {
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
            replaceFragmentSlideAnim(
                ContainerFragment.newInstance(driver),
                R.id.containerViewAuthentication
            )
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
            false
        )
    }

    companion object {

        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101

        fun newInstance() = AuthenticationFragment()
    }
}
