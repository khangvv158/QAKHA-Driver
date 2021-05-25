package com.example.qakhadriver.screens.container

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.viewpager.widget.ViewPager
import com.afollestad.materialdialogs.MaterialDialog
import com.example.qakhadriver.R
import com.example.qakhadriver.broadcast.LocationBroadcast
import com.example.qakhadriver.data.model.Driver
import com.example.qakhadriver.data.model.Event
import com.example.qakhadriver.screens.container.adapter.ContainerPagerAdapter
import com.example.qakhadriver.screens.earnings.EarningsFragment
import com.example.qakhadriver.screens.bill.BillFragment
import com.example.qakhadriver.screens.me.MeFragment
import com.example.qakhadriver.utils.IPositiveNegativeListener
import com.example.qakhadriver.utils.LocationHelper
import com.example.qakhadriver.utils.TypeMenu
import kotlinx.android.synthetic.main.fragment_container.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ContainerFragment : Fragment() {

    private val adapter: ContainerPagerAdapter by lazy {
        ContainerPagerAdapter(childFragmentManager)
    }
    private lateinit var billFragment: BillFragment
    private lateinit var earningsFragment: EarningsFragment
    private lateinit var meFragment: MeFragment
    private lateinit var materialDialog: MaterialDialog
    private val gpsReceiver = LocationBroadcast()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initViews()
        handleEvents()
        initDialog()
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
            }
        }
    }

    private fun initDialog() {
        materialDialog = getPositiveDialogWithListener(
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
            false
        )
    }

    override fun onStart() {
        super.onStart()
        requireContext().registerReceiver(
            gpsReceiver,
            IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().unregisterReceiver(gpsReceiver)
    }

    private fun initData() {
        arguments?.getParcelable<Driver>(BUNDLE_DRIVER)?.let {
            billFragment = BillFragment.newInstance(it)
            earningsFragment = EarningsFragment.newInstance(it)
            meFragment = MeFragment.newInstance(it)
            adapter.apply {
                addFragment(billFragment)
                addFragment(earningsFragment)
                addFragment(meFragment)
            }
        }
    }

    private fun initViews() {
        containerViewPager.apply {
            adapter = this@ContainerFragment.adapter
            offscreenPageLimit = OFF_SCREEN_PAGE_LIMIT
        }
    }

    private fun handleEvents() {
        containerViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) = Unit

            override fun onPageSelected(position: Int) {
                bottomNavigationView.selectedItemId =
                    bottomNavigationView.menu.getItem(position).itemId
            }

            override fun onPageScrollStateChanged(state: Int) = Unit
        })
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_menu_nav_bill -> {
                    containerViewPager.currentItem = TypeMenu.BILL.ordinal
                    true
                }
                R.id.item_menu_nav_earnings -> {
                    containerViewPager.currentItem = TypeMenu.EARNINGS.ordinal
                    true
                }
                R.id.item_menu_nav_me -> {
                    containerViewPager.currentItem = TypeMenu.ME.ordinal
                    true
                }
                else -> false
            }
        }
    }

    private fun showDialogEnableGPS() {
        materialDialog.show()
    }

    private fun hideDialogEnableGPS() {
        materialDialog.hide()
    }

    private fun getPositiveDialogWithListener(
        callingClassContext: Context,
        title: String, content: String,
        positiveNegativeListener: IPositiveNegativeListener,
        positiveText: String,
        cancelable: Boolean
    ): MaterialDialog {
        return buildDialog(callingClassContext, title, content)
            .builder
            .positiveText(positiveText)
            .positiveColor(ContextCompat.getColor(requireContext(), R.color.colorCarrotOrange))
            .onPositive { _, _ -> positiveNegativeListener.onPositive() }
            .cancelable(cancelable).build()
    }

    private fun buildDialog(
        callingClassContext: Context,
        title: String,
        content: String
    ): MaterialDialog {
        return MaterialDialog.Builder(callingClassContext)
            .title(title)
            .content(content)
            .build()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventBus(event: Event<String>) {
        if (event.keyEvent == LocationBroadcast.EVENT_REQUEST_ENABLE_GPS) {
            showDialogEnableGPS()
        } else {
            hideDialogEnableGPS()
        }
    }

    companion object {

        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 21
        const val BUNDLE_DRIVER = "BUNDLE_DRIVER"
        const val OFF_SCREEN_PAGE_LIMIT = 3

        fun newInstance(driver: Driver) = ContainerFragment().apply {
            arguments = bundleOf(BUNDLE_DRIVER to driver)
        }
    }
}
