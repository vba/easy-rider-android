package com.renault.easyrider.instantapps.orchestrator

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.renault.easyrider.instantapps.orchestrator.service.EventOrchestrationService
import org.greenrobot.eventbus.EventBus
import android.app.job.JobInfo
import android.content.ComponentName
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.app.job.JobScheduler
import android.content.Context
import android.util.Log
import com.renault.easyrider.instantapps.orchestrator.service.OrchestrationJobService
import java.util.concurrent.TimeUnit


class OrchestratorModuleActivity : Activity() {

    var jobId = 1;

    companion object {
        private val TAG = OrchestratorModuleActivity::javaClass.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        EventOrchestrationService.startEventOrchestrator(this.applicationContext)
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val status = jobScheduler.schedule(JobInfo.Builder(jobId++,
            ComponentName(this, OrchestrationJobService::class.java))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false)
//                .setBackoffCriteria(TimeUnit.SECONDS.toMillis(10), JobInfo.BACKOFF_POLICY_LINEAR)
                .setPeriodic(TimeUnit.SECONDS.toMillis(10))
                .build()
        )
        Log.d(OrchestratorModuleActivity.TAG, "Job scheduling status: $status")
//        finish()
    }

//    override fun onStart() {
//        super.onStart()
//        EventBus.getDefault().register(this)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        EventBus.getDefault().unregister(this)
//    }
}
