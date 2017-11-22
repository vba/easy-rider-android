package com.renault.easyrider.instantapps.orchestrator


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.renault.android.instantapps.base.event.concrete.*
import jp.renault.android.instantapps.base.event.raw.FuelLevelEvent
import jp.renault.android.instantapps.base.event.raw.RawEvent
import jp.renault.android.instantapps.base.event.raw.ServiceDueInDistanceEvent
import org.funktionale.option.Option
import org.funktionale.option.toOption
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class OrchestrationFragment : Fragment() {

    companion object {
        private val DISTANCE_KM_FROM_CAR_THRESHOLD = 0.02
        private val SERVICE_DUE_THRESHOLD = 5
        private val FUEL_LEVEL_THRESHOLD = 10
        private val TAG = OrchestrationFragment::class.java.name
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onRawEvent(event: RawEvent) {
        when (event) {
            is ServiceDueInDistanceEvent -> processServiceDueInDistanceEvent(event)
            is FuelLevelEvent -> processFuelLevelEvent(event)
            else -> {
                Log.w(TAG, "Unknown event type $event")
                Option.None
            }
        }.map {
            EventBus.getDefault().post(it)
        }
    }

    private fun processServiceDueInDistanceEvent(event: ServiceDueInDistanceEvent): Option<ConcreteEvent> = (
        if (event.duePercentage <= SERVICE_DUE_THRESHOLD
            && event.distanceFromCarKm <= DISTANCE_KM_FROM_CAR_THRESHOLD)
            ServiceDueEvent(event.duePercentage, event.javaClass)
        else
            ServiceOkEvent(event.javaClass)
        ).toOption()

    private fun processFuelLevelEvent(event: FuelLevelEvent): Option<ConcreteEvent> = (
        if (event.remainingPercentage <= FUEL_LEVEL_THRESHOLD
            && event.distanceFromCarKm <= DISTANCE_KM_FROM_CAR_THRESHOLD)
            LowFuelLevelEvent(event.remainingPercentage, event.javaClass)
        else
            NormalFuelLevelEvent(event.javaClass)
        ).toOption()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_orchestration, container, false)


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}