package jp.renault.android.instantapps.base.event.concrete

import kotlin.reflect.KClass

data class ServiceDueEvent(val duePercentage: Int, override val rawOrigin: Class<out Any>) : ConcreteEvent