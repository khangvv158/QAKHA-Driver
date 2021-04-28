package com.example.qakhadriver.screens.me

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.qakhadriver.R
import com.example.qakhadriver.data.model.Driver
import com.example.qakhadriver.data.model.Event
import com.example.qakhadriver.data.repository.ProfileRepositoryImpl
import com.example.qakhadriver.data.repository.TokenRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.screens.feedback.FeedbackFragment
import com.example.qakhadriver.screens.me.navigate.about.AboutFragment
import com.example.qakhadriver.screens.me.navigate.helpcenter.HelpCenterFragment
import com.example.qakhadriver.screens.me.navigate.settings.SettingFragment
import com.example.qakhadriver.utils.addFragmentBackStack
import com.example.qakhadriver.utils.makeText
import kotlinx.android.synthetic.main.fragment_me.*
import org.greenrobot.eventbus.EventBus

class MeFragment : Fragment(), MeContract.View {

    private val presenter by lazy {
        MePresenter(
            TokenRepositoryImpl.getInstance(SharedPrefsImpl.getInstance(requireContext())),
            ProfileRepositoryImpl.getInstance()
        )
    }
    private lateinit var driver: Driver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_me, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initViews()
        handleEvent()
    }

    override fun onCheckStatusIsOfflineSuccess() {
        presenter.signOut()
    }

    override fun onCheckStatusIsOfflineFailure() {
        makeText(getString(R.string.content_signout_failure))
    }

    override fun onSignOutSuccess() {
        EventBus.getDefault().post(Event(EVENT_SIGN_OUT, EVENT_SIGN_OUT_ID))
    }

    private fun initViews() {
        presenter.setView(this)
    }

    override fun onError(message: String) {
        makeText(message)
    }

    private fun initData() {
        arguments?.getParcelable<Driver>(BUNDLE_DRIVER)?.let {
            Glide.with(requireContext()).load(it.image.imageUrl).into(imageViewAvatar)
            textViewName.text = it.name
            driver = it
        }
    }

    private fun handleEvent() {
        signOutButton.setOnClickListener {
            presenter.checkStatusIsOffline()
        }
        setupNavigation()
    }

    private fun setupNavigation() {
        navProfile.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.settingsItemMenu -> navigateToFragment(SettingFragment.newInstance())
                R.id.helpCenterItemMenu -> navigateToFragment(HelpCenterFragment.newInstance())
                R.id.aboutItemMenu -> navigateToFragment(AboutFragment.newInstance())
                R.id.feedbackItemMenu -> navigateToFragment(FeedbackFragment.newInstance(driver))
            }
            true
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        parentFragment?.addFragmentBackStack(fragment, R.id.containerViewAuthentication)
    }

    companion object {

        private const val BUNDLE_DRIVER = "BUNDLE_DRIVER"
        private const val EVENT_SIGN_OUT_ID = 1
        const val EVENT_SIGN_OUT = "EVENT_SIGN_OUT"

        fun newInstance(driver: Driver) = MeFragment().apply {
            arguments = bundleOf(BUNDLE_DRIVER to driver)
        }
    }
}
