package com.renault.android.instantapps.map.tools

import com.google.android.gms.maps.model.LatLng
import java.util.*

fun getRandomCoordinates (center: LatLng, radius: Int = 500) : LatLng {
    val y0 = center.latitude
    val x0 = center.longitude
    val rd = radius / 111300.00

    val u = Random().nextDouble()
    val v = Random().nextDouble()

    val w = rd * Math.sqrt(u)
    val t = 2 * Math.PI * v
    val x = w * Math.cos(t)
    val y = w * Math.sin(t)

    val xp = x / Math.cos(y0)

    return LatLng(y + y0, xp + x0)
}