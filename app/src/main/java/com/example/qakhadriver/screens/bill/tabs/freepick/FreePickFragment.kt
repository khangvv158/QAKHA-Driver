package com.example.qakhadriver.screens.bill.tabs.freepick

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qakhadriver.R
import com.example.qakhadriver.utils.GoogleMapHelper
import com.example.qakhadriver.utils.show
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_free_pick.*

class FreePickFragment : Fragment() {

    private lateinit var googleMap: GoogleMap
    private val googleMapHelper by lazy {
        GoogleMapHelper()
    }
    private val locationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
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

    @SuppressLint("MissingPermission")
    private fun setUpGoogleMap(map: GoogleMap) {
        googleMap = map
        googleMap.isMyLocationEnabled = true
        locationProviderClient.lastLocation.addOnSuccessListener {
            animateCamera(LatLng(it.latitude, it.longitude))
        }
    }

    private fun animateCamera(latLng: LatLng) {
        val cameraUpdate = googleMapHelper.buildCameraUpdate(latLng)
        googleMap.animateCamera(cameraUpdate, 10, null)
    }

    companion object {
        fun newInstance() = FreePickFragment()
    }
}
