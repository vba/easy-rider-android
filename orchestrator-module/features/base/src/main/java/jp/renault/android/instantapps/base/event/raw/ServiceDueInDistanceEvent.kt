package jp.renault.android.instantapps.base.event.raw

class ServiceDueInDistanceEvent(val duePercentage: Int,
                                override val distanceFromCarKm: Double,
                                override val sender: Class<out Any>): RawEvent