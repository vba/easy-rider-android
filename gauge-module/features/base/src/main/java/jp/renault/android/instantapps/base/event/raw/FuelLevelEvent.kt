package jp.renault.android.instantapps.base.event.raw

data class FuelLevelEvent (val remainingPercentage: Int,
                           override val distanceFromCarKm: Double,
                           override val sender: Class<out Any>) : RawEvent