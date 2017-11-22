package jp.renault.android.instantapps.base.event.concrete

interface ConcreteEvent {
    val rawOrigin: Class<out Any>
}