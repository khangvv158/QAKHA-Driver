package com.example.qakhadriver.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.example.qakhadriver.R
import com.example.qakhadriver.utils.Constants.NOT_EXISTS
import com.example.qakhadriver.utils.LocationHelper.getLocationRequest
import com.google.android.gms.location.*
import java.util.*

class FirebaseLocationService : Service() {

    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        locationRequest = getLocationRequest()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getIntExtra(BUNDLE_ID, NOT_EXISTS)?.let {
            if (it != NOT_EXISTS) {
                createLocationCallback(it)
                createNotificationChannel()
                startForeground(getNotificationId(), createNotification())
                requestLocationUpdates()
            }
        }
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        locationProviderClient.removeLocationUpdates(locationCallback)
        // working with firebase when stop
        super.onDestroy()
    }

    private fun createLocationCallback(idDriver: Int) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                locationResult?.lastLocation?.let {
                    //working firebase here with id driver
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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.foreground_service_channel),
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotification(): Notification {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this, CHANNEL_ID)
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

    private fun getNotificationId(): Int {
        return Date().time.toInt()
    }

    companion object {
        const val CHANNEL_ID = "CHANNEL_ID"
        const val BUNDLE_ID = "BUNDLE_ID"
    }
}
