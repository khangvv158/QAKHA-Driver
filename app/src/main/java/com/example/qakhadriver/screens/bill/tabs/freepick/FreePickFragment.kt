package com.example.qakhadriver.screens.bill.tabs.freepick

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.qakhadriver.R
import com.example.qakhadriver.data.model.Driver
import com.example.qakhadriver.data.model.Event
import com.example.qakhadriver.data.model.Order
import com.example.qakhadriver.data.model.OrderFirebase
import com.example.qakhadriver.data.repository.OrderRepositoryImpl
import com.example.qakhadriver.data.repository.TokenRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.screens.orderdetail.OrderDetailFragment
import com.example.qakhadriver.service.FirebaseLocationService
import com.example.qakhadriver.utils.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.fragment_free_pick.*
import kotlinx.android.synthetic.main.item_layout_free_pick.*
import kotlinx.android.synthetic.main.item_layout_free_pick.addressPartnerTextView
import kotlinx.android.synthetic.main.item_layout_free_pick.addressUserTextView
import kotlinx.android.synthetic.main.item_layout_free_pick.coinTextView
import kotlinx.android.synthetic.main.item_layout_free_pick.namePartnerTextView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FreePickFragment : Fragment(), FreePickContract.View {

    private lateinit var googleMap: GoogleMap
    private val googleMapHelper by lazy {
        GoogleMapHelper()
    }
    private val locationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }
    private val presenter by lazy {
        FreePickPresenter(
            OrderRepositoryImpl.getInstance(),
            TokenRepositoryImpl.getInstance(SharedPrefsImpl.getInstance(requireContext()))
        )
    }
    private lateinit var driver: Driver
    private var markerPartner: Marker? = null
    private var markerUser: Marker? = null

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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onEventGetOrderFromFirebase(event: Event<OrderFirebase>) {
        if (event.keyEvent == FirebaseLocationService.EVENT_ORDER_FIREBASE_RESPONSE) {
            presenter.getOrderDetail(event.obj.idDriver, event.obj.idOrder)
        } else {
            freePickLayout.gone()
            removeMarker()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map.onLowMemory()
    }

    override fun onGetOrderDetailSuccess(order: Order) {
        initViewsFreePickLayout(order)
        handleEventsFreePickLayout(order)
    }

    private fun initViewsFreePickLayout(order: Order) {
        coinTextView.text = order.orderDetail.shippingFee.toString()
        namePartnerTextView.text = order.orderDetail.partner.name
        addressPartnerTextView.text = order.orderDetail.partner.address
        addressUserTextView.text = order.orderDetail.addressUser
        nameUserTextView.text = order.orderDetail.nameUser
        payTextView.text = order.orderDetail.subtotal.toString()
        if (order.orderDetail.typeCheckout == Constants.CASH) {
            typeCheckoutTextView.text = getString(R.string.cash)
        } else {
            typeCheckoutTextView.text = getString(R.string.coin)
        }
        freePickLayout.show()
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
    }

    private fun initViews() {
        presenter.setView(this)
    }

    private fun handleEventsFreePickLayout(order: Order) {
        val positionPartner = LatLng(
            order.orderDetail.partner.latitude.toDouble(),
            order.orderDetail.partner.longitude.toDouble()
        )
        val positionUser = LatLng(
            order.locationUser.latitude.toDouble(),
            order.locationUser.longitude.toDouble()
        )
        freePickLayout.setOnClickListener {
            parentFragment?.parentFragment?.addFragmentBackStack(
                OrderDetailFragment.newInstance(order),
                R.id.containerViewMain
            )
        }
        deliveryTextView.setOnClickListener {
            markerPartner =
                googleMap.addMarker(googleMapHelper.getPartnerMarkerOptions(positionPartner))
            markerUser = googleMap.addMarker(googleMapHelper.getUserMarkerOptions(positionUser))
            val builder = LatLngBounds.Builder().apply {
                include(positionPartner)
                include(positionUser)
            }.build()
            animateCameraBoundSmooth(builder)
        }
    }

    private fun animateCamera(latLng: LatLng) {
        val cameraUpdate = googleMapHelper.buildCameraUpdate(latLng)
        googleMap.animateCamera(cameraUpdate, 10, null)
    }

    private fun animateCameraBoundSmooth(bounds: LatLngBounds) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150))
    }

    private fun removeMarker() {
        googleMap.clear()
    }

    companion object {

        private const val BUNDLE_DRIVER = "BUNDLE_DRIVER"

        fun newInstance(driver: Driver) = FreePickFragment().apply {
            arguments = bundleOf(BUNDLE_DRIVER to driver)
        }
    }
}
