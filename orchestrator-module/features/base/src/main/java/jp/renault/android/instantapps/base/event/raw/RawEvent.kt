package jp.renault.android.instantapps.base.event.raw

interface RawEvent {
    val distanceFromCarKm: Double
    val sender: Class<out Any>
}