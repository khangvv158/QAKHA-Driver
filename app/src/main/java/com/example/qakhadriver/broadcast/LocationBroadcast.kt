package com.example.qakhadriver.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import com.example.qakhadriver.data.model.Event
import org.greenrobot.eventbus.EventBus

class LocationBroadcast : BroadcastReceiver() {

    private var firstEnable = true

    override fun onReceive(context: Context?, intent: Intent?) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            if (intent?.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                if (!(context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isLocationEnabled) {
                    if (firstEnable) {
                        EventBus.getDefault()
                            .post(Event(EVENT_REQUEST_ENABLE_GPS, EVENT_REQUEST_ENABLE_GPS))
                        firstEnable = false
                    }
                } else {
                    EventBus.getDefault().post(Event(EVENT_ENABLED_GPS, EVENT_ENABLED_GPS))
                    firstEnable = true
                }
            }
        } else {
            val mode: Int = Settings.Secure.getInt(
                context?.contentResolver, Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )
            if (mode == Settings.Secure.LOCATION_MODE_OFF) {
                if (firstEnable) {
                    EventBus.getDefault()
                        .post(Event(EVENT_REQUEST_ENABLE_GPS, EVENT_REQUEST_ENABLE_GPS))
                    firstEnable = false
                }
            } else {
                EventBus.getDefault().post(Event(EVENT_ENABLED_GPS, EVENT_ENABLED_GPS))
                firstEnable = true
            }
        }
    }

    companion object {

        const val EVENT_REQUEST_ENABLE_GPS = "EVENT_REQUEST_ENABLE_GPS"
        const val EVENT_ENABLED_GPS = "EVENT_ENABLED_GPS"
    }
}
