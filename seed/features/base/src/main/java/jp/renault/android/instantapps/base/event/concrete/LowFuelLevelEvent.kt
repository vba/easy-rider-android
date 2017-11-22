package jp.renault.android.instantapps.base.event.concrete

import kotlin.reflect.KClass

data class LowFuelLevelEvent (val remainingPercentage: Int,
                              override val rawOrigin: Class<out Any>) : ConcreteEvent