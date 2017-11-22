package com.renault.instantapps.poc.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import jp.renault.android.instantapps.base.event.raw.FuelLevelEvent
import jp.renault.android.instantapps.base.event.concrete.ServiceDueEvent
import org.funktionale.option.Option
import org.funktionale.option.toOption
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class EventOrchestrationService() : IntentService(EventOrchestrationService::class.java.getName()) {

    companion object {
        private val OrchestrateMaestro = "OrchestrateMaestro"

        fun startEventOrchestrator(context: Context) {
            val intent = Intent(context, EventOrchestrationService::class.java)
            intent.setAction(OrchestrateMaestro)
            context.startService(intent)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        intent.toOption()
            .flatMap { if (it.action == OrchestrateMaestro) it.toOption() else Option.None }
    }

    override fun onCreate() {
        super.onCreate()
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy()
    }
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onServiceDueEvent(event: ServiceDueEvent) {
        TODO()
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onFuelLevelEvent(event: FuelLevelEvent) {
        TODO()
    }
}
