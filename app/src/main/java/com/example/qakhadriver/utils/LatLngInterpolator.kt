package com.example.qakhadriver.utils

import com.google.android.gms.maps.model.LatLng
import kotlin.math.*

interface LatLngInterpolator {

    fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng

    class Spherical : LatLngInterpolator {

        override fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng {
            val fromLat = Math.toRadians(a.latitude)
            val fromLng = Math.toRadians(a.longitude)
            val toLat = Math.toRadians(b.latitude)
            val toLng = Math.toRadians(b.longitude)
            val cosFromLat = cos(fromLat)
            val cosToLat = cos(toLat)

            val angle = computeAngleBetween(fromLat, fromLng, toLat, toLng)
            val sinAngle = sin(angle)
            if (sinAngle < 1E-6) {
                return a
            }
            val temp1 = sin((1 - fraction) * angle) / sinAngle
            val temp2 = sin(fraction * angle) / sinAngle

            val x = temp1 * cosFromLat * cos(fromLng) + temp2 * cosToLat * cos(toLng)
            val y = temp1 * cosFromLat * sin(fromLng) + temp2 * cosToLat * sin(toLng)
            val z = temp1 * sin(fromLat) + temp2 * sin(toLat)

            val lat = atan2(z, sqrt(x * x + y * y))
            val lng = atan2(y, x)
            return LatLng(Math.toDegrees(lat), Math.toDegrees(lng))
        }

        private fun computeAngleBetween(fromLat: Double, fromLng: Double, toLat: Double, toLng: Double): Double {
            val dLat = fromLat - toLat
            val dLng = fromLng - toLng
            return 2 * asin(
                sqrt(
                    sin(dLat / 2).pow(2.0) + cos(fromLat) * cos(toLat) * sin(dLng / 2).pow(2.0)
                )
            )
        }
    }
}
