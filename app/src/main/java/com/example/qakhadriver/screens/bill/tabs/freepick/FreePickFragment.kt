package com.example.qakhadriver.screens.bill.tabs.freepick

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qakhadriver.R
import com.example.qakhadriver.data.model.DriverFirebase
import com.example.qakhadriver.data.model.OrderFirebase
import com.example.qakhadriver.data.repository.DriverRepositoryImpl
import com.example.qakhadriver.utils.GoogleMapHelper
import com.example.qakhadriver.utils.gone
import com.example.qakhadriver.utils.makeText
import com.example.qakhadriver.utils.show
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
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
        FreePickPresenter(DriverRepositoryImpl.getInstance())
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_free_pick, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync {
            setUpGoogleMap(it)
        }
    }

    override fun onGetOrderFirebaseSuccess(orderFirebase: OrderFirebase) {
        freePickLayout.show().apply {
            namePartnerTextView.text = orderFirebase.id.toString()
        }
    }

    override fun onGetOrderFirebaseRemove() {
        freePickLayout.gone()
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
        presenter.getOrderFirebase(2)
    }

    private fun handleEvents() {
    }

    private fun animateCamera(latLng: LatLng) {
        val cameraUpdate = googleMapHelper.buildCameraUpdate(latLng)
        googleMap.animateCamera(cameraUpdate, 10, null)
    }

    companion object {
        fun newInstance() = FreePickFragment()
    }
}
