package com.renault.instantapps.poc

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.renault.instantapps.poc.service.EventOrchestrationService

class MainInstalledActivity : AppCompatActivity() {

//    var jobId = 1;

    companion object {
        private val TAG = MainInstalledActivity::javaClass.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_installed)
        EventOrchestrationService.startEventOrchestrator(applicationContext)

//        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
//        val status = jobScheduler.schedule(JobInfo.Builder(jobId++,
//            ComponentName(this, OrchestrationJobService::class.java))
//            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//            .setRequiresDeviceIdle(false)
//            .setRequiresCharging(false)
////                .setBackoffCriteria(TimeUnit.SECONDS.toMillis(10), JobInfo.BACKOFF_POLICY_LINEAR)
//            .setPeriodic(TimeUnit.SECONDS.toMillis(10))
//            .build()
//        )
//        Log.d(MainInstalledActivity.TAG, "Job scheduling status: $status")
    }
}
