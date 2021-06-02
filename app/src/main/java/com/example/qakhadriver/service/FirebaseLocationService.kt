package com.example.qakhadriver.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.example.qakhadriver.R
import com.example.qakhadriver.data.model.DriverFirebase
import com.example.qakhadriver.data.model.Event
import com.example.qakhadriver.data.model.OrderFirebase
import com.example.qakhadriver.data.repository.DriverRepositoryImpl
import com.example.qakhadriver.data.repository.OrderFirebaseRepositoryImpl
import com.example.qakhadriver.utils.Constants
import com.example.qakhadriver.utils.LocationHelper.getLocationRequest
import com.google.android.gms.location.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.greenrobot.eventbus.EventBus

class FirebaseLocationService : Service() {

    private val driverRepository by lazy {
        DriverRepositoryImpl.getInstance()
    }
    private val orderFirebaseRepository by lazy {
        OrderFirebaseRepositoryImpl.getInstance()
    }
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var idDriver = Constants.NOT_EXISTS
    private lateinit var notificationManager: NotificationManager

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        locationRequest = getLocationRequest()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getParcelableExtra<DriverFirebase>(BUNDLE_DRIVER)?.let {
            idDriver = it.id
            createLocationCallback(it)
            createNotificationChannelGPS()
            createNotificationChannelOrder()
            startForeground(NOTIFICATION_GPS_ID, createNotificationGPS())
            requestLocationUpdates()
            onListenerOrderFromFirebase(it.id)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        locationProviderClient.removeLocationUpdates(locationCallback)
        driverRepository.removeLocationDriver(idDriver)
        super.onDestroy()
    }

    private fun onListenerOrderFromFirebase(idDriver: Int) {
        orderFirebaseRepository.listenerOrder(idDriver, object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(OrderFirebase::class.java).let {
                    if (it != null) {
                        notificationManager.notify(NOTIFICATION_ORDER_ID, createNotificationOrder())
                        EventBus.getDefault().postSticky(Event(EVENT_ORDER_FIREBASE_RESPONSE, it))
                    } else {
                        snapshot.getValue(OrderFirebase::class.java).let {
                            EventBus.getDefault().postSticky(Event(EVENT_ORDER_FIREBASE_REMOVE, it))
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) = Unit
        })
    }

    private fun createLocationCallback(locationDriver: DriverFirebase) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                locationResult?.lastLocation?.let {
                    driverRepository.updateLocationDriver(
                        locationDriver.copy(
                            latitude = it.latitude.toFloat(),
                            longitude = it.longitude.toFloat()
                        )
                    )
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        locationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()!!
        )
    }

    private fun createNotificationChannelGPS() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_GPS_ID,
                getString(R.string.foreground_service_channel),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotificationChannelOrder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ORDER_ID,
                getString(R.string.notification_order_chanel),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(serviceChannel)
        }
    }


    private fun createNotificationGPS(): Notification {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this, CHANNEL_GPS_ID)
                .setContentTitle(getString(R.string.location_turned_on))
                .setContentText(getString(R.string.have_a_nice_working_day))
                .setSmallIcon(R.drawable.ic_gps)
                .setAutoCancel(false)
                .build()
        } else {
            NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.location_turned_on))
                .setContentText(getString(R.string.have_a_nice_working_day))
                .setSmallIcon(R.drawable.ic_gps)
                .setAutoCancel(false)
                .build()
        }
    }

    private fun createNotificationOrder(): Notification {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this, CHANNEL_ORDER_ID)
                .setContentTitle(getString(R.string.you_have_an_order_pending_delivery))
                .setContentText(getString(R.string.please_deliver_the_goods_quickly))
                .setSmallIcon(R.drawable.ic_box)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_delivery_man))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .build()
        } else {
            NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.you_have_an_order_pending_delivery))
                .setContentText(getString(R.string.please_deliver_the_goods_quickly))
                .setSmallIcon(R.drawable.ic_box)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_delivery_man))
                .setAutoCancel(true)
                .build()
        }
    }

    companion object {
        const val NOTIFICATION_GPS_ID = 1
        const val NOTIFICATION_ORDER_ID = 2
        const val CHANNEL_GPS_ID = "CHANNEL_GPS_ID"
        const val CHANNEL_ORDER_ID = "CHANNEL_ORDER_ID"
        const val BUNDLE_DRIVER = "BUNDLE_DRIVER"
        const val EVENT_ORDER_FIREBASE_RESPONSE = "EVENT_ORDER_FIREBASE_RESPONSE"
        const val EVENT_ORDER_FIREBASE_REMOVE = "EVENT_ORDER_FIREBASE_REMOVE"
    }
}
