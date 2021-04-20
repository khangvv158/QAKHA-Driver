package com.example.qakhadriver.screens.bill.tabs.freepick

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.qakhadriver.R
import com.example.qakhadriver.data.model.Driver
import com.example.qakhadriver.data.model.Order
import com.example.qakhadriver.data.model.OrderFirebase
import com.example.qakhadriver.data.repository.OrderFirebaseRepositoryImpl
import com.example.qakhadriver.screens.orderdetail.OrderDetailFragment
import com.example.qakhadriver.utils.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_free_pick.*
import kotlinx.android.synthetic.main.item_layout_free_pick.*

class FreePickFragment : Fragment(), FreePickContract.View {

    private lateinit var googleMap: GoogleMap
    private val googleMapHelper by lazy {
        GoogleMapHelper()
    }
    private val locationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }
    private val presenter by lazy {
        FreePickPresenter(OrderFirebaseRepositoryImpl.getInstance())
    }
    private lateinit var driver: Driver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_free_pick, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        arguments?.getParcelable<Driver>(BUNDLE_DRIVER)?.let {
            driver = it
        }
        MapsInitializer.initialize(requireContext())
        mapFragment?.getMapAsync {
            setUpGoogleMap(it)
        }
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        map.onDestroy()
    }

    override fun onGetOrderFirebaseSuccess(orderFirebase: OrderFirebase) {
        // call api fetch order
    }

    override fun onGetOrderFirebaseRemove() {
        freePickLayout.gone()
    }

    override fun onGetOrderDetailSuccess(order: Order) {
        // bind order to view
    }

    override fun onGetOrderDetailFailure() {
       //error plesed reload
    }

    override fun onError(message: String) {
        makeText(message)
    }

    @SuppressLint("MissingPermission")
    private fun setUpGoogleMap(map: GoogleMap) {
        googleMap = map
        googleMap.isMyLocationEnabled = true
        locationProviderClient.lastLocation.addOnSuccessListener {
            animateCamera(LatLng(it.latitude, it.longitude))
        }
        initViews()
        initData()
        handleEvents()
    }

    private fun initViews() {
        presenter.setView(this)
    }

    private fun initData() {
        presenter.getOrderFirebase(driver.idDriver)
    }

    private fun handleEvents() {
        freePickLayout.setOnClickListener {
            parentFragment?.parentFragment?.addFragmentBackStack(
                OrderDetailFragment.newInstance(),
                R.id.containerViewMain
            )
        }
        deliveryTextView.setOnClickListener {
            // map add marker partner and marker user
        }
    }

    private fun animateCamera(latLng: LatLng) {
        val cameraUpdate = googleMapHelper.buildCameraUpdate(latLng)
        googleMap.animateCamera(cameraUpdate, 10, null)
    }

    companion object {

        private const val BUNDLE_DRIVER = "BUNDLE_DRIVER"

        fun newInstance(driver: Driver) = FreePickFragment().apply {
            arguments = bundleOf(BUNDLE_DRIVER to driver)
        }
    }
}
